package com.cornershop.counterstest.presentation.parcelable

data class CounterAdapter(
    val id:String,
    val title:String,
    val count:Int,
    var selected:Boolean
)

