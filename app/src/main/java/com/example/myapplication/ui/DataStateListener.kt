package com.example.myapplication.ui

import com.example.myapplication.util.DataState

interface DataStateListener{
    fun onDataStateChange(dataState: DataState<*>?)
}