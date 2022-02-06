package com.cornershop.counterstest.presentation.viewModels


import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import kotlinx.coroutines.flow.onEach
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.usecase.CounterUseCases
import com.example.requestmanager.CounterService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import java.lang.RuntimeException

class CountersViewModel(
    private val counterUseCases: CounterUseCases
): ViewModel() {

    private val _events = MutableLiveData<Event<CounterNavigation>>()
    val events:LiveData<Event<CounterNavigation>> get() = _events


    init {



    }

    val counterList = liveData{
       // _events.value = Event(CounterNavigation.setLoaderState(true))
        emitSource(counterUseCases.getListCounterUseCase()
            .onEach {
               // _events.value = Event( CounterNavigation.setLoaderState(false))
            }
            .asLiveData())
    }


    sealed class CounterNavigation(){
        data class setLoaderState(val state:Boolean):CounterNavigation()
        data class setCounterList(val listCounter:List<Counter>?):CounterNavigation()
    }
}