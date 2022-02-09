package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException
import javax.inject.Inject

class DatabaseDataSource @Inject constructor(
    private val counterDao:CounterDao
):LocalCounterDataSource {

    override suspend fun getListCounters(): Flow<Result<List<Counter>>> {
        return flow {
            emit(
                Result.success(counterDao.getAllCounters())
                .map(List<CounterEntity>::toCounterDomainList)
            )
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun createCounter(counter: Counter?): Flow<Result<Boolean>> {
        val counterEntity = counter?.toCounterEntity()
        return flow {
                emit(
                    Result.success(counterDao.insertCounter(counterEntity))
                )
            }
    }

    override suspend fun deleteCounter(counter: Counter?): Flow<Result<Boolean>> {
        val counterEntity = counter?.toCounterEntity()
        return flow {
            emit(
                Result.success(counterDao.deleteCounter(counterEntity))
            )
        }
    }



    override suspend fun increaseCounter(id: String?): Flow<Result<List<Counter>>> {
        TODO("Not yet implemented")
    }

    override suspend fun decreaseCounter(id: String?): Flow<Result<List<Counter>>> {
        TODO("Not yet implemented")
    }


}