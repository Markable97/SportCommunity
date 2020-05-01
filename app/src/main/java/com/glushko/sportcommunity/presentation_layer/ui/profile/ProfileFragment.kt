package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment(model: AccountViewModel, dataLogin: LiveData<Register.Params>) : Fragment() {

    val layoutId: Int = R.layout.fragment_profile
    val model: AccountViewModel = model
    val dataLogin: LiveData<Register.Params> = dataLogin

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_notification.setOnClickListener {
            Toast.makeText(activity, "This is Notification", Toast.LENGTH_SHORT).show()
        }
        bt_chat.setOnClickListener {
            Toast.makeText(activity, "This is chat", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onResume() {
        super.onResume()
        dataLogin.observe(this, Observer<Register.Params>{
            if(it.name != null){
                tv_profile_name.text = it.name
            }
        })
    }
}
