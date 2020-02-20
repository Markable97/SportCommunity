package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.Login
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.login.LoginActivity

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    var liveData: MutableLiveData<String> = MutableLiveData()
    val liveDataLogin: MutableLiveData<Register.Params> = MutableLiveData()
    private val register = Register()
    private val login = Login()
    fun getData(): MutableLiveData<String>{

        return liveData
    }

    fun getLoginData(): MutableLiveData<Register.Params>{
        //Получаем данные из репозитори, а именно из SharedPreferences
        getAccountRepository()
        return liveDataLogin
    }

    fun getAccountRepository(){
        val context = getApplication<Application>()
        val pref = context.getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE)
        val prefManager = SharedPrefsManager(pref)
        println("Данные из SharedPreferences ${prefManager.getAccount()}")
        liveDataLogin.postValue(prefManager.getAccount())
    }

    fun saveAccountRepository(account: Register.Params){
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName,Context.MODE_PRIVATE))
        pref.saveAccount(account)
    }

    fun loginUser(email: String, password: String){
        val loginParam = Login.Params(email, password)
        println("Значения для входа $loginParam")
        login.sendData(loginParam, liveData)
    }

    fun registerUser(email: String, name: String, password: String) {
        val registerParam = Register.Params(email, name, password)
        println("Значение для регистраици $registerParam")
        //saveAccountRepository(registerParam)
        register.sendData(registerParam, liveData)

        /*GlobalScope.launch(Dispatchers.IO) {
            var request =  NetworkService.makeNetworkService().register(createRegisterMap(email, name, password, "1234", 0))
            try{
                var answer = request.await()
                println("Ответ от сервера ${answer.message} ${answer.success}")
                liveData.postValue(answer.message)
            }catch (ce: Exception){
                println("ERRRRRROOOOOOORRRRRR")
                liveData.postValue("Пошел в жопу ниче не работает")
            }


        }*/

        //liveData.postValue("Привет из Корутины через 3 секунлу")
    }

    override fun onCleared() {
        super.onCleared()
        register.useCase.cancel()
        login.useCase.cancel()
    }
}