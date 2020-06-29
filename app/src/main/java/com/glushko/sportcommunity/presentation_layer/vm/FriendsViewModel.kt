package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.Friend
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFriends
import com.glushko.sportcommunity.data_layer.repository.FriendshipNotification
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FriendsViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private val dao = MainDatabase.getDatabase(application).mainDao()
    private val dao_dop = MainDatabase.getNotificationDao(application)
    private val liveData: MutableLiveData<ResponseFriends> = MutableLiveData()
    var liveDataRepository: LiveData<List<Friend.Params>>

    var liveDataNotification: LiveData<List<FriendshipNotification>>

    private var myCompositeDisposable: CompositeDisposable? = null

    init{
        myCompositeDisposable = CompositeDisposable()
        liveDataRepository = useCaseRepository.getFriends(dao)
        liveDataNotification = dao_dop.getFriendsNotification()
    }

    fun getData(): MutableLiveData<ResponseFriends>{
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser
        getFriends(idUser)
        return liveData
    }

    private fun getFriends(idUser: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                useCaseRepository.getFriends(idUser, liveData, dao, dao_dop)
            }catch (err: NetworkErrors){
                println(err.message)
                liveData.postValue(ResponseFriends(
                    -1,
                    "Server Error"))
            }
        }
    }

    fun searchUser(text: String) {

        //Add all RxJava disposables to a CompositeDisposable//

        if(text.isNotEmpty()){

            myCompositeDisposable?.add(
                useCaseRepository.searchUser(text)

                    //Send the Observable’s notifications to the main UI thread//

                    .observeOn(AndroidSchedulers.mainThread())

                    //Subscribe to the Observer away from the main UI thread//

                    .subscribeOn(Schedulers.io())
                    .subscribe(this::handleResponse, this::handleError)
            )
        }else{
            liveData.postValue(ResponseFriends(2, "Empty search", useCaseRepository.getFriendsList(dao) as MutableList<Friend.Params>))

        }


    }

    private fun handleResponse(responseServer: ResponseFriends) {

        println(" Поиск вернул ${responseServer.success} ${responseServer.message} ${responseServer.friends}")
        responseServer.success = if(responseServer.success == 1) 2 else 0
        liveData.postValue(responseServer)

    }

    private fun handleError(err: Throwable){
        liveData.postValue(ResponseFriends(0, err.localizedMessage))
        println("ошибка поиска ${err.message}")
    }
    override fun onCleared() {
        super.onCleared()
        myCompositeDisposable?.clear()
        println("Метод очистки FriendsVewModel!!")
    }
}