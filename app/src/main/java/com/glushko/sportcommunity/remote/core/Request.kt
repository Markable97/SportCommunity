package com.glushko.sportcommunity.remote.core

import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.exception.Failure
import retrofit2.Call
import retrofit2.Response
import javax.inject.Inject
import javax.inject.Singleton

/**
Что?
Класс, выполняющий сетевые запросы. Содержит: объект для проверки подключения (val networkHandler),
функции для выполнения запроса(fun execute(…)) и проверки ответа(extension fun Response.isSucceed()).

Зачем?
Для выполнения сетевых запросов и проверки ответа сервера.

Поля:
val networkHandler объект для проверки сети.

Функции:
-   fun make(…) вспомогательная функция для проверки сети и вызова fun execute(…).
-   fun execute(…) выполняет сетевой запрос с помощью переданного в параметрах call (call.execute()).
В блоке catch формирует маркеры ошибок для дальнейшей обработки(Either.Left(Failure.ServerError)).
Функция имеет параметризированные типы: T(наследуемый от BaseResponse) и R.
Принимает Call и функцию высшего порядка для трансформации transform(принимает T, возвращает R). Возвращает Either<Failure, R>.

fun Response.isSucceed() – extension ф-ция, которая проверяет ответ от сервера. Ничего не принимает. Возвращает Boolean.
 */
@Singleton
class Request @Inject constructor(private val networkHandler: NetworkHandler) {

    fun <T :BaseResponse, R>make(call: Call<T>, transform:(T)->R): Either<Failure, R>{
        return when(networkHandler.isConnected){
            true -> execute(call, transform)
            false, null -> Either.Left(Failure.NetworkConnectionError)
        }
    }
    private fun <T : BaseResponse, R> execute(call: Call<T>, transform: (T) -> R): Either<Failure, R> {
        return try {
            val response = call.execute()
            when (response.isSucceed()) {
                true -> Either.Right(transform((response.body()!!)))
                false -> Either.Left(Failure.ServerError)
            }
        } catch (exception: Throwable) {
            Either.Left(Failure.ServerError)
        }
    }
}
fun <T : BaseResponse> Response<T>.isSucceed(): Boolean {
    return isSuccessful && body() != null && (body() as BaseResponse).success == 1
}
