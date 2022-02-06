package com.cornershop.counterstest.presentation.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.entities.Counter
import kotlin.properties.Delegates

class CounterRecyclerViewAdapter(
    private var values: List<Counter>?,
    private val listener:(String?)->Unit,
    private val listenerInc:(String?)->Unit,
    private val listenerDec:(String?)->Unit,
) : RecyclerView.Adapter<CounterRecyclerViewAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var binding = ItemCounterBinding.inflate(LayoutInflater.from(parent.context),
         parent, false)

        return ViewHolder(binding)
    }

    fun updateData(newData: List<Counter>?) {
        values  = newData
        notifyDataSetChanged()
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = values?.get(position)

        holder.title.text = item?.title
        holder.count.text = "${item?.count}"

        if(isBiggerThanZero(item))
            holder.count.setTextColor(Color.BLACK)
        else
            holder.count.setTextColor(holder.grayColor)

        holder.root.setOnClickListener{
                listener(item?.id)
        }

        holder.increase.setOnClickListener {
            listenerInc(item?.id)
        }

        holder.decrease.setOnClickListener {
            if(isBiggerThanZero(item))
                listenerDec(item?.id)
        }
    }

    private fun isBiggerThanZero(item: Counter?) =
        item?.count!! > 0

    override fun getItemCount(): Int = values?.size?:0

    inner class ViewHolder(view: ItemCounterBinding) : RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.tvTitle
        val count: TextView = view.tvCount
        val increase: ImageView = view.ivInc
        val decrease: ImageView = view.ivDec
        val blackColor: Int = R.color.orange
        val grayColor: Int = R.color.gray

        val root: View =  view.root
    }

}