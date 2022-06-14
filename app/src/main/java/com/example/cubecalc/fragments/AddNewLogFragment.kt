package com.example.cubecalc.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cubecalc.R
import com.example.cubecalc.databinding.FragmentAddNewLogBinding
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.model.Log
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.viewmodel.LogViewModel
import com.example.cubecalc.viewmodel.LogViewModelFactory
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
    private lateinit var mLogViewModel: LogViewModel
    private lateinit var mHarvestViewModel: HarvestViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_new_log, container, false)

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)
        val logViewModelFactory = LogViewModelFactory(requireActivity().application, args.harvest.id)
        mLogViewModel = ViewModelProvider(this, logViewModelFactory).get(LogViewModel::class.java)

        binding.addNewLogButton.setOnClickListener { addNewLog() }
        binding.backButton.setOnClickListener { view : View -> view.findNavController().navigate(AddNewLogFragmentDirections.actionAddNewLogFragmentToEditHarvestFragment(args.harvest.id)) }

        return binding.root
    }

    private fun addNewLog() {
        val length = binding.logLength.text.toString()
        val diameter = binding.logDiameter.text.toString()
        val checked = binding.radioGroupLogType.checkedRadioButtonId
        val type = setLogType(checked)
        if (length.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter length!", Toast.LENGTH_SHORT).show()

        } else if (diameter.isEmpty()){
            Toast.makeText(requireContext(), "Please enter diameter!", Toast.LENGTH_SHORT).show()
        } else {
            val radius = (diameter.toInt() / 100.0) / 2.0;
            val cubeMetres = Math.PI * radius.pow(2) * length.toInt()
            val log = Log(0, args.harvest.id, type, length.toInt(), diameter.toInt(), cubeMetres)
            mLogViewModel.addLog(log)
            // val updatedHarvest = updateHarvestSummary(log, cubeMetres)
            updateHarvestSummary(log, cubeMetres)
            Toast.makeText(requireContext(), "Log succesfully added!", Toast.LENGTH_SHORT).show()
            requireView().findNavController().navigate(AddNewLogFragmentDirections.actionAddNewLogFragmentToEditHarvestFragment(args.harvest.id))
        }
    }

    private fun updateHarvestSummary(log: Log, cubeMetres: Double) {
        val harvest = args.harvest
        val updatedHarvest: Harvest
        if (log.type.equals("SPRUCE")) {
            updatedHarvest = Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount + 1, harvest.beechLogsCount, harvest.firLogsCount, harvest.spruceCubeMetres + cubeMetres, harvest.beechCubeMetres, harvest.firCubeMetres, harvest.createdAt)
        } else if (log.type.equals("BEECH")) {
            updatedHarvest = Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount, harvest.beechLogsCount + 1, harvest.firLogsCount, harvest.spruceCubeMetres, harvest.beechCubeMetres + cubeMetres, harvest.firCubeMetres, harvest.createdAt)
        } else {
            updatedHarvest = Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount, harvest.beechLogsCount, harvest.firLogsCount + 1, harvest.spruceCubeMetres, harvest.beechCubeMetres, harvest.firCubeMetres + cubeMetres, harvest.createdAt)
        }
        mHarvestViewModel.updateHarvest(updatedHarvest)
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