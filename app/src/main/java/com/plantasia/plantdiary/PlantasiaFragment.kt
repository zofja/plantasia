package com.plantasia.plantdiary

import android.content.Intent
import android.os.Bundle
import android.provider.CalendarContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.plantasia.database.PlantDatabase
import com.google.android.material.snackbar.Snackbar
import com.plantasia.R
import com.plantasia.database.Plant
import com.plantasia.databinding.FragmentPlantasiaBinding

class PlantasiaFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentPlantasiaBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_plantasia, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = PlantDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = PlantasiaViewModelFactory(dataSource, application)

        val plantasiaViewModel = ViewModelProviders.of(this, viewModelFactory).get(PlantasiaViewModel::class.java)

        binding.plantasiaViewModel = plantasiaViewModel

        val adapter = PlantAdapter(this)
        binding.plantList.adapter = adapter

        plantasiaViewModel.plants.observe(viewLifecycleOwner, Observer {
            it?.let {
                adapter.submitList(it)
            }
        })

        binding.setLifecycleOwner(this)

        plantasiaViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) {
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        getString(R.string.cleared_message),
                        Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                plantasiaViewModel.doneShowingSnackbar()
            }
        })

        plantasiaViewModel.navigateToNewPlantFragment.observe(this, Observer {
            if (it == true) {

                this.findNavController().navigate(
                        PlantasiaFragmentDirections.actionPlantasiaFragmentToNewPlantFragment())
                plantasiaViewModel.doneNavigating()
            }
        })

        return binding.root
    }

    fun onAddToCalendar(plant: Plant) {

        val wateringTime = plant.wateringDate

        val intent = Intent(Intent.ACTION_INSERT)
                .setData(CalendarContract.Events.CONTENT_URI)
                .putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, wateringTime)
                .putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, 1)
                .putExtra(CalendarContract.Events.TITLE, "Water " + plant.plantName)
        startActivity(intent)

    }
}
