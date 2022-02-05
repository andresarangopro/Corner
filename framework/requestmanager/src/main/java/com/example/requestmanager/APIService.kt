package com.example.requestmanager

import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTERS
import retrofit2.http.GET

interface CounterService{
    @GET(ENDPOINT_COUNTERS)
    suspend fun getListCounter():List<Counter>

}