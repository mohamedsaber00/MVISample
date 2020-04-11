package com.example.myapplication.util

data class DataState<T>(
    var message: Event<String>? = null,
    var loading: Boolean = false,
    val data: Event<T>?  = null
) {
    companion object {
        fun <T> error(message: String): DataState<T> {
            return DataState(message = Event(message), loading = false, data = null)
        }
        fun <T> isLoading(isLoading: Boolean): DataState<T> {
            return DataState(message = null, loading = isLoading, data = null)
        }

        fun <T> data(message: String? = null,data: T? = null): DataState<T> {
            return DataState(message = Event.messageEvent(message), loading = false, data = Event.dataEvent(data))
        }
    }

    override fun toString(): String {
        return "DataState(message=$message, loading=$loading, data=$data)"
    }

}