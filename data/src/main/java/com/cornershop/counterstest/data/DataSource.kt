package com.cornershop.counterstest.data

import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.vo.CounterState
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow

interface CommonCounterDataSource{

}

interface RemoteCounterDataSource:CommonCounterDataSource{
   suspend fun getListCounters():CounterRemoteState
   suspend fun createCounter(title:String?):CounterRemoteState
   suspend fun deleteCounter(id:String?):CounterRemoteState
   suspend fun increaseCounter(id:String?):CounterRemoteState
   suspend fun decreaseCounter(id:String?):CounterRemoteState
}

interface LocalCounterDataSource:CommonCounterDataSource{
   suspend fun getListCounters():CounterState
   suspend fun createCounter(counter:Counter?): Long
   suspend fun createCounterFromServer(counter:Counter)
   suspend fun deleteCounter(counter:Counter?):Int
   suspend fun increaseCounter(counter:Counter):Int
   suspend fun decreaseCounter(counter:Counter):Int
   suspend fun deleteAllCounterTable():Int
}