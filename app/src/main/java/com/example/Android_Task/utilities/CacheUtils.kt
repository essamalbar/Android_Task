package com.example.Android_Task.utilities

import android.content.Context
import java.io.File

object CacheUtils {
    fun getVideoCacheDir(context: Context): File?{
        return File(context.externalCacheDir,"video-cache")
    }
    fun cleanVideoCacheDir(context: Context){
        getVideoCacheDir(context)?.deleteRecursively()
    }
}