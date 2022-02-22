package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.entities.Counter

fun List<CounterEntity>.toCounterDomainList() = map(CounterEntity::toCounterDomain)

fun CounterEntity.toCounterDomain() =
    Counter(
        id,
        id_remote,
        title,
        count
    )

fun Counter.toCounterEntity() = CounterEntity(
    id,
    id_remote,
    title,
    count
)

fun Counter.toCounterEntitySetCount(countSet: Int) = CounterEntity(
    id,
    id_remote,
    title,
    count + countSet
)

