package com.cornershop.counterstest.framwework.databasemanager.di


import android.app.Application
import android.content.Context
import androidx.room.Room
import com.cornershop.counterstest.data.LocalCounterDataSource
import com.cornershop.counterstest.data.RemoteCounterDataSource
import com.cornershop.counterstest.framwework.databasemanager.CounterDao
import com.cornershop.counterstest.framwework.databasemanager.CounterDatabase
import com.cornershop.counterstest.framwework.databasemanager.DatabaseDataSource
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.internal.managers.ApplicationComponentManager
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton




@Module
@InstallIn(SingletonComponent::class)
object Module {

    private val DATABASE_NAME = "counter_db"


    @Singleton // Tell Dagger-Hilt to create a singleton accessible everywhere in ApplicationCompenent (i.e. everywhere in the application)
    @Provides
    fun provideCounterDatabase(
        @ApplicationContext app: Context
    ) = Room.databaseBuilder(
        app,
        CounterDatabase::class.java,
        DATABASE_NAME
    ).build() // The reason we can construct a database for the repo

    @Singleton
    @Provides
    fun providesCounterLocalDataSource(
        databaseDatasource: DatabaseDataSource
    ):LocalCounterDataSource {
        return databaseDatasource
    }

    @Singleton
    @Provides
    fun provideCounterDao(db: CounterDatabase):CounterDao = db.counterDao()

}