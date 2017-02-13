package com.etam.cashier.activity;

import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.etam.cashier.R;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * Author:  admin
 * Date:    2017/2/9.
 * Description:
 */

public class CashierActivity extends BaseCashierActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }



    @Override
    protected List<GSYVideoModel> getVideoUrls() {
        ArrayList urls = new ArrayList<>();
        urls.add(new GSYVideoModel(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EtamVideo/14564977406580.mp4", ""));
        urls.add(new GSYVideoModel(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EtamVideo/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", ""));
//        urls.add(new GSYVideoModel("http://baobab.wdjcdn.com/14564977406580.mp4", ""));
        return urls;
    }

    @Override
    protected ArrayList<View> generateViewPagerData() {
        LayoutInflater lf = getLayoutInflater().from(this);
        ImageView view1 = (ImageView) lf.inflate(R.layout.cashier_image, null);
        ImageView view2 = (ImageView) lf.inflate(R.layout.cashier_image, null);
        ImageView view3 = (ImageView) lf.inflate(R.layout.cashier_image, null);
        ImageView view4 = (ImageView) lf.inflate(R.layout.cashier_image, null);
        Picasso.with(this).load("http://imgsrc.baidu.com/forum/pic/item/41df0f338744ebf84d3d941cdaf9d72a6159a780.jpg").into(view1);
        Picasso.with(this).load("http://imgsrc.baidu.com/forum/w%3D580%3B/sign=d38f52c427dda3cc0be4b82831d23801/35a85edf8db1cb13d1edf748d454564e93584be1.jpg").into(view2);
        Picasso.with(this).load("http://imgsrc.baidu.com/forum/pic/item/41df0f338744ebf84d3d941cdaf9d72a6159a780.jpg").into(view3);
        Picasso.with(this).load("http://imgsrc.baidu.com/forum/pic/item/41df0f338744ebf84d3d941cdaf9d72a6159a780.jpg").into(view4);
//        view1.setImageResource(R.drawable.message_tip_bg);
//        view2.setImageResource(R.drawable.message_tip_bg);
//        view3.setImageResource(R.drawable.message_tip_bg);
//        view4.setImageResource(R.drawable.message_tip_bg);
        ArrayList viewList = new ArrayList<>();// 将要分页显示的View装入数组中
        viewList.add(view1);
        viewList.add(view2);
        viewList.add(view3);
        viewList.add(view4);
        return viewList;
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        super.onScanQRCodeSuccess(result);
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        super.onScanQRCodeOpenCameraError();
    }
}
