package com.example.myapplication.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.myapplication.api.MyRetrofitBuilder
import com.example.myapplication.model.BlogPost
import com.example.myapplication.model.User
import com.example.myapplication.ui.main.state.MainViewState
import com.example.myapplication.util.*
import retrofit2.Retrofit

object Repository {

    fun getBlogPosts(): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<List<BlogPost>,MainViewState>() {
            override fun handleApiSuccessResponse(apiSuccessResponse: ApiSuccessResponse<List<BlogPost>>) {
                result.value = DataState.data(data = MainViewState(blogPosts = apiSuccessResponse.body))
            }

            override fun createCall(): LiveData<GenericApiResponse<List<BlogPost>>> {
                return MyRetrofitBuilder.apiService.getBlogPosts()
            }

        }.asLiveData()
    }


    fun getUser(userId: String): LiveData<DataState<MainViewState>> {
        return object : NetworkBoundResource<User,MainViewState>() {
            override fun handleApiSuccessResponse(apiSuccessResponse: ApiSuccessResponse<User>) {
                result.value = DataState.data(data = MainViewState(user = apiSuccessResponse.body))
            }

            override fun createCall(): LiveData<GenericApiResponse<User>> {
                return MyRetrofitBuilder.apiService.getUser(userId)
            }

        }.asLiveData()
    }
}