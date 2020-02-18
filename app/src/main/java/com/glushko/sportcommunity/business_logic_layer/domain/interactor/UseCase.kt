package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import kotlinx.coroutines.*

class UseCase() {

     private val job = Job()
     fun request(lam: suspend ()->BaseResponse): BaseResponse{

        var response = BaseResponse(0,"")
        CoroutineScope(Dispatchers.IO + job).launch{
            response = lam.invoke()
        }
        return response
    }
    fun cancel(){
        job.apply {
            cancelChildren()
            cancel()
        }
    }

}