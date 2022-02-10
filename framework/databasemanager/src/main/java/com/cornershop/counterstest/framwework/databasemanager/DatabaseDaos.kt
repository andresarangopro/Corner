package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.*
import com.cornershop.counterstest.entities.Counter

@Dao
interface CounterDao {

    @Query("SELECT * FROM Counter")
    suspend fun getAllCounters():List<CounterEntity>

    @Query("SELECT * FROM counter WHERE counter_remote_id = :id")
    suspend fun getCounterById(id: String): CounterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counterEntity: CounterEntity?):Long

    @Update(entity = CounterEntity::class)
    fun increaseCounterUpd(obj: CounterEntity):Int

    @Update(entity = CounterEntity::class)
    fun decreaseCounterUpd(obj: CounterEntity):Int

    @Query("UPDATE counter SET counter_count = counter_count+1 WHERE counter_id = :id")
    fun increaseCounter(id: Int)

    @Query("UPDATE counter SET counter_count = counter_count-1 WHERE counter_id = :id")
    fun decreaseCounter(id: Int)

    @Delete
    suspend fun deleteCounter(counterEntity: CounterEntity?): Int
}