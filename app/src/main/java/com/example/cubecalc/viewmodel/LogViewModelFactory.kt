package com.example.cubecalc.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class LogViewModelFactory(application: Application, harvestId: Int) : ViewModelProvider.Factory {

    private var mApplication : Application
    private var mHarvestId : Int

    init {
        mApplication = application
        mHarvestId = harvestId
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return LogViewModel(mApplication, mHarvestId) as T
    }
}