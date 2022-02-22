package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.*

@Dao
interface CounterDao {

    @Query("SELECT * FROM Counter")
    fun getAllCounters(): List<CounterEntity>

    @Query("SELECT * FROM counter WHERE counter_remote_id = :id")
    suspend fun getCounterById(id: String): CounterEntity

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCounter(counterEntity: CounterEntity?): Long

    @Update(entity = CounterEntity::class)
    fun increaseCounterUpd(obj: CounterEntity): Int

    @Update(entity = CounterEntity::class)
    fun decreaseCounterUpd(obj: CounterEntity): Int

    @Delete
    suspend fun deleteCounter(counterEntity: CounterEntity?): Int

    @Query("DELETE FROM counter")
    fun nukeTable(): Int
}