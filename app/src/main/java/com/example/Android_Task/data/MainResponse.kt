package com.example.Android_Task.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable

class MainResponse:Serializable  {

    @SerializedName("success")
    val success : Boolean = false

    @SerializedName("message")
    val message : String = ""

    @SerializedName("data")
    @Expose
    val data  = VideoServiceResponse()
}