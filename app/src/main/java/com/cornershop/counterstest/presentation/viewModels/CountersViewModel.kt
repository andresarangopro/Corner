package com.cornershop.counterstest.presentation.viewModels



import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.*
import com.cornershop.counterstest.R
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.presentation.parcelable.CounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toCounterDomain
import com.cornershop.counterstest.presentation.parcelable.toListCounterAdapter
import com.cornershop.counterstest.presentation.parcelable.toListCounterDomain
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

    private var listSelectedCAdapter = ArrayList<CounterAdapter>()


    init{
        this.postEvent(CounterEvent.getListCounterInit)
    }


    fun getListCounter(eventStart:CounterNavigation,
                               eventResponse:CounterNavigation) {
        _events.value = Event(eventStart)
        viewModelScope.launch {
            counterUseCases.getListCounterUseCase().collect {
                if (it.isSuccess) {
//                    setListAdapter(it.getOrNull())
//                    setListCounterOnView()
                    it.getOrNull()?.let { listCounter -> insertInLocalDatabaseCounter(listCounter) }
                    _events.value = Event(eventResponse)
                } else {
                    getLocalListCounter()
                    _events.value = Event(eventResponse)
                }
          }
        }
    }


    private fun setListCounterOnView() {
        if (listCounterAdapter.value?.isNotEmpty() == true) {
            _events.value = Event(
                CounterNavigation.setCounterList(
                    listCounterAdapter.value,
                    listCounterAdapter?.value?.getTimesSum()
                )
            )
        } else {
            _events.value = Event(
                CounterNavigation.onErrorLoadingCounterList(
                    R.string.no_counters,
                    R.string.no_counters_phrase
                )
            )
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
                increaseLocalCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.DecreaseCounter->{
                decreaseLocalCounter(event.counter.toCounterDomain())
            }

            is CounterEvent.FilterCounter->{
                filterCounterName(event.counterName)
            }

            is CounterEvent.SelectCounters->{
                selectedCounter(event.counter)
            }

            is CounterEvent.DeleteSelectedCounters->{
                deleteLocalCounter()
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

     fun createCounter(title:String){
         _events.value = Event(CounterNavigation.showLoaderSave)
         viewModelScope.launch {
             counterUseCases.createCounterUseCase(title).collect{
                 if(it.isSuccess){
                     setListAdapter(it.getOrNull())
                     _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                         listCounterAdapter.value.getTimesSum()))
                     _events.value = Event(CounterNavigation.hideLoaderSave)
                     it.getOrNull()?.let { listCounter -> insertInLocalDatabaseCounter(listCounter) }
                 }else{
                      createCounterLocal(Counter(0,"0",title,0))
                     _events.value = Event(CounterNavigation.hideLoaderSave)
                     Log.d("CounterCreated","errr ")
                 }
             }
         }
    }



    fun selectedCounter(counterAdapter:CounterAdapter){
        when(counterAdapter.selected){
            true->{listSelectedCAdapter.add(counterAdapter)}
            false->{listSelectedCAdapter.remove(counterAdapter)}
        }
        if(listSelectedCAdapter?.size?:0 > 0) {
            _listSelectedCounterAdapter.value = listSelectedCAdapter!!
            _events.value = Event(CounterNavigation.setSelectedItemState(listSelectedCAdapter?.size))
        }else
            _events.value = Event(CounterNavigation.hideSelectedItemState)
    }

    suspend fun increaseCounter(id: String?){
            counterUseCases.increaseCounterUseCase(id).collect()
    }

    suspend fun decreaseCounter(id: String?){
        counterUseCases.decreaseCounterUseCase(id).collect()
    }

    fun postEvent(event: CounterEvent) {
        manageEvent(event)
    }

    fun getLocalListCounter(){
        viewModelScope.launch {
            counterUseCases.getLocalListCounterUseCase().collect {
                if(it.isSuccess){
                    if(it.getOrNull().isNullOrEmpty())
                        _events.value = Event(CounterNavigation.onErrorLoadingCounterList(R.string.no_counters, R.string.no_counters_phrase))
                    else{
                        setListAdapter(it.getOrNull())
                        setListCounterOnView()
                    }
                }else{
                    _events.value = Event(CounterNavigation.onErrorLoadingCounterList(R.string.no_counters, R.string.no_counters_phrase))
                }
            }
        }
    }


    fun setListAdapter(listCounter:List<Counter>?){
        _listCounterAdapter.value = listCounter?.toListCounterAdapter()
        clearSelectedArray()
    }

    fun unselectAll(){
        _listCounterAdapter.value?.forEach {
           it.selected = false
        }

        _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
            listCounterAdapter.value.getTimesSum()))
        clearSelectedArray()
    }

    fun clearSelectedArray(){
        listSelectedCAdapter.clear()
        _listSelectedCounterAdapter.value = listSelectedCAdapter

    }


    fun insertInLocalDatabaseCounter(listInsert:List<Counter>){
        viewModelScope.launch {
            listInsert.forEach {
                counterUseCases.createCounterFromServerUseCase(it).collect{
                    setListAdapter(listInsert)
                    setListCounterOnView()
                }
            }

        }
    }

    private suspend fun createCounterLocal(counter: Counter) {
        counterUseCases.createLocalCounterUseCase(counter).collect {
            if (it.isSuccess) {
                Log.d("CounterCreated", "success")
            } else {
                Log.d("CounterCreated", "errr ${it.exceptionOrNull()}")
            }
        }
    }


    fun increaseLocalCounter(counter:Counter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                counterUseCases.increaseLocalCounterUseCase(counter).collect {
                    withContext(Dispatchers.Main) {
                        if (it.isSuccess) {
                            setListAdapter(it.getOrNull())
                            _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                                listCounterAdapter.value.getTimesSum()))
                                counterUseCases.increaseCounterUseCase(counter.id_remote).collect()
                        } else {

                            Log.d("CounterCreated", "err ${it.exceptionOrNull()}")
                        }
                    }
                }
            }
        }
    }

    fun decreaseLocalCounter(counter:Counter){
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                counterUseCases.decreaseLocalCounterUseCase(counter).collect{
                    withContext(Dispatchers.Main) {
                        if (it.isSuccess) {
                            setListAdapter(it.getOrNull())
                            _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
                                listCounterAdapter.value.getTimesSum()))
                                counterUseCases.decreaseCounterUseCase(counter.id_remote).collect()
                        } else {

                            Log.d("CounterCreated", "err ${it.exceptionOrNull()}")
                        }
                    }
                }
            }
        }
    }

    fun deleteLocalCounter(){
        viewModelScope.launch {
            _listSelectedCounterAdapter.value?.map {counter->
                GlobalScope.async {
                    counterUseCases.deleteLocalCounterUseCase(counter.toCounterDomain()).collect{
                        if(it.isSuccess){
                            counterUseCases.deleteCounterUseCase(counter.id_remote).collect()
                            withContext(Dispatchers.Main) {
                                setListAdapter(it.getOrNull())
                            }
                        }    else {
                            Log.d("CounterCDeletes", "errr ")
                        }
                    }
                }
            }?.awaitAll()
            _events.value = Event(CounterNavigation.hideSelectedItemState)
            _events.value = Event(CounterNavigation.updateCounterList(listCounterAdapter.value,
            listCounterAdapter.value.getTimesSum()))
        }
    }


}