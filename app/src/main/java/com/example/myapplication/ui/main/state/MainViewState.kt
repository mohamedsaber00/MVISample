package com.example.myapplication.ui.main.state

import com.example.myapplication.model.BlogPost
import com.example.myapplication.model.User

data class MainViewState(
    var blogPosts:List<BlogPost>? = null,
    var user: User? = null
)

