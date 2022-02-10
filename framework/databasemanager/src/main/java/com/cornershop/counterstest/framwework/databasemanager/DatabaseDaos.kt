package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.*
import com.cornershop.counterstest.entities.Counter

@Dao
interface CounterDao {

    @Query("SELECT * FROM Counter")
    suspend fun getAllCounters():List<CounterEntity>

    @Query("SELECT * FROM counter WHERE counter_id = :id")
    suspend fun getCounterById(id: String): CounterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counterEntity: CounterEntity?)

    @Delete
    suspend fun deleteCounter(counterEntity: CounterEntity?)
}