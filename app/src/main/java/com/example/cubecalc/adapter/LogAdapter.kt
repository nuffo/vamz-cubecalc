package com.example.cubecalc.adapter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageButton
import androidx.recyclerview.widget.RecyclerView
import com.example.cubecalc.R
import com.example.cubecalc.model.Log
import com.example.cubecalc.recyclerview.HarvestRecyclerViewInterface
import com.example.cubecalc.recyclerview.LogRecyclerViewInterface
import org.w3c.dom.Text

class LogAdapter(private val logRecyclerViewInterface: LogRecyclerViewInterface) : RecyclerView.Adapter<LogAdapter.ItemViewHolder>() {

    private var logList = emptyList<Log>()

    class ItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val type: TextView
        val length: TextView
        val diameter: TextView
        val cubeMetres: TextView
        val deleteButton: AppCompatImageButton

        init {
            type = view.findViewById(R.id.log_type)
            length = view.findViewById(R.id.log_length_number)
            diameter = view.findViewById(R.id.log_diameter_number)
            cubeMetres = view.findViewById(R.id.log_cubic_metres_number)
            deleteButton = view.findViewById(R.id.log_delete_button)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val adapterLayout = LayoutInflater.from(parent.context)
            .inflate(R.layout.log_item, parent, false)
        return ItemViewHolder(adapterLayout)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = logList[position]
        holder.type.text = item.type.toString()
        holder.length.text = item.length.toString()
        holder.diameter.text = item.diameter.toString()
        holder.cubeMetres.text = String.format("%.2f", item.cubeMetres)

        holder.deleteButton.setOnClickListener {
            deleteLog(item, holder.itemView.context)
        }
    }

    override fun getItemCount() = logList.size

    fun setData(log: List<Log>) {
        this.logList = log
        notifyDataSetChanged()
    }

    private fun deleteLog(item: Log, context: Context?) {
        val builder = AlertDialog.Builder(context)
        builder.setPositiveButton("Yes") { _, _ ->
            logRecyclerViewInterface.onLogDelete(item)
            notifyDataSetChanged()
        }
        builder.setNegativeButton("No") { _, _ -> }
        builder.setTitle("Delete log")
        builder.setMessage("Are you sure to delete log?")
        builder.show()
    }
}
