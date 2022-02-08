package com.example.requestmanager

object APIConstants {

    const val BASE_API_URL ="http://192.168.1.6:3000/api/v1/"

    const val ENDPOINT_COUNTERS ="counters"
    const val ENDPOINT_COUNTER ="counter"
    const val ENDPOINT_COUNTER_DELETE ="counter/{id}"
    const val ENDPOINT_COUNTER_INC ="$ENDPOINT_COUNTER/inc"
    const val ENDPOINT_COUNTER_DEC ="$ENDPOINT_COUNTER/dec"


    const val TITLE="title"
    const val ID="id"
}