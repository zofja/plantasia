package com.plantasia.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update


@Dao
interface PlantDatabaseDao {

    @Insert
    fun insert(night: Plant)

    @Update
    fun update(night: Plant)

    @Query("SELECT * from plants WHERE plantId = :key")
    fun get(key: Long): Plant

    @Query("DELETE FROM plants")
    fun clear()

    @Query("SELECT * FROM plants ORDER BY plantId DESC")
    fun getAllPlants(): LiveData<List<Plant>>

    @Query("SELECT * FROM plants ORDER BY plantId DESC LIMIT 1")
    fun getPlant(): Plant?
}
