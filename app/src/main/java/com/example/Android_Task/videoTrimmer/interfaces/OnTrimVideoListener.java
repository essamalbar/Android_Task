
package com.example.Android_Task.videoTrimmer.interfaces;

import android.net.Uri;

public interface OnTrimVideoListener {

    void onTrimStarted();

    void getResult(final Uri uri);
    void ffmpegResultSUCCESS();
    void ffmpegResultCANCEL();
    String setPath();

    void cancelAction();

    void onError(final String message);
}
