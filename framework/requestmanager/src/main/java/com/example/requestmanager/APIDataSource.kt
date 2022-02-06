package com.example.requestmanager

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

    override suspend fun createCounter(title: String?): Flow<Result<List<Counter>>> {
        return flow{
            emit(Result.success(api.createCounter(title?.toTitleJson())))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun increaseCounter(id: String?): Flow<Result<List<Counter>>> {
        return flow{
            emit(Result.success(api.increaseCounter(id?.toIdJson())))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun decreaseCounter(id: String?): Flow<Result<List<Counter>>> {
        return flow{
            emit(Result.success(api.decreaseCounter(id?.toIdJson())))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun deleteCounter(id: String?): Flow<Result<List<Counter>>> {
        return flow{
            emit(Result.success(api.deleteCounter(id?.toIdJson())))
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }


}