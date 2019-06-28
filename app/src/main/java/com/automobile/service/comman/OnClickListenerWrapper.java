package com.automobile.service.comman;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnimationUtils;

public class OnClickListenerWrapper implements OnClickListener {
    private static final long DEFAULT_MIN_INTERVAL = 500;
    private final OnClickListener mListener;
    private long mLastClickTime = 0;

    public OnClickListenerWrapper(OnClickListener listener) {
        mListener = listener;
    }

    @Override
    public void onClick(View v) {
        long currentTime = AnimationUtils.currentAnimationTimeMillis();
        if (currentTime - mLastClickTime > DEFAULT_MIN_INTERVAL) {
            mListener.onClick(v);
            mLastClickTime = currentTime;
        }
    }
}
