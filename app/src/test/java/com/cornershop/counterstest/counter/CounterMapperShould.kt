package com.cornershop.counterstest.counter

import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.utils.BaseUnitTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterMapperShould : BaseUnitTest() {

    private val counterList = listOf(Counter(0, "1", "title", 1))
    private val counterAdapter = counterList.toListCounterAdapter()

    @Test
    fun keepSameId() {

        counterList.forEachIndexed { index, item ->
            assertEquals(item.id, counterAdapter[index].id)
        }

    }

    @Test
    fun keepSameRemoteId() {
        counterList.forEachIndexed { index, item ->
            assertEquals(item.id_remote, counterAdapter[index].id_remote)
        }

    }

    @Test
    fun keepSameTitle() {
        counterList.forEachIndexed { index, item ->
            assertEquals(item.title, counterAdapter[index].title)
        }
    }

    @Test
    fun keepSameCount() {
        counterList.forEachIndexed { index, item ->
            assertEquals(item.count, counterAdapter[index].count)
        }
    }

    @Test
    fun mapDefaultSelectedStatus() {
        counterList.forEachIndexed { index, item ->
            assertEquals(false, counterAdapter[index].selected)
        }

    }

}