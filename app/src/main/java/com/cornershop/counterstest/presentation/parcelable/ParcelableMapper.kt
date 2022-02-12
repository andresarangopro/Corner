package com.cornershop.counterstest.presentation.parcelable

import com.cornershop.counterstest.entities.Counter

fun List<Counter>.toListCounterAdapter() = this.map {
    it.run{
            CounterAdapter(
                id,
                id_remote,
                title,
                count,
                false
            )
    }

}

fun List<CounterAdapter>.toListCounterDomain() = this.map {
         it.toCounterDomain()
    }

fun CounterAdapter.toCounterDomain() = Counter(
    id,
    id_remote,
    title,
    count
)