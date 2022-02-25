package com.cornershop.counterstest.usecase

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.vo.FetchingState
import com.cornershop.counterstest.entities.Counter
import javax.inject.Inject

class CounterUseCases @Inject constructor(private val counterRepository: CounterRepository) {

    suspend fun getListCounterUseCase(): FetchingState = counterRepository.getListCounter()

    suspend fun createCounterUseCase(title: String?): FetchingState =
        counterRepository.createCounter(title)

    suspend fun increaseCounterUseCase(counter: Counter): FetchingState =
        counterRepository.increaseCounter(counter)

    suspend fun decreaseCounterUseCase(counter: Counter): FetchingState =
        counterRepository.decreaseCounter(counter)

    suspend fun deleteCounterUseCase(counter: List<Counter>): FetchingState =
        counterRepository.deleteCounter(counter)

}
