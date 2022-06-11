package com.example.onigiri_restaurant_recommendation.data.remote.network

import androidx.viewbinding.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiConfig {
    fun provideApiService(): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://focused-house-350008.et.r.appspot.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor(
                        if(BuildConfig.DEBUG) {
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
                        } else {
                            HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
                        }
                    )
                    .build()
            )
            .build()
        return retrofit.create(ApiService::class.java)
    }
}