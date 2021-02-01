package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.Squad
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseEventsTeam
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseSquadTeamList
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class SquadViewModel(application: Application) : AndroidViewModel(application) {
    val liveDataSquadListResponse: MutableLiveData<ResponseSquadTeamList> = MutableLiveData()
    private var myCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()

    val liveDataSquadList: MutableLiveData<MutableList<Squad.Params>> = MutableLiveData()

    val liveDataCompare: MutableLiveData<BaseResponse> = MutableLiveData()

    val liveDataEventsList: MutableLiveData<ResponseEventsTeam> = MutableLiveData()

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

    fun compareUsers(type_compare: Int, team_id: Int, user_id: Long, player_id: Long){
        myCompositeDisposable.add(
            useCaseRepository.compareUsers(type_compare,team_id, user_id, player_id)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseCompare, this::handlerErrorCompare)
        )
    }

    private fun handlerResponseSquadList(responseServer: ResponseSquadTeamList){
        liveDataSquadListResponse.postValue(responseServer)
    }

    private fun handleErrorSquadList(err: Throwable){
        liveDataSquadListResponse.postValue(ResponseSquadTeamList(0, err.localizedMessage))
        println("ошибка поиска ${err.message}")
    }
    private fun handlerResponseCompare(responseServer: BaseResponse){
        liveDataCompare.postValue(responseServer)
    }
    private fun handlerErrorCompare(err: Throwable){
        println("Ошибка подключения ${err.message}")
        liveDataCompare.postValue(BaseResponse(0, err.localizedMessage))
    }

    fun getEventsList(id_team: Long){
        val pref = SharedPrefsManager(getApplication<Application>().getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser.toLong()
        val token = pref.getToken()
        myCompositeDisposable.add(
            useCaseRepository.getEventsTeam(team_id = id_team, token = token, user_id = idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseEventsList, this::handlerErrorEvents)
        )
    }

    private fun handlerResponseEventsList(responseServer: ResponseEventsTeam){
        liveDataEventsList.postValue(responseServer)
    }

    private fun handlerErrorEvents(err: Throwable){
        liveDataEventsList.postValue(ResponseEventsTeam(0, err.localizedMessage))
    }

}