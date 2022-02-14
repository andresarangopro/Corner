package com.cornershop.counterstest.data


import android.content.Context
import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.data.vo.NetworkError
import com.cornershop.counterstest.data.vo.isOnline
import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.vo.CounterState
import com.example.requestmanager.vo.FetchingState
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject


class CounterRespositoryImp @Inject constructor(
    private val remoteCounterDataSource: RemoteCounterDataSource,
    private val localCounterDataSource: LocalCounterDataSource,
    @ApplicationContext val appContext: Context
):CounterRepository{


    override suspend fun getListCounter(): FetchingState = withContext(Dispatchers.IO) {
        var remoteResponse = getNetworkStateCounterSource(remoteCounterDataSource.getListCounters())

        when(remoteResponse){
             is CounterRemoteState.Success ->{
                localCounterDataSource.deleteAllCounterTable()
                remoteResponse.data.forEach{
                    localCounterDataSource.createCounterFromServer(it)
                }
            }
            is CounterRemoteState.Error->{
                var localListCounter = localCounterDataSource.getListCounters()
                when(localListCounter){
                    is CounterState.Success->{
                        return@withContext FetchingState.Success(localListCounter.data)
                    }
                    is CounterState.Error->{
                        return@withContext FetchingState.Error(remoteResponse.error)
                    }
                }
                return@withContext FetchingState.Error(remoteResponse.error)
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }



    override suspend fun createCounter(title: String?) : FetchingState = withContext(Dispatchers.IO) {
        var remoteResponse = getNetworkStateCounterSource( remoteCounterDataSource.createCounter(title))

        when(remoteResponse){
            is CounterRemoteState.Success->{
                remoteResponse.data.forEach{
                    localCounterDataSource.createCounterFromServer(it)
                }
            }
            is CounterRemoteState.Error->{
                var createLocalCounter = localCounterDataSource.createCounter(title?.let {
                    Counter(0,"0",
                        it,0)
                })
                if(createLocalCounter.equals(1)){
                    var localListCounter = localCounterDataSource.getListCounters()
                    when(localListCounter){
                        is CounterState.Success->{
                            return@withContext FetchingState.Success(localListCounter.data)
                        }
                        is CounterState.Error->{
                            return@withContext FetchingState.Error(remoteResponse.error)
                        }
                    }
                    return@withContext FetchingState.Error(remoteResponse.error)
                }else{
                    return@withContext FetchingState.Error(remoteResponse.error)
                }

                return@withContext FetchingState.Error(remoteResponse.error)
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }


    override suspend fun increaseCounter(counter:Counter): FetchingState = withContext(Dispatchers.IO) {
        var remoteResponse = getNetworkStateCounterSource( remoteCounterDataSource.increaseCounter(counter.id_remote))

        when(remoteResponse){
            is CounterRemoteState.Success->{
                    localCounterDataSource.increaseCounter(counter)
            }
            is CounterRemoteState.Error->{
                var increaseCounter = localCounterDataSource.increaseCounter(counter)
                if(increaseCounter.equals(1)){
                    var localListCounter = localCounterDataSource.getListCounters()
                    when(localListCounter){
                        is CounterState.Success->{
                            return@withContext FetchingState.Success(localListCounter.data)
                        }
                        is CounterState.Error->{
                            return@withContext FetchingState.Error(remoteResponse.error)
                        }
                    }
                    return@withContext FetchingState.Error(remoteResponse.error)
                }else{
                    return@withContext FetchingState.Error(remoteResponse.error)
                }

                return@withContext FetchingState.Error(remoteResponse.error)
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }

    override suspend fun decreaseCounter(counter:Counter): FetchingState = withContext(Dispatchers.IO) {
        var remoteResponse = getNetworkStateCounterSource( remoteCounterDataSource.decreaseCounter(counter.id_remote))

        when(remoteResponse){
            is CounterRemoteState.Success->{
                localCounterDataSource.decreaseCounter(counter)
            }
            is CounterRemoteState.Error->{
                var increaseCounter = localCounterDataSource.decreaseCounter(counter)
                if(increaseCounter.equals(1)){
                    var localListCounter = localCounterDataSource.getListCounters()
                    when(localListCounter){
                        is CounterState.Success->{
                            return@withContext FetchingState.Success(localListCounter.data)
                        }
                        is CounterState.Error->{
                            return@withContext FetchingState.Error(remoteResponse.error)
                        }
                    }
                    return@withContext FetchingState.Error(remoteResponse.error)
                }else{
                    return@withContext FetchingState.Error(remoteResponse.error)
                }

                return@withContext FetchingState.Error(remoteResponse.error)
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }


    override suspend fun deleteCounter(counter:Counter): FetchingState= withContext(Dispatchers.IO) {
        var remoteResponse = getNetworkStateCounterSource(remoteCounterDataSource.deleteCounter(counter.id_remote))

        when(remoteResponse){
            is CounterRemoteState.Success->{
                localCounterDataSource.deleteCounter(counter)
            }
            is CounterRemoteState.Error->{
                var increaseCounter = localCounterDataSource.deleteCounter(counter)
                if(increaseCounter.equals(1)){
                    var localListCounter = localCounterDataSource.getListCounters()
                    when(localListCounter){
                        is CounterState.Success->{
                            return@withContext FetchingState.Success(localListCounter.data)
                        }
                        is CounterState.Error->{
                            return@withContext FetchingState.Error(remoteResponse.error)
                        }
                    }
                    return@withContext FetchingState.Error(remoteResponse.error)
                }else{
                    return@withContext FetchingState.Error(remoteResponse.error)
                }

                return@withContext FetchingState.Error(remoteResponse.error)
            }
        }
        return@withContext FetchingState.Success(remoteResponse.data)
    }

    private fun getNetworkStateCounterSource(counterRemoteState: CounterRemoteState): CounterRemoteState {
        var remoteResponse: CounterRemoteState? = null
        if (isOnline(appContext))
            remoteResponse =counterRemoteState
        else
            remoteResponse = CounterRemoteState.Error(RuntimeException(NetworkError))
        return remoteResponse
    }

}