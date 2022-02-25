package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.vo.FetchingState
import com.cornershop.counterstest.entities.Counter

interface CounterRepository {
    suspend fun getListCounter(): FetchingState
    suspend fun createCounter(title: String?): FetchingState
    suspend fun increaseCounter(counter: Counter): FetchingState
    suspend fun decreaseCounter(counter: Counter): FetchingState
    suspend fun deleteCounter(counter: List<Counter>): FetchingState
}