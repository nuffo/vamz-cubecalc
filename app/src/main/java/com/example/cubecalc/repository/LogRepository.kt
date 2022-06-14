package com.example.cubecalc.repository

import androidx.lifecycle.LiveData
import com.example.cubecalc.data.LogDao
import com.example.cubecalc.model.Log

class LogRepository(private val logDao: LogDao, harvestId: Int) {

    val getDataWithHarvestId: LiveData<List<Log>> = logDao.loadLogsByHarvestId(harvestId)

    suspend fun addLog(log: Log) {
        logDao.addLog(log)
    }

    suspend fun deleteLog(log: Log) {
        logDao.deleteLog(log)
    }
}