package com.example.Android_Task.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class VideoServiceResponse:Serializable {
    @SerializedName("data")
    val dataList :MutableList<Video> = ArrayList()
}