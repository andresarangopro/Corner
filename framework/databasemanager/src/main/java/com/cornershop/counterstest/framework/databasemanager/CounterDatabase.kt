package com.cornershop.counterstest.framework.databasemanager


import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CounterEntity::class], version = 1)
abstract class CounterDatabase : RoomDatabase() {
    abstract fun counterDao(): CounterDao
}