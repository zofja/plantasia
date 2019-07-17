package com.plantasia.plantinfo

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.plantasia.database.PlantDatabaseDao
import com.plantasia.database.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class NewPlantViewModel(
        dataSource: PlantDatabaseDao,
        application: Application) : ViewModel() {

    val database = dataSource
    val app = application

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private var _showSnackbarEvent = MutableLiveData<Boolean?>()

    private var _navigateToPlantasiaFragment = MutableLiveData<Boolean?>()

    val navigateToPlantasiaFragment: LiveData<Boolean?>
        get() = _navigateToPlantasiaFragment

    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent

    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }

    fun doneNavigating() {
        _navigateToPlantasiaFragment.value = null
    }

    private suspend fun insert(plant: Plant) {
        withContext(Dispatchers.IO) {
            database.insert(plant)
        }
    }

    fun onAdd(plant: Plant) {
        uiScope.launch {
            insert(plant)
            _showSnackbarEvent.value = true
            _navigateToPlantasiaFragment.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}