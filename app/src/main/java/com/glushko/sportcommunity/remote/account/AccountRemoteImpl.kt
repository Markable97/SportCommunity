package com.glushko.sportcommunity.remote.account

import com.glushko.sportcommunity.data.account.AccountRemote
import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure
import com.glushko.sportcommunity.remote.core.Request
import com.glushko.sportcommunity.remote.service.ApiService
import javax.inject.Inject

/**
Что?
Класс, содержащий функции взаимодействия с аккаунтом в сети. Содержит: объект для создания сетевых запросов(val request),
API сервис(val service), функции для выполнения регистрации(fun register(…)) и map’а параметров запроса(fun createRegisterMap(…)).

Зачем?
Для взаимодействия с аккаунтом в сети.

Наследуется от:
Интерфейса AccountRemote, который находится в Data. Имплементирует его функцию fun register(…).

Поля:
val request объект для создания запросов.
val service объект для формирования API запросов.

Функции:
-   fun register(…) выполняет регистрацию. Делегирует создание запроса(request.make(…)). Преобразовывает ответ в None ({ None() }).
Принимает строки: email, name, password, token; Long: userDate. Возвращает Either<Failure, None>.
-   fun createRegisterMap(…) создает map параметров. С помощью map.put(…) добавляет параметры в map. Принимает строки: email,
name, password, token; Long: userDate. Возвращает Map<String, String>.
 */
class AccountRemoteImpl @Inject constructor(
    private val request: Request,
    private val service: ApiService
) : AccountRemote {

    override fun register(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None> {
        return request.make(service.register(createRegisterMap(email, name, password, token, userDate))) { None() }
    }

    private fun createRegisterMap(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Map<String, String> {
        val map = HashMap<String, String>()
        map.put(ApiService.PARAM_EMAIL, email)
        map.put(ApiService.PARAM_NAME, name)
        map.put(ApiService.PARAM_PASSWORD, password)
        map.put(ApiService.PARAM_TOKEN, token)
        map.put(ApiService.PARAM_USER_DATE, userDate.toString())
        return map
    }
}