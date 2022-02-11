package com.example.requestmanager.vo

import com.cornershop.counterstest.entities.Counter


sealed class CounterState {
    data class Success(val data: List<Counter>) : CounterState()
    data class Error(val error: Exception) : CounterState()
}

sealed class FetchingState {
    class Success(val data: List<Counter>) : FetchingState()
    class Error(val error: Exception) : FetchingState()
}

