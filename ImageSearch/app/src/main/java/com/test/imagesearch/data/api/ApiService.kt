package com.test.imagesearch.data.api

import com.google.gson.JsonObject
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

const val END_POINT = "w/api.php"

interface ApiService {

    @GET(END_POINT)
    suspend fun getImageContents(
        @Query("action") action: String,
        @Query("prop") prop: String,
        @Query("format") format: String,
        @Query("piprop") piprop: String,
        @Query("pithumbsize") pithubsize: Int,
        @Query("pilimit") pilimit: Int,
        @Query("generator") generator: String,
        @Query("gpssearch") gpssearch: String
    ): Response<JsonObject>
}