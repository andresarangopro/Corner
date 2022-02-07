package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterListAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.utils.BaseViewModel
import com.cornershop.counterstest.usecase.CounterUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
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

    init{
        viewModelScope.launch {
            _events.value = Event(CounterNavigation.setLoaderState(true))
            counterUseCases.getListCounterUseCase().collect {
                if(it.isSuccess){
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                     setListAdapter(it.getOrNull())
                    _events.value = Event(CounterNavigation.setCounterList(listCounterAdapter.value))
                }else{
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                }
            }
        }
    }

    fun filterCounterName(counterName:String?){
        if(counterName!!.isNotEmpty()){
            val temp = listCounterAdapter.value?.filterList {
                title.contains(counterName)
            }
            _events.value = Event(CounterNavigation.setCounterList(temp))
        }else{
            _events.value = Event(CounterNavigation.setCounterList(listCounterAdapter.value))
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

            is CounterEvent.cancelFilter->{
                _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value))
            }
        }
    }

     fun createCounter(title:String?){
         viewModelScope.launch {
             counterUseCases.createCounterUseCase(title).collect{
                 if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                     _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value))
                 }else{
                     Log.d("CounterCreated","errr ")
                 }
             }
         }
    }

    fun increaseCounter(id: String?){
        viewModelScope.launch {
            counterUseCases.increaseCounterUseCase(id).collect{
                if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                    _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value))
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
                    _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value))
                }else{
                    Log.d("CounterCreated","errr ")
                }
            }
        }
    }

    fun setListAdapter(listCounter:List<Counter>?){
        _listCounterAdapter.value = listCounter?.toListCounterAdapter()
    }

    sealed class CounterEvent {
        object cancelFilter : CounterEvent()
        data class CreateCounter(val title: String?) : CounterEvent()
        data class FilterCounter(val counterName: String?) : CounterEvent()
        data class IncreaseCounter( val id: String? ) : CounterEvent()
        data class DecreaseCounter(val id: String?) : CounterEvent()
    }

    sealed class CounterNavigation(){
        data class setLoaderState(val state:Boolean):CounterNavigation()
        data class setCounterList(val listCounter:List<CounterListAdapter>?):CounterNavigation()
        data class updateCounterList(val listCounter:List<CounterListAdapter>?):CounterNavigation()
    }
}