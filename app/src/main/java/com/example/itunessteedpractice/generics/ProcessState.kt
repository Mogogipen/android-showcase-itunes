package com.example.itunessteedpractice.generics

sealed class ProcessState<out T> {
    data class Success<T>(val result: T): ProcessState<T>()
    object Loading: ProcessState<Nothing>()
    data class Error(val error: Throwable): ProcessState<Nothing>()
}