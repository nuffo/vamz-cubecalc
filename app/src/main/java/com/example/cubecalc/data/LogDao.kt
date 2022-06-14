package com.example.cubecalc.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cubecalc.model.Log

@Dao
interface LogDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addLog(log: Log)

    @Delete
    suspend fun deleteLog(log: Log)

    @Query("SELECT * FROM logs WHERE logHarvestId = :harvestId")
    fun loadLogsByHarvestId(harvestId: Int): LiveData<List<Log>>
}