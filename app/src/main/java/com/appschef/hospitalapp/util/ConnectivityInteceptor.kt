package com.appschef.hospitalapp.util

import okhttp3.Interceptor
import okhttp3.Response
import okio.IOException

class ConnectivityInteceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        if (!InternetService.instance.isOnline()) {
            throw IOException("No internet connection")
        } else {
            return chain.proceed(chain.request())
        }
    }
}