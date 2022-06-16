package com.example.cubecalc.fragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.cubecalc.R
import com.example.cubecalc.adapter.HarvestAdapter
import com.example.cubecalc.viewmodel.HarvestViewModel
import com.example.cubecalc.databinding.FragmentAllHarvestsBinding
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.recyclerview.HarvestRecyclerViewInterface

/**
 * A simple [Fragment] subclass.
 * Use the [AllHarvestsFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class AllHarvestsFragment : Fragment(), HarvestRecyclerViewInterface {

    private lateinit var mHarvestViewModel: HarvestViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        val binding: FragmentAllHarvestsBinding = DataBindingUtil.inflate<FragmentAllHarvestsBinding>(inflater, R.layout.fragment_all_harvests, container, false)

        val adapter = HarvestAdapter(this)
        val recyclerView = binding.recyclerViewHarvests
        recyclerView.adapter = adapter
        recyclerView.setHasFixedSize(true)

        mHarvestViewModel = ViewModelProvider(this).get(HarvestViewModel::class.java)
        mHarvestViewModel.getAllData.observe(viewLifecycleOwner, Observer { harvest ->
            adapter.setData(harvest)
            binding.noHarvests.visibility = if (adapter.itemCount == 0) View.VISIBLE else View.GONE
        })

        binding.addNewHarvestButton.setOnClickListener { this.findNavController().navigate(AllHarvestsFragmentDirections.actionAllHarvestsFragmentToAddNewHarvestFragment()) }

        return binding.root
    }

    override fun onHarvestDelete(harvest: Harvest) {
        mHarvestViewModel.deleteHarvest(harvest)
        Toast.makeText(requireContext(), "Succesfully removed harvest: ${harvest.title}!", Toast.LENGTH_SHORT).show()
    }
}