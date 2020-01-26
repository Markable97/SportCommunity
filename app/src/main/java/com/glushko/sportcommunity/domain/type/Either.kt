package com.glushko.sportcommunity.domain.type

/**
Представляет значение одного из двух возможных типов (непересекающееся объединение).
Экземпляры [Either] являются либо экземпляром [Left], либо [Right].
[Left] используется для " отказа"
и [Right] используется для "успеха".
@see Left
@see Right
 */
sealed class Either<out L, out R> {
    /**Представляет левую сторону [Either] класса, который по соглашению является "Failure"*/
    data class Left<out L>(val a: L) :Either<L, Nothing>()
    /**Представляет правую сторону [Either] класса, который по соглашению является "Success"*/
    data class Right<out R>(val b: R):Either<Nothing, R>()

    val isRight get() = this is Right<R>
    val isLeft get() = this is Left<L>

    fun <L> left(a: L) = Left(a)
    fun <R> right(b: R) = Right(b)

    fun either(functionLeft: (L) -> Any, functionRight: (R) -> Any): Any =
        when (this) {
            is Left -> functionLeft(a)
            is Right -> functionRight(b)
        }
}
fun <A, B, C> ((A) -> B).compose(f: (B) -> C): (A) -> C = {
    f(this(it))
}

fun <T, L, R> Either<L, R>.flatMap(fn: (R) -> Either<L, T>): Either<L, T> {
    return when (this) {
        is Either.Left -> Either.Left(a)
        is Either.Right -> fn(b)
    }
}

fun <T, L, R> Either<L, R>.map(fn: (R) -> (T)): Either<L, T> {
    return this.flatMap(fn.compose(::right))
}

fun <L, R> Either<L, R>.onNext(fn: (R) -> Unit): Either<L, R> {
    this.flatMap(fn.compose(::right))
    return this
}
/**
 * Что?
Параметризированный класс-обертка. Содержит в себе: два типа данных(Left и Right); функции для их проверки(val isLeft(), val isRight()) и обработки с помощью ф-ций высшего
порядка(fun either(…)); операторы для трансформации(fun map(…), fun flatMap(…), fun onNext(…)).

Зачем?
Для передачи одного из двух возможных типов данных, неизвестного в момент компиляции, но известного в момент выполнения(кот Шредингера).

Пример:
Сетевой запрос может возвращать как данные, так и ошибку. Either компонует их, что помогает обработать их вместе с меньшим количеством кода.

Внутренние классы:
Left – может содержать в себе только левую часть Either.
Right – может содержать в себе только правую часть Either.

Поля:
—   val isRight() проверяет, является ли объект Either типом Right. Возвращает Boolean.
—   val isLeft() проверяет, является ли объект Either типом Left. Возвращает Boolean.

Функции:
—   fun either(…) выполняет одну из двух ф-ций высшего порядка, переданных в параметрах. Принимает две ф-ции высшего порядка(для параметризированных типов L(Left) и R(Right)): functionLeft(принимает L, возвращает Any) и functionRight(принимает R и возвращает Any). Возвращает Any.
Операторы:
—   fun map(…) выполнят преобразование. если объект Either является типом L – возвращает его без изменений; если объект Either является типом R – возвращает преобразованный с помощью переданной ф-ции высшего порядка(fn: (R) -> (T) объект типа R. Принимает ф-цию высшего порядка fn(принимает R, возвращает T). Возвращает Either<L, T>, где T – преобразованный R.
—   fun flatMap(…) выполняет преобразование. если объект Either является типом L – возвращает его без изменений; если объект Either является типом R – с помощью переданной ф-ции высшего порядка(fn: (R) -> Either<L, T>) подменяет исходный Either другим Either, преобразовывая его содержимое (R). Принимает ф-цию высшего порядка fn(принимает R, возвращает Eihter<L, T>). Возвращает Either<L, T>, где T – преобразованный R.
—   fun onNext(…) выполнят функцию. Если объект Either является типом L – возвращает его без изменений; если объект Either является типом R – возвращает его без изменений. Выполняет ф-цию высшего порядка(fn: (R) -> Unit). Принимает ф-цию высшего порядка fn(принимает R, ничего не возвращает). Возвращает Either<L, R>
 * */