package com.cornershop.counterstest.entities

data class Counter(
    var id:Int,
    var id_remote:String,
    var title:String,
    var count:Int
)

data class CounterRaw(
    var id:String,
    var title:String,
    var count:Int
)
