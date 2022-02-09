package com.cornershop.counterstest.framwework.databasemanager.di

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.framwework.databasemanager.CounterDao
import com.cornershop.counterstest.framwework.databasemanager.CounterDatabase
import com.cornershop.counterstest.framwework.databasemanager.DatabaseDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@InstallIn(SingletonComponent::class)
@Module
class Module {
    @Provides
    fun provideCounterDao(appDatabase: CounterDatabase): CounterDao {
        return appDatabase.counterDao()
    }

    @Provides
    fun provideAppDatabase(
        @ApplicationContext appContext: Context
    ): CounterDatabase {
        return CounterDatabase.getDatabase(appContext)
    }
}