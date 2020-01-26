package com.glushko.sportcommunity.data.account

import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure

interface AccountCache {
    fun getToken(): Either.Right<String?>
    fun saveToken(token: String): Either<Failure, None>
}
/***
Что?
Интерфейс, содержащий функции для взаимодействия с аккаунтом в локальной базе данных.

Зачем?
Для взаимодействия Data с Cache. При этом Data ничего не знает о Cache и его реализации, так как использует свой интерфейс.
Благодаря этому не нарушается правило Dependency Rule.

Пример:
Репозиторий(AccountRepositoryImpl) выполняет метод кэша saveToken(…) используя интерфейс(AccountCache),
реализация которого(AccountCacheImpl) находится в слое Cache.

Функции:
fun saveToken(…) выполняет сохранение токена в локальную базу данных. Принимает cтроки: token. Возвращает Either<Failure, None>.
fun getToken(…) возвращает токен из локальной базы данных. Ничего не принимает. Возвращает Either<Failure, String>.
 */
