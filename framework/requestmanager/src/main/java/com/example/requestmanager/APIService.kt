package com.example.requestmanager

import com.cornershop.counterstest.entities.Counter
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTERS
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER_DEC
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER_INC
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST

interface CounterService{

    @GET(ENDPOINT_COUNTERS)
    suspend fun getListCounter():List<Counter>

    @POST(ENDPOINT_COUNTER)
    suspend fun createCounter(@Body title:JsonTitleServer):List<Counter>

    @POST(ENDPOINT_COUNTER_INC)
    suspend fun increaseCounter(@Body id:JsonIdServer):List<Counter>

    @POST(ENDPOINT_COUNTER_DEC)
    suspend fun decreaseCounter(@Body id:JsonIdServer):List<Counter>

    @DELETE(ENDPOINT_COUNTER)
    suspend fun deleteCounter(@Body id:JsonIdServer):List<Counter>


}