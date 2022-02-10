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

    override suspend fun createCounter(counter: Counter?):  Flow<Result<List<Counter>>>  {
        return flow {
            if(counterDao.insertCounter(counter?.toCounterEntity()) == 1 ){
                emit(Result.success(counterDao.getAllCounters().toCounterDomainList()))
            }else{
                emit(Result.failure(RuntimeException("Something went wrong")))
            }
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun createCounterFromServer(counter: Counter):  Flow<Result<List<Counter>>>  {
        return flow {
            if(counter.id_remote != "0"){
                if(counterDao.getCounterById(counter.id_remote) == null)
                    if(counterDao.insertCounter(counter?.toCounterEntity()) == 1 )
                        emit(Result.success(counterDao.getAllCounters().toCounterDomainList()))

            }else{
                emit(Result.failure(RuntimeException("Something went wrong")))
            }
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun deleteCounter(counter: Counter?): Flow<Result<List<Counter>>> {
        val counterEntity = counter?.toCounterEntity()
        return flow {
            if(counterDao.deleteCounter(counterEntity) == 1 ){
                emit(Result.success(counterDao.getAllCounters().toCounterDomainList()))
            }else{
                emit(Result.failure(RuntimeException("Something went wrong")))
            }
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong")))
        }
    }

    override suspend fun increaseCounter(counter:Counter): Flow<Result<List<Counter>>>{
        return flow {
            if(counterDao.increaseCounterUpd(counter.toUpdateEntity(1))== 1 ){
                emit(Result.success(counterDao.getAllCounters().toCounterDomainList()))
            }else{
                emit(Result.failure(RuntimeException("Something went wrong")))
            }
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong ${it.message}")))
        }
    }

    override suspend fun decreaseCounter(counter:Counter): Flow<Result<List<Counter>>> {
        return flow {
            if(counterDao.decreaseCounterUpd(counter.toUpdateEntity(-1))== 1 ){
                emit(Result.success(counterDao.getAllCounters().toCounterDomainList()))
            }else{
                emit(Result.failure(RuntimeException("Something went wrong")))
            }
        }.catch {
            emit(Result.failure(RuntimeException("Something went wrong ${it.message}")))
        }
    }





}