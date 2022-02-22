package com.cornershop.counterstest.counter

import com.cornershop.counterstest.data.vo.FetchingState
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.viewModels.CounterNavigation
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import com.cornershop.counterstest.presentation.viewModels.utils.State
import com.cornershop.counterstest.usecase.CounterUseCases
import com.cornershop.counterstest.utils.BaseUnitTest
import com.cornershop.counterstest.utils.captureValues
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.times
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test

class CounterViewModelShould : BaseUnitTest() {

    private var title: String = "title_2"

    private var id: String = "2"

    private val counterlist: List<Counter> = listOf(Counter(0, "1", "title", 1))

    private val counterlistCreatingTitle: List<Counter> =
        listOf(Counter(0, "1", "title", 1), Counter(0, id, title, 0))

    private val counterAdapterlist: List<CounterAdapter> = counterlist.toListCounterAdapter()

    private val expectedCounterlistAdapterCreatingTitle =
        Result.success(counterlistCreatingTitle.toListCounterAdapter())


    private val mockCounterUserCaseSuccess = mock<CounterUseCases> {
        onBlocking { getListCounterUseCase() } doReturn FetchingState.Success(counterlist)
        onBlocking { createCounterUseCase(title) } doReturn FetchingState.Success(
            counterlistCreatingTitle
        )
        onBlocking { increaseCounterUseCase(counterlist[0]) } doReturn FetchingState.Success(
            counterlist
        )
        onBlocking { decreaseCounterUseCase(counterlist[0]) } doReturn FetchingState.Success(
            counterlist
        )
        onBlocking { deleteCounterUseCase(counterlist[0]) } doReturn FetchingState.Success(
            counterlist
        )
    }

    @Test
    fun `when getListConterUseCase is called it should be called only once time`() {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        runBlocking {
            verify(mockCounterUserCaseSuccess, times(1)).getListCounterUseCase()
        }
    }

    @Test
    fun `when getListCounterUseCase is called it should return a list of CounterAdapter`() {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        runBlocking {
            //verify(mockCounterUserCaseSuccess, times(1)).getListCounterUseCase()
            assert(counterAdapterlist == viewModel.listCounterAdapter.value)
        }
    }

    @Test
    fun closeLoaderAfterCounterListLoad() {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        runBlocking {
            viewModel.states.captureValues {
                assertEquals(State(CounterNavigation.SetLoaderState(false)), values.last())
            }
        }
    }

    @Test
    fun `when createCounter is called it should be called only once time`() {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        runBlocking {
            viewModel.createCounter(title)
            verify(mockCounterUserCaseSuccess, times(1)).createCounterUseCase(title)
        }
    }

    @Test
    fun `when createCounter is called it should be create a counter`() {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)

        runBlocking {
            viewModel.createCounter(title)
            assert(
                expectedCounterlistAdapterCreatingTitle.getOrNull()?.equals(
                    viewModel.listCounterAdapter.value
                ) == true
            )
        }
    }

    @Test
    fun `when increaseCounter is called it should be called only once time`() = runBlockingTest {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        viewModel.increaseCounter(counterlist[0])
        verify(mockCounterUserCaseSuccess, times(1)).increaseCounterUseCase(counterlist[0])
    }

    @Test
    fun `when decrease is called it should be called only once time`() = runBlockingTest {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        viewModel.decreaseCounter(counterlist[0])
        verify(mockCounterUserCaseSuccess, times(1)).decreaseCounterUseCase(counterlist[0])

    }

    @Test
    fun `when delete is called it should be called only once time`() = runBlockingTest {
        val viewModel = CountersViewModel(mockCounterUserCaseSuccess)
        val selectCounter = counterAdapterlist[0]
        selectCounter.selected = true
        viewModel.selectedCounter(selectCounter)
        viewModel.deleteSelectedCounters()
        verify(mockCounterUserCaseSuccess, times(1)).deleteCounterUseCase(counterlist[0])
    }

}