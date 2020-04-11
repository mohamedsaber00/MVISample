package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.example.myapplication.ui.main.state.MainViewState
import com.example.myapplication.util.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class NetworkBoundResource <ResponseObject,ViewStateType>{

    protected val result = MediatorLiveData<DataState<ViewStateType>>()

    init {
        result.value = DataState.isLoading(true)

        GlobalScope.launch (IO){
            delay(Constants.TESTING_NETWROK_DELAY)
            withContext(Main){
                val apiResponse = createCall()
                result.addSource(apiResponse){response ->
                    result.removeSource(apiResponse)
                    handleNetworkCall(response)
                }
            }
        }
    }

     fun handleNetworkCall(apiResponse: GenericApiResponse<ResponseObject>){
         when (apiResponse) {
             is ApiSuccessResponse -> {
                 handleApiSuccessResponse(apiResponse)
             }
             is ApiErrorResponse -> {
                 println("Debug: NetworkBoundResource : ${apiResponse.errorMessage}")
                 onReturnError(apiResponse.errorMessage)
             }
             is ApiEmptyResponse ->{
                 println("Debug: NetworkBoundResource : HTTP 204")
                 onReturnError("HTTP 204")
             }
         }
     }
    fun onReturnError(message:String){
        result.value = DataState.error(message)
    }
    abstract fun handleApiSuccessResponse(apiSuccessResponse: ApiSuccessResponse<ResponseObject>)

    abstract fun createCall(): LiveData<GenericApiResponse<ResponseObject>>

    fun asLiveData() = result as LiveData<DataState<ViewStateType>>
}