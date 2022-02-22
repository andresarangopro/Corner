package com.cornershop.counterstest.framwework.databasemanager.di


import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.framwework.databasemanager.CounterDao
import com.cornershop.counterstest.framwework.databasemanager.CounterDatabase
import com.cornershop.counterstest.framwework.databasemanager.LocalCounterDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object Module {

    private const val DATABASE_NAME = "counter_db"

    @Provides
    @Singleton
    fun provideCounterDatabase(
        @ApplicationContext app: Context
    ): CounterDatabase = Room.databaseBuilder(
        app,
        CounterDatabase::class.java,
        DATABASE_NAME
    ).build() // The reason we can construct a database for the repo

    @Provides
    @Singleton
    fun providesCounterLocalDataSource(
        localCounterDatasourceImpl: LocalCounterDataSourceImpl
    ): LocalCounterDataSource {
        return localCounterDatasourceImpl
    }

    @Provides
    @Singleton
    fun provideCounterDao(db: CounterDatabase): CounterDao = db.counterDao()

}