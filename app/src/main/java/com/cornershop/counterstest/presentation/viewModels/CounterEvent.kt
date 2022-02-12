package com.cornershop.counterstest.presentation.viewModels

import com.cornershop.counterstest.presentation.parcelable.CounterAdapter


sealed class CounterEvent {
    data class CreateCounter(val title: String) : CounterEvent()
    data class FilterCounter(val counterName: String?) : CounterEvent()
    data class IncreaseCounter( val counter: CounterAdapter) : CounterEvent()
    data class DecreaseCounter(val counter: CounterAdapter) : CounterEvent()
    object DeleteSelectedCounters : CounterEvent()
    object getListCounterInit : CounterEvent()
    object unselectAll : CounterEvent()
    object getListCounterFromSwipe : CounterEvent()
    data class SelectCounters(val counter: CounterAdapter) : CounterEvent()
}

