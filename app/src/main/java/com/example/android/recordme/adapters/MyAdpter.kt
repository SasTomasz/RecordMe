package com.example.android.recordme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recordme.databinding.ListOfRecordingsBinding

class MyAdapter(private val myDataset: List<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    class MyViewHolder(val binding: ListOfRecordingsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ListOfRecordingsBinding
            .inflate(inflater, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = myDataset[position]
        holder.binding.recordItem.text = item
    }

    override fun getItemCount(): Int {
        return myDataset.size
    }
// TODO 04 Make recyclerview working:
//  - Set recyclerview to show all recordings
//  - Add new feature -> when user click current name, he will hear record

}