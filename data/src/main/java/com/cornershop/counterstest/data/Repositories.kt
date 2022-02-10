package com.cornershop.counterstest.data

import com.cornershop.counterstest.entities.Counter
import dagger.Binds
import javax.inject.Inject


class CounterRespository @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource,
    private val localCounterDataSource: LocalCounterDataSource
){
    suspend fun getListCounter() = remoteCounterDataSource.getListCounters()
    suspend fun createCounter(title: String?) = remoteCounterDataSource.createCounter(title)
    suspend fun increaseCounter(id:String?) = remoteCounterDataSource.increaseCounter(id)
    suspend fun decreaseCounter(id:String?) = remoteCounterDataSource.decreaseCounter(id)
    suspend fun deleteCounter(id:String?) = remoteCounterDataSource.deleteCounter(id)

    suspend fun getLocalListCounter() = localCounterDataSource.getListCounters()
    suspend fun createLocalCounter(counter: Counter) = localCounterDataSource.createCounter(counter)
    suspend fun createCounterFromListServer(counter: Counter) = localCounterDataSource.createCounterFromServer(counter)
    suspend fun increaseLocalCounter(counter:Counter) = localCounterDataSource.increaseCounter(counter)
    suspend fun decreaseLocalCounter(counter: Counter) = localCounterDataSource.decreaseCounter(counter)
    suspend fun deleteLocalCounter(counter: Counter) = localCounterDataSource.deleteCounter(counter)

}