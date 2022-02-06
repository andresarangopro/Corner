package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.utils.BaseViewModel
import com.cornershop.counterstest.usecase.CounterUseCases
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class CountersViewModel(
    private val counterUseCases: CounterUseCases
): BaseViewModel<CountersViewModel.CounterEvent, CountersViewModel.CounterNavigation>() {

    private val _events = MutableLiveData<Event<CounterNavigation>>()
    val events:LiveData<Event<CounterNavigation>> get() = _events

    init{
        viewModelScope.launch {
            _events.value = Event(CounterNavigation.setLoaderState(true))
            counterUseCases.getListCounterUseCase().collect {
                if(it.isSuccess){
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                    _events.value = Event(CounterNavigation.setCounterList(it.getOrNull()))
                }else{
                    _events.value = Event( CounterNavigation.setLoaderState(false))
                }
            }
        }
    }

    override fun manageEvent(event: CounterEvent) {
        when(event){
            is CounterEvent.testEvent->{
                _events.value = Event(CounterNavigation.setLoaderState(true))
            }

            is CounterEvent.CreateCounter->{
                createCounter(event.title)
            }

            is CounterEvent.IncreaseCounter->{
                increaseCounter(event.id)
            }

            is CounterEvent.DecreaseCounter->{
                decreaseCounter(event.id)
            }
        }
    }

     fun createCounter(title:String){
         viewModelScope.launch {
             counterUseCases.createCounterUseCase(title).collect{
                 if(it.isSuccess){
                        Log.d("CounterCreated","Success")
                 }else{
                     Log.d("CounterCreated","errr ")
                 }
             }
         }
    }

    fun increaseCounter(id: String){
        viewModelScope.launch {
            counterUseCases.increaseCounterUseCase(id).collect{
                if(it.isSuccess){
                    Log.d("CounterCreated","Success")
                }else{
                    Log.d("CounterCreated","errr ")
                }
            }
        }
    }

    fun decreaseCounter(id: String){
        viewModelScope.launch {
            counterUseCases.decreaseCounterUseCase(id).collect{
                if(it.isSuccess){
                    Log.d("CounterCreated","Success")
                }else{
                    Log.d("CounterCreated","errr ")
                }
            }
        }
    }

    sealed class CounterEvent {
        object testEvent : CounterEvent()
        data class CreateCounter(val title: String) : CounterEvent()
        data class IncreaseCounter( val id: String ) : CounterEvent()
        data class DecreaseCounter(val id: String) : CounterEvent()
    }

    sealed class CounterNavigation(){
        data class setLoaderState(val state:Boolean):CounterNavigation()
        data class setCounterList(val listCounter:List<Counter>?):CounterNavigation()
    }
}