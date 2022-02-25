package com.cornershop.counterstest.presentation.viewModels

import com.cornershop.counterstest.presentation.parcelable.CounterAdapter


sealed class CounterNavigation {
    object HideSelectedItemState : CounterNavigation()
    object ShowLoaderSave : CounterNavigation()
    object HideLoaderSave : CounterNavigation()
    object HideSwipeLoaderSave : CounterNavigation()
    object IsSwipeLoaderSave : CounterNavigation()
    data class OnErrorLoadingCounterListNetwork(val title: Int?, val message: Int?) :
        CounterNavigation()

    data class OnErrorLoadingCounterList(val title: Int?, val message: Int?) : CounterNavigation()
    data class OnNoResultCounterList(val message: Int?) : CounterNavigation()
    data class SetLoaderState(val state: Boolean) : CounterNavigation()
    data class SetSelectedItemState(val items: Int?) : CounterNavigation()
    data class SetCounterList(val listCounter: List<CounterAdapter>?, val timesSum: Int) :
        CounterNavigation()

    data class UpdateCounterList(val listCounter: List<CounterAdapter>?, val timesSum: Int) :
        CounterNavigation()
}
