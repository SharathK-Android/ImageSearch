package com.test.imagesearch.data.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiHelper {

    private val baseUrl = "https://en.wikipedia.org/"

    private fun getRetrofit(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    private val apiService: ApiService = getRetrofit().create(ApiService::class.java)

    suspend fun getImageContents(searchQuery: String) =
        apiService.getImageContents(
            PARAM_ACTION,
            PARAM_PROP,
            PARAM_FORMAT,
            PARAM_PIPROP,
            PARAM_THUMB_SIZE,
            PARAM_LIMIT,
            PARAM_GENERATOR,
            searchQuery
        )

    companion object {
        const val PARAM_ACTION = "query"
        const val PARAM_PROP = "pageimages"
        const val PARAM_FORMAT = "json"
        const val PARAM_PIPROP = "thumbnail"
        const val PARAM_THUMB_SIZE = 400
        const val PARAM_LIMIT = 50
        const val PARAM_GENERATOR = "prefixsearch"
    }
}