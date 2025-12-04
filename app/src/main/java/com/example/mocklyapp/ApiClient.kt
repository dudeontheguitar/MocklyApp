// ApiClient.kt
package com.example.mocklyapp

import android.content.Context
import com.example.mocklyapp.data.auth.local.AuthLocalDataSource
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiClient {

    private const val BASE_URL = "http://10.0.2.2:8080/api/"

    val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun authedRetrofit(authLocal: AuthLocalDataSource): Retrofit {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val authInterceptor = Interceptor { chain ->
            val original = chain.request()

            // не добавляем токен для /auth/*
            if (original.url.encodedPath.contains("/auth/")) {
                return@Interceptor chain.proceed(original)
            }

            val tokens = authLocal.getTokens()
            val newRequest = if (tokens != null) {
                original.newBuilder()
                    .addHeader("Authorization", "Bearer ${tokens.accessToken}")
                    .build()
            } else {
                original
            }

            chain.proceed(newRequest)
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(authInterceptor)
            .addInterceptor(logging)
            .build()

        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}
