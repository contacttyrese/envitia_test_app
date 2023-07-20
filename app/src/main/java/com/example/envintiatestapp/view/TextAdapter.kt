package com.example.envintiatestapp.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.envintiatestapp.databinding.ItemviewSavedTextBinding
import com.example.envintiatestapp.model.CapturedText

class TextAdapter(private val textList: List<CapturedText>) : RecyclerView.Adapter<TextAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemviewSavedTextBinding.inflate(
            LayoutInflater.from(parent.context)
        )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val capturedText = textList[position]
        holder.bind(capturedText)
    }

    override fun getItemCount(): Int {
        val limit = 30
        return when (textList.size > limit) {
            true -> limit
            false -> textList.size
        }
    }

    class ViewHolder(private val binding: ItemviewSavedTextBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(capturedText: CapturedText) = with(binding) {
            val text = itemviewTextId.text.toString()
            itemviewTextId.text = text.format(capturedText.time, capturedText.text)
        }
    }
}