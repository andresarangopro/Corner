package com.cornershop.counterstest.framwework.databasemanager

import com.cornershop.counterstest.data.LocalCounterDataSource
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Before


class LocalDatabaseCounterImplTest {

    private lateinit var localCounterDataSource: LocalCounterDataSource

    private var listCounterEntity = listOf(CounterEntity(1,"1","",0))
    private var listCounterDomain = listCounterEntity.toCounterDomainList()
    private val counterId = "1"

    private val counterDao = mock<CounterDao> {
        onBlocking { getAllCounters() } doReturn listCounterEntity
        onBlocking { getCounterById(counterId) } doReturn listCounterEntity[0]
        onBlocking { insertCounter(listCounterEntity[0]) } doReturn 1
    }

    @Before
    fun setUp() {
        localCounterDataSource = LocalCounterDataSourceImpl(counterDao)
    }

    @Test
    fun testSaveCounter() {
        runBlocking {
            localCounterDataSource.createCounter(listCounterDomain[0])
            verify(counterDao).insertCounter(listCounterEntity[0])
        }
    }

}