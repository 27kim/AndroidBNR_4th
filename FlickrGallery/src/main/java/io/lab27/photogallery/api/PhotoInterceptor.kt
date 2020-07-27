package io.lab27.photogallery.api

import okhttp3.Interceptor
import okhttp3.Response

const val API_KEY = "470195bba985c84bd483cb5d75524b89"

class PhotoInterceptor : Interceptor{
    override fun intercept(chain: Interceptor.Chain): Response {
        val originalRequest = chain.request()

        val newUrl = originalRequest.url().newBuilder()
            .addQueryParameter("api_key",API_KEY)
            .addQueryParameter("format","json")
            .addQueryParameter("nojsoncallback","1")
            .addQueryParameter("extras","url_s")
//            .addQueryParameter("safesearch","1")
            .build()

        val newRequest = originalRequest.newBuilder()
            .url(newUrl)
            .build()
        return chain.proceed(newRequest)
    }

}