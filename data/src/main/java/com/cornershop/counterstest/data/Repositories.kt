package com.cornershop.counterstest.data


class CounterRespository(
    private val remoteCounterDataSource: RemoteCounterDataSource
){
    suspend fun getListCounter() = remoteCounterDataSource.getListCounters()
}