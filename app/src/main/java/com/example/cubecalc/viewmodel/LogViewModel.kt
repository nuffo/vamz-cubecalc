package com.example.cubecalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.cubecalc.data.HarvestDatabase
import com.example.cubecalc.model.Log
import com.example.cubecalc.repository.LogRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LogViewModel(application: Application, harvestId: Int) : AndroidViewModel(application) {

    val getDataWithHarvestId: LiveData<List<Log>>
    private val repository: LogRepository

    init {
        val logDao = HarvestDatabase.getDatabase(application).logDao()
        repository = LogRepository(logDao, harvestId)
        getDataWithHarvestId = repository.getDataWithHarvestId
    }

    fun addLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addLog(log)
        }
    }

    fun deleteLog(log: Log) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteLog(log)
        }
    }
}