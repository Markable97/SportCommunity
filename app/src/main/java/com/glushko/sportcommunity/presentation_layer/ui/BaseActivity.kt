package com.glushko.sportcommunity.presentation_layer.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.data_layer.repository.SharedPrefsManager
import kotlinx.android.synthetic.main.toolbar.*

abstract class BaseActivity : AppCompatActivity() {

    abstract var fragment: BaseFragment

    open val contentId = R.layout.activity_main

    lateinit var navigator: Navigator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        navigator = Navigator()
        setSupportActionBar(toolbar)
        addFragment(savedInstanceState)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        fragment.onActivityResult(requestCode, resultCode, data)
    }
    override fun onBackPressed() {
        (supportFragmentManager.findFragmentById(
            R.id.fragmentContainer
        ) as BaseFragment).onBackPressed()
        super.onBackPressed()
    }
    //Здесь тоже тип выебываемся показываем, какой котлин ахуенный и в параметрах есть свойство по умолчанию
    // которое можно не передавать
    fun addFragment(savedInstanceState: Bundle? = null, fragment: BaseFragment = this.fragment) {
        savedInstanceState ?: supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, fragment)
        }
        //val manager = supportFragmentManager
        /*val transaction = supportFragmentManager.beginTransaction()
        transaction.add(R.id.fragmentContainer, fragment).commit()*/

    }

    fun changeTitle(title: String){
        toolbar.title = title
    }

    fun replaceFragment(fragment: BaseFragment) {
        this.fragment = fragment
        supportFragmentManager.inTransaction {
            replace(R.id.fragmentContainer, fragment)
        }
    }
    //Показывает кружочек загрузки
    fun progressStatus(viewStatus: Int) {
        toolbar_progress_bar.visibility = viewStatus
    }
    fun showProgress() = progressStatus(View.VISIBLE)
    fun hideProgress() = progressStatus(View.GONE)
    //Скрываем клавиатуру
    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }
    //Показываем Toast-ы c сообщением
    fun showMessage(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
//Типо выебоны, мы расширяем класс FragmentManager функцикей inTransaction передам туда лямбы из add и replace
inline fun FragmentManager.inTransaction(func: FragmentTransaction.() -> FragmentTransaction) =
    beginTransaction().func().commit()
//прям большие выебоны. Расширяем класс Activity, для передачи различных лямб.
//let в связке с ? даем нам проверку на null и выводит только если не нулевое значение
//данные блок вызываем из фрагментов
inline fun Activity?.base(block: BaseActivity.() -> Unit) {
    (this as? BaseActivity)?.let(block)
}