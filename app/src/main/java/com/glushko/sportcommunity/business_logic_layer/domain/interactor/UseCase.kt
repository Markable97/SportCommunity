package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class UseCase() {

     fun request(lam: suspend ()->BaseResponse): BaseResponse{
        var response = BaseResponse(0,"")
        GlobalScope.launch(Dispatchers.IO) {
            response = lam.invoke()
        }
        return response
    }

}