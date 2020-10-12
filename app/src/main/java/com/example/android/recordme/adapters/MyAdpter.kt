package com.example.android.recordme.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.recordme.data.Record
import com.example.android.recordme.databinding.ListOfRecordingsBinding

class MyAdapter : ListAdapter<Record, MyAdapter.MyViewHolder>(RecordDiffCallback()) {

    class MyViewHolder private constructor(val binding: ListOfRecordingsBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Record) {
            binding.recordItem.text = item.recordName
        }

        companion object {
            fun from(parent: ViewGroup): MyViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                val binding = ListOfRecordingsBinding
                    .inflate(layoutInflater, parent, false)

                return MyViewHolder(binding)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder.from(parent)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

// TODO 04 Make recyclerview working:
//  - Add new feature -> when user click current name, he will hear record

}

/**
 * Callback for calculating the diff between two non-null items in a list.
 *
 * Used by ListAdapter to calculate the minumum number of changes between and old list and a new
 * list that's been passed to `submitList`.
 */
class RecordDiffCallback : DiffUtil.ItemCallback<Record>() {
    override fun areItemsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem.recordId == newItem.recordId
    }

    override fun areContentsTheSame(oldItem: Record, newItem: Record): Boolean {
        return oldItem == newItem
    }
}