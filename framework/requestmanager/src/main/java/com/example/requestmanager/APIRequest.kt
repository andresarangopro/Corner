package com.example.requestmanager

import com.example.requestmanager.APIConstants.BASE_API_URL
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val client = OkHttpClient()
//val idlingResource = OkHttp3IdlingResource.create("okhttp", client)


//@Module
//@InstallIn(FragmentComponent::class)
class PlaylistModule {

//    @Provides
    fun playlistAPI(retrofit: Retrofit)= retrofit.create(CounterService::class.java)


//    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}