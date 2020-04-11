package com.example.myapplication.api

import androidx.lifecycle.LiveData
import com.example.myapplication.model.BlogPost
import com.example.myapplication.model.User
import com.example.myapplication.util.GenericApiResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {

    @GET("placeholder/user/{userId}")
    fun getUser(@Path("userId") userId:String  ):LiveData<GenericApiResponse<User>>

    @GET("placeholder/blogs")
    fun getBlogPosts( ):LiveData<GenericApiResponse<List<BlogPost>>>

}