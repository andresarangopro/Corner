package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.vo.CounterState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.Exception
import java.lang.RuntimeException
import javax.inject.Inject

class LocalCounterDataSourceImpl @Inject constructor(
    private val counterDao:CounterDao
):LocalCounterDataSource {

    override suspend fun getListCounters(): CounterState {
        return  try {
            CounterState.Success(counterDao.getAllCounters().map {
                it.toCounterDomain()})
        } catch (ex: Exception) {
            CounterState.Error(error = ex)
        }
    }

    override suspend fun createCounter(counter: Counter?): Long {
        return  counterDao.insertCounter(counter?.toCounterEntity())
    }

    override suspend fun createCounterFromServer(counter: Counter) {
        if(counter.id_remote != "0") {
            if (counterDao.getCounterById(counter.id_remote) == null)
                    counterDao.insertCounter(counter?.toCounterEntity())
        }

    }

    override suspend fun deleteCounter(counter: Counter?): Int {
        val counterEntity = counter?.toCounterEntity()
        return counterDao.deleteCounter(counterEntity)
    }

    override suspend fun increaseCounter(counter:Counter): Int{
        return counterDao.increaseCounterUpd(counter.toCounterEntitySetCount(1))
    }

    override suspend fun decreaseCounter(counter:Counter): Int {
         return counterDao.decreaseCounterUpd(counter.toCounterEntitySetCount(-1))
    }

    override suspend fun deleteAllCounterTable(): Int {
        return counterDao.nukeTable()
    }

}