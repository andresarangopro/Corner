package com.cornershop.counterstest.presentation.parcelable

import androidx.databinding.BaseObservable

class CounterAdapter(
    val id:Int,
    val id_remote:String,
    val title:String,
    val count:Int,
    var selected:Boolean
){

    override fun equals(other: Any?): Boolean {
        if(other is CounterAdapter){
            if(other.id == id && other.id_remote == id_remote && other.title == title && other.count == count && other.selected == selected)
                return true
        }
        return false
    }

     override fun toString(): String {
         return title
     }

}

