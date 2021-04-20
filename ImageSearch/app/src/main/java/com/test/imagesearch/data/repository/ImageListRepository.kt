package com.test.imagesearch.data.repository

import com.test.imagesearch.data.api.ApiHelper

class ImageListRepository(private val apiHelper: ApiHelper) {

    suspend fun getImageContents(searchQuery: String) = apiHelper.getImageContents(searchQuery)

}