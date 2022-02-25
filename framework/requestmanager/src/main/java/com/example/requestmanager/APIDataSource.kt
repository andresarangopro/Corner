package com.example.requestmanager

import com.cornershop.counterstest.data.RemoteCounterDataSource
import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.entities.Counter
import java.lang.Exception
import javax.inject.Inject


class CounterDataSource @Inject constructor(private val api: CounterService) :
    RemoteCounterDataSource {

    override suspend fun getListCounters(): CounterRemoteState {
        return try {
            CounterRemoteState.Success(
                api.getListCounter().toListCounterDomain()
            )
        } catch (ex: Exception) {
            CounterRemoteState.Error(error = ex)
        }

    }

    override suspend fun createCounter(title: String?): CounterRemoteState {
        return try {
            CounterRemoteState.Success(
                api.createCounter(title?.toTitleJson()).toListCounterDomain()
            )
        } catch (ex: Exception) {
            CounterRemoteState.Error(error = ex)
        }
    }

    override suspend fun increaseCounter(id: String?): CounterRemoteState {
        return try {
            CounterRemoteState.Success(
                api.increaseCounter(id?.toIdJson()).toListCounterDomain()
            )
        } catch (ex: Exception) {
            CounterRemoteState.Error(error = ex)
        }
    }

    override suspend fun decreaseCounter(id: String?): CounterRemoteState {
        return try {
            CounterRemoteState.Success(
                api.decreaseCounter(id?.toIdJson()).toListCounterDomain()
            )
        } catch (ex: Exception) {
            CounterRemoteState.Error(error = ex)
        }
    }

    override suspend fun deleteCounter(listCounters: List<Counter>): CounterRemoteState {
        return try {
            CounterRemoteState.Success(
                listCounters.map {
                    api.deleteCounter(it.id.toString().toIdJson())
                }.last().toListCounterDomain()
            )
        } catch (ex: Exception) {
            CounterRemoteState.Error(error = ex)
        }
    }


}