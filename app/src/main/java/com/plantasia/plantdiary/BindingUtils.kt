package com.plantasia.plantdiary

import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.plantasia.convertLongToDateString
import com.plantasia.database.Plant

@BindingAdapter("wateringDateFormatted")
fun TextView.setWateringDateFormatted(item: Plant) {
    text = convertLongToDateString(item.wateringDate)
}