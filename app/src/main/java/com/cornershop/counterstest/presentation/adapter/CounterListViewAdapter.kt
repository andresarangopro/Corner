package com.cornershop.counterstest.presentation.adapter


import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.cornershop.counterstest.R
import com.cornershop.counterstest.databinding.ItemCounterBinding
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toCounterDomain
import com.cornershop.counterstest.presentation.utils.DIFF_CALLBACK
import kotlinx.android.synthetic.main.item_counter.view.*

class CounterListViewAdapter(private val listenerInc:(CounterAdapter)->Unit,
                             private val listenerDec:(CounterAdapter)->Unit,
                             private val listenerSelect:(CounterAdapter)->Unit):
    ListAdapter<CounterAdapter, CounterListViewAdapter.CounterViewHolder>(
        DIFF_CALLBACK) {

    class CounterViewHolder(itemView: ItemCounterBinding,
                            var listenerInc:(CounterAdapter)->Unit,
                            var listenerDec:(CounterAdapter)->Unit,
                            var listenerSelect:(CounterAdapter)->Unit) : RecyclerView.ViewHolder(itemView.root){

        val transparentOrange: Drawable? = ContextCompat.getDrawable(itemView.root.context, R.drawable.background_counter_selected)
        val grayColor: Int = ContextCompat.getColor(itemView.root.context,R.color.gray)
        val transparentColor: Int = ContextCompat.getColor(itemView.root.context,R.color.transparent)

        fun bind(counter: CounterAdapter){
            itemView.tvTitle.text = counter?.title
            itemView.tvCount.text = "${counter?.count}"

            if(isBiggerThanZero(counter))
                itemView.tvCount.setTextColor(Color.BLACK)
            else
                itemView.tvCount.setTextColor(grayColor)

            setVisibilitySelect(counter)

            itemView.tvTitle.setOnClickListener{
                when(counter?.selected){
                    true->{counter.selected = false}
                    false->{counter?.selected = true}
                }
                setVisibilitySelect(counter)
                listenerSelect(counter)
            }

            itemView.ivDec.setOnClickListener {
                if(isBiggerThanZero(counter)) {
                    listenerDec(counter)
                }
            }

            itemView.ivInc.setOnClickListener {
                listenerInc(counter)
            }
        }

        fun setVisibilitySelect(counter:CounterAdapter){
            if(counter?.selected == true) {
                itemView.clItemCounter.background = transparentOrange
                itemView.gpCounterHandler.visibility = View.GONE
                itemView.ivCheck.visibility = View.VISIBLE
            }else {
                itemView.clItemCounter.setBackgroundColor(transparentColor)
                itemView.gpCounterHandler.visibility = View.VISIBLE
                itemView.ivCheck.visibility = View.GONE

            }
        }

        private fun isBiggerThanZero(item: CounterAdapter?) =
            item?.count!! > 0
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CounterViewHolder {
        var binding = ItemCounterBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false)

        return CounterViewHolder(binding,
            listenerInc,listenerDec,listenerSelect)
    }


    override fun onBindViewHolder(holder: CounterViewHolder, position: Int) {
        holder.bind(getItem(position));
    }

}

