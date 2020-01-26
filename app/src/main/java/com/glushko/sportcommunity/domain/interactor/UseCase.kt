package com.glushko.sportcommunity.domain.interactor

import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.exception.Failure
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

abstract class UseCase<out Type, in Params> {
    var backgroundContext: CoroutineContext = Dispatchers.IO
    var foregroundContext: CoroutineContext = Dispatchers.Main

    private var parentJob: Job = Job()

    abstract suspend fun run(params: Params): Either<Failure, Type>

    operator fun invoke(params: Params, onResult: (Either<Failure, Type>) -> Unit = {}){
        unsubscribe()
        parentJob = Job()
        CoroutineScope(foregroundContext + parentJob).launch {
            val result = withContext(backgroundContext){
                run(params)
            }
            onResult(result)
        }
    }

    fun unsubscribe() {
        parentJob.apply {
            cancelChildren()
            cancel()
        }
    }
}
/**
 * Что?
Абстрактный(обобщающий) параметризированный класс. Оперирует двумя параметризированными типами:
Type(тип возвращаемого объекта с данными) и Params(тип класса-оболочки, хранящей параметры для выполнения функции).
Содержит: объекты контекста выполнения работы(val backgoundContext, val foregroundContext), объект работы(val parentJob),
абстрактную функцию выполнения(fun run(…)), оператор вызова(operator fun invoke(…)), функцию отписки работы(fun unsubscribe(…)).

Зачем?
Для выполнения любых задач.

Пример:
Регистрация – задача, одна часть которой выполняется в фоновом потоке(API запрос), а другая – в UI потоке(вывод тоста о успехе
или ошибке).

Поля:
—   val backgroundContext создает объект фонового контекста для выполнения работ.
—   val foregroundContext создает объект UI контекста для выполнения работ.
—   val parentJob создает объект, используемый для инициирования и отмены работы.

Функции:
fun run(…) абстрактная функция, имплементируемая в дочерних классах. Выполняется в фоновом контексте.
Принимает объект параметризированного типа Params. Возвращает Either<Failure, Type>, где Failure – ошибка, а Type – тип данных.
operator fun invoke(…) отменяет предыдущую(вызывает unsubscribe(…)) и инициирует новую работу. С помощью функций launch и
withContext разбивает работу между потоками(корутин контекстами). Вызывает функцию run(…) в фоне. Вызывает функцию высшего порядка,
переданную в параметрах(onResult: ((Either<Failure, Type>) -> Unit), в UI потоке.
Принимает объект params параметризированного типа Params и ф-цию высшего порядка onResult(принимает Either<Failure, Type>,
ничего не возвращает). Ничего не возвращает.
fun unsubscribe() отменяет работу.*/