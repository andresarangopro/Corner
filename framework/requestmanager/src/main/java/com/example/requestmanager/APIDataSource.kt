package com.example.requestmanager

import android.util.Log
import com.cornershop.counterstest.data.RemoteCounterDataSource
import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject


class CounterDataSource @Inject constructor(private val api:CounterService): RemoteCounterDataSource {

    override suspend fun getListCounters(): Flow<Result<List<Counter>>> {
        return flow{
            emit(Result.success(api.getListCounter()))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

}