package com.glushko.sportcommunity.data.account

import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure

interface AccountRemote {
    fun register(
        email: String,
        name: String,
        password: String,
        token: String,
        userDate: Long
    ): Either<Failure, None>
}
/**
Что?
Интерфейс, содержащий функции для взаимодействия с аккаунтом на сервере.

Зачем?
Для взаимодействия Data с Remote. При этом Data ничего не знает о Remote и его реализации, так как использует свой интерфейс. Благодаря этому не нарушается Dependency Rule.

Пример:
Репозиторий(AccountRepositoryImpl) выполняет метод кэша register используя интерфейс(AccountRemote), реализация которого(AccountRemoteImpl) находится в слое Remote.

Функции:
fun register(…) выполняет регистрацию. Принимает cтроки: email, name, password, token, userDate. Возвращает Either<Failure, None>.
 */
