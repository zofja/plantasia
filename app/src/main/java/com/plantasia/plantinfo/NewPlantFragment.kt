package com.plantasia.plantinfo

import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.plantasia.database.PlantDatabase
import com.google.android.material.snackbar.Snackbar
import com.plantasia.R
import com.plantasia.database.Plant
import com.plantasia.databinding.FragmentNewPlantBinding
import java.text.SimpleDateFormat
import java.util.*

class NewPlantFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val binding: FragmentNewPlantBinding = DataBindingUtil.inflate(
                inflater, R.layout.fragment_new_plant, container, false)

        val application = requireNotNull(this.activity).application

        val dataSource = PlantDatabase.getInstance(application).sleepDatabaseDao
        val viewModelFactory = NewPlantModelFactory(dataSource, application)

        val newPlantViewModel = ViewModelProviders.of(this, viewModelFactory).get(NewPlantViewModel::class.java)

        binding.newPlantViewModel = newPlantViewModel
        binding.setLifecycleOwner(this)

        newPlantViewModel.showSnackBarEvent.observe(this, Observer {
            if (it == true) { // Observed state is true.
                Snackbar.make(
                        activity!!.findViewById(android.R.id.content),
                        "Added new plant to database",
                        Snackbar.LENGTH_SHORT // How long to display the message.
                ).show()
                newPlantViewModel.doneShowingSnackbar()
            }
        })

        newPlantViewModel.navigateToPlantasiaFragment.observe(this, Observer {
            if (it == true) {

                val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                this.findNavController().navigate(
                        NewPlantFragmentDirections.actionNewPlantFragmentToPlantasiaFragment())
                newPlantViewModel.doneNavigating()
            }
        })


        binding.editDate.text = SimpleDateFormat("dd MMM yyyy").format(System.currentTimeMillis())

        var cal = Calendar.getInstance()

        val dateSetListener = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val myFormat = "dd MMM yyyy" // mention the format you need
            val sdf = SimpleDateFormat(myFormat, Locale.US)
            binding.editDate.text = sdf.format(cal.time)

        }

        binding.editDate.setOnClickListener {
            DatePickerDialog(activity, dateSetListener,
                    cal.get(Calendar.YEAR),
                    cal.get(Calendar.MONTH),
                    cal.get(Calendar.DAY_OF_MONTH)).show()
        }

        binding.addButton.setOnClickListener {
            val plant = Plant()
            plant.plantName = binding.editPlantName.text.toString()
            plant.wateringDate = cal.timeInMillis
            if (plant.plantName.isEmpty()) {
                showSnackbarPlantWithNoName()
            } else {
                newPlantViewModel.onAdd(plant)
            }
        }

        return binding.root
    }

    private fun showSnackbarPlantWithNoName() {
        Snackbar.make(
                activity!!.findViewById(android.R.id.content),
                "Plant has to have a name",
                Snackbar.LENGTH_SHORT // How long to display the message.
        ).show()
    }

}


