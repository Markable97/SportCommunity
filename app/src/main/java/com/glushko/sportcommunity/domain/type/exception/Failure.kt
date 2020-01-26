package com.glushko.sportcommunity.domain.type.exception

/**
Что?
Класс-маркер. Содержит в себе возможные типы данных: NetworkConnectionError и ServerError. В будущем будут
добавляться новые типы.

Зачем?
Для передачи маркера об ошибке с дальнейшей ее обработкой.

Пример:
API запросы подразумевают риск получения ошибок: сервер не доступен, нет подключения к сети.
При их получении мы искусственно создаем объекты ошибок, которые можно удобно обработать (Выводить разные тосты в
зависимости от типа объекта).
 */
sealed class Failure {
    object NetworkConnectionError : Failure()
    object ServerError : Failure()
}