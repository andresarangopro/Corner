@file:Suppress("unused", "unused")

package com.example.requestmanager.di

import com.cornershop.counterstest.data.RemoteCounterDataSource
import com.example.requestmanager.CounterDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class RquestManagerModule {

    @Binds
    abstract fun providesCounterRemoteSource(
        counterDataSource: CounterDataSource
    ): RemoteCounterDataSource

}