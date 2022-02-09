package com.cornershop.counterstest.presentation.viewModels

import com.cornershop.counterstest.presentation.parcelable.CounterAdapter


sealed class CounterEvent {
    data class CreateCounter(val title: String?) : CounterEvent()
    data class FilterCounter(val counterName: String?) : CounterEvent()
    data class IncreaseCounter( val id: String? ) : CounterEvent()
    data class DecreaseCounter(val id: String?) : CounterEvent()
    object DeleteSelectedCounters : CounterEvent()
    object getListCounterInit : CounterEvent()
    object getListCounterFromSwipe : CounterEvent()
    data class SelectCounters(val listCounter: List<CounterAdapter>?) : CounterEvent()
}

