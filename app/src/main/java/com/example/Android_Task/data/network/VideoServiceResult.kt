package com.example.Android_Task.data.network

import com.example.Android_Task.data.MainResponse
import com.example.Android_Task.data.VideoServiceResponse

sealed class VideoServiceResult {
    data class Success(val data: MainResponse) : VideoServiceResult()
    data class Error(val data: Exception) : VideoServiceResult()

}