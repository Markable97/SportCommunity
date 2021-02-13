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

    private val pref = SharedPrefsManager(getApplication<Application>().getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
    private val idUser = pref.getAccount().idUser.toLong()
    private val token = pref.getToken()

    val liveDataSquadList: MutableLiveData<MutableList<Squad.Params>> = MutableLiveData()

    val liveDataBaseResponse: MutableLiveData<BaseResponse> = MutableLiveData()

    val liveDataEventsList: MutableLiveData<ResponseEventsTeam> = MutableLiveData()

    fun getSquadList(team_id: Int){
        myCompositeDisposable.add(
            useCaseRepository.getSquadList( team_id, idUser)
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
                .subscribe(this::handlerResponseBaseResponse, this::handlerErrorBaseResponse)
        )
    }

    private fun handlerResponseSquadList(responseServer: ResponseSquadTeamList){
        liveDataSquadListResponse.postValue(responseServer)
    }

    private fun handleErrorSquadList(err: Throwable){
        liveDataSquadListResponse.postValue(ResponseSquadTeamList(0, err.localizedMessage))
        println("ошибка поиска ${err.message}")
    }
    private fun handlerResponseBaseResponse(responseServer: BaseResponse){
        liveDataBaseResponse.postValue(responseServer)
    }
    private fun handlerErrorBaseResponse(err: Throwable){
        println("Ошибка подключения ${err.message}")
        liveDataBaseResponse.postValue(BaseResponse(0, err.localizedMessage))
    }

    fun getEventsList(id_team: Long){
        myCompositeDisposable.add(
            useCaseRepository.getEventsTeam(team_id = id_team, token = token, user_id = idUser)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseEventsList, this::handlerErrorEvents)
        )
    }

    fun modifyChoice(event_id: Long, choice_mode: String, choice:String){
        myCompositeDisposable.add(
            useCaseRepository.modifyChoiceEvent(idUser, token, event_id, choice_mode, choice)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseBaseResponse, this::handlerErrorBaseResponse)
        )
    }

    private fun handlerResponseEventsList(responseServer: ResponseEventsTeam){
        liveDataEventsList.postValue(responseServer)
    }

    private fun handlerErrorEvents(err: Throwable){
        liveDataEventsList.postValue(ResponseEventsTeam(0, err.localizedMessage))
    }

    fun deleteEvent(id_event: Long){
        myCompositeDisposable.add(
            useCaseRepository.deleteEvent(idUser, id_event, token)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseBaseResponse, this::handlerErrorBaseResponse)
        )
    }


}