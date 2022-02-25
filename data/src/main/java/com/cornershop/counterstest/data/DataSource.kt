package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.data.vo.CounterState
import com.cornershop.counterstest.entities.Counter


interface RemoteCounterDataSource {
    suspend fun getListCounters(): CounterRemoteState
    suspend fun createCounter(title: String?): CounterRemoteState
    suspend fun deleteCounter(id: List<Counter>): CounterRemoteState
    suspend fun increaseCounter(id: String?): CounterRemoteState
    suspend fun decreaseCounter(id: String?): CounterRemoteState
}

interface LocalCounterDataSource {
    suspend fun getListCounters(): CounterState
    suspend fun createCounter(counter: Counter?): Long
    suspend fun createCounterFromServer(counter: Counter)
    suspend fun deleteCounter(counter: List<Counter>): Int
    suspend fun increaseCounter(counter: Counter): Int
    suspend fun decreaseCounter(counter: Counter): Int
    suspend fun deleteAllCounterTable(): Int
}