package com.cornershop.counterstest.usecase

import com.cornershop.counterstest.data.CounterRespository
import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.flow.Flow

class UseCasesCounter(private val counterRespository: CounterRespository) {

    suspend fun getListCounterUseCase(): Flow<Result<List<Counter>>> = counterRespository.getListCounter()
}