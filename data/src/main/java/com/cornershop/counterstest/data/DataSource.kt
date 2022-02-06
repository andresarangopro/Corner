package com.cornershop.counterstest.data

import com.cornershop.counterstest.entities.Counter
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.FragmentComponent
import dagger.hilt.android.components.ViewModelComponent
import kotlinx.coroutines.flow.Flow


interface RemoteCounterDataSource{
   suspend fun getListCounters():Flow<Result<List<Counter>>>
   suspend fun createCounter(title:String):Flow<Result<List<Counter>>>
   suspend fun increaseCounter(id:String):Flow<Result<List<Counter>>>
   suspend fun decreaseCounter(id:String):Flow<Result<List<Counter>>>
   suspend fun deleteCounter(id:String):Flow<Result<List<Counter>>>
}