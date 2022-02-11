package com.example.requestmanager

import com.cornershop.counterstest.data.vo.CounterRemoteState
import com.cornershop.counterstest.entities.Counter
import com.cornershop.counterstest.entities.CounterRaw
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTERS
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER_DEC
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER_DELETE
import com.example.requestmanager.APIConstants.ENDPOINT_COUNTER_INC
import retrofit2.http.*

interface CounterService{

    @GET(ENDPOINT_COUNTERS)
    suspend fun getListCounter():List<CounterRaw>

    @POST(ENDPOINT_COUNTER)
    suspend fun createCounter(@Body title:JsonTitleServer?):List<CounterRaw>

    @POST(ENDPOINT_COUNTER_INC)
    suspend fun increaseCounter(@Body id:JsonIdServer?):List<CounterRaw>

    @POST(ENDPOINT_COUNTER_DEC)
    suspend fun decreaseCounter(@Body id:JsonIdServer?):List<CounterRaw>

    @HTTP(method = "DELETE", path = ENDPOINT_COUNTER, hasBody = true)
    suspend fun deleteCounter(@Body id:JsonIdServer?):List<CounterRaw>


}