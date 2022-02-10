package com.cornershop.counterstest.presentation.parcelable

import com.cornershop.counterstest.entities.Counter

fun List<Counter>.toListCounterAdapter() = this.map {
    it.run{
            CounterAdapter(
                id,
                title,
                count,
                false
            )
    }

}

fun List<CounterAdapter>.toListCounterDomain() = this.map {
    it.run{
            Counter(
                id,
                title,
                count
            )
        }
    }
