package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseSquadTeamList
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SquadViewModel(application: Application) : AndroidViewModel(application) {
    val liveDataSquadList: MutableLiveData<ResponseSquadTeamList> = MutableLiveData()
    private var myCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()

    fun getSquadList(team_id: Int){
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser
        myCompositeDisposable.add(
            useCaseRepository.getSquadList( team_id, idUser.toLong())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseSquadList, this::handleErrorSquadList)
        )
    }

    private fun handlerResponseSquadList(responseServer: ResponseSquadTeamList){
        liveDataSquadList.postValue(responseServer)
    }

    private fun handleErrorSquadList(err: Throwable){
        liveDataSquadList.postValue(ResponseSquadTeamList(0, err.localizedMessage))
        println("ошибка поиска ${err.message}")
    }
}