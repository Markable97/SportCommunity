package com.glushko.sportcommunity.presentation_layer.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R

abstract class BaseFragment : Fragment(){

    abstract val layoutId: Int

    open val titleToolbar = R.string.app_name
    open val showToolbar = true

    var navigator: Navigator = Navigator()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(layoutId, container, false)

    }

    override fun onResume() {
        super.onResume()

        base {
            if (showToolbar)
                supportActionBar?.show()
            else
                supportActionBar?.hide()
            supportActionBar?.title = getString(titleToolbar)
        }
    }
    open fun onBackPressed() {}


    open fun updateProgress(status: Boolean?) {
        if (status == true) {
            showProgress()
        } else {
            hideProgress()
        }
    }

    //показ сообщений, метож выполняется в activity
    fun showMessage(message: String) = base { showMessage(message) }
    //Показываем ProgressBar
    fun showProgress() = base { progressStatus(View.VISIBLE) }
    //Убираем ProgressBAr
    fun hideProgress() = base { progressStatus(View.GONE) }
    //Убираем клаву
    fun hideSoftKeyboard() = base { hideSoftKeyboard() }

    inline fun base(block: BaseActivity.() -> Unit) {
        activity.base(block)
    }
}