package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseNotifications
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class NotificationsViewModel(application: Application) : AndroidViewModel(application) {
    private var myCompositeDisposable: CompositeDisposable? = null
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    val liveData: MutableLiveData<ResponseNotifications> = MutableLiveData()

    init {
        myCompositeDisposable = CompositeDisposable()
    }

    fun getNotification(idUser: Long){
        val pref = SharedPrefsManager(getApplication<Application>().
        getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))

        val token = pref.getToken()
        myCompositeDisposable?.add(
            useCaseRepository.getNotification(idUser, token)

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(responseServer: ResponseNotifications){
        println("Запрос вернул $responseServer")
        liveData.postValue(responseServer)
    }

    private fun handleError(err: Throwable){
        liveData.postValue(ResponseNotifications(0, err.localizedMessage))
        println("ошибка сервера ${err.message}")
    }
    override fun onCleared() {
        super.onCleared()
        myCompositeDisposable?.clear()
        println("Метод очистки NotificationsViewModel!!")
    }
}