
package com.example.Android_Task.videoTrimmer.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Build;

import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;

import com.example.Android_Task.R;
import com.example.Android_Task.videoTrimmer.interfaces.OnProgressVideoListener;
import com.example.Android_Task.videoTrimmer.interfaces.OnRangeSeekBarListener;


public class ProgressBarView extends View implements OnRangeSeekBarListener, OnProgressVideoListener {

    private int mProgressHeight;
    private int mViewWidth;

    private final Paint mBackgroundColor = new Paint();
    private final Paint mProgressColor = new Paint();

    private Rect mBackgroundRect;
    private Rect mProgressRect;

    public ProgressBarView(@NonNull Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ProgressBarView(@NonNull Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        int lineProgress = ContextCompat.getColor(getContext(), R.color.line_color);
        int lineBackground = ContextCompat.getColor(getContext(), R.color.line_color);

        mProgressHeight = getContext().getResources().getDimensionPixelOffset(R.dimen.progress_video_line_height);

        mBackgroundColor.setAntiAlias(true);
        mBackgroundColor.setColor(lineBackground);

        mProgressColor.setAntiAlias(true);
        mProgressColor.setColor(lineProgress);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int minW = getPaddingLeft() + getPaddingRight() + getSuggestedMinimumWidth();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            mViewWidth = resolveSizeAndState(minW, widthMeasureSpec, 1);
        }

        int minH = getPaddingBottom() + getPaddingTop() + mProgressHeight;
        int viewHeight = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            viewHeight = resolveSizeAndState(minH, heightMeasureSpec, 1);
        }

        setMeasuredDimension(mViewWidth, viewHeight);
    }

    @Override
    protected void onDraw(@NonNull Canvas canvas) {
        super.onDraw(canvas);

        drawLineBackground(canvas);
        drawLineProgress(canvas);
    }

    private void drawLineBackground(@NonNull Canvas canvas) {
        if (mBackgroundRect != null) {
            canvas.drawRect(mBackgroundRect, mBackgroundColor);
        }
    }

    private void drawLineProgress(@NonNull Canvas canvas) {
        if (mProgressRect != null) {
            canvas.drawRect(mProgressRect, mProgressColor);
        }
    }

    @Override
    public void onCreate(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeek(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeekStart(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    @Override
    public void onSeekStop(RangeSeekBarView rangeSeekBarView, int index, float value) {
        updateBackgroundRect(index, value);
    }

    private void updateBackgroundRect(int index, float value) {

        if (mBackgroundRect == null) {
            mBackgroundRect = new Rect(0, 0, mViewWidth, mProgressHeight);
        }

        int newValue = (int) ((mViewWidth * value) / 100);
        if (index == 0) {
            mBackgroundRect = new Rect(newValue, mBackgroundRect.top, mBackgroundRect.right, mBackgroundRect.bottom);
        } else {
            mBackgroundRect = new Rect(mBackgroundRect.left, mBackgroundRect.top, newValue, mBackgroundRect.bottom);
        }

        updateProgress(0, 0, 0.0f);
    }

    @Override
    public void updateProgress(int time, int max, float scale) {

        if (scale == 0) {
            mProgressRect = new Rect(0, mBackgroundRect.top, 0, mBackgroundRect.bottom);
        } else {
            int newValue = (int) ((mViewWidth * scale) / 100);
            mProgressRect = new Rect(mBackgroundRect.left, mBackgroundRect.top, newValue, mBackgroundRect.bottom);
        }

        invalidate();
    }
}
