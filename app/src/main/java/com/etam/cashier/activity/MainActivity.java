package com.etam.cashier.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.TextView;

import com.etam.cashier.BaseActivity;
import com.etam.cashier.R;

import java.io.File;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {


    @BindView(R.id.tv_user_id)
    TextView tvUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        downloadVideo();

        showUserId();

    }

    private void showUserId() {

        SharedPreferences sharedPreferences= PreferenceManager.getDefaultSharedPreferences(this);
        String userID = sharedPreferences.getString("userId", "未设置");
        tvUserId.setText(userID);
    }

    private void downloadVideo() {
        if (!isVideoLoadSuccess()) {
            Intent intent = new Intent(this, DownloadManagerActivity.class);
            intent.putExtra("needUpdate", true);
            startActivity(intent);
        }
    }

    private boolean isVideoLoadSuccess() {
        boolean isFilesAllExist = true;
        ArrayList<File> videos = new ArrayList();
        videos.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EtamVideo/14564977406580.mp4"));
        videos.add(new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EtamVideo/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"));
        for (File file : videos
                ) {
            if (!file.exists()) {
                isFilesAllExist = false;
                break;
            }
        }

        return isFilesAllExist;
    }


    @OnClick({R.id.btn_cashier, R.id.btn_submit_user_id, R.id.btn_update_video})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_cashier:
                startActivity(new Intent(this, CashierActivity.class));
                break;
            case R.id.btn_submit_user_id:
                startActivityForResult(new Intent(this, UserIdActivity.class),0);

                break;
            case R.id.btn_update_video:
                startActivity(new Intent(this, DownloadManagerActivity.class));
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode==RESULT_OK){
            //userId设置成功
            showUserId();
        }
    }
}
