package com.glushko.sportcommunity.business_logic_layer.domain.interactor

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.data_layer.datasource.BaseResponse
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.*

class UseCase {

    companion object {

        private const val TAG = "UseCase"
    }

     private val job = Job()
     fun request(lam: suspend ()->Unit){

        //var response = BaseResponse(0,"")
        CoroutineScope(Dispatchers.IO + job).launch{
            delay(10000)
            lam.invoke()
        }

    }
    fun cancel(){
        println("Отмена задачи в USE_CASE")
        job.apply {
            cancelChildren()
            cancel()
        }
    }

    fun getToken() : String{
        Log.i(TAG, "Начало токена!!!!!! ")
        var token = ""
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener(OnCompleteListener {
                task ->
            if(!task.isSuccessful){
                Log.d(TAG, "getInstanceId failed", task.exception)
                token = "ERROR"
                println("getInstanceId failed ${task.exception}")
                return@OnCompleteListener
            }
            val token = task.result?.token

            println("new token = $token")
            Log.d(TAG, "new Token = $token")
        })
        Log.d(TAG, "Мой токен = $token")
        println("Мой токен $token")
        return token
    }

}