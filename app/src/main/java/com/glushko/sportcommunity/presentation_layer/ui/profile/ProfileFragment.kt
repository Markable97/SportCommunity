package com.glushko.sportcommunity.presentation_layer.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.business_logic_layer.domain.TeamPlayer
import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.data_layer.datasource.ResponseMainPage
import com.glushko.sportcommunity.presentation_layer.ui.BaseFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import com.glushko.sportcommunity.presentation_layer.vm.ProfileViewModel
import com.realpacific.clickshrinkeffect.applyClickShrink
import kotlinx.android.synthetic.main.fragment_profile.*

class ProfileFragment(model: AccountViewModel, dataLogin: LiveData<Register.Params>) : BaseFragment() {

    override val layoutId: Int = R.layout.fragment_profile
    override val titleToolbar: Int = R.string.screen_profile
     var  model: AccountViewModel = model
     var dataLogin: LiveData<Register.Params> = dataLogin

    lateinit var adapter: ProfileTeamsAdapter
    lateinit var modelPage: ProfileViewModel
    lateinit var dataProfile: MutableLiveData<ResponseMainPage>
    val listTeams: MutableList<TeamPlayer> = MutableList(4) {TeamPlayer("Name $it", "Нападающий", "Голы: ${10*it} Ассисты: ${10+it} ЖК: $it КК: ${it+1}")}

    var listInfoProfile: MutableList<TeamsUserInfo.Params> = mutableListOf()

    /*override fun onCreateView(
        inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(layoutId, container, false)
    }*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        modelPage = ViewModelProviders.of(this).get(ProfileViewModel::class.java)
        dataProfile = modelPage.getData()
        dataProfile.observe(this, Observer<ResponseMainPage>{
            println("ProfileFragment: \n${it.success} ${it.message}, ${it.teamsUserinfo}")
            listInfoProfile = it.teamsUserinfo
            adapter.list = listInfoProfile
            adapter.notifyDataSetChanged()
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bt_notification.applyClickShrink()
        bt_chat.applyClickShrink()

        adapter = ProfileTeamsAdapter(listInfoProfile, object : ProfileTeamsAdapter.Callback{
            override fun onItemCkicked(item: TeamsUserInfo.Params) {
                Toast.makeText(activity, "This is team - ${item.team_name}", Toast.LENGTH_SHORT).show()
            }

        })
        profile_recycler_teams.adapter = adapter
        profile_recycler_teams.layoutManager = LinearLayoutManager(activity)

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
