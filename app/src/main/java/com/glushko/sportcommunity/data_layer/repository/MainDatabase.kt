package com.glushko.sportcommunity.data_layer.repository


import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

@Entity
data class Person(
    //@PrimaryKey val id: Int,
    val name: String,
    @PrimaryKey val email: String,
    val password: String,
    val token: String
)
@Dao
interface MainDao{
    @Insert
    suspend fun insertPerson(person: Person)

    @Query("select * from Person")
    fun getPerson(): LiveData<Person>
}
@Database(entities = [Person::class], version = 1, exportSchema = false)
abstract class MainDatabase: RoomDatabase(){

    abstract fun mainDao(): MainDao

    companion object{
        private val INSTANCE: MainDatabase? = null

        fun getDatabase(context: Context): MainDatabase{
            if(INSTANCE==null){
                synchronized(this){
                    return Room.databaseBuilder(context.applicationContext,
                                                MainDatabase::class.java,
                                                "sport_community_db").build()
                }
            }
            return INSTANCE
        }
    }
}