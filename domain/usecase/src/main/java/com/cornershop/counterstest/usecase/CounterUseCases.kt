package com.cornershop.counterstest.usecase

import com.cornershop.counterstest.data.CounterRespository
import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class CounterUseCases @Inject constructor(private val counterRespository: CounterRespository) {

    suspend fun getListCounterUseCase(): Flow<Result<List<Counter>>> = counterRespository.getListCounter()
    suspend fun createCounterUseCase(title:String): Flow<Result<List<Counter>>> = counterRespository.createCounter(title)
    suspend fun increaseCounterUseCase(id:String): Flow<Result<List<Counter>>> = counterRespository.increaseCounter(id)
    suspend fun decreaseCounterUseCase(id:String): Flow<Result<List<Counter>>> = counterRespository.decreaseCounter(id)
    suspend fun deleteCounterUseCase(id:String): Flow<Result<List<Counter>>> = counterRespository.deleteCounter(id)
}