package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterListAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.utils.BaseViewModel
import com.cornershop.counterstest.usecase.CounterUseCases
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import okhttp3.internal.filterList

class CountersViewModel(
    private val counterUseCases: CounterUseCases
): BaseViewModel<CountersViewModel.CounterEvent, CountersViewModel.CounterNavigation>() {

    private val _events = MutableLiveData<Event<CounterNavigation>>()
    val events:LiveData<Event<CounterNavigation>> get() = _events

    private val _currentCounterName = MutableLiveData<String>()
    val currentCounterName:LiveData<String> get()=_currentCounterName

    private val _listCounterAdapter = MutableLiveData<List<CounterListAdapter>>()
    private val listCounterAdapter:LiveData<List<CounterListAdapter>> get() = _listCounterAdapter

    private val _listSelectedCounterAdapter = MutableLiveData<List<CounterListAdapter>>()
    private val listSelectedCounterAdapter:LiveData<List<CounterListAdapter>> get() = _listSelectedCounterAdapter

    init{
        viewModelScope.launch {
            _events.value = Event(CounterNavigation.setLoaderState(true))
            counterUseCases.getListCounterUseCase().collect {
                if(it.isSuccess){
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                     setListAdapter(it.getOrNull())
                    _events.value = Event(CounterNavigation.setCounterList(listCounterAdapter.value,listCounterAdapter?.value?.getTimesSum()))
                }else{
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                }
            }
        }
    }

    fun List<CounterListAdapter>?.getTimesSum():Int? = this?.map { it.count }?.sum()

    fun filterCounterName(counterName:String?){
        if(counterName!!.isNotEmpty()){
            val temp = listCounterAdapter.value?.filterList {
                title.lowercase().contains(counterName.lowercase())
            }
            _events.value = Event(CounterNavigation.setCounterList(temp,temp.getTimesSum()))
        }else{
            _events.value = Event(CounterNavigation.setCounterList(listCounterAdapter.value,listCounterAdapter.value.getTimesSum()))
        }
    }


    override fun manageEvent(event: CounterEvent) {
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

        }
    }

     fun createCounter(title:String?){
         viewModelScope.launch {
             counterUseCases.createCounterUseCase(title).collect{
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

    fun selectedCounter(listCounterAdapter:List<CounterListAdapter>?){
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

    sealed class CounterEvent {
        data class CreateCounter(val title: String?) : CounterEvent()
        data class FilterCounter(val counterName: String?) : CounterEvent()
        data class IncreaseCounter( val id: String? ) : CounterEvent()
        data class DecreaseCounter(val id: String?) : CounterEvent()
        object DeleteSelectedCounters : CounterEvent()
        data class SelectCounters(val listCounter: List<CounterListAdapter>?) : CounterEvent()
    }

    sealed class CounterNavigation(){
        object hideSelectedItemState:CounterNavigation()
        data class setLoaderState(val state:Boolean):CounterNavigation()
        data class setSelectedItemState(val items:Int?):CounterNavigation()
        data class setCounterList(val listCounter:List<CounterListAdapter>?,val timesSum:Int?):CounterNavigation()
        data class updateCounterList(val listCounter:List<CounterListAdapter>?,val timesSum:Int?):CounterNavigation()
    }
}