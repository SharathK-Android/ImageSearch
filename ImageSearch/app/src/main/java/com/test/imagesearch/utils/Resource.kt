package com.test.imagesearch.utils

import com.test.imagesearch.utils.Status.*

data class Resource<out T>(val status: Status, val data: T?) {
    companion object {
        fun <T> success(data: T): Resource<T> =
            Resource(status = SUCCESS, data = data)

        fun <T> error(data: T? = null): Resource<T> =
            Resource(status = ERROR, data = data)

        fun <T> loading(data: T? = null): Resource<T> =
            Resource(status = LOADING, data = data)
    }
}
