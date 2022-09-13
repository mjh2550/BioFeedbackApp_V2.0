package com.android.tne.data.network

import com.android.tne.domain.model.Test
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import retrofit2.http.Body
import retrofit2.http.GET

/**
 * Created by KimBH on 2021/10/18.
 */
interface ApiClient {

    companion object {

        fun getClient(): ApiClient = retrofit.create(ApiClient::class.java)

        private fun okHttpClient(): OkHttpClient = OkHttpClient.Builder()
            .addInterceptor(HeaderInterceptor())
            .build()

        private fun gsonFactory(): GsonConverterFactory {
            val gson = GsonBuilder().setLenient().create()
            return GsonConverterFactory.create(gson)
        }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(HttpDefine.getHost())
                .client(okHttpClient())
                .addConverterFactory(gsonFactory())
                .build()
        }

    }

    //Api List
    @GET("/mobile/v1/api/getAllList.do")
    fun getAllList() : Call<ArrayList<Test>>
}