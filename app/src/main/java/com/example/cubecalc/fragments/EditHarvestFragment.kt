package com.example.cubecalc.fragments

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.OnBackPressedCallback
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cubecalc.R
import com.example.cubecalc.adapter.LogAdapter
import com.example.cubecalc.databinding.FragmentEditHarvestBinding
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.recyclerview.LogRecyclerViewInterface
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.viewmodel.LogViewModel
import com.example.cubecalc.viewmodel.LogViewModelFactory
import java.util.*


/**
 * A simple [Fragment] subclass.
 * Use the [EditHarvestFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditHarvestFragment : Fragment(), LogRecyclerViewInterface {

    private lateinit var binding: FragmentEditHarvestBinding
    private val args by navArgs<EditHarvestFragmentArgs>()
    private lateinit var mHarvestViewModel: HarvestViewModel
    private lateinit var mLogViewModel: LogViewModel

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    private lateinit var logList: LiveData<List<com.example.cubecalc.model.Log>>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_harvest, container, false)
        android.util.Log.i("EditHarvestFragment", "onCreateView Called")

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)

        val calendar = Calendar.getInstance()

        mHarvestViewModel.getDataWithId(args.harvestId).observe(viewLifecycleOwner, Observer<Harvest> { harvest ->
            binding.harvestTitle.setText(harvest.title)
            binding.harvestDate.setText(harvest.dateToString("dd.MM.yyyy"))
            binding.harvest = harvest

            calendar.time = harvest.date

            binding.addNewLogButton.setOnClickListener { view : View -> view.findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAddNewLogFragment(harvest)) }
        })

        selectedYear = calendar.get(Calendar.YEAR)
        selectedMonth = calendar.get(Calendar.MONTH)
        selectedDay = calendar.get(Calendar.DAY_OF_MONTH)

        val adapter = LogAdapter(this)
        val recyclerView = binding.recyclerViewLogs
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val logViewModelFactory = LogViewModelFactory(requireActivity().application, args.harvestId)

        mLogViewModel = ViewModelProvider(this, logViewModelFactory).get(LogViewModel::class.java)
        mLogViewModel.getDataWithHarvestId.observe(viewLifecycleOwner, Observer { log ->
            adapter.setData(log)
        })

        logList = mLogViewModel.getDataWithHarvestId

        binding.editHarvestButton.setOnClickListener {
            updateHarvest()
        }
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

//    override fun onAttach(context: Context) {
//        super.onAttach(context)
//        showAreYouSureDialog()
////        val callback: OnBackPressedCallback = object : OnBackPressedCallback(
////            true // default to enabled
////        ) {
////            override fun handleOnBackPressed() {
////                if (args.new) {
////
////                } else {
////                    true
////                }
////            }
////        }
////        requireActivity().onBackPressedDispatcher.addCallback(
////            this,  // LifecycleOwner
////            callback
////        )
//    }

    private fun showAreYouSureDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes") { _, _ ->
            requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
        }
        builder.setNegativeButton("No") { _, _ -> }
        //builder.setTitle("")
        builder.setMessage("Are you sure to exit without saving?")
        builder.show()
    }

//    private fun addNewHarvest(harvest: Harvest) {
//        val title = binding.harvestTitle.text.toString()
//        val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
//        val date = Date(dateText)
//        //mHarvestViewModel.addHarvest(harvest)
//
//        requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
//    }

    private fun updateHarvest() {
        val title = binding.harvestTitle.text.toString()
        val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
        val date = Date(dateText)

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter title of harvest!", Toast.LENGTH_SHORT).show()
        } else {
            mHarvestViewModel.getDataWithId(args.harvestId).observe(viewLifecycleOwner, Observer<Harvest> { harvest ->
                val updatedHarvest = Harvest(args.harvestId,
                    title,
                    date,
                    harvest.spruceLogsCount,
                    harvest.beechLogsCount,
                    harvest.firLogsCount,
                    harvest.spruceCubeMetres,
                    harvest.beechCubeMetres,
                    harvest.firCubeMetres,
                    harvest.createdAt)
                mHarvestViewModel.updateHarvest(updatedHarvest)
            })
            Toast.makeText(requireContext(), "Succesfully updated harvest: ${title}", Toast.LENGTH_SHORT).show()
            requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
        }
    }

    override fun onLogDelete(log: com.example.cubecalc.model.Log) {
        mLogViewModel.deleteLog(log)
        mHarvestViewModel.getDataWithId(args.harvestId).observe(viewLifecycleOwner, Observer<Harvest>() { h ->
            updateHarvestSummary(h, log)
        })

        Toast.makeText(requireContext(), "Log succesfully removed!", Toast.LENGTH_SHORT).show()
    }

    private fun updateHarvestSummary(harvest: Harvest, log: com.example.cubecalc.model.Log) {
        val updatedHarvest: Harvest
        val cubeMetres = log.cubeMetres
        updatedHarvest = when {
            log.type.equals("SPRUCE") -> {
                Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount - 1, harvest.beechLogsCount, harvest.firLogsCount, harvest.spruceCubeMetres - cubeMetres, harvest.beechCubeMetres, harvest.firCubeMetres, harvest.createdAt)
            }
            log.type.equals("BEECH") -> {
                Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount, harvest.beechLogsCount - 1, harvest.firLogsCount, harvest.spruceCubeMetres, harvest.beechCubeMetres - cubeMetres, harvest.firCubeMetres, harvest.createdAt)
            }
            else -> {
                Harvest(harvest.id, harvest.title, harvest.date, harvest.spruceLogsCount, harvest.beechLogsCount, harvest.firLogsCount - 1, harvest.spruceCubeMetres, harvest.beechCubeMetres, harvest.firCubeMetres - cubeMetres, harvest.createdAt)
            }
        }
        mHarvestViewModel.updateHarvest(updatedHarvest)

        binding.spruceLogsCount.text = updatedHarvest.spruceLogsCount.toString()
        binding.spruceMetresCount.text = String.format("%.2f", updatedHarvest.spruceCubeMetres)
        binding.beechLogsCount.text = updatedHarvest.beechLogsCount.toString()
        binding.beechMetresCount.text = String.format("%.2f", updatedHarvest.beechCubeMetres)
        binding.firLogsCount.text = updatedHarvest.firLogsCount.toString()
        binding.firMetresCount.text = String.format("%.2f", updatedHarvest.firCubeMetres)
    }
}