package com.cornershop.counterstest.presentation.parcelable

import androidx.databinding.BaseObservable

class CounterAdapter(
    val id:Int,
    val id_remote:String,
    val title:String,
    val count:Int,
    var selected:Boolean
){

     override fun toString(): String {
         return title
     }

}

