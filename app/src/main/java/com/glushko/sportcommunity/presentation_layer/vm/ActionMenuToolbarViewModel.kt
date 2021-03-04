package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.BaseResponse
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class ActionMenuToolbarViewModel(application: Application) : AndroidViewModel(application) {

    private var myCompositeDisposable: CompositeDisposable = CompositeDisposable()
    private val useCaseRepository: UseCaseRepository = UseCaseRepository()

    val liveDataBaseResponse: MutableLiveData<BaseResponse> = MutableLiveData()

    fun leaveTeam(team_id: Long, user_id: Long){
        myCompositeDisposable.add(
            useCaseRepository.compareUsers(3/*удаление из команды*/,team_id.toInt(), user_id, 0 /*Можно передать 0 так он не используется*/)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handlerResponseBaseResponse, this::handlerErrorBaseResponse)
        )
    }

    private fun handlerResponseBaseResponse(responseServer: BaseResponse){
        liveDataBaseResponse.postValue(responseServer)
    }
    private fun handlerErrorBaseResponse(err: Throwable){
        println("Ошибка подключения ${err.message}")
        liveDataBaseResponse.postValue(BaseResponse(0, err.localizedMessage))
    }
}