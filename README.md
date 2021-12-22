# video browser with editing video
This application is used by libraries to process videos

-An Android application that was designed using Kotlin and Java, as it relies mainly on a set of technologies

*Retrofit2 for calling Api 

*jetPack Components (room-lifecycle)

*ExoPlayer to view video

*MVVM ,MVI 

*FFmpeg to cutting video

*System Download Manger 

*Broadcast reciver


-In the beginning, the application connects to the server and fetches the videos through Retrofit, then displays them using Exoplayer. The user can share a specific video, where the application downloads the video to the device’s memory using a download manager, then the video is displayed in the form of a group of images to determine what we want to cut using FFmpeg

-FFmpeg has a problem with Api 29 because of the permissions From the Android system



-Knowing that the process of displaying the video when cutting was accomplished in a wonderful way similar to WhatsApp


-I am ready for any question about the work of the code
