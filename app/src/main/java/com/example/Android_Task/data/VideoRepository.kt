package com.example.Android_Task.data

import com.example.Android_Task.data.network.VideoService
import com.example.Android_Task.data.network.VideoServiceResult
import kotlinx.coroutines.channels.ConflatedBroadcastChannel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import retrofit2.HttpException
import java.io.IOException


class VideoRepository(private val videoService: VideoService) {

    private val videoResult=ConflatedBroadcastChannel<VideoServiceResult>()
    suspend fun getVideoLine(): Flow<VideoServiceResult> {
        requestVideo()
        return videoResult.asFlow()
    }
    private suspend fun requestVideo(){
        try {
            val response=videoService.fetchVideos()
            videoResult.offer(VideoServiceResult.Success(response))
        }catch (exp:IOException){
            videoResult.offer(VideoServiceResult.Error(exp))
        }
        catch (exp: HttpException){
            videoResult.offer(VideoServiceResult.Error(exp))
        }
    }

}