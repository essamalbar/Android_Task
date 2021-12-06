package com.example.Android_Task.data

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.io.Serializable


class Video:Serializable {

    @SerializedName("_id")
    val id : String = ""

    @SerializedName("media_base_url")
    val mediaBaseUrl : String = ""

    @SerializedName("recording_details")
    @Expose
    val recordingDetails = RecordingDetails()

}