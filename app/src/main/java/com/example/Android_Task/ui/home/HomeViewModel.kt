package com.example.Android_Task.ui.home

import androidx.lifecycle.*
import com.example.Android_Task.data.VideoRepository
import com.example.Android_Task.data.network.VideoServiceResult
import com.example.Android_Task.ui.home.player.PlayerState
import com.google.android.exoplayer2.Player

class HomeViewModel(private val repository: VideoRepository) : ViewModel() {

   private val playerState= PlayerState(true,0,0L)
   val retry: MutableLiveData<Boolean> =MutableLiveData(false)
    private fun getVideoServiceResult():LiveData<VideoServiceResult> = liveData{
        val videos=repository.getVideoLine().asLiveData()
         emitSource(videos)

   }
    private val _retry:LiveData<VideoServiceResult> =
        Transformations.switchMap(retry){getVideoServiceResult()}

    val urls:LiveData<List<String>> =
        Transformations.map(_retry) { videos ->
            val ListofUrl = mutableListOf<String> ()

            when(videos){
                is VideoServiceResult.Success ->
              {
                videos.data.data.dataList.forEach{ video ->
                    ListofUrl.add(video.recordingDetails.streamingHls)


                }
                  return@map ListofUrl
             }
                is VideoServiceResult.Error -> emptyList<String>()
            }


        }
    val DownloadUrls:LiveData<List<String>> =
        Transformations.map(_retry) { videos ->

            val ListofDownloadUrl=mutableListOf<String> ()
            when(videos){
                is VideoServiceResult.Success ->
                {
                    videos.data.data.dataList.forEach{ video ->

                        ListofDownloadUrl.add(video.mediaBaseUrl+video.recordingDetails.recordingUrl)

                    }
                    return@map ListofDownloadUrl
                }
                is VideoServiceResult.Error -> emptyList<String>()
            }


        }

    fun setPlayerState(player:Player){
        playerState.playWhenReady=player.playWhenReady
        playerState.currentWindow = player.currentWindowIndex
        playerState.playbackPosition=player.currentPosition

    }
 fun getPlayerState()=playerState

}