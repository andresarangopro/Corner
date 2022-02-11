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
    abstract fun counterDao(): CounterDao
}