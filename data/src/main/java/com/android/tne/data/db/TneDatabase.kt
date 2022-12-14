package com.android.tne.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.android.tne.data.model.entity.BioEntity
import com.android.tne.domain.model.Bio
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        BioEntity::class
    ] ,
    version = 1)
abstract class TneDatabase : RoomDatabase() {

    abstract fun bioDao() : BioDao

   /* companion object{

        @Volatile
        private var INSTANCE : TneDatabase? = null

        fun getDatabase(
            context: Context
            ,scope: CoroutineScope
        ): TneDatabase{
            return INSTANCE ?: synchronized(this){
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TneDatabase::class.java,
                    "bio_database"
                ).fallbackToDestructiveMigration()
                    .addCallback(BioRoomDatabaseCallBack(scope))
                    .build()
                INSTANCE = instance
                instance
            }
        }


        private class BioRoomDatabaseCallBack(private val scope: CoroutineScope): RoomDatabase.Callback(){
            override fun onCreate(db: SupportSQLiteDatabase) {
                super.onCreate(db)
                INSTANCE?.let {
                        bioRoomDatabase ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(bioRoomDatabase.bioDao())
                    }
                }
            }


        }


//        suspend fun populateDatabase(bioDao: BioDao){
//            bioDao.deleteAll()
//            val bioDataQueue = CircularQueue()
//            for(i in 0..10){
//                bioDataQueue.insert(0.12522525f)
//            }
//
//            val format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
//            val format_time: String = format.format(System.currentTimeMillis())
////            var bio = Bio(0,1.0F,1.0F,1.0F,1.0F,format_time)
//            var bio = Bio(0,bioDataQueue,format_time)
//            bioDao.insert(bio)
//        }
    }*/
}