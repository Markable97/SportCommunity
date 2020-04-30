package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.glushko.sportcommunity.business_logic_layer.domain.Login
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.datasource.ResponseLogin
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.glushko.sportcommunity.presentation_layer.ui.login.LoginActivity
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlin.math.acos

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    var liveData: MutableLiveData<String> = MutableLiveData()
    val liveDataLogin: MutableLiveData<Register.Params> = MutableLiveData()
    val liveDataResponseLogin: MutableLiveData<ResponseLogin> = MutableLiveData()
    private val register = Register()
    private lateinit var registerParams: Register.Params
    private lateinit var loginParam: Login.Params
    private val login = Login()
    fun getData(): MutableLiveData<String>{

        return liveData
    }

    fun getLoginData(): MutableLiveData<Register.Params>{
        //Получаем данные из репозитори, а именно из SharedPreferences
        getAccountRepository()
        return liveDataLogin
    }

    fun getLiveDateResponseLogin(): MutableLiveData<ResponseLogin>{
        return liveDataResponseLogin
    }
    fun getAccountRepository(){
        val context = getApplication<Application>()
        val pref = context.getSharedPreferences(this.getApplication<Application>().packageName, Context.MODE_PRIVATE)
        val prefManager = SharedPrefsManager(pref)
        println("Данные из SharedPreferences ${prefManager.getAccount()}")
        liveDataLogin.postValue(prefManager.getAccount())
    }

    fun saveAccountRepository(){
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName,Context.MODE_PRIVATE))
        pref.saveAccount(registerParams)
    }

    fun saveAccountRepository(userName: String){
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName,Context.MODE_PRIVATE))
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    println("getInstanceId failed ${task.exception}")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                println("Token in LoginFragmnet $token")
                pref.saveAccount(Register.Params(loginParam.email, userName, loginParam.password, token!!))
            })

    }
    fun loginUser(email: String, password: String){
       loginParam = Login.Params(email, password)
        println("Значения для входа $loginParam")
        login.sendData(loginParam, liveDataResponseLogin)
    }

    fun registerUser(email: String, name: String, password: String, token: String) {
        registerParams = Register.Params(email, name, password, token)
        println("Значение для регистраици $registerParams")
        //saveAccountRepository(registerParam)
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    println("getInstanceId failed ${task.exception}")
                    return@OnCompleteListener
                }

                // Get new Instance ID token
                val token = task.result?.token
                // Log and toast
                println("Token in FragmentRegister $token")
                registerParams.token = token!!
                println("Значение для регистраици2 $registerParams")
                register.sendData(registerParams, liveData)

                //pref.saveAccount(Register.Params(loginParam.email, userName, loginParam.password, token!!))
            })


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
        println("Метод очистки VewModel!!")
        register.useCase.cancel()
        login.useCase.cancel()
    }
}