package com.example.android.recordme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recordme.databinding.ListOfRecordingsBinding

class MyAdapter(private val myDataset: List<String>) :
    RecyclerView.Adapter<MyAdapter.MyViewHolder>() { //todo check sleep app and remeberer hot to use recyclerview

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

}