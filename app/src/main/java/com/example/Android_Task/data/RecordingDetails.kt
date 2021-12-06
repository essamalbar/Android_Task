package com.example.Android_Task.data

import com.google.gson.annotations.SerializedName
import java.io.Serializable


class RecordingDetails:Serializable {

    @SerializedName("duration")
    val duration : Float = 0F

    @SerializedName("cover_img_url")
    val coverImgUrl : String = ""

    @SerializedName("type")
    val type : String = ""

    @SerializedName("description")
    val description : String = ""

    @SerializedName("recording_url")
    val recordingUrl : String = ""

    @SerializedName("status")
    val status : Int = 0

    @SerializedName("recording_id")
    val recordingId : Long = 0

    @SerializedName("streaming_hls")
    val streamingHls : String = ""


}