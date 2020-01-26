package com.glushko.sportcommunity.domain.account

import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure

/**
Что?
Интерфейс, содержащий функции для взаимодействия с аккаунтом.

Зачем?
Для взаимодействия Domain с Data. При этом Domain ничего не знает о Data и ее реализации, так как использует свой интерфейс.
Благодаря этому не нарушается Dependency Rule. Это правило говорит нам, что внутренние слои не должны зависеть от внешних.
То есть наша бизнес-логика и логика приложения не должны зависеть от презентеров, UI, баз данных и т.п.

Пример:
UseCase регистрации выполняет метод репозитория register используя интерфейс(AccountRepository), реализация которого(AccountRepositoryImpl) находится в слое Data.

Функции:
fun register(…) выполняет регистрацию. Принимает cтроки: email, name, password. Возвращает Either<Failure, None>.
fun updateToken(…) выполняет обновление токена. Принимает строку: token. Возвращает Either<Failure, None>.
 */

interface AccountRepository {
    fun login(email: String, password: String): Either<Failure, AccountEntity>
    fun logout(): Either<Failure, None>
    fun register(email: String, name: String, password: String): Either<Failure, None>
    fun forgetPassword(email: String): Either<Failure, None>

    fun getCurrentAccount(): Either<Failure, AccountEntity>

    fun updateAccountToken(token: String): Either<Failure, None>
    fun updateAccountLastSeen(): Either<Failure, None>

    fun editAccount(entity: AccountEntity):Either<Failure, AccountEntity>

}