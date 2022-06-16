package com.example.cubecalc.fragments

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cubecalc.R
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.databinding.FragmentAddNewHarvestBinding
import java.util.*

const val KEY_YEAR_ADD = "year_key"
const val KEY_MONTH_ADD = "month_key"
const val KEY_DAY_ADD = "day_key"

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewHarvestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewHarvestFragment : Fragment() {

    private lateinit var binding: FragmentAddNewHarvestBinding
    private lateinit var mHarvestViewModel: HarvestViewModel

    private var selectedYear = 0;
    private var selectedMonth = 0;
    private var selectedDay = 0;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_harvest, container, false)
        binding.addNewHarvestButton.setOnClickListener { onAddHarvest() }
        binding.backButton.setOnClickListener { view : View -> view.findNavController().navigate(AddNewHarvestFragmentDirections.actionAddNewHarvestFragmentToAllHarvestsFragment()) }

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)

        val currentDate = Calendar.getInstance()
        selectedYear = currentDate.get(Calendar.YEAR)
        selectedMonth = currentDate.get(Calendar.MONTH)
        selectedDay = currentDate.get(Calendar.DAY_OF_MONTH)

        if (savedInstanceState != null) {
            selectedYear = savedInstanceState.getInt(KEY_YEAR_ADD, 0)
            selectedMonth = savedInstanceState.getInt(KEY_MONTH_ADD, 0)
            selectedDay = savedInstanceState.getInt(KEY_DAY_ADD, 0)

            binding.harvestDate.text = "$selectedDay.${selectedMonth + 1}.$selectedYear"
        }

        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_YEAR_ADD, selectedYear)
        outState.putInt(KEY_MONTH_ADD, selectedMonth)
        outState.putInt(KEY_DAY_ADD, selectedDay)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.harvestDate.setOnClickListener {

            val listener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                this.selectedYear = selectedYear
                this.selectedMonth = selectedMonth
                this.selectedDay = selectedDay

                binding.harvestDate.text = "$selectedDay.${selectedMonth + 1}.$selectedYear"
            }

            val datePicker = DatePickerDialog(view.context, listener, selectedYear, selectedMonth, selectedDay)
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }
    }

    private fun onAddHarvest() {
        val title = binding.harvestTitle.text.toString()
        val date = binding.harvestDate.text.toString()

        when {
            title.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter title of harvest!", Toast.LENGTH_SHORT).show()
            }
            date.isEmpty() -> {
                Toast.makeText(requireContext(), "Please select date of harvest!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                insertHarvestToDatabase()
                requireView().findNavController().navigate(AddNewHarvestFragmentDirections.actionAddNewHarvestFragmentToAllHarvestsFragment())
            }
        }
    }

    private fun insertHarvestToDatabase() {
        val title = binding.harvestTitle.text.toString()
        val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
        val date = Date(dateText)
        val harvest = Harvest(0, title, date, 0,0,0,0.0,0.0,0.0, System.currentTimeMillis())
        mHarvestViewModel.addHarvest(harvest)
        Toast.makeText(requireContext(), "Harvest succesfully added!", Toast.LENGTH_SHORT).show()
    }
}