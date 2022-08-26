package com.android.tne.network

import okhttp3.Interceptor
import okhttp3.Response

/**
 * Created by KimBH on 2021/10/18.
 */
class HeaderInterceptor : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val request = original.newBuilder()
            .build()

        return chain.proceed(request)
    }
}