package com.android.tne.data.network

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Created by KimBH on 2021/10/18.
 */
class ApiClient {

    companion object {

        fun getClient(): Retrofit = retrofit

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(HttpDefine.getHost())
                .client(okHttpClient())
                .addConverterFactory(gsonFactory())
                .build()
        }


        private fun gsonFactory(): GsonConverterFactory {
            val gson = GsonBuilder().setLenient().create()
            return GsonConverterFactory.create(gson)
        }

        private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

    }
}