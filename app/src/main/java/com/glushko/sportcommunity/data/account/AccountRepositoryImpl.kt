package com.glushko.sportcommunity.data.account

import com.glushko.sportcommunity.domain.account.AccountEntity
import com.glushko.sportcommunity.domain.account.AccountRepository
import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure
import com.glushko.sportcommunity.domain.type.flatMap
import java.util.*

/**
Что?
Класс, содержащий функции взаимодействия с аккаунтом. При этом решает откуда брать данные: из локальной базы или из сети.
Содержит: объекты для работы с бд(val accountCache) и  сервером(val accountRemote), функции которые выполняют регистрацию
пользователя(fun register(…)) и обновление токена(fun updateAcountToken(…)).

Зачем?
Для взаимодействия с аккаунтом в бд и сети.

Наследуется от:
Интерфейса AccountRepository, который находится в Domain. Имплементирует его функции fun register(…) и updateAccountToken(…).

Поля:
val accountCache объект для взаимодействия с аккаунтом в бд(AccountCache).
val accountRemote объект для взаимодействия с аккаунтом на сервере(AccountRemote).

Функции:
fun register(…) выполняет регистрацию. Запрашивает токен у бд(accountCache.getToken()) и при помощи оператора flatMap
выполняет регистрацию(accountRemote.register(…)), тем самым подменяя Either с токеном(String) на Either с данными о
регистрации(None). Принимает строки: email, name, password. Возвращает Either<Failure, None>.
fun updateAccountToken(…) выполняет обновление токена. Сохраняет токен в бд(accountCache.saveToken(…)).
Принимает строки: token. Возвращает Either<Failure, None>.
 */
class AccountRepositoryImpl(
    private val accountRemote: AccountRemote,
    private val accountCache: AccountCache
) : AccountRepository {
    override fun login(email: String, password: String): Either<Failure, AccountEntity> {
        throw UnsupportedOperationException("Login is not supported")
    }

    override fun logout(): Either<Failure, None> {
        throw UnsupportedOperationException("Logout is not supported")
    }

    override fun register(email: String, name: String, password: String): Either<Failure, None> {
        return accountCache.getToken().flatMap {
            accountRemote.register(email, name, password, it, Calendar.getInstance().timeInMillis)
        }
    }

    override fun forgetPassword(email: String): Either<Failure, None> {
        throw UnsupportedOperationException("Password recovery is not supported")
    }

    override fun getCurrentAccount(): Either<Failure, AccountEntity> {
        throw UnsupportedOperationException("Get account is not supported")
    }

    override fun updateAccountToken(token: String): Either<Failure, None> {
        return accountCache.saveToken(token)
    }

    override fun updateAccountLastSeen(): Either<Failure, None> {
        throw UnsupportedOperationException("Updating last seen is not supported")
    }

    override fun editAccount(entity: AccountEntity): Either<Failure, AccountEntity> {
        throw UnsupportedOperationException("Editing account is not supported")
    }
}