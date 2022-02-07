package com.cornershop.counterstest.presentation.parcelable

data class CounterListAdapter(
    val id:String,
    val title:String,
    val count:Int,
    var selected:Boolean
)

