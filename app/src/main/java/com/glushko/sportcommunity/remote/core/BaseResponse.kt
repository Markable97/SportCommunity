package com.glushko.sportcommunity.remote.core

/**
Что?
POJO-класс. Содержит поля: код ответа(val success) и текст ошибки(val message).

Зачем?
Для хранения ответа сервера.

Пример:
Сервер в каждом ответе отправляет код успешности и текст ошибки, если она произошла. Чтобы ее распарсить и нужен этот класс.

Поля:
val success содержит код ответа сервера. Успех – 1, ошибка – 0.
val message содержит текст ошибки.
 */
open class BaseResponse(
    val success: Int,
    val message: String
) {

}