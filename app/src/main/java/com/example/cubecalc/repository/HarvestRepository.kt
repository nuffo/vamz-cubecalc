package com.example.cubecalc.repository

import androidx.lifecycle.LiveData
import com.example.cubecalc.data.HarvestDao
import com.example.cubecalc.model.Harvest

class HarvestRepository(private val harvestDao: HarvestDao) {

    val getAllData: LiveData<List<Harvest>> = harvestDao.getAllHarvests()

    suspend fun addHarvest(harvest: Harvest) {
        return harvestDao.addHarvest(harvest)
    }

    suspend fun updateHarvest(harvest: Harvest) {
        harvestDao.updateHarvest(harvest)
    }

    suspend fun deleteHarvest(harvest: Harvest) {
        harvestDao.deleteHarvest(harvest)
    }
}