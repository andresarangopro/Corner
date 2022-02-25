package com.cornershop.counterstest.framework.databasemanager

import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.data.vo.CounterState
import com.cornershop.counterstest.entities.Counter
import java.lang.Exception
import javax.inject.Inject

class LocalCounterDataSourceImpl @Inject constructor(
    private val counterDao: CounterDao
) : LocalCounterDataSource {

    override suspend fun getListCounters(): CounterState {
        return try {
            CounterState.Success(counterDao.getAllCounters().map {
                it.toCounterDomain()
            })
        } catch (ex: Exception) {
            CounterState.Error(error = ex)
        }
    }

    override suspend fun createCounter(counter: Counter?): Long {
        return counterDao.insertCounter(counter?.toCounterEntity())
    }

    override suspend fun createCounterFromServer(counter: Counter) {
        if (counter.id_remote != "0") {
            if (counterDao.getCounterById(counter.id_remote) == null)
                counterDao.insertCounter(counter.toCounterEntity())
        }
    }

    override suspend fun deleteCounter(counter: List<Counter>): Int {
        val counterEntityList = counter.toCounterEntityList()
        return counterEntityList.map { counterDao.deleteCounter(it) }.last()
    }

    override suspend fun increaseCounter(counter: Counter): Int {
        return counterDao.increaseCounterUpd(counter.toCounterEntitySetCount(1))
    }

    override suspend fun decreaseCounter(counter: Counter): Int {
        return counterDao.decreaseCounterUpd(counter.toCounterEntitySetCount(-1))
    }

    override suspend fun deleteAllCounterTable(): Int {
        return counterDao.nukeTable()
    }

}