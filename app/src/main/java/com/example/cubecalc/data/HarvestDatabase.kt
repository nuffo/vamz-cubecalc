package com.example.cubecalc.data

import android.content.Context
import androidx.room.*
import com.example.cubecalc.converters.Converters
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.model.Log

@Database(entities = [Harvest::class, Log::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class HarvestDatabase: RoomDatabase() {

    abstract fun harvestDao(): HarvestDao
    abstract fun logDao(): LogDao

    companion object{
        @Volatile
        private var INSTANCE: HarvestDatabase? = null

        fun getDatabase(context: Context): HarvestDatabase{
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HarvestDatabase::class.java,
                    "harvest_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}