package com.etam.cashier.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.avos.avoscloud.LogUtil;
import com.etam.cashier.activity.CashierActivity;

import org.json.JSONException;
import org.json.JSONObject;


public class CustomReceiver extends BroadcastReceiver {

    private static final String TAG = "CustomReceiver";

    @Override
    public void onReceive(Context context, Intent intent) {
        LogUtil.log.d(TAG, "Get Broadcat");
        try {
            //获取消息内容
            JSONObject json = new JSONObject(intent.getExtras().getString("com.avos.avoscloud.Data"));

            Intent intentToCashierActivity = new Intent(context, CashierActivity.class);
            intentToCashierActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intentToCashierActivity.putExtra("LeanCloudMessage",json.toString());
            context.startActivity(intentToCashierActivity);

        } catch (JSONException e) {
            Log.d(TAG, "JSONException: " + e.getMessage());
        }

    }
}

