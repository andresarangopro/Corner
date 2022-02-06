package com.cornershop.counterstest.data

import com.cornershop.counterstest.entities.Counter
import dagger.Binds
import javax.inject.Inject


class CounterRespository @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource
){
    suspend fun getListCounter() = remoteCounterDataSource.getListCounters()
    suspend fun createCounter(title: String?) = remoteCounterDataSource.createCounter(title)
    suspend fun increaseCounter(id:String?) = remoteCounterDataSource.increaseCounter(id)
    suspend fun decreaseCounter(id:String?) = remoteCounterDataSource.decreaseCounter(id)
    suspend fun deleteCounter(id:String?) = remoteCounterDataSource.deleteCounter(id)
}