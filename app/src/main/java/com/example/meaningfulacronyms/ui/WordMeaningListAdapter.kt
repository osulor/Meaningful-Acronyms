package com.example.meaningfulacronyms.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.meaningfulacronyms.R
import com.example.meaningfulacronyms.model.Acromine
import com.example.meaningfulacronyms.model.LongForm

class WordMeaningListAdapter : RecyclerView.Adapter<WordMeaningListAdapter.WordMeaningListViewHolder>(){
    private val differCallback = object: DiffUtil.ItemCallback<LongForm>(){
        override fun areItemsTheSame(oldItem: LongForm, newItem: LongForm): Boolean {
            return oldItem.lf == newItem.lf
        }
        override fun areContentsTheSame(oldItem: LongForm, newItem: LongForm): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this,differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WordMeaningListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.word_list_item,parent,false)
        return WordMeaningListViewHolder(view)
    }

    override fun getItemCount() = differ.currentList.size

    override fun onBindViewHolder(holder: WordMeaningListViewHolder, position: Int) {
        val acromineResponse = differ.currentList[position]

        holder.apply {
            wordMeaning.text = acromineResponse.lf
            wordFrequency.text = "Frequency: ${acromineResponse.freq}"
            wordDate.text = "Used since: ${acromineResponse.since}"
        }
    }

    inner class WordMeaningListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val wordMeaning: TextView = itemView.findViewById(R.id.word_meaning)
        val wordFrequency: TextView = itemView.findViewById(R.id.word_freq)
        val wordDate: TextView = itemView.findViewById(R.id.word_date)
    }
}