package com.example.myapplication.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.myapplication.model.BlogPost
import com.example.myapplication.model.User
import com.example.myapplication.repository.Repository
import com.example.myapplication.ui.main.state.MainStateEvent
import com.example.myapplication.ui.main.state.MainStateEvent.*
import com.example.myapplication.ui.main.state.MainViewState
import com.example.myapplication.util.AbsentLiveData
import com.example.myapplication.util.DataState

class MainViewModel :ViewModel(){


    //different States :
    private val _stateEvent : MutableLiveData<MainStateEvent> = MutableLiveData()


    // get User .. get Blogs
    private val  _viewState : MutableLiveData<MainViewState> = MutableLiveData()

    val viewState : LiveData<MainViewState>
    get() = _viewState

    //
    val dataState : LiveData<DataState<MainViewState>> = Transformations.switchMap(_stateEvent){
        _stateEvent ->
     _stateEvent?.let {
         handleStateEvent(it)
     }
    }

    private fun handleStateEvent(stateEvent: MainStateEvent): LiveData<DataState<MainViewState>>{
        return when(stateEvent){
            is GetBlogPostEvent ->{
                Repository.getBlogPosts()
            }
            is GetUserEvent ->{
                Repository.getUser(stateEvent.userId )
            }
            is None ->{
                AbsentLiveData.create()
            }
        }
    }

    fun setBlogListData(blogPosts : List<BlogPost>){
        val update = getCurrentStateOrNew()
        update.blogPosts = blogPosts
        _viewState.value = update
    }
    fun setUser(user:User){
        val update = getCurrentStateOrNew()
        update.user = user
        _viewState.value = update
    }
    fun setStateEvent(event: MainStateEvent){
        _stateEvent.value = event
    }

    fun getCurrentStateOrNew(): MainViewState {
        return viewState.value?.let { it }?: MainViewState()
    }
}