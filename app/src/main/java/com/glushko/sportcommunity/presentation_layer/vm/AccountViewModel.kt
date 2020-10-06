package com.glushko.sportcommunity.presentation_layer.vm

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.glushko.sportcommunity.business_logic_layer.domain.Login
import com.glushko.sportcommunity.business_logic_layer.domain.NetworkErrors
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseRepository
import com.glushko.sportcommunity.data_layer.datasource.response.ResponseLogin
import com.glushko.sportcommunity.data_layer.repository.MainDatabase
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.coroutines.*

class AccountViewModel(application: Application) : AndroidViewModel(application) {

    private val useCaseRepository: UseCaseRepository

    //val person: LiveData<Person>

    init{
        //val mainDao = MainDatabase.getDatabase(application).mainDao()
        //useCaseRepository = UseCaseRepository(mainDao)
        //person = useCaseRepository.personInfo
        useCaseRepository = UseCaseRepository()
    }

    var job: Job = Job()
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

    fun saveAccountRepository(userName: String, idUser: Int){
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
                pref.saveAccount(Register.Params(loginParam.email, userName, loginParam.password, token!!, idUser))
            })

    }
    fun loginUser(email: String, password: String){
        loginParam = Login.Params(email, password)
        println("Значения для входа $loginParam")
        FirebaseInstanceId.getInstance().instanceId.addOnCompleteListener {
            if(!it.isSuccessful){
                println("getInstanceId failed ${it.exception}")
                liveDataResponseLogin.postValue(
                    ResponseLogin(
                        -1,
                        "get instanced failed",
                        "Err",
                        0
                    )
                )
            }else{
                val token = it.result?.token
                if(token != null){
                    loginParam.token = token
                    job = viewModelScope.launch {
                        println("Зашел в корутину, Ждем 5 секунд")
                        try{
                            useCaseRepository.loginUser(loginParam, liveDataResponseLogin)
                        }catch(err: NetworkErrors){
                            println(err.message)
                            liveDataResponseLogin.postValue(
                                ResponseLogin(
                                    -1,
                                    "Server Error",
                                    "Err",
                                    0
                                )
                            )
                        }
                    }
                }else{
                    println("TOKEN IS NULL")
                    liveDataResponseLogin.postValue(
                        ResponseLogin(
                            -1,
                            "Token is not received ",
                            "Err",
                            0
                        )
                    )
                }
            }
        }

    }

    fun logout(){
        val pref = SharedPrefsManager(getApplication<Application>().
            getSharedPreferences(this.getApplication<Application>().packageName,Context.MODE_PRIVATE))
        pref.logout()
        val dao = MainDatabase.getDatabase(this.getApplication()).mainDao()
        val messageDao = MainDatabase.getMessageDao(this.getApplication())
        val notificationDao = MainDatabase.getNotificationDao(this.getApplication())
        job = viewModelScope.launch(Dispatchers.IO) {
            println("Clear all table")
            dao.deleteMainPage()
            dao.deleteFriends()
            messageDao.deleteAllMessages()
            messageDao.deleteAllLastMessage()
            notificationDao.deleteNotificationChats()
            notificationDao.deleteAllFriendsNotification()
            notificationDao.deleteAllNotification()
            println("Clear done" +
                    "")
        }
    }

    fun registerUser(email: String, name: String, password: String, token: String) {
        registerParams = Register.Params(email, name, password, token)
        println("Значение для регистраици $registerParams")
        FirebaseInstanceId.getInstance().instanceId
            .addOnCompleteListener(OnCompleteListener { task ->
                if (!task.isSuccessful) {
                    println("getInstanceId failed ${task.exception}")
                    println("TOKEN IS NULL")
                    liveData.postValue("Can`t receive token")
                }else {
                    val token = task.result?.token
                    if (token != null) {
                        println("Token in FragmentRegister $token")
                        registerParams.token = token
                        println("Значение для регистраици2 $registerParams")
                        viewModelScope.launch{
                            try {
                                useCaseRepository.registerUser(registerParams, liveData)
                            } catch (err: NetworkErrors) {
                                println(err.message)
                                liveData.postValue("Can`t register user now")
                            }
                        }
                    } else {
                        println("TOKEN IS NULL")
                        liveData.postValue("Can`t receive token")
                    }
                }
            })
    }

    fun cancelDownloading(){
        println("ViewModelScope  -  ${viewModelScope.isActive}")
        println("ViewModelScope Context  -  ${viewModelScope.coroutineContext.isActive}")
        println("ViewModelScope Job - ${job.isActive}")
        if(job.isActive){
            job.cancel()
        }
        println("ViewModelScope Job - ${job.isActive}")
        println("ViewModelScope  Context-  ${viewModelScope.coroutineContext.isActive}")
        println("ViewModelScope  -  ${viewModelScope.isActive}")

    }

    override fun onCleared() {
        super.onCleared()
        println("Метод очистки VewModel!!")
    }
}