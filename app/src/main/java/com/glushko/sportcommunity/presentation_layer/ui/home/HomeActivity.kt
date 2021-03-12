package com.glushko.sportcommunity.presentation_layer.ui.home

import android.content.Context
import android.graphics.Bitmap
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.glushko.sportcommunity.R
import com.glushko.sportcommunity.business_logic_layer.domain.Register
import com.glushko.sportcommunity.business_logic_layer.domain.TeamsUserInfo
import com.glushko.sportcommunity.business_logic_layer.domain.interactor.UseCaseNotificationHelper
import com.glushko.sportcommunity.data_layer.datasource.ApiService
import com.glushko.sportcommunity.presentation_layer.ui.Navigator
import com.glushko.sportcommunity.presentation_layer.ui.chat.ChatsFragment
import com.glushko.sportcommunity.presentation_layer.ui.dialog.DialogFragment
import com.glushko.sportcommunity.presentation_layer.ui.friends.FriendsFragment
import com.glushko.sportcommunity.presentation_layer.ui.friends_request.FriendsRequestFragment
import com.glushko.sportcommunity.presentation_layer.ui.notification.NotificationFragment
import com.glushko.sportcommunity.presentation_layer.ui.profile.ProfileFragment
import com.glushko.sportcommunity.presentation_layer.ui.setting.SettingFragment
import com.glushko.sportcommunity.presentation_layer.ui.team.squad.SquadFragment
import com.glushko.sportcommunity.presentation_layer.ui.team.TeamFragment
import com.glushko.sportcommunity.presentation_layer.ui.team.events.EventsFragment
import com.glushko.sportcommunity.presentation_layer.ui.tournament_table.TournamentTableFootballFragment
import com.glushko.sportcommunity.presentation_layer.vm.AccountViewModel
import com.glushko.sportcommunity.presentation_layer.vm.ActionMenuToolbarViewModel
import com.glushko.sportcommunity.presentation_layer.vm.NotificationDrawerViewModel
import kotlinx.android.synthetic.main.home_activity.*
import kotlinx.android.synthetic.main.navigation.*
//import kotlinx.android.synthetic.main.toolbar.*


class HomeActivity :  AppCompatActivity(), NotificationFragment.CallbackNotificationFragment, TeamFragment.CallbackTeamFragment {

    val contentId = R.layout.home_activity
    val fragmentContainer = R.id.fragmentContainer

    val navigator = Navigator()

    companion object{
        var USER_ID: Long? = null
        var USER_NAME: String? = null
        var IS_LEADER: Boolean = false
        var whichFragmentOpen: String? = null //Какой фрмагмент открыт в данный момент
    }


    lateinit var toolbar: Toolbar

    lateinit var modelActionMenu: ActionMenuToolbarViewModel

    lateinit var model: AccountViewModel
    //lateinit var dataLogin: LiveData<Register.Params>
    lateinit var modelNotification: NotificationDrawerViewModel

    var isStart: Boolean = false

    var teamId: Long? = null

