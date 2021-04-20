package com.test.imagesearch.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.test.imagesearch.data.api.ApiHelper
import com.test.imagesearch.data.repository.ImageListRepository
import com.test.imagesearch.utils.Resource
import kotlinx.coroutines.*
import retrofit2.Response

class ImageListViewModel : ViewModel() {

    private val imageListRepository: ImageListRepository = ImageListRepository(ApiHelper())
    var liveData = MutableLiveData<Resource<JsonObject?>>()
    var job: Job? = null

    fun getImageContents(searchQuery: String) {
        liveData.value = Resource.loading()
        job = CoroutineScope(Dispatchers.IO).launch {
            try {
                handleResponse(response = imageListRepository.getImageContents(searchQuery))
            } catch (exception: Exception) {
                handleResponse(isError = true)
            }
        }
    }

    private suspend fun handleResponse(
        isError: Boolean = false,
        response: Response<JsonObject>? = null
    ) {
        withContext(Dispatchers.Main) {
            if (!isError) {
                liveData.value = Resource.success(response?.body())
            } else {
                liveData.value = Resource.error()
            }
        }
    }

    fun cancelJob() {
        job?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        job?.cancel()
    }
}