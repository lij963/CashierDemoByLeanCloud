package com.etam.cashier.view;

import android.content.Context;
import android.util.AttributeSet;

import com.shuyu.gsyvideoplayer.video.ListGSYVideoPlayer;

/**
 * Author:  admin
 * Date:    2017/2/7.
 * Description: 本来是打算做一些修改的，时间不够，暂留这里
 */

public class CustomMediaPlayer extends ListGSYVideoPlayer {

    public CustomMediaPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public CustomMediaPlayer(Context context) {
        super(context);
    }

    public CustomMediaPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void init(Context context) {
        super.init(context);
    }
}
