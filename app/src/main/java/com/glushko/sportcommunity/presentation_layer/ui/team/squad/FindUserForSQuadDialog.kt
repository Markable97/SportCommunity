package com.glushko.sportcommunity.presentation_layer.ui.team.squad


import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Friend
import com.glushko.sportcommunity.presentation_layer.vm.FriendsViewModel
import io.reactivex.Observable
import io.reactivex.ObservableOnSubscribe
import java.util.concurrent.TimeUnit


class FindUserForSQuadDialog :  DialogFragment() {

    var adapter: FindUserAdapter? = null

    var list:  MutableList<Friend.Params> = mutableListOf()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        println("onCreateDialog")
        return activity?.let {_activity ->
            val modelFriend = ViewModelProviders.of(this).get(FriendsViewModel::class.java)
            val builder = AlertDialog.Builder(_activity)
            val inflater = _activity.layoutInflater
            val view: View = inflater.inflate(R.layout.dialog_find_user, null)
            val sr_user = view.findViewById<SearchView>(R.id.user_search)
            val recycler = view.findViewById<RecyclerView>(R.id.dialog_find_user_recycler)
            val callback = (object : FindUserAdapter.Callback{
                override fun onClickInvite(user_id: Int, position: Int, isSend: Boolean) {
                    if(isSend){
                        Toast.makeText(_activity, "Приглашение уже отправлено", Toast.LENGTH_SHORT).show()
                    }else{
                        println("Отправка invite user_id = $user_id")
                        list[position].status_friend = "invite to send"
                        adapter?.setList(list)
                    }

                }
            })
            adapter = FindUserAdapter(callback = callback)
            recycler.adapter = adapter
            recycler.layoutManager = LinearLayoutManager(_activity)
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
                    modelFriend.searchUser(text)
                }
            modelFriend.liveData.observe(this, Observer {
                println("Live data 1 Find user: ${it.success} ${it.message} ${it.friends}")
                list = it.friends
                adapter?.setList(it.friends)
            })
            builder.setView(view)
            builder.create()
        }?: throw IllegalStateException("Activity cannot be null")
    }


}