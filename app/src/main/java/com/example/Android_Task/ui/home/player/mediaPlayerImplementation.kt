package com.example.Android_Task.ui.home.player

import android.content.Context
import android.net.Uri
import com.danikula.videocache.CacheListener
import com.danikula.videocache.HttpProxyCacheServer
import com.example.Android_Task.utilities.CacheUtils
import com.google.android.exoplayer2.DefaultLoadControl
import com.google.android.exoplayer2.DefaultRenderersFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.source.ConcatenatingMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.source.ShuffleOrder
import com.google.android.exoplayer2.source.hls.HlsMediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.upstream.BandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.internal.synchronized
import java.io.File



class mediaPlayerImplementation  private constructor(private val context: Context):CacheListener{

private val proxy:HttpProxyCacheServer=
    HttpProxyCacheServer.Builder(context.applicationContext)
        .cacheDirectory (CacheUtils.getVideoCacheDir(context.applicationContext))
        .build()

    val player: SimpleExoPlayer

    init {
        player= initializePlayer()
    }

    companion object{
        @Volatile
        public var INSTANCE :mediaPlayerImplementation?=null

        @InternalCoroutinesApi
        fun getInstance(context: Context):mediaPlayerImplementation{
            val tempInstance=INSTANCE
            if(tempInstance != null) return tempInstance
            synchronized(this){
                val newInstance =mediaPlayerImplementation(context)
                INSTANCE=newInstance
                return newInstance
            }
        }
    }
 private fun initializePlayer():SimpleExoPlayer{
    val bandWidthMeter:BandwidthMeter=DefaultBandwidthMeter.Builder(context).build()
    val videoTrackSelectionFactory: TrackSelection.Factory=AdaptiveTrackSelection.Factory()
    val trackSelector =DefaultTrackSelector(context,videoTrackSelectionFactory)
    val loadControl =DefaultLoadControl()
    val rendersFactory=DefaultRenderersFactory(context)

    return SimpleExoPlayer.Builder(context,rendersFactory)
        .setBandwidthMeter(bandWidthMeter)
        .setLoadControl(loadControl)
        .setTrackSelector(trackSelector)
        .build()

    }
  fun play(playerState: PlayerState ,urls:List<String>){
      player.prepare(buildMediaSourcePlayList(urls),false,false)

      player.repeatMode=Player.REPEAT_MODE_ONE
      player.seekTo(playerState.currentWindow,playerState.playbackPosition)

      player.playWhenReady=playerState.playWhenReady

  }
private fun buildMediaSource(url :String):MediaSource{
    setProxy(url)
    val userAgent = Util.getUserAgent(context,"Android task")
    return HlsMediaSource
        .Factory(DefaultDataSourceFactory(context,userAgent))
        .createMediaSource(Uri.parse(proxy.getProxyUrl(url)))

}

    private fun setProxy(url: String) {
       proxy.registerCacheListener(this,url)
    }

    fun releasePlayer() {
        player.release()
        proxy.unregisterCacheListener(this)
    }
    override fun onCacheAvailable(cacheFile: File?, url: String?, percentsAvailable: Int) {}


    private fun buildMediaSourcePlayList(urls: List<String>): MediaSource {
     val concatenatingMediaSource = ConcatenatingMediaSource(
     false,
     true,
      ShuffleOrder.DefaultShuffleOrder(urls.size-1),
      buildMediaSource(urls[1])
 )
    for(i in 2..urls.lastIndex){
        concatenatingMediaSource.addMediaSource(buildMediaSource(urls[i]))
    }
    return concatenatingMediaSource
    }



}