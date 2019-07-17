package com.plantasia.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plants")
data class Plant(
        @PrimaryKey(autoGenerate = true)
        var plantId: Long = 0L,

        @ColumnInfo(name = "plant_name")
        var plantName: String = "Plant",

        @ColumnInfo(name = "watering_date")
        var wateringDate: Long = System.currentTimeMillis())
