package com.example.cubecalc.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.cubecalc.R
import com.example.cubecalc.fragments.AllHarvestsFragmentDirections
import com.example.cubecalc.model.Harvest
import com.example.cubecalc.recyclerview.HarvestRecyclerViewInterface

/**
 * Adapter for the [RecyclerView] in [AllHarvestFragment]. Displays [Harvest] data object.
 */
class HarvestAdapter(private val harvestRecyclerViewInterface: HarvestRecyclerViewInterface) : RecyclerView.Adapter<HarvestAdapter.ItemViewHolder>() {

    private var harvestList = emptyList<Harvest>()

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder.
    // Each data item is just an Harvest object.
    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val title: TextView
        val date: TextView
        val logsCount: TextView
        val cubicMetreCount: TextView
        val editButton: AppCompatImageButton
        val deleteButton: AppCompatImageButton

        init {
            title = view.findViewById(R.id.harvest_title)
            date = view.findViewById(R.id.harvest_date)
            logsCount = view.findViewById(R.id.harvest_logs_count)
            cubicMetreCount = view.findViewById(R.id.harvest_cubic_metre_count)
            editButton = view.findViewById(R.id.harvest_edit_button)
            deleteButton = view.findViewById(R.id.harvest_delete_button)
        }
    }

    /**
     * Create new views (invoked by the layout manager)
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.harvest_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    /**
     * Replace the contents of a view (invoked by the layout manager)
     */
    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = harvestList[position]
//        holder.itemView.harvest_title.text = item.title.toString()
//        holder.itemView.harvest_date.text = item.date.toString()
        holder.title.text = item.title.toString()
        holder.date.text = item.dateToString("dd.MM.yyyy")
        holder.logsCount.text = (item.spruceLogsCount + item.beechLogsCount + item.firLogsCount).toString()
        holder.cubicMetreCount.text = String.format("%.2f", item.spruceCubeMetres + item.beechCubeMetres + item.firCubeMetres)

        holder.editButton.setOnClickListener {
            val action = AllHarvestsFragmentDirections.actionAllHarvestsFragmentToEditHarvestFragment(item.id)
            holder.itemView.findNavController().navigate(action)
        }

        holder.deleteButton.setOnClickListener {
            deleteHarvest(item, holder.itemView.context)
        }
    }

    /**
     * Return the size of your dataset (invoked by the layout manager)
     */
    override fun getItemCount() = harvestList.size

    fun setData(harvest: List<Harvest>) {
        this.harvestList = harvest
        notifyDataSetChanged()
    }

    private fun deleteHarvest(item: Harvest, context: Context) {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes") { _, _ ->
            harvestRecyclerViewInterface.onHarvestDelete(item)
            notifyDataSetChanged()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete ${item.title}")
        builder.setMessage("Are you sure to delete this harvest?")
        builder.show()
    }
}