package com.cornershop.counterstest.framwework.databasemanager

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import javax.inject.Inject

@Database(entities = [CounterEntity::class], version = 1)
@TypeConverters(ListStringConverters::class)
abstract class CounterDatabase:RoomDatabase() {
    //region Abstract Methods

    abstract fun counterDao(): CounterDao
    //endregion

    //region Companion Object

//    companion object {
//
//        private const val DATABASE_NAME = "counter_db"
//
//        @Synchronized
//        fun getDatabase(context: Context): CounterDatabase = Room.databaseBuilder(
//            context.applicationContext,
//            CounterDatabase::class.java,
//            DATABASE_NAME
//        ).build()
//    }

    //endregion
}