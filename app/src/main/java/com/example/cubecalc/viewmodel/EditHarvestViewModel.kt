package com.example.cubecalc.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.cubecalc.model.Log
import java.util.*

class EditHarvestViewModel : ViewModel() {

    private val _newLogs = MutableLiveData<MutableList<Log>>(mutableListOf())
    val newLogs: LiveData<MutableList<Log>> = _newLogs

    private val _deletedLogs = MutableLiveData<MutableList<Log>>(mutableListOf())
    val deletedLogs: LiveData<MutableList<Log>> = _deletedLogs

    private val _spruceLogsCount = MutableLiveData<Int>(0)
    val spruceLogsCount: LiveData<Int> = _spruceLogsCount

    private val _beechLogsCount = MutableLiveData<Int>(0)
    val beechLogsCount: LiveData<Int> = _beechLogsCount

    private val _firLogsCount = MutableLiveData<Int>(0)
    val firLogsCount: LiveData<Int> = _firLogsCount

    private val _spruceCubicMetres = MutableLiveData<Double>(0.0)
    val spruceCubicMetres: LiveData<Double> = _spruceCubicMetres

    private val _beechCubicMetres = MutableLiveData<Double>(0.0)
    val beechCubicMetres: LiveData<Double> = _beechCubicMetres

    private val _firCubicMetres = MutableLiveData<Double>(0.0)
    val firCubicMetres: LiveData<Double> = _firCubicMetres

    private val _date = MutableLiveData<Date>(Date(System.currentTimeMillis()))
    val date: LiveData<Date> = _date

    fun setSpruceLogsCount(number: Int) {_spruceLogsCount.value = number }
    fun incSpruceLogsCount() { _spruceLogsCount.value?.let { a -> _spruceLogsCount.value = a.inc() } }
    fun decSpruceLogsCount() { _spruceLogsCount.value?.let { a -> _spruceLogsCount.value = a.dec() } }

    fun setBeechLogsCount(number: Int) { _beechLogsCount.value = number }
    fun incBeechLogsCount() { _beechLogsCount.value?.let { a -> _beechLogsCount.value = a.inc() } }
    fun decBeechLogsCount() { _beechLogsCount.value?.let { a -> _beechLogsCount.value = a.dec() } }

    fun setFirLogsCount(number: Int) { _firLogsCount.value = number }
    fun incFirLogsCount() { _firLogsCount.value?.let { a -> _firLogsCount.value = a.inc() } }
    fun decFirLogsCount() { _firLogsCount.value?.let { a -> _firLogsCount.value = a.dec() } }

    fun setSpruceCubicMetres(number: Double) {_spruceCubicMetres.value = number }
    fun addToSpruceCubicMetres(number: Double) { _spruceCubicMetres.value?.let { a -> _spruceCubicMetres.value = a.plus(number) } }
    fun deductOfSpruceCubicMetres(number: Double) { _spruceCubicMetres.value?.let { a -> _spruceCubicMetres.value = a.minus(number) } }

    fun setBeechCubicMetres(number: Double) {_beechCubicMetres.value = number }
    fun addToBeechCubicMetres(number: Double) { _beechCubicMetres.value?.let { a -> _beechCubicMetres.value = a.plus(number) } }
    fun deductOfBeechCubicMetres(number: Double) { _beechCubicMetres.value?.let { a -> _beechCubicMetres.value = a.minus(number) } }

    fun setFirCubicMetres(number: Double) {_firCubicMetres.value = number }
    fun addToFirCubicMetres(number: Double) { _firCubicMetres.value?.let { a -> _firCubicMetres.value = a.plus(number) } }
    fun deductOfFirCubicMetres(number: Double) { _firCubicMetres.value?.let { a -> _firCubicMetres.value = a.minus(number) } }

    fun setDate(date: Date) { _date.value = date }

    fun addToListOfNewLogs(log: Log) {
        _newLogs.value?.add(log)
        _newLogs.notifyObserver()
    }

    fun removeFromListOfNewLogs(log: Log) {
        _newLogs.value?.remove(log)
        _newLogs.notifyObserver()
    }

    fun addToListOfDeletedLogs(log: Log) {
        _deletedLogs.value?.add(log)
        _deletedLogs.notifyObserver()
    }

    fun resetLists() {
        _newLogs.value?.clear()
        _newLogs.notifyObserver()

        _deletedLogs.value?.clear()
        _deletedLogs.notifyObserver()
    }

    fun logsListsAreEmpty() : Boolean {
        return _newLogs.value?.size == 0 && _deletedLogs.value?.size == 0
    }

    private fun <T> MutableLiveData<T>.notifyObserver() {
        this.value = this.value
    }
}