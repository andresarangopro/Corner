package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.vo.*
import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CounterRepositoryImp @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource,
    private val localCounterDataSource: LocalCounterDataSource,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : CounterRepository {

    override suspend fun getListCounter(): FetchingState = withContext(dispatcher) {
        val remoteResponse = remoteCounterDataSource.getListCounters()

        when (remoteResponse) {
            is CounterRemoteState.Success -> {
                localCounterDataSource.deleteAllCounterTable()
                remoteResponse.data.forEach {
                    localCounterDataSource.createCounterFromServer(it)
                }
                return@withContext FetchingState.Success(remoteResponse.data)
            }
            is CounterRemoteState.Error -> {
                getLocalCounterList()
            }
        }
    }

    private suspend fun getLocalCounterList():FetchingState= withContext(dispatcher){
        when (val localListCounter = localCounterDataSource.getListCounters()) {
            is CounterState.Success -> {
                return@withContext FetchingState.Success(localListCounter.data)
            }
            is CounterState.Error -> {
                return@withContext FetchingState.Error(localListCounter.error)
            }
        }
    }

    override suspend fun createCounter(title: String?): FetchingState =
        withContext(dispatcher) {
            when (val remoteResponse = remoteCounterDataSource.createCounter(title)) {
                is CounterRemoteState.Success -> {
                    remoteResponse.data.forEach {
                        localCounterDataSource.createCounterFromServer(it)
                    }
                    return@withContext FetchingState.Success(remoteResponse.data)
                }
                is CounterRemoteState.Error -> {
                    val createLocalCounter = localCounterDataSource.createCounter(title?.let {
                        Counter(
                            0, "0",
                            it, 0
                        )
                    })
                    if (createLocalCounter == 1.toLong()) {
                        getLocalCounterList()
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
        }

    override suspend fun increaseCounter(counter: Counter): FetchingState =
        withContext(dispatcher) {
            when (val remoteResponse = remoteCounterDataSource.increaseCounter(counter.id_remote)) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.increaseCounter(counter)
                    return@withContext FetchingState.Success(remoteResponse.data)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.increaseCounter(counter)
                    if (increaseCounter == 1) {
                        getLocalCounterList()
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
        }

    override suspend fun decreaseCounter(counter: Counter): FetchingState =
        withContext(dispatcher) {
            when (val remoteResponse = remoteCounterDataSource.decreaseCounter(counter.id_remote)) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.decreaseCounter(counter)
                    return@withContext FetchingState.Success(remoteResponse.data)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.decreaseCounter(counter)
                    if (increaseCounter == 1) {
                        getLocalCounterList()
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
        }

    override suspend fun deleteCounter(counter: List<Counter>): FetchingState =
        withContext(dispatcher) {
            when (val remoteResponse = remoteCounterDataSource.deleteCounter(counter)) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.deleteCounter(counter)
                    return@withContext FetchingState.Success(remoteResponse.data)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.deleteCounter(counter)
                    if (increaseCounter == 1) {
                        getLocalCounterList()
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
        }
}