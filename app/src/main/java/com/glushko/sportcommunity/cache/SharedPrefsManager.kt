package com.glushko.sportcommunity.cache

import android.content.SharedPreferences
import com.glushko.sportcommunity.domain.type.Either
import com.glushko.sportcommunity.domain.type.None
import com.glushko.sportcommunity.domain.type.exception.Failure
import javax.inject.Inject

class SharedPrefsManager @Inject constructor(private val prefs: SharedPreferences) {
    companion object {
        const val ACCOUNT_TOKEN = "account_token"
    }

    fun saveToken(token: String): Either<Failure, None> {
        prefs.edit().apply {
            putString(ACCOUNT_TOKEN, token)
        }.apply()

        return Either.Right(None())
    }

    fun getToken(): Either.Right<String?> {
        return Either.Right(prefs.getString(ACCOUNT_TOKEN, ""))
    }
}
/**
Что?
Класс для работы с SharedPreferences. Содержит: объект SharedPreferences(val prefs), вспомогательные константы(val ACCOUNT_TOKEN),
функции для редактирования(fun saveToken(…)) и получения(fun getToken()) токена из SharedPreferences.

Зачем?
Сохранение и восстановление данных.

Поля:
val prefs объект SharedPreferences.

Функции:
fun saveToken(…) сохраняет токен в SharedPreferences(edit().putString(…)). Принимает строку token. Возвращает Either<Failure, None>.
fun getToken() восстанавливает токен из SharedPreferences(getString(…)). Ничего не принимает. Возвращает Either<Failure, String>.
 */
