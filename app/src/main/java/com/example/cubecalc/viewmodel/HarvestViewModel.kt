package com.example.cubecalc.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.cubecalc.data.HarvestDatabase
import com.example.cubecalc.repository.HarvestRepository
import com.example.cubecalc.model.Harvest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.util.*

class HarvestViewModel(application: Application) : AndroidViewModel(application) {

    val getAllData: LiveData<List<Harvest>>
    private val repository: HarvestRepository

    init {
        val harvestDao = HarvestDatabase.getDatabase(application).harvestDao()
        repository = HarvestRepository(harvestDao)
        getAllData = repository.getAllData
    }

    fun addHarvest(harvest: Harvest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addHarvest(harvest)
        }
    }

    fun updateHarvest(harvest: Harvest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateHarvest(harvest)
        }
    }

    fun deleteHarvest(harvest: Harvest) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteHarvest(harvest)
        }
    }
}