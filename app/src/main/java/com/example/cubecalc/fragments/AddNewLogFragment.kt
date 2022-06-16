package com.example.cubecalc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cubecalc.R
import com.example.cubecalc.databinding.FragmentAddNewLogBinding
import com.example.cubecalc.model.Log
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.viewmodel.LogViewModel
import com.example.cubecalc.viewmodel.LogViewModelFactory
import com.example.cubecalc.viewmodel.EditHarvestViewModel
import java.util.*
import kotlin.math.pow

/**
 * A simple [Fragment] subclass.
 * Use the [AddNewLogFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AddNewLogFragment : Fragment() {

    private lateinit var binding: FragmentAddNewLogBinding
    private val args by navArgs<AddNewLogFragmentArgs>()
    private val mEditHarvestViewModel: EditHarvestViewModel by activityViewModels()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_log, container, false)

        binding.addNewLogButton.setOnClickListener { addNewLog() }
        binding.backButton.setOnClickListener { view : View -> view.findNavController().popBackStack() }

        return binding.root
    }

    private fun addNewLog() {
        val length = binding.logLength.text.toString()
        val diameter = binding.logDiameter.text.toString()
        val checked = binding.radioGroupLogType.checkedRadioButtonId
        val type = setLogType(checked)

        when {
            length.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter length!", Toast.LENGTH_SHORT).show()
            }
            diameter.isEmpty() -> {
                Toast.makeText(requireContext(), "Please enter diameter!", Toast.LENGTH_SHORT).show()
            }
            else -> {
                val radius = (diameter.toInt() / 100.0) / 2.0;
                val cubeMetres = Math.PI * radius.pow(2) * length.toInt()
                val log = Log(0, args.harvestId, type, length.toInt(), diameter.toInt(), cubeMetres)

                mEditHarvestViewModel.addToListOfNewLogs(log)
                updateHarvestSummary(log)
                Toast.makeText(requireContext(), "Log succesfully added!", Toast.LENGTH_SHORT).show()
                requireView().findNavController().popBackStack()
            }
        }
    }

   private fun updateHarvestSummary(log: Log) {
        when (log.type) {
            "SPRUCE" -> {
                mEditHarvestViewModel.incSpruceLogsCount()
                mEditHarvestViewModel.addToSpruceCubicMetres(log.cubeMetres)
            }
            "BEECH" -> {
                mEditHarvestViewModel.incBeechLogsCount()
                mEditHarvestViewModel.addToBeechCubicMetres(log.cubeMetres)
            }
            "FIR" -> {
                mEditHarvestViewModel.incFirLogsCount()
                mEditHarvestViewModel.addToFirCubicMetres(log.cubeMetres)
            }
        }
   }

    private fun setLogType(checked: Int): String {
        return when (checked) {
            binding.radioSpruce.id -> "SPRUCE"
            binding.radioBeech.id -> "BEECH"
            binding.radioFir.id -> "FIR"
            else -> "NAN"
        }
    }
}