package com.glushko.sportcommunity.domain.account

import com.glushko.sportcommunity.domain.interactor.UseCase
import com.glushko.sportcommunity.domain.type.None
import javax.inject.Inject

/**
Что?
UseCase. Содержит: объект репозитория(val repository), класс для хранения данных(class Params), имплементированную функцию выполнения(fun run(…)).

Зачем?
Для выполнения обновления токена.

Пример:
Firebase  прислал новый токен, который нужно сохранить в локальной базе данных и обновить на сервере. Токен Firebase будет использоваться в дальнейшем для аутентификации пользователя.

Наследуется от:
UseCase с параметризированными типами None(для данных) и UpdateToken.Params(внутренний класс для передачи параметров).

Внутренние классы:
UpdateToken.Params содержит в себе поля(token) для передачи параметров.

Поля:
val repository объект репозитория(AccountRepository).

Функции:
fun run(…) обращается к репозиторию для выполнения обновления токена. Принимает Params. Возвращает Either<Failure, None>.
 */
class UpdateToken @Inject constructor(
    private val accountRepository: AccountRepository
) : UseCase<None, UpdateToken.Params>() {

    override suspend fun run(params: Params) = accountRepository.updateAccountToken(params.token)

    data class Params(val token: String)
}