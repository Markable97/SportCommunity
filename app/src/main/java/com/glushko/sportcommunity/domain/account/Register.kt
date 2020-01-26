package com.glushko.sportcommunity.domain.account

import com.glushko.sportcommunity.domain.interactor.UseCase
import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure
import javax.inject.Inject
/***
Что?
UseCase. Содержит: объект репозитория(val repository), класс для хранения данных(class Params),
имплементированную функцию выполнения(fun run(…)).

Зачем?
Для выполнения регистрации.

Наследуется от:
UseCase с параметризированными типами None(для данных) и Register.Params(внутренний класс для передачи параметров).

Внутренние классы:
Register.Params содержит в себе поля(email, name, password) для передачи параметров.

Поля:
val repository объект репозитория(AccountRepository).

Функции:
fun run(…) обращается к репозиторию для выполнения регистрации. Принимает Params. Возвращает Either<Failure, None>.
 */

class Register @Inject constructor(
    private val repository: AccountRepository
) : UseCase<None, Register.Params>(){
    override suspend fun run(params: Params): Either<Failure, None> {
        return repository.register(params.email, params.name, params.password)
    }

    data class Params(val email: String, val name: String, val password: String)
}