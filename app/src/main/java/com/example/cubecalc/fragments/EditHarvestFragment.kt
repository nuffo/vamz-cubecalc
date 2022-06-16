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
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cubecalc.R
import com.example.cubecalc.adapter.LogAdapter
import com.example.cubecalc.databinding.FragmentEditHarvestBinding
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.model.Log
import com.example.cubecalc.recyclerview.LogRecyclerViewInterface
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.viewmodel.LogViewModel
import com.example.cubecalc.viewmodel.LogViewModelFactory
import com.example.cubecalc.viewmodel.EditHarvestViewModel
import java.util.*

const val KEY_YEAR_EDIT = "year_key"
const val KEY_MONTH_EDIT = "month_key"
const val KEY_DAY_EDIT = "day_key"

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
    private val mEditHarvestViewModel: EditHarvestViewModel by activityViewModels()
    private lateinit var adapter : LogAdapter

    private var selectedYear = 0
    private var selectedMonth = 0
    private var selectedDay = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_edit_harvest, container, false)

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)

        binding.viewModel = mEditHarvestViewModel
        binding.lifecycleOwner = viewLifecycleOwner

        binding.harvestTitle.setText(args.harvest.title)

        if (savedInstanceState != null) {
            selectedYear = savedInstanceState.getInt(KEY_YEAR_EDIT, 0)
            selectedMonth = savedInstanceState.getInt(KEY_MONTH_EDIT, 0)
            selectedDay = savedInstanceState.getInt(KEY_DAY_EDIT, 0)
            binding.harvestDate.setText("$selectedDay.${selectedMonth + 1}.$selectedYear")
        } else {
            val calendar = Calendar.getInstance()
            calendar.time = mEditHarvestViewModel.date.value!!
            selectedYear = calendar.get(Calendar.YEAR)
            selectedMonth = calendar.get(Calendar.MONTH)
            selectedDay = calendar.get(Calendar.DAY_OF_MONTH)
            binding.harvestDate.text = "$selectedDay.${selectedMonth + 1}.$selectedYear"
        }

        binding.addNewLogButton.setOnClickListener { view : View -> view.findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAddNewLogFragment(args.harvest.id)) }

        adapter = LogAdapter(this)
        val recyclerView = binding.recyclerViewLogs
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        val logViewModelFactory = LogViewModelFactory(requireActivity().application, args.harvest.id)

        mLogViewModel = ViewModelProvider(this, logViewModelFactory).get(LogViewModel::class.java)

        mEditHarvestViewModel.newLogs.observe(viewLifecycleOwner, Observer { newLogs ->
            mLogViewModel.getDataWithHarvestId.observe(viewLifecycleOwner, Observer { oldLogs ->
                mEditHarvestViewModel.deletedLogs.observe(viewLifecycleOwner, Observer { deletedLogs ->
                    var allLogs = mutableListOf<Log>()
                    for (log in oldLogs) {
                        if (!(deletedLogs.contains(log))) {
                            allLogs.add(log)
                        }
                    }
                    allLogs.addAll(newLogs)
                    adapter.setData(allLogs)
                })
            })
        })

        binding.editHarvestButton.setOnClickListener {
            updateHarvest()
        }
        return binding.root
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putInt(KEY_YEAR_EDIT, selectedYear)
        outState.putInt(KEY_MONTH_EDIT, selectedMonth)
        outState.putInt(KEY_DAY_EDIT, selectedDay)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if(harvestChanged())
                    showAreYouSureDialog()
                else {
                    mEditHarvestViewModel.resetLists()
                    requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
                }
            }
        })

        binding.harvestDate.setOnClickListener {

            val listener = DatePickerDialog.OnDateSetListener { _, selectedYear, selectedMonth, selectedDay ->
                this.selectedYear = selectedYear
                this.selectedMonth = selectedMonth
                this.selectedDay = selectedDay

                val calendar = Calendar.getInstance()
                calendar.set(selectedYear, selectedMonth, selectedDay)
                val date : Date = calendar.time
                mEditHarvestViewModel.setDate(date)

                binding.harvestDate.text = "$selectedDay.${selectedMonth + 1}.$selectedYear"
            }

            val datePicker = DatePickerDialog(view.context, listener, selectedYear, selectedMonth, selectedDay)
            datePicker.datePicker.maxDate = Calendar.getInstance().timeInMillis
            datePicker.show()
        }
    }

    private fun harvestChanged(): Boolean {
        val oldTitle = args.harvest.title
        val newTitle = binding.harvestTitle.text.toString()
        val oldDate = args.harvest.dateToString("dd.MM.yyyy")
        val newDate = binding.harvestDate.text.toString()

        return !(newTitle == oldTitle && newDate == oldDate && mEditHarvestViewModel.logsListsAreEmpty())
    }

    private fun showAreYouSureDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes") { _, _ ->
            mEditHarvestViewModel.resetLists()
            requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setMessage("Are you sure to exit without saving?")
        builder.show()
    }

    private fun updateHarvest() {
        val title = binding.harvestTitle.text.toString()
        val dateText = "${selectedMonth + 1}/$selectedDay/$selectedYear"
        val date = Date(dateText)

        if (title.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter title of harvest!", Toast.LENGTH_SHORT).show()
        } else {
            mEditHarvestViewModel.newLogs.observe(viewLifecycleOwner, Observer { newLogs ->
                for (log in newLogs) {
                    mLogViewModel.addLog(log)
                }
            })
            mEditHarvestViewModel.deletedLogs.observe(viewLifecycleOwner, Observer { deletedLogs ->
                for (log in deletedLogs) {
                    mLogViewModel.deleteLog(log)
                }
            })
            mEditHarvestViewModel.resetLists()
            val spruceLogsCount = mEditHarvestViewModel.spruceLogsCount.value
            val beechLogsCount = mEditHarvestViewModel.beechLogsCount.value
            val firLogsCount = mEditHarvestViewModel.firLogsCount.value
            val spruceCubeMetres = mEditHarvestViewModel.spruceCubicMetres.value
            val beechCubeMetres = mEditHarvestViewModel.beechCubicMetres.value
            val firCubeMetres = mEditHarvestViewModel.firCubicMetres.value
            val updatedHarvest = Harvest(args.harvest.id,
                title,
                date,
                spruceLogsCount!!,
                beechLogsCount!!,
                firLogsCount!!,
                spruceCubeMetres!!,
                beechCubeMetres!!,
                firCubeMetres!!,
                args.harvest.createdAt)
            mHarvestViewModel.updateHarvest(updatedHarvest)
            Toast.makeText(requireContext(), "Succesfully updated harvest: ${title}", Toast.LENGTH_SHORT).show()
            requireView().findNavController().navigate(EditHarvestFragmentDirections.actionEditHarvestFragmentToAllHarvestsFragment())
        }
    }

    override fun onLogDelete(log: com.example.cubecalc.model.Log) {
        mLogViewModel.getDataWithHarvestId.observe(viewLifecycleOwner, Observer { logList ->
            var find = false

            for (item in logList) {
                if (item == log) {
                    find = true
                }
            }

            if (find)
                mEditHarvestViewModel.addToListOfDeletedLogs(log)
            else
                mEditHarvestViewModel.removeFromListOfNewLogs(log)

            adapter.removeLog(log)
            updateHarvestSummary(log)
        })
        Toast.makeText(requireContext(), "Log succesfully removed!", Toast.LENGTH_SHORT).show()
    }

    private fun updateHarvestSummary(log: Log) {
        when (log.type) {
            "SPRUCE" -> {
                mEditHarvestViewModel.decSpruceLogsCount()
                mEditHarvestViewModel.deductOfSpruceCubicMetres(log.cubeMetres)

                mEditHarvestViewModel.spruceLogsCount.observe(viewLifecycleOwner, Observer { binding.spruceLogsCount.text = it.toString() })
                mEditHarvestViewModel.spruceCubicMetres.observe(viewLifecycleOwner, Observer { binding.spruceMetresCount.text = String.format("%.2f", it) })
            }
            "BEECH" -> {
                mEditHarvestViewModel.decBeechLogsCount()
                mEditHarvestViewModel.deductOfBeechCubicMetres(log.cubeMetres)

                mEditHarvestViewModel.beechLogsCount.observe(viewLifecycleOwner, Observer { binding.beechLogsCount.text = it.toString() })
                mEditHarvestViewModel.beechCubicMetres.observe(viewLifecycleOwner, Observer { binding.beechMetresCount.text = String.format("%.2f", it) })
            }
            "FIR" -> {
                mEditHarvestViewModel.decFirLogsCount()
                mEditHarvestViewModel.deductOfFirCubicMetres(log.cubeMetres)

                mEditHarvestViewModel.firLogsCount.observe(viewLifecycleOwner, Observer { binding.firLogsCount.setText(it.toString()) })
                mEditHarvestViewModel.firCubicMetres.observe(viewLifecycleOwner, Observer { binding.firMetresCount.text = String.format("%.2f", it) })

            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        android.util.Log.i("TitleFragment", "onAttach called")
        mEditHarvestViewModel.setDate(args.harvest.date)
        mEditHarvestViewModel.setSpruceLogsCount(args.harvest.spruceLogsCount)
        mEditHarvestViewModel.setSpruceCubicMetres(args.harvest.spruceCubeMetres)
        mEditHarvestViewModel.setBeechLogsCount(args.harvest.beechLogsCount)
        mEditHarvestViewModel.setBeechCubicMetres(args.harvest.beechCubeMetres)
        mEditHarvestViewModel.setFirLogsCount(args.harvest.firLogsCount)
        mEditHarvestViewModel.setFirCubicMetres(args.harvest.firCubeMetres)
    }
}