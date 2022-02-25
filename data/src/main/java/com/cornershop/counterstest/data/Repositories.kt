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
            }
            is CounterRemoteState.Error -> {
                when (val localListCounter = localCounterDataSource.getListCounters()) {
                    is CounterState.Success -> {
                        return@withContext FetchingState.Success(localListCounter.data)
                    }
                    is CounterState.Error -> {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }


    override suspend fun createCounter(title: String?): FetchingState =
        withContext(dispatcher) {
            val remoteResponse = remoteCounterDataSource.createCounter(title)

            when (remoteResponse) {
                is CounterRemoteState.Success -> {
                    remoteResponse.data.forEach {
                        localCounterDataSource.createCounterFromServer(it)
                    }
                }
                is CounterRemoteState.Error -> {
                    val createLocalCounter = localCounterDataSource.createCounter(title?.let {
                        Counter(
                            0, "0",
                            it, 0
                        )
                    })
                    if (createLocalCounter == 1.toLong()) {
                        when (val localListCounter = localCounterDataSource.getListCounters()) {
                            is CounterState.Success -> {
                                return@withContext FetchingState.Success(localListCounter.data)
                            }
                            is CounterState.Error -> {
                                return@withContext FetchingState.Error(remoteResponse.error)
                            }
                        }
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
            return@withContext FetchingState.Success(remoteResponse.data)
        }


    override suspend fun increaseCounter(counter: Counter): FetchingState =
        withContext(dispatcher) {
            val remoteResponse = remoteCounterDataSource.increaseCounter(counter.id_remote)

            when (remoteResponse) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.increaseCounter(counter)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.increaseCounter(counter)
                    if (increaseCounter == 1) {
                        when (val localListCounter = localCounterDataSource.getListCounters()) {
                            is CounterState.Success -> {
                                return@withContext FetchingState.Success(localListCounter.data)
                            }
                            is CounterState.Error -> {
                                return@withContext FetchingState.Error(remoteResponse.error)
                            }
                        }
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
            return@withContext FetchingState.Success(remoteResponse.data)
        }

    override suspend fun decreaseCounter(counter: Counter): FetchingState =
        withContext(dispatcher) {
            val remoteResponse = remoteCounterDataSource.decreaseCounter(counter.id_remote)

            when (remoteResponse) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.decreaseCounter(counter)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.decreaseCounter(counter)
                    if (increaseCounter == 1) {
                        when (val localListCounter = localCounterDataSource.getListCounters()) {
                            is CounterState.Success -> {
                                return@withContext FetchingState.Success(localListCounter.data)
                            }
                            is CounterState.Error -> {
                                return@withContext FetchingState.Error(remoteResponse.error)
                            }
                        }
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
            return@withContext FetchingState.Success(remoteResponse.data)
        }


    override suspend fun deleteCounter(counter: List<Counter>): FetchingState =
        withContext(dispatcher) {
            val remoteResponse = remoteCounterDataSource.deleteCounter(counter)

            when (remoteResponse) {
                is CounterRemoteState.Success -> {
                    localCounterDataSource.deleteCounter(counter)
                }
                is CounterRemoteState.Error -> {
                    val increaseCounter = localCounterDataSource.deleteCounter(counter)
                    if (increaseCounter == 1) {
                        when (val localListCounter = localCounterDataSource.getListCounters()) {
                            is CounterState.Success -> {
                                return@withContext FetchingState.Success(localListCounter.data)
                            }
                            is CounterState.Error -> {
                                return@withContext FetchingState.Error(remoteResponse.error)
                            }
                        }
                    } else {
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
            }
            return@withContext FetchingState.Success(remoteResponse.data)
        }
}