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
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.utils.DIFF_CALLBACK

class CounterListViewAdapter(private val listenerInc:(CounterAdapter)->Unit,
                             private val listenerDec:(CounterAdapter)->Unit,
                             private val listenerSelect:(CounterAdapter)->Unit):
    ListAdapter<CounterAdapter, CounterListViewAdapter.CounterViewHolder>(
        DIFF_CALLBACK) {

    class CounterViewHolder(val binding: ItemCounterBinding,
                            var listenerInc:(CounterAdapter)->Unit,
                            var listenerDec:(CounterAdapter)->Unit,
                            var listenerSelect:(CounterAdapter)->Unit) : RecyclerView.ViewHolder(binding.root){

        val transparentOrange: Drawable? = ContextCompat.getDrawable(binding.root.context, R.drawable.background_counter_selected)
        val grayColor: Int = ContextCompat.getColor(binding.root.context,R.color.gray)
        val transparentColor: Int = ContextCompat.getColor(binding.root.context,R.color.transparent)

        fun bind(counter: CounterAdapter){
            binding.tvCounterName.text = counter?.title
            binding.tvCount.text = "${counter?.count}"

            if(isBiggerThanZero(counter))
                binding.tvCount.setTextColor(Color.BLACK)
            else
                binding.tvCount.setTextColor(grayColor)

            setVisibilitySelect(counter)

            binding.tvCounterName.setOnClickListener{
                when(counter?.selected){
                    true->{counter.selected = false}
                    false->{counter?.selected = true}
                }
                setVisibilitySelect(counter)
                listenerSelect(counter)
            }

            binding.ivDec.setOnClickListener {
                if(isBiggerThanZero(counter)) {
                    listenerDec(counter)
                }
            }

            binding.ivInc.setOnClickListener {
                listenerInc(counter)
            }
        }

        fun setVisibilitySelect(counter:CounterAdapter){
            if(counter?.selected == true) {
                binding.clItemCounter.background = transparentOrange
                binding.gpCounterHandler.visibility = View.GONE
                binding.ivCheck.visibility = View.VISIBLE
            }else {
                binding.clItemCounter.setBackgroundColor(transparentColor)
                binding.gpCounterHandler.visibility = View.VISIBLE
                binding.ivCheck.visibility = View.GONE

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

