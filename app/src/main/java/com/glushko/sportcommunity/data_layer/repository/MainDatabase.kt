package com.glushko.sportcommunity.data_layer.repository


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.glushko.sportcommunity.business_logic_layer.domain.*
import io.reactivex.Observable
import io.reactivex.Single

@Entity
data class Person(
    //@PrimaryKey val id: Int,
    val name: String,
    @PrimaryKey val email: String,
    val password: String,
    val token: String
)
@Entity(tableName = "notification_chats")
data class ChatsNotification(
    @PrimaryKey
    var contact_id: Long = 1,
    var count: Int = 0
)
@Entity(tableName = "notification_friendship")
data class FriendshipNotification(
    @PrimaryKey
    val contact_id: Long,
    val contact_name: String,
    val status_friend: String
)

/*@Entity
data class TeamsUserInfo(
    @PrimaryKey val team_id: Int = 0,
    val team_name: String = "",
    val amplua: String = "",
    val team_desc: String = "",
    val leader_id: Int = 0,
    val games: Int = 0,
    val goals: Int = 0,
    val assists: Int = 0,
    val yellow: Int  = 0,
val red: Int = 0
)*/
/*@Entity(tableName = "messages_table")
data class MessageEntity(
    @PrimaryKey(autoGenerate = true) var message_id: Long,
    var sender_id: Long,
    var receiver_id: Long,
    var message_text: String,
    var date: Long
)*/


@Dao
interface MainDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainPage(teamsUserInfo: MutableList<TeamsUserInfo.Params>)
    @Update
    suspend fun updateMainPage(teamsUserInfo: MutableList<TeamsUserInfo.Params>)
    @Query("delete from main_page")
    suspend fun deleteMainPage()
    @Query("delete from main_page where team_id not in (:ids)")
    suspend fun deleteBadInfoMainPage(ids: List<Long>)

    @Query("select * from main_page")
    fun getMainPage(): LiveData<List<TeamsUserInfo.Params>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFriends(friends: MutableList<Friend.Params>)

    @Query("select * from friends_table")
    fun getFriends():LiveData<List<Friend.Params>>

    @Query("select * from friends_table")
    fun getFriendsList():List<Friend.Params>

    @Delete
    fun deleteFriend(friend: Friend.Params): Single<Int>

    @Query("delete from friends_table")
    suspend fun deleteFriends()



}

@Dao
interface NotificationDao{
    //@Query("insert into notification_chats select count + 1 from notification_chats")
    //fun setNotificationChats()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setNotificationChats(entity: ChatsNotification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setNotificationFriend(entity: FriendshipNotification)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun setNotificationFriend(entity: MutableList<FriendshipNotification>)


    @Query("select * from notification_chats")
    fun getNotificationChatsLiveData():LiveData<List<ChatsNotification>>

    @Query("select sum(count) from notification_chats")
    fun getNotificationChats():Int

    @Query("select sum(count) from notification_chats where contact_id = :contact_id")
    fun getNotificationChats(contact_id: Long):Int

    @Query("select * from notification_friendship")
    fun getFriendsNotification(): LiveData<List<FriendshipNotification>>

    @Query("select * from notification_friendship")
    fun getFriendsNotificationList(): List<FriendshipNotification>

    @Query("delete from notification_chats")
    fun deleteNotificationChats()

    @Query("delete from notification_chats where contact_id = :contact_id")
    fun deleteNotificationChats(contact_id: Long)

    @Query("delete from notification_friendship where contact_id = :contact_id ")
    fun deleteNotificationFriend(contact_id: Long)

    @Delete
    fun deleteFiendsNotification(item:  FriendshipNotification): Single<Int>

    @Query("delete from notification_friendship")
    fun deleteAllFriendsNotification()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(entity: Notification.Params)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertNotification(entity: MutableList<Notification.Params>)

    @Query("select * from notifications")
    fun getNotifications(): LiveData<List<Notification.Params>>

    @Query("delete from notifications where notification_id = :notification_id ")
    fun deleteNotification(notification_id: Long)

    @Query("delete from notifications")
    fun deleteAllNotification()
}

@Dao
interface MessageDao{
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(entity: Message.Params)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: List<Message.Params>)

    @Query("select * from messages_table where sender_id in (:user_id, :friend_id) and receiver_id in (:user_id, :friend_id)order by message_date desc")
    fun getMessages(user_id: Long, friend_id: Long):LiveData<List<Message.Params>>

    @Query("select * from messages_table where receiver_id = :team_id order by message_date desc")
    fun getMessages(team_id: Int):LiveData<List<Message.Params>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastMessage(entity: List<LastMessage.Params>)

    @Query("update last_messages_table  set count_notification = (select sum(count) from notification_chats  where  notification_chats.contact_id = last_messages_table.contact_id)")
    fun updateLastMessageNotification()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertLastMessage(entity: LastMessage.Params)

    @Query("select * from last_messages_table order by message_date desc")
    fun getLastMessage(): LiveData<List<LastMessage.Params>>

    @Query("delete from messages_table")
    suspend fun deleteAllMessages()

    @Query("delete from last_messages_table")
    suspend fun deleteAllLastMessage()
}

@Database(entities = [TeamsUserInfo.Params::class, Message.Params::class, Friend.Params::class, LastMessage.Params::class, ChatsNotification::class,
    FriendshipNotification::class, Notification.Params::class],
    version = 1, exportSchema = false)
abstract class MainDatabase: RoomDatabase(){

     abstract fun mainDao(): MainDao
     abstract fun messageDao(): MessageDao
     abstract fun notificationDao(): NotificationDao

    companion object{
        private val INSTANCE: MainDatabase? = null
        private var MESSAGE_DAO: MessageDao? = null
        private var NOTIFICATION_DAO: NotificationDao? = null
        fun getDatabase(context: Context): MainDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    return Room.databaseBuilder(context.applicationContext,
                                                MainDatabase::class.java,
                                                "sport_community_db")
                        .build()
                }
            }
            return INSTANCE
        }

        fun getMessageDao(context: Context): MessageDao{
            if(MESSAGE_DAO ==null){
                MESSAGE_DAO = getDatabase(context).messageDao()
            }
            return MESSAGE_DAO!!
        }
        fun getNotificationDao(context: Context): NotificationDao{
            if(NOTIFICATION_DAO ==null){
                NOTIFICATION_DAO = getDatabase(context).notificationDao()
            }
            return NOTIFICATION_DAO!!
        }
            private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // empty migration.
            }
        }
    }

}