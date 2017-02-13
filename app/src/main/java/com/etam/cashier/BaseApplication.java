package com.etam.cashier;

import android.app.Application;

import com.avos.avoscloud.AVAnalytics;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVInstallation;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.PushService;
import com.avos.avoscloud.SaveCallback;
import com.etam.cashier.activity.MainActivity;
import com.lzy.okgo.OkGo;

/**
 * Author:  admin
 * Date:    2017/2/8.
 * Description:
 */

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        OkGo.init(this);
        // leancloud初始化应用信息
        AVOSCloud.initialize(this, "o724pIp4h0Wm93MNkB40RC70-gzGzoHsz",
                "USCUVJC8w3gJRJxKmwlzXMqd");
        // 启用崩溃错误统计
        AVAnalytics.enableCrashReport(this.getApplicationContext(), true);
        AVOSCloud.setLastModifyEnabled(true);
        AVOSCloud.setDebugLogEnabled(true);
        // 订阅频道，当该频道消息到来的时候，打开对应的 Activity
        PushService.setDefaultPushCallback(this, MainActivity.class);
//		PushService.subscribe(this, "", MainActivity.class);
//		PushService.subscribe(this, "private", Callback1.class);
//		PushService.subscribe(this, "protected", Callback2.class);
// 保存 installation 到服务器

        AVInstallation.getCurrentInstallation().saveInBackground(new SaveCallback() {
            @Override
            public void done(AVException e) {

                AVInstallation.getCurrentInstallation().saveInBackground();
            }
        });

    }
}
