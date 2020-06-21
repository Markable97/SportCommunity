package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseMainPage
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch

class ProfileViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    //val person: LiveData<Person>
    val liveData:MutableLiveData<ResponseMainPage> = MutableLiveData()

    val liveDataFriendShip: MutableLiveData<BaseResponse> = MutableLiveData()
    //val LiveDataRepository: LiveData<List<TeamsUserInfo.Params>>
    private val mainDao = MainDatabase.getDatabase(application).mainDao()
    private var myCompositeDisposable: CompositeDisposable? = null

    private val pref = SharedPrefsManager(getApplication<Application>().
                getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
    private val idUser = pref.getAccount().idUser

    init{
        //LiveDataRepository = useCaseRepository.mainPage(mainDao)
        myCompositeDisposable = CompositeDisposable()
    }

    fun getData():MutableLiveData<ResponseMainPage>{

        getMainPage(idUser)
        return liveData
    }

    fun getData(id: Long):MutableLiveData<ResponseMainPage>{
        getMainPage(id.toInt())
        return liveData
    }

    fun friendshipAction(friend_id: Long, action: String){
        myCompositeDisposable?.add(
            useCaseRepository.friendshipAction(idUser.toLong(), friend_id, action, pref.getToken())

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleError)
        )
    }

    private fun handleResponse(responseServer: BaseResponse) {
        println("Ответ сервера: ${responseServer.success} ${responseServer.message}")
        liveDataFriendShip.postValue(responseServer)

    }

    private fun handleError(err: Throwable){
        println("ошибка поиска ${err.message}")
        liveDataFriendShip.postValue(BaseResponse(0, err.localizedMessage))

    }

    private fun getMainPage(user_id: Int = 0){
        viewModelScope.launch {
            try {
                useCaseRepository.mainPage(user_id, liveData, mainDao)
            }catch(err: NetworkErrors){
                println(err.message)
                liveData.postValue(
                    ResponseMainPage(
                        -1,
                        "Server Error"
                    )
                )
            }
        }
    }


    override fun onCleared() {
        super.onCleared()
        myCompositeDisposable?.clear()
        println("Метод очистки ProfileVewModel!!")
    }
}