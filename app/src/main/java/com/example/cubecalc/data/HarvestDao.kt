package com.example.cubecalc.data

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.cubecalc.model.Harvest

@Dao
interface HarvestDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addHarvest(harvest: Harvest)

    @Update
    suspend fun updateHarvest(harvest: Harvest)

    @Delete
    suspend fun deleteHarvest(harvest: Harvest)

    @Query("SELECT * FROM harvests")
    fun getAllHarvests(): LiveData<List<Harvest>>
}