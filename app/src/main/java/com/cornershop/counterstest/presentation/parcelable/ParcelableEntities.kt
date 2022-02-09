package com.cornershop.counterstest.presentation.parcelable

 class CounterAdapter(
    val id:String,
    val title:String,
    val count:Int,
    var selected:Boolean
){

     override fun toString(): String {
         return title
     }
 }

