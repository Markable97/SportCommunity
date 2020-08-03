package com.glushko.sportcommunity.presentation_layer.ui.team.squad


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.glushko.sportcommunity.R
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit


class FindUserForSQuadDialog :  DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        println("onCreateDialog")
        return activity?.let {_activity ->
            val builder = AlertDialog.Builder(_activity)
            val inflater = _activity.layoutInflater
            val view: View = inflater.inflate(R.layout.dialog_find_user, null)
            val sr_user = view.findViewById<SearchView>(R.id.user_search)
            Observable.create(ObservableOnSubscribe<String> {
                sr_user.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
                    override fun onQueryTextSubmit(newText: String?): Boolean {
                        it.onNext(newText!!)
                        return false
                    }

                    override fun onQueryTextChange(query: String?): Boolean {
                        it.onNext(query!!)
                        return false
                    }

                })
            })
                .map { text -> text.trim() }
                .debounce(250, TimeUnit.MILLISECONDS)
                //.distinct()
                //.filter { text -> text.isNotBlank() }
                .subscribe {text ->
                    println("с клавиатуры $text")
                }
            builder.setView(view)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }


}