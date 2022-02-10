package com.example.requestmanager

import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.entities.CounterRaw

fun String.toTitleJson():JsonTitleServer = JsonTitleServer(this)

fun String.toIdJson():JsonIdServer = JsonIdServer(this)

fun List<CounterRaw>.toListCounterDomain()=this.map{
    it.run {
        Counter(
            0,
            id,
            title,
            count
        )
    }
}