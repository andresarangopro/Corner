package com.example.requestmanager

import com.example.requestmanager.APIConstants.BASE_API_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

val client = OkHttpClient()

@Module
@InstallIn(SingletonComponent::class)
object CounterModule {

    @Singleton
    @Provides
    fun counterAPI(retrofit: Retrofit): CounterService = retrofit.create(CounterService::class.java)


    @Singleton
    @Provides
    fun retrofit(): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_API_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

}