package com.example.Android_Task.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.Android_Task.data.VideoRepository

class HomeViewModelFactory( private val videoRepository:VideoRepository) :ViewModelProvider.Factory{
@Suppress("UNCHECKED_CASt")
    override fun <T : ViewModel> create(modelClass: Class<T>): T
    {
        return HomeViewModel(videoRepository) as T
    }

}