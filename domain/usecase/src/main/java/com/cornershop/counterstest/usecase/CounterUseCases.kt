package com.cornershop.counterstest.usecase

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.vo.FetchingState
import javax.inject.Inject

class CounterUseCases @Inject constructor(private val counterRespository: CounterRepository) {

    suspend fun getListCounterUseCase(): FetchingState = counterRespository.getListCounter()
    suspend fun createCounterUseCase(title:String?): FetchingState= counterRespository.createCounter(title)
    suspend fun increaseCounterUseCase(counter:Counter): FetchingState = counterRespository.increaseCounter(counter)
    suspend fun decreaseCounterUseCase(counter:Counter): FetchingState = counterRespository.decreaseCounter(counter)
    suspend fun deleteCounterUseCase(counter:Counter): FetchingState = counterRespository.deleteCounter(counter)

}
