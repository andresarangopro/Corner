package com.cornershop.counterstest.presentation.viewModels

import com.cornershop.counterstest.presentation.parcelable.CounterAdapter


sealed class CounterNavigation(){
    object hideSelectedItemState:CounterNavigation()
    object showLoaderSave:CounterNavigation()
    object hideLoaderSave:CounterNavigation()
    object hideSwipeLoaderSave:CounterNavigation()
    object isSwipeLoaderSave:CounterNavigation()
    data class onErrorLoadingCounterListNetwork(val title: Int?, val message:Int?):CounterNavigation()
    data class onErrorLoadingCounterList(val title: Int?, val message:Int?):CounterNavigation()
    data class onNoResultCounterList(val message: Int?):CounterNavigation()
    data class setLoaderState(val state:Boolean):CounterNavigation()
    data class setSelectedItemState(val items:Int?):CounterNavigation()
    data class setCounterList(val listCounter:List<CounterAdapter>?, val timesSum:Int?):CounterNavigation()
    data class updateCounterList(val listCounter:List<CounterAdapter>?, val timesSum:Int?):CounterNavigation()
}
