package com.cornershop.counterstest.counter

import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.viewModels.CountersViewModel
import com.cornershop.counterstest.presentation.viewModels.utils.State
import org.junit.Assert.assertEquals
import com.cornershop.counterstest.usecase.CounterUseCases
import com.cornershop.counterstest.utils.BaseUnitTest
import com.cornershop.counterstest.utils.captureValues
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.flow.flow
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.times
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test

class CounterViewModelShould : BaseUnitTest() {

    private var title: String = "title_2"

    private var id: String = "2"

    private val counterUseCases: CounterUseCases = mock()

    private val counterlist: List<Counter> = listOf(Counter("1", "title", 1))

    private val CounterlistCreatingTitle: List<Counter> =
        listOf(Counter("1", "title", 1), Counter("$id", "$title", 0))

    private val counterAdapterlist: List<CounterAdapter> = counterlist.toListCounterAdapter()

    private val expected = Result.success(counterlist)

    private val expectedCounterlistCreatingTitle = Result.success(CounterlistCreatingTitle)

    private val expectedCounterlistAdapterCreatingTitle =
        Result.success(CounterlistCreatingTitle.toListCounterAdapter())

    private val exception = RuntimeException("Something went wrong")

    private val error = Result.failure<List<Counter>>(exception)

    private lateinit var viewModel: CountersViewModel

    @Test
    fun `when getListConterUseCase is called it should be called only once time`() =
        runBlockingTest {

            mockSuccessfulCase()

            verify(counterUseCases, times(1)).getListCounterUseCase()
        }

    @Test
    fun `when getListCounterUseCase is called it should return a list of CounterAdapter`() =
        runBlockingTest {

            mockSuccessfulCase()

            assertEquals(counterAdapterlist, viewModel.listCounterAdapter.value)
        }

    @Test
    fun emitErrorWhengetListCounterUseCaseFail() = runBlockingTest {
        whenever(counterUseCases.getListCounterUseCase()).thenReturn(
            flow {
                emit(error)
            }
        )

    }

    @Test
    fun closeLoaderAfterCounterListLoad()= runBlockingTest{

        mockSuccessfulCase()

        viewModel.states.captureValues {
            assertEquals(State(CountersViewModel.CounterNavigation.setLoaderState(false)), values.last())
        }
    }


    @Test
    fun `when createCounter is called it should be called only once time`() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.createCounter(title)

        verify(counterUseCases, times(1)).createCounterUseCase(title)

    }

    @Test
    fun `when createCounter is called it should be create a counter`() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.createCounter(title)

        assertEquals(
            expectedCounterlistAdapterCreatingTitle.getOrNull(),
            viewModel.listCounterAdapter.value
        )

    }

    @Test
    fun `when increaseCounter is called it should be called only once time`() = runBlockingTest {
        mockSuccessfulCase()

        viewModel.increaseCounter(id)

        verify(counterUseCases, times(1)).increaseCounterUseCase(id)

    }

    private suspend fun mockSuccessfulCase(){
        whenever(counterUseCases.getListCounterUseCase()).thenReturn(
            flow {
                emit(expected)
            }
        )

        whenever(counterUseCases.createCounterUseCase(title)).thenReturn(
            flow {
                emit(expectedCounterlistCreatingTitle)
            }
        )

        whenever(counterUseCases.createCounterUseCase(id)).thenReturn(
            flow {
                emit(expected)
            }
        )

        viewModel = CountersViewModel(counterUseCases)
    }
}