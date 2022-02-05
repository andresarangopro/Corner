package com.cornershop.counterstest.data

import com.cornershop.counterstest.entities.Counter
import kotlinx.coroutines.flow.Flow

interface RemoteCounterDataSource{
   suspend fun getListCounters():Flow<Result<List<Counter>>>
}