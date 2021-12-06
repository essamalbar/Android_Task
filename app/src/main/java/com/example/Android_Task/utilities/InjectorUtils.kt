package com.example.Android_Task.utilities

import com.example.Android_Task.data.VideoRepository
import com.example.Android_Task.data.network.VideoService
import com.example.Android_Task.ui.home.HomeViewModel
import com.example.Android_Task.ui.home.HomeViewModelFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview

@FlowPreview
@ExperimentalCoroutinesApi
object InjectorUtils {
    fun provideHomeViewModelFactory():HomeViewModelFactory{
        val repository= VideoRepository(VideoService.create())
        return HomeViewModelFactory(repository)
    }
}