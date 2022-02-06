package com.cornershop.counterstest.data

import dagger.Binds
import javax.inject.Inject


class CounterRespository @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource
){
    suspend fun getListCounter() = remoteCounterDataSource.getListCounters()
}