    //lateinit var dataNotificationChats: LiveData<List<ChatsNotification>>

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        println("onCreateOptionsMenu")
        whichFragmentOpen?.let {
            when (it){
                TeamFragment.TAG -> menuInflater.inflate(R.menu.team_menu, menu)
            }
        }
        return true
    }

    override fun onPrepareOptionsMenu(menu: Menu?): Boolean {
        println("onPrepareOptionsMen")
        whichFragmentOpen?.let {
            when (it){
                TeamFragment.TAG -> menu?.getItem(0)?.title = if(IS_LEADER) "Удалить команду" else "Выйти из команды"
            }
        }
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_leave_team -> if(teamId != null && USER_ID != null) modelActionMenu.leaveTeam(teamId!!, USER_ID!!)
        }
        return true
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(contentId)
        //setSupportActionBar(toolbar)
        toolbar = findViewById(R.id.toolbar)
        setSupportActionBar(toolbar)
        toolbar.title = "Профиль"

        supportActionBar?.setHomeAsUpIndicator(R.drawable.menu)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        modelActionMenu = ViewModelProviders.of(this).get(ActionMenuToolbarViewModel::class.java)

        model = ViewModelProviders.of(this).get(AccountViewModel::class.java)
        model.getLoginData()
        val typeOpenFragment: String? = intent.getStringExtra(UseCaseNotificationHelper.TYPE_OPEN)
        when(typeOpenFragment){
            UseCaseNotificationHelper.OPEN_NOTIFICATIONS ->{
                supportActionBar?.title = btnNotificationText.text
                openNotificationFragment()
            }
            UseCaseNotificationHelper.OPEN_FRIENDS -> {
                supportActionBar?.title = btnFriendsText.text
                openFriendsFragment()
            }
            UseCaseNotificationHelper.OPEN_DIALOG -> {
                val userId: Long = intent.getLongExtra(ApiService.PARAM_USER_ID, 0)
                val typeDialog = intent.getIntExtra(UseCaseNotificationHelper.TYPE_DIALOG, 0)
                val contactName = intent.getStringExtra(ApiService.PARAM_NAME)
                supportActionBar?.title = contactName
                openDialogFragment(userId, type_dialog = typeDialog, contact_name = contactName)
            }
            else ->{
                model.getLoginData()
                isStart = true
            }
        }






        model.liveDataLogin.observe(this, Observer<Register.Params> {
            tvUserName.text = it.name
            tvUserEmail.text = it.email
            USER_ID = it.idUser.toLong()
            USER_NAME = it.name
            if (isStart) openProfileFragment(it.idUser, it.name)
            //openProfileFragment(it.idUser, it.name)
        })






        val toggle = object : ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close){
            override fun onDrawerStateChanged(newState: Int) {
                super.onDrawerStateChanged(newState)
                if(newState == DrawerLayout.STATE_SETTLING){
                    hideSoftKeyboard()
                }
            }
        }
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()


        modelNotification = ViewModelProviders.of(this).get(NotificationDrawerViewModel::class.java)


        modelNotification.chatsLiveData.observe(this, Observer {
            println("HomeActivity Live Data Notification 1 ${it}")
            val count = if(it.isNotEmpty()) it.first().count else 0
            if(count > 0 ){
                btnChats_notification.text = if(count > 9) "9+" else count.toString()
                btnChats_notification.visibility = View.VISIBLE
            }else{
                btnChats_notification.visibility = View.GONE
            }
        })

        modelNotification.friendsLiveData.observe(this, Observer {
            println("HomeActivity Live Data Notification 2 $it")
            val count = if(it.isNotEmpty()) it.size else 0
            if(count > 0){
                btnFriends_notification.text = if(count > 9) "9+" else count.toString()
                btnFriends_notification.visibility = View.VISIBLE
            }else{
                btnFriends_notification.visibility = View.GONE
            }
        })

        modelNotification.notificationsLiveData.observe(this, Observer {
            println("HomeActivity Live Data Notification 3 $it")
            val count = if(it.isNotEmpty()) it.size else 0
            if(count > 0){
                btnNotification_notification.text = if(count > 9) "9+" else count.toString()
                btnNotification_notification.visibility = View.VISIBLE
            }else{
                btnNotification_notification.visibility = View.GONE
            }
        })

        profileContainer.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = "Профиль"
            openProfileFragment(USER_ID?.toInt(), USER_NAME)

        }

        btnChats.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnChatsText.text
            openChatsFragment()
        }


        btnFriends.setOnClickListener {
            drawerLayout.closeDrawers()
            toolbar.title = btnFriendsText.text
            openFriendsFragment()

        }
        btnNotification.setOnClickListener {
            drawerLayout.closeDrawers()
            openNotificationFragment()
        }

        btnSetting.setOnClickListener {
            drawerLayout.closeDrawers()
            openSettingFragment()
        }

        btnLogout.setOnClickListener {
            model.logout()
            navigator.showLogin(this)
        }

        modelActionMenu.liveDataBaseResponse.observe(this, Observer {
            println("Live data Action Menu ${it.message}")
            if(it.success == 1){
                when(whichFragmentOpen){
                    TeamFragment.TAG -> {
                        this.supportFragmentManager.popBackStack()
                        whichFragmentOpen = null
                        invalidateOptionsMenu()
                        toolbar.title = "Профиль"
                        Toast.makeText(this, "Вы покинули команду!", Toast.LENGTH_SHORT).show()
                    }
                }
            }else{
                Toast.makeText(this, it.message, Toast.LENGTH_SHORT).show()
            }

        })
    }

    private fun openProfileFragment(user_id: Int?, user_name: String?, status_friend: String? = null, isMe: Boolean = true){
        whichFragmentOpen = ProfileFragment.TAG
        invalidateOptionsMenu()
        if(user_id!=null && user_name!=null){
            toolbar.title = "Профиль"
            val fragmentProfile =  ProfileFragment(userId = user_id, userName = user_name, isMe = isMe, status_friend = status_friend, callbackActivity = object : ProfileFragment.Callback{
                override fun onClickTeam(teamItem: TeamsUserInfo.Params, bitmap: Bitmap) {
                    openTeamFragment(teamItem.team_name, teamItem.team_desc, bitmap, teamItem.leader_id, teamItem.leader_name, teamItem.team_id)
                }

                override fun onClickBtnLeft(isMe: Boolean, status_friend: String?) {
                    if(isMe){
                        openNotificationFragment()
                    }else{
                        openAddDialogFragment(status_friend)
                    }
                }

                override fun onClickBtnRight(isMe: Boolean, idUser: Int, user_name: String) {
                    if(isMe){
                        openSettingFragment()
                    }else{
                        toolbar.title = user_name
                        openDialogFragment(idUser.toLong(),  type_dialog = 0)
                    }
                }

            })
            supportFragmentManager.beginTransaction().replace(fragmentContainer, fragmentProfile).commit()
        }
    }

    private fun openChatsFragment(){
        val frgmentChats = ChatsFragment(object : ChatsFragment.Callback{
            override fun changeFragment(contact_id: Long, contact_name: String, count_notification: Int, type_dialog:Int) {
                toolbar.title = contact_name
                modelNotification.deleteChooseNotificationChat(contact_id)
                openDialogFragment(contact_id, count_notification, type_dialog)
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer, frgmentChats).commit()
    }

    private fun openDialogFragment(id_user: Long, count_notification: Int = 0, type_dialog:Int, contact_name: String = "") {
        supportFragmentManager.beginTransaction().add(fragmentContainer,DialogFragment(id_user, count_notification, type_dialog)).commit()
    }

    private fun openSettingFragment() {
        toolbar.title = btnSettingText.text
        supportFragmentManager.beginTransaction().replace(fragmentContainer, SettingFragment()).commit()
    }

    private fun openAddDialogFragment(status_friend: String?) {
        //Toast.makeText(this, "Диалог фрагмент отправки запроса на дружбу", Toast.LENGTH_SHORT).show()
        when(status_friend){
            "friend" -> Toast.makeText(this, "Это ваш друг.", Toast.LENGTH_SHORT).show()
            "request" -> Toast.makeText(this, "Запрос на дружбу уже отправлен", Toast.LENGTH_SHORT).show()
            else -> Toast.makeText(this, "Запрос на дружбу", Toast.LENGTH_SHORT).show()
        }
    }

    private fun openNotificationFragment() {
        toolbar.title = btnNotificationText.text
        //btnNotification_notification.visibility = View.GONE
        supportFragmentManager.beginTransaction().replace(fragmentContainer, NotificationFragment()).commit()
    }

    private fun openTeamFragment(teamName: String, teamId: Long){
        //Открытие по приглашению
        toolbar.title = teamName
        this.teamId = teamId
        invalidateOptionsMenu()
        whichFragmentOpen = null
        val fragmentTeam = TeamFragment.newInstance("from notification",teamId, teamName, USER_ID?:0.toLong())
        supportFragmentManager.beginTransaction().add(fragmentContainer, fragmentTeam).commit()
    }

    private fun openTeamFragment(teamName: String, teamDesc: String, bitmap: Bitmap, leader_id: Int, leader_name: String, team_id: Int){
        //Открытие со страницы пользователя
        toolbar.title = teamName
        val userId = model.liveDataLogin.value?.idUser?:0
        teamId = team_id.toLong()
        whichFragmentOpen = TeamFragment.TAG
        println("Invalidation menu")
        IS_LEADER = leader_id.toLong() == USER_ID
        invalidateOptionsMenu()

        /*var isLeader = false
        if(userId == leader_id)
            isLeader = true*/
        println("Создание фрагмента")
        val fragmentTeamProfile = TeamFragment.newInstance("from profile", team_id.toLong(), teamName, userId.toLong(), teamDesc, leader_name, leader_id.toLong(), bitmap)
        supportFragmentManager.beginTransaction().add(fragmentContainer, fragmentTeamProfile).addToBackStack(null).commit()
        println("фрагмент создан")

    }

    private fun openSquadEvents(team_id: Long, team_name: String, isLeader: Boolean){
        toolbar.title = "События"
        val squadEventsFragment = EventsFragment(team_id, team_name, isLeader)
        supportFragmentManager.beginTransaction().replace(fragmentContainer,
            squadEventsFragment
        ).commit()
    }

    private fun openTeamStatistics(team_id: Long){
        toolbar.title = "Статистика"
        whichFragmentOpen = null
        invalidateOptionsMenu()
        val tournamentTableFragment = TournamentTableFootballFragment.newInstance(team_id)
        supportFragmentManager.beginTransaction().replace(fragmentContainer,
            tournamentTableFragment
        ).commit()
    }

    private fun openSquadFragment(leader_id: Long, team_id: Int, team_name: String, isLeader: Boolean){
        whichFragmentOpen = null
        invalidateOptionsMenu()
        val squadFragment = SquadFragment(leader_id, team_id, team_name, isLeader, object : SquadFragment.Callback{
            override fun onClickPlayerInApp(
                user_id: Long,
                user_name: String?,
                status_friend: String?
            ) {
                openProfileFragment(user_id.toInt(), user_name, status_friend, USER_ID == user_id)
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer,
            squadFragment
        ).commit()
    }
    private fun openFriendsFragment(){
        val fragmentFriends = FriendsFragment(object : FriendsFragment.Callback{
            override fun changeFragment(friend_id: Int, friend_name: String, status_friend: String?) {
                openProfileFragment(friend_id, friend_name, status_friend,false)
            }

            override fun onClickFriendsRequest() {
                openFriendsRequestFragment()
            }

        })
        supportFragmentManager.beginTransaction().replace(fragmentContainer, fragmentFriends).commit()
    }

    private fun openFriendsRequestFragment(){
        toolbar.title = "Заявки"
        val fragmentFriendsRequest = FriendsRequestFragment()
        supportFragmentManager.beginTransaction().replace(fragmentContainer, fragmentFriendsRequest).commit()
    }

    fun hideSoftKeyboard() {
        if (currentFocus != null) {
            val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(currentFocus!!.windowToken, 0)
        }
    }

    //Нажатие внутри команды
    override fun onClickEvent(type_notification: String, team_id: Long, team_name: String, notification_id: Long) {
        if(type_notification != "event"){
            //открытие окна команды
            openTeamFragment(team_name, team_id)
        }else{
            //открытие окна события команды
            openSquadEvents(team_id, team_name, false)
        }
    }

    override fun onClickUpperRightButton(idLeader: Long, leaderName: String) {
        toolbar.title = leaderName
        openDialogFragment(idLeader, type_dialog = 0)
    }

    override fun onClickSquad(leader_id: Long, team_id: Long, team_name: String, isLeader: Boolean) {
        toolbar.title = "Состав"
        openSquadFragment(leader_id, team_id.toInt(), team_name, isLeader)
    }

    override fun onClickUpperLeftButton(teamName: String, teamId: Int) {
        openDialogFragment(teamId.toLong(), type_dialog = 1, contact_name = teamName )
    }

    override fun onClickEvents(team_id: Long, team_name: String, isLeader: Boolean) {
        openSquadEvents(team_id, team_name, isLeader)
    }

    override fun onClickStatistics(team_id: Long) {
        if(team_id!=0L){
            openTeamStatistics(team_id)
        }else{
            Toast.makeText(this, "Не удается открыть статистику", Toast.LENGTH_SHORT).show()
        }
    }
}

