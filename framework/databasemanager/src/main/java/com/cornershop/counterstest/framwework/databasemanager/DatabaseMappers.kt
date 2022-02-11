package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.entities.CounterRaw

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

fun Counter.toCounterEntitySetCount(countSet:Int) = CounterEntity(
    id,
    id_remote,
    title,
    count+countSet
)

fun CounterRaw.toCounterDomain() = Counter(
    0,
    id,
    title,
    count
)

fun List<CounterRaw>.toListCounterDomain() = map(CounterRaw::toCounterDomain)

fun Counter.toUpdateEntity(number:Int) = CounterEntity(
    id,
    id_remote,
    title,
    count+number
)