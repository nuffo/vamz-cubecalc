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
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.cubecalc.R
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.databinding.FragmentAddNewHarvestBinding
import java.util.*

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
        binding = DataBindingUtil.inflate(inflater,
            R.layout.fragment_add_new_harvest, container, false)
        Log.i("AddNewHarvestFragment", "onCreateView Called")
        binding.addNewHarvestButton.setOnClickListener { onAddHarvest() }
        binding.backButton.setOnClickListener { view : View -> view.findNavController().navigate(AddNewHarvestFragmentDirections.actionAddNewHarvestFragmentToAllHarvestsFragment()) }

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)

        val currentDate = Calendar.getInstance()
        selectedYear = currentDate.get(Calendar.YEAR)
        selectedMonth = currentDate.get(Calendar.MONTH)
        selectedDay = currentDate.get(Calendar.DAY_OF_MONTH)

        return binding.root
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

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter title of harvest!", Toast.LENGTH_SHORT).show()

        } else if (date.isEmpty()){
            Toast.makeText(requireContext(), "Please select date of harvest!", Toast.LENGTH_SHORT).show()
        } else {
            //insertDataToDatabase()
            val title = binding.harvestTitle.text.toString()
            val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
            val date = Date(dateText)
            val harvest = Harvest(0, title, date, 0,0,0,0.0,0.0,0.0, System.currentTimeMillis())
            //mHarvestViewModel.addHarvest(harvest)
            Toast.makeText(requireContext(), "Harvest succesfully added!", Toast.LENGTH_SHORT).show()

            mHarvestViewModel.getLastAdded().observe(viewLifecycleOwner, Observer<Harvest> { h ->
                requireView().findNavController().navigate(AddNewHarvestFragmentDirections.actionAddNewHarvestFragmentToEditHarvestFragment(h.id))
            })
        }
    }

//    private fun insertDataToDatabase() {
//        val title = binding.harvestTitle.text.toString()
//        val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
//        val date = Date(dateText)
//        val harvest = Harvest(0, title, date, 0,0,0,0.0,0.0,0.0)
//        mHarvestViewModel.addHarvest(harvest)
//        Toast.makeText(requireContext(), "Succesfully added!", Toast.LENGTH_SHORT).show()
//        requireView().findNavController().navigate(AddNewHarvestFragmentDirections.actionAddNewHarvestFragmentToEditHarvestFragment(harvest))
//    }

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        Log.i("AddNewHarvestFragment", "onAttach called")
//    }
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        Log.i("AddNewHarvestFragment", "onCreate called")
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        Log.i("AddNewHarvestFragment", "onViewCreated called")
//    }
//
//    override fun onStart() {
//        super.onStart()
//        Log.i("AddNewHarvestFragment", "onStart called")
//    }
//    override fun onResume() {
//        super.onResume()
//        Log.i("AddNewHarvestFragment", "onResume called")
//    }
//    override fun onPause() {
//        super.onPause()
//        Log.i("AddNewHarvestFragment", "onPause called")
//    }
//    override fun onStop() {
//        super.onStop()
//        Log.i("AddNewHarvestFragment", "onStop called")
//    }
//    override fun onDestroyView() {
//        super.onDestroyView()
//        Log.i("AddNewHarvestFragment", "onDestroyView called")
//    }
//    override fun onDetach() {
//        super.onDetach()
//        Log.i("AddNewHarvestFragment", "onDetach called")
//    }
}