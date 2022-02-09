package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.entities.Counter

fun List<CounterEntity>.toCounterDomainList() = map(CounterEntity::toCounterDomain)

fun CounterEntity.toCounterDomain() = Counter(
    id,
    title,
    count
)

fun Counter.toCounterEntity() = CounterEntity(
    id,
    title,
    count
)