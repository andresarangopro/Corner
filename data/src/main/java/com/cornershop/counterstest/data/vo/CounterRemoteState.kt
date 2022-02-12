package com.cornershop.counterstest.data.vo


import com.cornershop.counterstest.entities.Counter
import java.lang.Exception

sealed class CounterRemoteState {
    data class Success(val data: List<Counter>) : CounterRemoteState()
    data class Error(val error: Exception) : CounterRemoteState()
}