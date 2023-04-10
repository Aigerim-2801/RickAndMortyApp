package com.example.retrofitapp.data.repository

sealed class ApiResult<out T> {
    data class Success<out R>(val value: R): ApiResult<R>()
    data class Error(
        val message: String?,
        val throwable: Throwable?
    ): ApiResult<Nothing>()
}

