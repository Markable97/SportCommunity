package com.glushko.sportcommunity.cache

import com.glushko.sportcommunity.data.account.AccountCache
import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure
import javax.inject.Inject

class AccountCacheImpl @Inject constructor(private val prefsManager: SharedPrefsManager) : AccountCache {

    override fun saveToken(token: String): Either<Failure, None> {
        return prefsManager.saveToken(token)
    }

    override fun getToken(): Either.Right<String?> {
        return prefsManager.getToken()
    }
}
/***
Что?
Класс, содержащий функции взаимодействия с аккаунтом в бд. Содержит: объект для работы с SharedPrefsManager(val prefsManager),
функции которые выполняют сохранение(fun saveToken(…)) и восстановление(fun getToken()) токена.

Зачем?
Для взаимодействия с аккаунтом в бд.

Наследуется от:
Интерфейса AccountCache, который находится в Data. Имплементирует его функции fun saveToken(…) и getToken().

Поля:
val prefsManager объект для взаимодействия с SharedPrefsManager.

Функции:
fun saveToken(…) сохраняет токен в бд(prefsManager.saveToken(…)). Принимает строки: token. Возвращает Either<Failure, None>.
fun getToken(…) восстанавливает токен из бд(prefsManager.getToken()). Ничего не принимает. Возвращает Either<Failure, String>.
 */
