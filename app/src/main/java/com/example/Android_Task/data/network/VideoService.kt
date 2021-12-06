package com.example.Android_Task.data.network

import com.example.Android_Task.data.MainResponse
import com.example.Android_Task.data.VideoServiceResponse
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET

interface VideoService {
    @GET("list")
    suspend fun fetchVideos(): MainResponse
    companion object{
        private const val BASE_URL="http://assesment.shaadoow.com/api/android/"
        fun create():VideoService{
           val logger=
               HttpLoggingInterceptor().apply { level=HttpLoggingInterceptor.Level.BASIC }
            val client=
                OkHttpClient.Builder()
                    .addInterceptor(logger)
                    .build()
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(VideoService::class.java)
        }
    }
}