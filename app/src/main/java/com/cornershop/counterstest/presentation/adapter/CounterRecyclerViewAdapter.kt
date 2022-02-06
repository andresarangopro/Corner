package com.cornershop.counterstest.presentation.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.entities.Counter

class CounterRecyclerViewAdapter(
    private val values: List<Counter>?,
    private val listener:(String?)->Unit
) : RecyclerView.Adapter<CounterRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var binding = ItemCounterBinding.inflate(LayoutInflater.from(parent.context),
         parent, false)

        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)

        holder.title.text = item?.title
        holder.count.text = "${item?.count}"

        holder.root.setOnClickListener{
                listener(item?.id)
        }
    }

    override fun getItemCount(): Int = values?.size?:0

    inner class ViewHolder(view: ItemCounterBinding) : RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.tvTitle
        val count: TextView = view.tvCount
        val increase: ImageView = view.ivInc
        val decrease: ImageView = view.ivDec
        val root: View =  view.root
    }

}