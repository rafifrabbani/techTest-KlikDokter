package com.appschef.hospitalapp.util

sealed class Result<T> {

    data class Success<T>(val value: T) : Result<T>()

    data class Failure<T>(val message: String) : Result<T>()

}
