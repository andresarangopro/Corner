package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.R
import com.cornershop.counterstest.data.vo.Resource
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterListAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.utils.BaseViewModel
import com.cornershop.counterstest.presentation.viewModels.utils.Event
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
        this.postEvent(CounterEvent.getListCounterInit)
    }


    private fun getListCounter(eventStart:CounterNavigation,
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


    fun List<CounterListAdapter>?.getTimesSum():Int? = this?.map { it.count }?.sum()

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
        object getListCounterInit : CounterEvent()
        object getListCounterFromSwipe : CounterEvent()
        data class SelectCounters(val listCounter: List<CounterListAdapter>?) : CounterEvent()
    }

    sealed class CounterNavigation(){
        object hideSelectedItemState:CounterNavigation()
        object showLoaderSave:CounterNavigation()
        object hideLoaderSave:CounterNavigation()
        object hideSwipeLoaderSave:CounterNavigation()
        object isSwipeLoaderSave:CounterNavigation()
        data class onErrorLoadingCounterListNetork(val title: Int?, val message:Int?):CounterNavigation()
        data class onErrorLoadingCounterList(val title: Int?, val message:Int?):CounterNavigation()
        data class onNoResultCounterList(val message: Int?):CounterNavigation()
        data class setLoaderState(val state:Boolean):CounterNavigation()
        data class setSelectedItemState(val items:Int?):CounterNavigation()
        data class setCounterList(val listCounter:List<CounterListAdapter>?,val timesSum:Int?):CounterNavigation()
        data class updateCounterList(val listCounter:List<CounterListAdapter>?,val timesSum:Int?):CounterNavigation()
    }
}