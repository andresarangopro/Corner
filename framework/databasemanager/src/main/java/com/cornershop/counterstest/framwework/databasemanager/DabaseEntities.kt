package com.cornershop.counterstest.framwework.databasemanager

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "counter")
data class CounterEntity(
    @PrimaryKey @ColumnInfo(name = "counter_id") var id: String,
    @ColumnInfo(name = "counter_title") var title: String,
    @ColumnInfo(name = "counter_count") var count: Int?
)
