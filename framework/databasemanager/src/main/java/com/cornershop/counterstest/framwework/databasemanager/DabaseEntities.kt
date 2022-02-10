package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.*

@Entity(tableName = "counter")
data class CounterEntity(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "counter_id") var id: Int,
    @ColumnInfo(name = "counter_remote_id") var id_remote: String,
    @ColumnInfo(name = "counter_title") var title: String,
    @ColumnInfo(name = "counter_count") var count: Int
)
