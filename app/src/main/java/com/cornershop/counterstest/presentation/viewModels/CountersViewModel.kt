package com.cornershop.counterstest.presentation.viewModels


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.vo.FetchingState
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toCounterDomain
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.viewModels.utils.State
import com.cornershop.counterstest.usecase.CounterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import okhttp3.internal.filterList
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
) : ViewModel() {

    private val _states = MutableLiveData<State<CounterNavigation>>()
    val states: LiveData<State<CounterNavigation>> get() = _states

    private val _listCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listCounterAdapter: LiveData<List<CounterAdapter>> get() = _listCounterAdapter

    private val _listSelectedCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listSelectedCounterAdapter: LiveData<List<CounterAdapter>> get() = _listSelectedCounterAdapter

    private var listSelectedCAdapter = ArrayList<CounterAdapter>()


    init {
        this.postEvent(CounterEvent.GetListCounterInit)
    }

    private fun getListCounter(
        eventStart: CounterNavigation,
        eventResponse: CounterNavigation
    ) {
        _states.value = State(eventStart)
        viewModelScope.launch {
            when (val fetchState = counterUseCases.getListCounterUseCase()) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                }
                is FetchingState.Error -> {
                    _states.value = State(eventResponse)
                }
            }
            _states.value = State(eventResponse)
        }
    }


    fun createCounter(title: String?) {
        _states.value = State(CounterNavigation.ShowLoaderSave)
        viewModelScope.launch {
            when (val fetchState = counterUseCases.createCounterUseCase(title)) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.HideLoaderSave)
                }
                is FetchingState.Error -> {
                    _states.value = State(CounterNavigation.HideLoaderSave)
                }
            }
        }
    }

    fun increaseCounter(counter: Counter) {
        viewModelScope.launch {
            when (val fetchState = counterUseCases.increaseCounterUseCase(counter)) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.HideSelectedItemState)
                }
                is FetchingState.Error -> {
                    _states.value = State(CounterNavigation.HideLoaderSave)
                }
            }
        }
    }

    fun decreaseCounter(counter: Counter) {
        viewModelScope.launch {
            when (val fetchState = counterUseCases.decreaseCounterUseCase(counter)) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.HideSelectedItemState)
                }
                is FetchingState.Error -> {
                    _states.value = State(CounterNavigation.HideLoaderSave)
                }
            }
        }
    }

    fun deleteSelectedCounters() {
        viewModelScope.launch {
            _listSelectedCounterAdapter.value?.map {
                when (val fetchState = counterUseCases.deleteCounterUseCase(it.toCounterDomain())) {
                    is FetchingState.Success -> {
                        withContext(Dispatchers.Main) {
                            setListAdapter(fetchState.data)
                            setListCounterOnView()
                        }
                    }
                    is FetchingState.Error -> {
                        withContext(Dispatchers.Main) {
                            _states.value = State(CounterNavigation.HideLoaderSave)
                        }
                    }
                }
            }
            _states.value = State(CounterNavigation.HideSelectedItemState)
            _states.value = State(
                CounterNavigation.UpdateCounterList(
                    listCounterAdapter.value,
                    listCounterAdapter.value.getTimesSum()
                )
            )
        }
    }

    fun selectedCounter(counterAdapter: CounterAdapter) {
        when (counterAdapter.selected) {
            true -> {
                listSelectedCAdapter.add(counterAdapter)
            }
            false -> {
                listSelectedCAdapter.remove(counterAdapter)
            }
        }
        if (listSelectedCAdapter.size > 0) {
            _listSelectedCounterAdapter.value = listSelectedCAdapter
            _states.value = State(CounterNavigation.SetSelectedItemState(listSelectedCAdapter.size))
        } else
            _states.value = State(CounterNavigation.HideSelectedItemState)
    }

    private fun manageEvent(event: CounterEvent) {
        when (event) {

            is CounterEvent.CreateCounter -> {
                createCounter(event.title)
            }

            is CounterEvent.IncreaseCounter -> {
                increaseCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.DecreaseCounter -> {
                decreaseCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.FilterCounter -> {
                filterCounterName(event.counterName)
            }

            is CounterEvent.SelectCounters -> {
                selectedCounter(event.counter)
            }

            is CounterEvent.DeleteSelectedCounters -> {
                deleteSelectedCounters()
            }

            is CounterEvent.GetListCounterFromSwipe -> {
                getListCounter(
                    CounterNavigation.IsSwipeLoaderSave,
                    CounterNavigation.HideSwipeLoaderSave
                )
            }

            is CounterEvent.GetListCounterInit -> {
                getListCounter(
                    CounterNavigation.SetLoaderState(true),
                    CounterNavigation.SetLoaderState(false)
                )
            }

            is CounterEvent.UnselectAll -> {
                unselectAll()
            }

        }
    }

    private fun filterCounterName(counterName: String?) {
        if (counterName!!.isNotEmpty()) {
            val temp = listCounterAdapter.value?.filterList {
                title.lowercase().contains(counterName.lowercase())
            }
            if (temp?.isNotEmpty() == true)
                _states.value = State(CounterNavigation.SetCounterList(temp, temp.getTimesSum()))
            else
                _states.value = State(CounterNavigation.OnNoResultCounterList(R.string.no_results))
        } else {
            _states.value = State(
                CounterNavigation.SetCounterList(
                    listCounterAdapter.value,
                    listCounterAdapter.value.getTimesSum()
                )
            )
        }
    }

    private fun setListCounterOnView() {
        if (listCounterAdapter.value?.isNotEmpty() == true) {
            _states.value = State(
                CounterNavigation.SetCounterList(
                    listCounterAdapter.value,
                    listCounterAdapter.value.getTimesSum()
                )
            )
        } else {
            _states.value = State(
                CounterNavigation.OnErrorLoadingCounterList(
                    R.string.no_counters,
                    R.string.no_counters_phrase
                )
            )
        }
    }

    private fun List<CounterAdapter>?.getTimesSum(): Int = this?.sumOf { it.count } ?: 0

    fun postEvent(event: CounterEvent) {
        manageEvent(event)
    }

    private fun setListAdapter(listCounter: List<Counter>?) {
        _listCounterAdapter.value = listCounter?.toListCounterAdapter()
        clearSelectedArray()
    }

    private fun unselectAll() {
        _listCounterAdapter.value?.forEach {
            it.selected = false
        }
        _states.value = State(
            CounterNavigation.UpdateCounterList(
                listCounterAdapter.value,
                listCounterAdapter.value.getTimesSum()
            )
        )
        clearSelectedArray()
    }

    private fun clearSelectedArray() {
        listSelectedCAdapter.clear()
        _listSelectedCounterAdapter.value = listSelectedCAdapter

    }
}