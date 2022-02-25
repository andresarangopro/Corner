package com.example.requestmanager


import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.entities.CounterRaw
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import org.junit.Test
import org.junit.Assert.*

class RemoteDataSourceImplTest {

    private var listCounterRaw = listOf(CounterRaw("1", "", 0))

    private val mockCounterApi = mock<CounterService> {
        onBlocking { getListCounter() } doReturn listCounterRaw
    }


    @Test
    fun testGetCounterList() {
        val remoteCounter = CounterDataSource(mockCounterApi)
        runBlocking {
            val result: CounterRemoteState = remoteCounter.getListCounters()
            verify(mockCounterApi).getListCounter()
            assertTrue(result is CounterRemoteState.Success)
        }
    }

}