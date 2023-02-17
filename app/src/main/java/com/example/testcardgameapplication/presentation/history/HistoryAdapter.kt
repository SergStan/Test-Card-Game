package com.example.testcardgameapplication.presentation.history

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.testcardgameapplication.databinding.HeadHistoryBinding
import com.example.testcardgameapplication.databinding.RowHistoryBinding

class HistoryAdapter(private val layoutInflater: LayoutInflater) :
    ListAdapter<ListItem, RecyclerView.ViewHolder>(ItemStateDiffer) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ListItem.TYPE_HEADER ->
                HeaderHolder(HeadHistoryBinding.inflate(layoutInflater))
            else ->
                RowHolder(RowHistoryBinding.inflate(layoutInflater))
        }
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
            ListItem.TYPE_HEADER -> (holder as HeaderHolder).bind(
                getItem(position) as HeaderState
            )
            ListItem.TYPE_ROW -> (holder as RowHolder).bind(
                getItem(position) as RowState
            )
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).type
    }


    inner class RowHolder(private val binding: RowHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(rowState: RowState) {
            with(binding) {
                row = rowState
                executePendingBindings()
            }
        }
    }

    inner class HeaderHolder(private val binding: HeadHistoryBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(headerState: HeaderState) {
            with(binding) {
                header = headerState
                executePendingBindings()
            }
        }
    }
}

object ItemStateDiffer : DiffUtil.ItemCallback<ListItem>() {
    override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem === newItem
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean {
        return oldItem == newItem
    }
}

