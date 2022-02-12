package com.cornershop.counterstest.data.di

import com.cornershop.counterstest.data.CounterRepository
import com.cornershop.counterstest.data.CounterRespositoryImp
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent


@Module
@InstallIn(SingletonComponent::class)
abstract class DataModule {
    @Binds
    abstract fun providesCounterRepository(
        counterRespositoryImp: CounterRespositoryImp
    ): CounterRepository

}