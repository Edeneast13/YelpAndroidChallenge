package com.ngmatt.weedmapsandroidcodechallenge.network.api

import com.ngmatt.weedmapsandroidcodechallenge.BuildConfig
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.moshi.MoshiConverterFactory
import java.util.concurrent.TimeUnit

class NetworkingClient {
    private val baseUrl = "https://api.yelp.com/v3/"
    private val connectionTimeOut: Long = 30

    fun buildClient() : Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .client(getHttpClient())
            .build()
    }

    private fun getHttpClient() : OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val httpClientBuilder = OkHttpClient.Builder()
        httpClientBuilder.connectTimeout(connectionTimeOut, TimeUnit.SECONDS)
        httpClientBuilder.writeTimeout(connectionTimeOut, TimeUnit.SECONDS)
        httpClientBuilder.readTimeout(connectionTimeOut, TimeUnit.SECONDS)
        httpClientBuilder.addInterceptor(HeaderInterceptor())

        // only show http logs when running a debug build
        if(BuildConfig.DEBUG) {
            httpClientBuilder.addInterceptor(loggingInterceptor)
        }

        return httpClientBuilder.build()
    }

    class HeaderInterceptor: Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val requestBuilder = chain.request().newBuilder()
            requestBuilder.addHeader("Authorization", "Bearer 2dIFzlg8vmKe9zKfASz07UrfglNrOMgcuyI7fHOrCL4eKbXvWWEF53s3d2LsWf0IwEsoPg6CJYIn5wgPi-mGSuGDJC4yUB2edzDuLb0g683V0Bj0dRaH8ehsQUDeYXYx")
            return chain.proceed(requestBuilder.build())
        }
    }
}