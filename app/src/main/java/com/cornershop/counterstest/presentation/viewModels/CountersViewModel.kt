package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.R
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.viewModels.utils.Event
import com.cornershop.counterstest.usecase.CounterUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.filterList
import javax.inject.Inject

@HiltViewModel
class CountersViewModel @Inject constructor(
    private val counterUseCases: CounterUseCases
):ViewModel() {

    private val _events = MutableLiveData<Event<CounterNavigation>>()
    val events:LiveData<Event<CounterNavigation>> get() = _events

    private val _currentCounterName = MutableLiveData<String>()
    val currentCounterName:LiveData<String> get()=_currentCounterName

    private val _listCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listCounterAdapter:LiveData<List<CounterAdapter>> get() = _listCounterAdapter

    private val _listSelectedCounterAdapter = MutableLiveData<List<CounterAdapter>>()
    val listSelectedCounterAdapter:LiveData<List<CounterAdapter>> get() = _listSelectedCounterAdapter

    init{
        this.postEvent(CounterEvent.getListCounterInit)
    }


    fun getListCounter(eventStart:CounterNavigation,
                               eventResponse:CounterNavigation) {
        _events.value = Event(eventStart)
        viewModelScope.launch {
            counterUseCases.getListCounterUseCase().collect {
                if (it.isSuccess) {
                    setListAdapter(it.getOrNull())
                    if(listCounterAdapter.value?.isNotEmpty() == true){
                        _events.value = Event(
                            CounterNavigation.setCounterList(
                                listCounterAdapter.value,
                                listCounterAdapter?.value?.getTimesSum()
                            )
                        )
                    }else{
                        _events.value = Event(CounterNavigation.onErrorLoadingCounterList(R.string.no_counters, R.string.no_counters_phrase))
                    }
                    _events.value = Event(eventResponse)
                } else {
                    Log.d("it's", "err ${it.exceptionOrNull()?.message}")
                    _events.value = Event(CounterNavigation.onErrorLoadingCounterListNetork(R.string.error_load_counters_title,R
                        .string.connection_error_description))
                    _events.value = Event(eventResponse)
                }
        }
        }
    }


    fun List<CounterAdapter>?.getTimesSum():Int? = this?.map { it.count }?.sum()

    fun filterCounterName(counterName:String?){
        if(counterName!!.isNotEmpty()){
            val temp = listCounterAdapter.value?.filterList {
                title.lowercase().contains(counterName.lowercase())
            }
            if(temp?.isNotEmpty() == true)
                _events.value = Event(CounterNavigation.setCounterList(temp,temp.getTimesSum()))
            else
                _events.value = Event(CounterNavigation.onNoResultCounterList(R.string.no_results))
        }else{
            _events.value = Event(CounterNavigation.setCounterList(listCounterAdapter.value,listCounterAdapter.value.getTimesSum()))
        }
    }


    fun manageEvent(event: CounterEvent) {
        when(event){

            is CounterEvent.CreateCounter->{
                createCounter(event.title)
            }

            is CounterEvent.IncreaseCounter->{
                increaseCounter(event.id)
            }

            is CounterEvent.DecreaseCounter->{
                decreaseCounter(event.id)
            }

            is CounterEvent.FilterCounter->{
                filterCounterName(event.counterName)
            }

            is CounterEvent.SelectCounters->{
                selectedCounter(event.listCounter)
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

        }
    }

     fun createCounter(title:String?){
         _events.value = Event(CounterNavigation.showLoaderSave)
         viewModelScope.launch {
             counterUseCases.createCounterUseCase(title).collect{
                 if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                     _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                         listCounterAdapter.value.getTimesSum()))
                     _events.value = Event(CounterNavigation.hideLoaderSave)
                 }else{
                     _events.value = Event(CounterNavigation.hideLoaderSave)
                     Log.d("CounterCreated","errr ")
                 }
             }
         }
    }

    fun selectedCounter(listCounterAdapter:List<CounterAdapter>?){
        if(listCounterAdapter?.size?:0 > 0) {
            _listSelectedCounterAdapter.value = listCounterAdapter!!
            _events.value = Event(CounterNavigation.setSelectedItemState(listCounterAdapter?.size))
        }else
            _events.value = Event(CounterNavigation.hideSelectedItemState)
    }

    fun increaseCounter(id: String?){
        viewModelScope.launch {
            counterUseCases.increaseCounterUseCase(id).collect{
                if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                    _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                        listCounterAdapter.value.getTimesSum()))
                }else{
                    Log.d("CounterCreated","errr ")
                }
            }
        }
    }

    fun postEvent(event: CounterEvent) {
        manageEvent(event)
    }

    fun decreaseCounter(id: String?){
        viewModelScope.launch {
            counterUseCases.decreaseCounterUseCase(id).collect{
                if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                    _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                        listCounterAdapter.value.getTimesSum()))
                }else{
                    Log.d("CounterCreated","errr ")
                }
            }
        }
    }

    fun deleteSelectedCounters() {
        viewModelScope.launch {
             _listSelectedCounterAdapter.value?.map {
                 GlobalScope.async {
                     counterUseCases.deleteCounterUseCase(it.id).collect {
                         if(it.isSuccess){
                             withContext(Dispatchers.Main) {
                                 setListAdapter(it.getOrNull())
                             }
                         }else{
                             Log.d("CounterCDeletes","errr ")
                         }
                     }
                 }
                }?.awaitAll()
                _events.value = Event(CounterNavigation.hideSelectedItemState)
                _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                listCounterAdapter.value.getTimesSum()))

            }

    }

    fun setListAdapter(listCounter:List<Counter>?){
        _listCounterAdapter.value = listCounter?.toListCounterAdapter()
    }

}