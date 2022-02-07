package com.cornershop.counterstest.presentation.parcelable

import com.cornershop.counterstest.entities.Counter
import javax.inject.Inject

fun List<Counter>.toListCounterAdapter() = this.map {
    it.run{
      CounterListAdapter(
          id,
          title,
          count,
          false
      )
    }
}