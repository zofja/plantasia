package com.plantasia.plantdiary

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.plantasia.database.PlantDatabaseDao
import com.plantasia.database.Plant
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class PlantasiaViewModel(
        dataSource: PlantDatabaseDao,
        application: Application) : ViewModel() {

    val database = dataSource
    val app = application

    private var viewModelJob = Job()

    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    val plants = database.getAllPlants()

    val clearButtonVisible = Transformations.map(plants) {
        it?.isNotEmpty()
    }


    private var _showSnackbarEvent = MutableLiveData<Boolean?>()


    private var _navigateToNewPlantFragment = MutableLiveData<Boolean?>()


    val navigateToNewPlantFragment: LiveData<Boolean?>
        get() = _navigateToNewPlantFragment


    val showSnackBarEvent: LiveData<Boolean?>
        get() = _showSnackbarEvent


    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = null
    }


    fun doneNavigating() {
        _navigateToNewPlantFragment.value = null
    }


    private suspend fun insert(plant: Plant) {
        withContext(Dispatchers.IO) {
            database.insert(plant)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            database.clear()
        }
    }

    fun onStart() {
        uiScope.launch {
            _navigateToNewPlantFragment.value = true
        }
    }

    fun onClear() {
        uiScope.launch {
            clear()
            _showSnackbarEvent.value = true
        }
    }

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }
}