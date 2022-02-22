package com.cornershop.counterstest.data


import android.content.Context
import com.cornershop.counterstest.data.vo.*
import com.cornershop.counterstest.entities.Counter
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CounterRespositoryImp @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource,
    private val localCounterDataSource: LocalCounterDataSource,
    @ApplicationContext val appContext: Context
) : CounterRepository {


    override suspend fun getListCounter(): FetchingState = withContext(Dispatchers.IO) {
        val remoteResponse = getNetworkStateCounterSource(remoteCounterDataSource.getListCounters())

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
        withContext(Dispatchers.IO) {
            val remoteResponse =
                getNetworkStateCounterSource(remoteCounterDataSource.createCounter(title))

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
        withContext(Dispatchers.IO) {
            val remoteResponse =
                getNetworkStateCounterSource(remoteCounterDataSource.increaseCounter(counter.id_remote))

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
        withContext(Dispatchers.IO) {
            val remoteResponse =
                getNetworkStateCounterSource(remoteCounterDataSource.decreaseCounter(counter.id_remote))

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


    override suspend fun deleteCounter(counter: Counter): FetchingState =
        withContext(Dispatchers.IO) {
            val remoteResponse =
                getNetworkStateCounterSource(remoteCounterDataSource.deleteCounter(counter.id_remote))

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

    private fun getNetworkStateCounterSource(counterRemoteState: CounterRemoteState): CounterRemoteState {
        return if (isOnline(appContext))
            counterRemoteState
        else
            CounterRemoteState.Error(RuntimeException(NetworkError))

    }

}