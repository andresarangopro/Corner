package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.R
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toCounterDomain
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.viewModels.utils.State
import com.cornershop.counterstest.usecase.CounterUseCases
import com.example.requestmanager.vo.FetchingState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.filterList
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
):ViewModel() {

    private val _states = MutableLiveData<State<CounterNavigation>>()
    val states:LiveData<State<CounterNavigation>> get() = _states

    private val _currentCounterName = MutableLiveData<String>()
    val currentCounterName:LiveData<String> get()=_currentCounterName

    private val _listCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listCounterAdapter:LiveData<List<CounterAdapter>> get() = _listCounterAdapter

    private val _listSelectedCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listSelectedCounterAdapter:LiveData<List<CounterAdapter>> get() = _listSelectedCounterAdapter

    private var listSelectedCAdapter = ArrayList<CounterAdapter>()


    init{
        this.postEvent(CounterEvent.getListCounterInit)
    }

    fun getListCounter(eventStart:CounterNavigation,
                       eventResponse:CounterNavigation) {
        _states.value = State(eventStart)
        viewModelScope.launch {
            var fetchState = counterUseCases.getListCounterUseCase()
           when( fetchState){
               is FetchingState.Success->{
                   setListAdapter(fetchState.data)
                   setListCounterOnView()
               }
               is FetchingState.Error->{
                   _states.value = State(eventResponse)
               }
           }
            _states.value = State(eventResponse)
        }
    }


    fun createCounter(title:String?){
        _states.value = State(CounterNavigation.showLoaderSave)
        viewModelScope.launch {
            var fetchState =  counterUseCases.createCounterUseCase(title)
            when( fetchState) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.hideLoaderSave)
                }
                is FetchingState.Error -> {
                    _states.value = State(CounterNavigation.hideLoaderSave)
                }
            }
        }
    }

    fun increaseCounter(counter: Counter){
        viewModelScope.launch {
            var fetchState =  counterUseCases.increaseCounterUseCase(counter)
            when( fetchState) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.hideSelectedItemState)
                }
                is FetchingState.Error -> {
                     Log.d("err","${fetchState.error.message}")
                    _states.value = State(CounterNavigation.hideLoaderSave)
                }
            }
        }
    }

    fun decreaseCounter(counter: Counter){
        viewModelScope.launch {
            var fetchState =  counterUseCases.decreaseCounterUseCase(counter)
            when( fetchState) {
                is FetchingState.Success -> {
                    setListAdapter(fetchState.data)
                    setListCounterOnView()
                    _states.value = State(CounterNavigation.hideSelectedItemState)
                }
                is FetchingState.Error -> {
                    Log.d("err","${fetchState.error.message}")
                    _states.value = State(CounterNavigation.hideLoaderSave)
                }
            }
        }
    }

    fun deleteSelectedCounters() {
        viewModelScope.launch {
            _listSelectedCounterAdapter.value?.map {
                GlobalScope.async {
                    var fetchState = counterUseCases.deleteCounterUseCase(it.toCounterDomain())
                    when (fetchState) {
                        is FetchingState.Success -> {
                            withContext(Dispatchers.Main){
                                setListAdapter(fetchState.data)
                                setListCounterOnView()
                            }
                        }
                        is FetchingState.Error -> {
                            withContext(Dispatchers.Main) {
                                _states.value = State(CounterNavigation.hideLoaderSave)
                            }
                        }
                    }
                }
            }?.awaitAll()
            _states.value = State(CounterNavigation.hideSelectedItemState)
            _states.value = State(CounterNavigation.updateCounterList(listCounterAdapter.value,
            listCounterAdapter.value.getTimesSum()))
        }
    }

    fun selectedCounter(counterAdapter:CounterAdapter){
        when(counterAdapter.selected){
            true->{listSelectedCAdapter.add(counterAdapter)}
            false->{listSelectedCAdapter.remove(counterAdapter)}
        }
        if(listSelectedCAdapter?.size?:0 > 0) {
            _listSelectedCounterAdapter.value = listSelectedCAdapter!!
            _states.value = State(CounterNavigation.setSelectedItemState(listSelectedCAdapter?.size))
        }else
            _states.value = State(CounterNavigation.hideSelectedItemState)
    }

    fun manageEvent(event: CounterEvent) {
        when(event){

            is CounterEvent.CreateCounter->{
                createCounter(event.title)
            }

            is CounterEvent.IncreaseCounter->{
                increaseCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.DecreaseCounter->{
                decreaseCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.FilterCounter->{
                filterCounterName(event.counterName)
            }

            is CounterEvent.SelectCounters->{
                selectedCounter(event.counter)
            }

            is CounterEvent.DeleteSelectedCounters->{
                deleteSelectedCounters()
            }

            is CounterEvent.getListCounterFromSwipe->{
                getListCounter(CounterNavigation.isSwipeLoaderSave,
                    CounterNavigation.hideSwipeLoaderSave)
            }

            is CounterEvent.getListCounterInit->{
                getListCounter(CounterNavigation.setLoaderState(true),
                    CounterNavigation.setLoaderState(false))
            }

            is CounterEvent.unselectAll->{
                unselectAll()
            }

        }
    }

    fun filterCounterName(counterName:String?){
        if(counterName!!.isNotEmpty()){
            val temp = listCounterAdapter.value?.filterList {
                title.lowercase().contains(counterName.lowercase())
            }
            if(temp?.isNotEmpty() == true)
                _states.value = State(CounterNavigation.setCounterList(temp,temp.getTimesSum()))
            else
                _states.value = State(CounterNavigation.onNoResultCounterList(R.string.no_results))
        }else{
            _states.value = State(CounterNavigation.setCounterList(listCounterAdapter.value,listCounterAdapter.value.getTimesSum()))
        }
    }

    private fun setListCounterOnView() {
        if (listCounterAdapter.value?.isNotEmpty() == true) {
            _states.value = State(
                CounterNavigation.setCounterList(
                    listCounterAdapter.value,
                    listCounterAdapter?.value?.getTimesSum()
                )
            )
        } else {
            _states.value = State(
                CounterNavigation.onErrorLoadingCounterList(
                    R.string.no_counters,
                    R.string.no_counters_phrase
                )
            )
        }
    }

    fun List<CounterAdapter>?.getTimesSum():Int? = this?.map { it.count }?.sum()

    fun postEvent(event: CounterEvent) {
        manageEvent(event)
    }

    fun setListAdapter(listCounter:List<Counter>?){
        _listCounterAdapter.value = listCounter?.toListCounterAdapter()
        clearSelectedArray()
    }

    fun unselectAll(){
        _listCounterAdapter.value?.forEach {
           it.selected = false
        }
        _states.value = State(CounterNavigation.updateCounterList(listCounterAdapter.value,
            listCounterAdapter.value.getTimesSum()))
        clearSelectedArray()
    }

    fun clearSelectedArray(){
        listSelectedCAdapter.clear()
        _listSelectedCounterAdapter.value = listSelectedCAdapter

    }
}