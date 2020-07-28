package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballDivisions
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballLeagues
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseFootballTeams
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class InfoFootballTeamViewModel(application: Application) : AndroidViewModel(application) {
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()
    private var myCompositeDisposable: CompositeDisposable? = null
    val liveDataLeagues: MutableLiveData<ResponseFootballLeagues> = MutableLiveData()
    val liveDataDivisions: MutableLiveData<ResponseFootballDivisions> = MutableLiveData()
    val liveDataTeams: MutableLiveData<ResponseFootballTeams> = MutableLiveData()
    val liveDataCreateTeam : MutableLiveData<BaseResponse> = MutableLiveData()

    init{
        myCompositeDisposable = CompositeDisposable()
        //getFootballLeagues()
    }
    fun getFootballLeagues(){
        myCompositeDisposable?.add(
            useCaseRepository.getFootballLeagues()

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleErrorLeague)
        )
    }


    fun getFootballDivisions(league_id: Int){
        myCompositeDisposable?.add(
            useCaseRepository.getFootballDivisions(league_id)

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleErrorDivisions)
        )
    }
    fun getFootballTeams(division_id: Int){
        myCompositeDisposable?.add(
            useCaseRepository.getFootballTeams(division_id)

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleErrorTeams)
        )
    }


    fun createTeam(team_id: String){
        val pref = SharedPrefsManager(getApplication<Application>().
        getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE))
        val idUser = pref.getAccount().idUser
        myCompositeDisposable?.add(
            useCaseRepository.createTeam(idUser, team_id)

                //Send the Observable’s notifications to the main UI thread//

                .observeOn(AndroidSchedulers.mainThread())

                //Subscribe to the Observer away from the main UI thread//

                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse, this::handleErrorCreate)
        )
    }

    private fun handleResponse(responseServer: ResponseFootballLeagues) {

        println(" Запрос лиг вернул ${responseServer.success} ${responseServer.message} ${responseServer.football_leagues}")
        liveDataLeagues.postValue(responseServer)

    }

    private fun handleResponse(responseServer: ResponseFootballDivisions) {

        println("Запрос дивизионов вернул ${responseServer.success} ${responseServer.message} ${responseServer.football_divisions}")
        liveDataDivisions.postValue(responseServer)

    }

    private fun handleResponse(responseServer: ResponseFootballTeams) {
        println("Запрос команд вернул ${responseServer.success} ${responseServer.message} ${responseServer.football_teams}")
        liveDataTeams.postValue(responseServer)
    }

    private fun handleResponse(responseServer: BaseResponse){
        println("Создание команды вернуло ${responseServer.success} ${responseServer.message}")
        liveDataCreateTeam.postValue(responseServer)
    }

    private fun handleErrorLeague(err: Throwable){
        liveDataLeagues.postValue(ResponseFootballLeagues(0, err.localizedMessage))
        println("ошибка лиг ${err.message}")
    }

    private fun handleErrorDivisions(err: Throwable){
        liveDataDivisions.postValue(ResponseFootballDivisions(0, err.localizedMessage))
        println("ошибка дивизионов ${err.message}")
    }
    private fun handleErrorTeams(err: Throwable){
        liveDataTeams.postValue(ResponseFootballTeams(0, err.localizedMessage))
        println("ошибка команд ${err.message}")
    }
    private fun handleErrorCreate(err: Throwable){
        liveDataCreateTeam.postValue(BaseResponse(0, err.localizedMessage))
        println("ошибка создания ${err.message}")
    }
}