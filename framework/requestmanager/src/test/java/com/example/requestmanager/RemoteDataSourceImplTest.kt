package com.example.requestmanager

import com.cornershop.counterstest.data.RemoteCounterDataSource
import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.entities.CounterRaw
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class RemoteDataSourceImplTest {

    private var listCounter= listOf(Counter(1,"1","",0))
    private var listCounterRaw= listOf(CounterRaw("1","",0))

    private val mockCounterApi = mock<CounterService> {
        onBlocking { getListCounter() } doReturn listCounterRaw
    }

    private val mockRemoteCounter = mock<RemoteCounterDataSource> {
        onBlocking { getListCounters() } doReturn CounterRemoteState.Success(
            listCounter
        )
    }

    @Test
    fun testGetCounterList() {
        val remoteCounter = CounterDataSource(mockCounterApi)
        runBlocking {
            val result:CounterRemoteState = remoteCounter.getListCounters()
            verify(mockCounterApi).getListCounter()
            assertTrue(result is CounterRemoteState.Success)
        }
    }

}