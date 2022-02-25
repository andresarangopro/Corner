package com.cornershop.counterstest.presentation.utils

import android.widget.SearchView
import androidx.annotation.MainThread
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter

inline fun SearchView.onQueryTextChanged(crossinline onQueryTextChanged: (String) -> Unit) {
    setOnQueryTextListener(object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String): Boolean {
            onQueryTextChanged(query)
            return false
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            return false
        }
    })
}

@MainThread
fun <T> MutableLiveData<T>.mutate(mutator: T.() -> Unit) {
    this.value = this.value?.apply(mutator)
}

@MainThread
fun <T> MutableLiveData<T>.modifyValue(transform: T.() -> T) {
    this.value = this.value?.run(transform)
}

val DIFF_CALLBACK: DiffUtil.ItemCallback<CounterAdapter> = object :
    DiffUtil.ItemCallback<CounterAdapter>() {
    override fun areItemsTheSame(oldItem: CounterAdapter, newItem: CounterAdapter): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: CounterAdapter, newItem: CounterAdapter): Boolean {
        return oldItem == newItem
    }

}