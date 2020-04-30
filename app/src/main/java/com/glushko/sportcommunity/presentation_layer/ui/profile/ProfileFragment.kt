package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.glushko.sportcommunity.R
import kotlinx.android.synthetic.main.profile_activity.*

class ProfileFragment : Fragment() {

    val layoutId: Int = R.layout.profile_activity

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
}
