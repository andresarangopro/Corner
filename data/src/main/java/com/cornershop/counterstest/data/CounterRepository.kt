package com.cornershop.counterstest.data

import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.vo.FetchingState

interface CounterRepository{
    suspend fun getListCounter(): FetchingState
    suspend fun createCounter(title: String?) : FetchingState
    suspend fun increaseCounter(counter: Counter): FetchingState
    suspend fun decreaseCounter(counter:Counter): FetchingState
    suspend fun deleteCounter(counter:Counter): FetchingState
}