package com.cornershop.counterstest.presentation.adapter

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterListAdapter
import okhttp3.internal.filterList
import kotlin.properties.Delegates

class CounterRecyclerViewAdapter(
    private var context: Context,
    private var values: List<CounterListAdapter>?,
    private val listenerInc:(String?)->Unit,
    private val listenerDec:(String?)->Unit,
    private val listenerSelect:(List<CounterListAdapter>?)->Unit
) : RecyclerView.Adapter<CounterRecyclerViewAdapter.ViewHolder>() {

    var tempSelectedCounterList= ArrayList<CounterListAdapter>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
         var binding = ItemCounterBinding.inflate(LayoutInflater.from(parent.context),
         parent, false)

        return ViewHolder(binding)
    }

    fun updateData(newData: List<CounterListAdapter>?) {
        values  = newData
        tempSelectedCounterList = ArrayList<CounterListAdapter>()
        notifyDataSetChanged()
    }

    fun unselectAllCounters(){
        tempSelectedCounterList = ArrayList<CounterListAdapter>()
        values?.forEach {
            it.selected = false
        }
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

        if(item?.selected == true) {
            holder.itemCounter.background = holder.transparentOrange
            holder.gpCounterHandler.visibility = View.GONE
            holder.ivCheck.visibility = View.VISIBLE

        } else {
            holder.itemCounter.setBackgroundColor(Color.TRANSPARENT)
            holder.gpCounterHandler.visibility = View.VISIBLE
            holder.ivCheck.visibility = View.GONE
        }

        holder.title.setOnClickListener{
                if(item?.selected == true) {
                    item?.selected = false
                    tempSelectedCounterList.remove(item)
                }else {
                    item?.selected = true
                    tempSelectedCounterList.add(item!!)
                }
            notifyDataSetChanged()
            listenerSelect(tempSelectedCounterList)
        }

        holder.increase.setOnClickListener {
            listenerInc(item?.id)
        }

        holder.decrease.setOnClickListener {
            if(isBiggerThanZero(item))
                listenerDec(item?.id)
        }
    }

    private fun isBiggerThanZero(item: CounterListAdapter?) =
        item?.count!! > 0

    override fun getItemCount(): Int = values?.size?:0

    inner class ViewHolder(view: ItemCounterBinding) : RecyclerView.ViewHolder(view.root) {
        val title: TextView = view.tvTitle
        val count: TextView = view.tvCount
        val increase: ImageView = view.ivInc
        val decrease: ImageView = view.ivDec
        val itemCounter = view.clItemCounter
        val gpCounterHandler:Group = view.gpCounterHandler
        val ivCheck:ImageView = view.ivCheck
        val transparentOrange: Drawable = ContextCompat.getDrawable(context,R.drawable.background_counter_selected)!!
        val grayColor: Int = R.color.gray

        val root: View =  view.root
    }

}