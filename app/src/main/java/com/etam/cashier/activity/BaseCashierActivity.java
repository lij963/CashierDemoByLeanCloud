package com.etam.cashier.activity;

import android.animation.ValueAnimator;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.etam.cashier.BaseActivity;
import com.etam.cashier.R;
import com.etam.cashier.adapter.CashierPageAdapter;
import com.etam.cashier.bean.LeanCloudMessage;
import com.etam.cashier.callback.SamplePlayerListener;
import com.etam.cashier.uitls.DensityUtil;
import com.etam.cashier.view.CustomMediaPlayer;
import com.etam.cashier.view.GoodsListItem;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.shuyu.gsyvideoplayer.GSYPreViewManager;
import com.shuyu.gsyvideoplayer.GSYVideoManager;
import com.shuyu.gsyvideoplayer.GSYVideoPlayer;
import com.shuyu.gsyvideoplayer.model.GSYVideoModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.bingoogolapple.qrcode.core.QRCodeView;
import cn.bingoogolapple.qrcode.zxing.ZXingView;

/**
 * Author:  admin
 * Date:    2017/2/9.
 * Description:
 */

public abstract class BaseCashierActivity extends BaseActivity  implements QRCodeView.Delegate{


    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.player)
    CustomMediaPlayer mPlayer;
    @BindView(R.id.ll_left)
    LinearLayout llLeft;
    @BindView(R.id.rl_right)
    RelativeLayout rlRight;
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.zxingview)
    ZXingView zXingView;
    @BindView(R.id.iv_wechat)
    ImageView ivWechat;
    @BindView(R.id.iv_alipay)
    ImageView ivAlipay;
    @BindView(R.id.ll_qr_code)
    LinearLayout llQrCode;
    @BindView(R.id.tv_order_number)
    TextView tvOrderNumber;
    @BindView(R.id.ll_goods)
    LinearLayout llGoods;
    @BindView(R.id.tv_total_price)
    TextView tvTotalPrice;
    @BindView(R.id.tv_payed_price)
    TextView tvPayedPrice;
    @BindView(R.id.tv_change_price)
    TextView tvChangePrice;
    @BindView(R.id.tv_cashier_name)
    TextView tvCashierName;
    @BindView(R.id.tv_cashier_number)
    TextView tvCashierNumber;
    @BindView(R.id.iv_qr_code)
    ImageView ivQrCode;
//    private boolean isPlay;
//    private boolean isPause;
    private int screenWidth;
    private int screenHeight;
    int leftMenuWidth = 200;//左侧菜单栏的宽度，单位：dp

    List<GSYVideoModel> urls;
    private Timer timer;
    int viewPagerIndex = 0;
    private boolean menuIsShown;
    protected LeanCloudMessage leanCloudMessage;
    private boolean isPlaying;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cashier);
        ButterKnife.bind(this);
        zXingView.setDelegate(this);
        zXingView.changeToScanBarcodeStyle();

        //初始化播放器
        initPlayerView();
        //初始化自动播放
        initViewPager();
        //获取屏幕宽高
        loadScreenSize();

        loadLeanCloudMsg(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        loadLeanCloudMsg(intent);
    }

    private void loadLeanCloudMsg(Intent intent) {

        String leanCloudMessageJson = intent.getStringExtra("LeanCloudMessage");
        if (TextUtils.isEmpty(leanCloudMessageJson)) {
            return;
        }
        try {
            leanCloudMessage = new Gson().fromJson(leanCloudMessageJson, LeanCloudMessage.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
            return;
        }
        //区分消息内容
        switch (leanCloudMessage.getTitle()) {
            case "showListOfGoods":
                showMenuAndDetail();
                break;
            case "hideListOfGoods":
                hideMenuAndDetail();
                break;
            case "showVipCode":
                showVipCode();
                break;
            case "hideVipCode":
                hideVipCode();
                break;
            case "showQrCode":
                showQrCode();
                break;
            case "hideQrCode":
                hideQrCode();
                break;
            case "other"://其他需求
                break;
        }
    }

    private void loadScreenSize() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        screenWidth = dm.widthPixels;
        screenHeight = dm.heightPixels;
    }

    /**
     * 缩放视频，显示左侧和下方的部分
     */
    public void showMenuAndDetail() {
        //填充数据
        if (leanCloudMessage == null) {
            //获取不到商品数据则停止
            return;
        } else {
            //将商品列表填充到界面上
            if (leanCloudMessage.getAlert() != null && leanCloudMessage.getAlert().getGoodsList() != null && leanCloudMessage.getAlert().getGoodsList().size() > 0) {
                tvOrderNumber.setText(leanCloudMessage.getAlert().getOrderNumber());
                llGoods.removeAllViews();
                for (int x = 0; x < leanCloudMessage.getAlert().getGoodsList().size(); x++) {
                    GoodsListItem item = new GoodsListItem(this);
                    item.setGoodsName(leanCloudMessage.getAlert().getGoodsList().get(x).getName());
                    item.setGoodsNumber(leanCloudMessage.getAlert().getGoodsList().get(x).getNumber());
                    item.setGoodsPrice(leanCloudMessage.getAlert().getGoodsList().get(x).getPrice());
                    llGoods.addView(item);
                }
                tvTotalPrice.setText(leanCloudMessage.getAlert().getTotalPrice());
                tvPayedPrice.setText(leanCloudMessage.getAlert().getPayedPrice());
                tvChangePrice.setText(leanCloudMessage.getAlert().getChangePrice());
                tvCashierName.setText(leanCloudMessage.getAlert().getCashierName());
                tvCashierNumber.setText(leanCloudMessage.getAlert().getCashierNumber());
            }
        }

        //判断是否已经显示了，已经显示则不执行以下动画
        if (menuIsShown) {
            return;
        }
        menuIsShown = true;
        //1.调用ofInt(int...values)方法创建ValueAnimator对象
        ValueAnimator mAnimator = ValueAnimator.ofInt(0, leftMenuWidth);
        //2.为目标对象的属性变化设置监听器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                int animatorValue = (int) animation.getAnimatedValue();
                //将右侧缩放
                ViewGroup.LayoutParams llRightLayoutParams = llRight.getLayoutParams();
                llRightLayoutParams.width = screenWidth - DensityUtil.dip2px(BaseCashierActivity.this, animatorValue);
                llRight.setLayoutParams(llRightLayoutParams);
                //显示底部
                ViewGroup.LayoutParams rlRightLayoutParams = rlRight.getLayoutParams();
                rlRightLayoutParams.height = screenHeight - DensityUtil.dip2px(BaseCashierActivity.this, animatorValue / 2);
                rlRight.setLayoutParams(rlRightLayoutParams);
            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(1500);
        //5.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.setTarget(rlRight);
        mAnimator.start();
    }

    /**
     * 放大视频到全屏，隐藏左侧和下方部分
     */
    public void hideMenuAndDetail() {
        if (!menuIsShown) {
            return;
        }
        menuIsShown = false;
        //1.调用ofInt(int...values)方法创建ValueAnimator对象
        ValueAnimator mAnimator = ValueAnimator.ofInt(leftMenuWidth, 0);
        //2.为目标对象的属性变化设置监听器
        mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 3.为目标对象的属性设置计算好的属性值
                int animatorValue = (int) animation.getAnimatedValue();
                //将右侧放大
                ViewGroup.LayoutParams llRightLayoutParams = llRight.getLayoutParams();
                llRightLayoutParams.width = screenWidth - DensityUtil.dip2px(BaseCashierActivity.this, animatorValue);
                llRight.setLayoutParams(llRightLayoutParams);
                //隐藏底部
                ViewGroup.LayoutParams rlRightLayoutParams = rlRight.getLayoutParams();
                rlRightLayoutParams.height = screenHeight - DensityUtil.dip2px(BaseCashierActivity.this, animatorValue / 2);
                rlRight.setLayoutParams(rlRightLayoutParams);
            }
        });
        //4.设置动画的持续时间、是否重复及重复次数等属性
        mAnimator.setDuration(1500);
        //5.为ValueAnimator设置目标对象并开始执行动画
        mAnimator.setTarget(rlRight);
        mAnimator.start();
    }

    /**
     * 弹出VIP扫码
     */
    public void showVipCode() {




        //判断商品列表是否展开，未展开则打开
        if (!menuIsShown) {
            showMenuAndDetail();
        }
        if (GSYVideoManager.instance().getMediaPlayer().isPlaying()) {
            mPlayer.onVideoPause();
            mPlayer.setVisibility(View.GONE);
        } else {

            cancelTimer();
        }
        zXingView.setVisibility(View.VISIBLE);
        llQrCode.setVisibility(View.GONE);
        zXingView.startCamera();
//        zXingView.startCamera(Camera.CameraInfo.CAMERA_FACING_FRONT);//前置摄像头

        zXingView.showScanRect();
        zXingView.startSpot();
    }

    /**
     * 隐藏VIP扫码
     */
    public void hideVipCode() {
        if (isPlaying) {
            mPlayer.setVisibility(View.VISIBLE);
            mPlayer.onVideoResume();
        } else {
            startTimer();
        }
        zXingView.stopCamera();
        zXingView.setVisibility(View.GONE);
        llQrCode.setVisibility(View.GONE);
    }

    /**
     * 显示支付的二维码
     */
    public void showQrCode() {
        //填充数据
        if (leanCloudMessage == null) {
            //获取不到商品数据则停止
            return;
        } else {
            if (leanCloudMessage.getAlert() != null
                    && leanCloudMessage.getAlert().getQrCodeInfo() != null
                    ) {
                if (!TextUtils.isEmpty(leanCloudMessage.getAlert().getQrCodeInfo().getAlipayQrCodeUrl())) {
                    Picasso.with(this)
                            .load(leanCloudMessage.getAlert().getQrCodeInfo().getAlipayQrCodeUrl())
                            .placeholder(R.mipmap.default_qr_code)
                            .into(ivAlipay);
                }
                if (!TextUtils.isEmpty(leanCloudMessage.getAlert().getQrCodeInfo().getWechatQrCodeUrl())) {
                    Picasso.with(this)
                            .load(leanCloudMessage.getAlert().getQrCodeInfo().getWechatQrCodeUrl())
                            .placeholder(R.mipmap.default_qr_code)
                            .into(ivWechat);
                }

            }

        }

        //判断商品列表是否展开，未展开则打开
        if (!menuIsShown) {
            showMenuAndDetail();
        }
        if (GSYVideoManager.instance().getMediaPlayer().isPlaying()) {
            mPlayer.onVideoPause();
            mPlayer.setVisibility(View.GONE);
        } else {
            cancelTimer();
        }
        llQrCode.setVisibility(View.VISIBLE);
        zXingView.setVisibility(View.GONE);
    }

    /**
     * 隐藏支付的二维码
     */
    public void hideQrCode() {
        if (isPlaying) {
            mPlayer.setVisibility(View.VISIBLE);
            mPlayer.onVideoResume();
        } else {
            startTimer();
        }
        llQrCode.setVisibility(View.GONE);
        zXingView.setVisibility(View.GONE);
    }

    /**
     * 开始viewpager轮播
     */
    public void startViewPager() {
        mPlayer.setVisibility(View.GONE);
        startTimer();
    }

    //使用timer轮播vp
    private void startTimer() {
        timer = new Timer();
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        viewPagerIndex++;
                        if (viewPagerIndex % 4 == 0) {
                            cancelTimer();
                            startPlayer();
                        }
                        vp.setCurrentItem(viewPagerIndex);

                    }
                });
            }
        };
        timer.schedule(task, 2000, 2000);
    }

    private void cancelTimer() {
        if (timer != null) {
            timer.cancel();
        }
    }


    /**
     * 重新开始播放视频
     */
    public void startPlayer() {
        mPlayer.setVisibility(View.VISIBLE);
        mPlayer.setUp(urls, false, 0, "");
        mPlayer.startPlayLogic();

    }

    private void initPlayerView() {
        urls = getVideoUrls();
        mPlayer.setUp(urls, false, 0, "");//false表示不缓存，本地视频不用缓存
        //增加封面
        ImageView imageView = new ImageView(this);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setImageResource(getDefaultPlayerImage());
        mPlayer.setThumbImageView(imageView);
        resolveNormalVideoUI();//一些基本设置，允许全屏、快进、音量。。。
        mPlayer.setStandardVideoAllCallBack(new SamplePlayerListener() {
            @Override
            public void onPrepared(String url, Object... objects) {
                super.onPrepared(url, objects);
                //开始播放了才能旋转和全屏
//                isPlay = true;
                isPlaying=true;
                Log.d("MainActivityPlayer", "onPrepared");
            }

            @Override
            public void onAutoComplete(String url, Object... objects) {
                super.onAutoComplete(url, objects);
                Log.d("MainActivityPlayer", "onAutoComplete");
                isPlaying=false;
                startViewPager();
            }

            @Override
            public void onClickStartError(String url, Object... objects) {
                super.onClickStartError(url, objects);
                Log.d("MainActivityPlayer", "onClickStartError");
            }

            @Override
            public void onQuitFullscreen(String url, Object... objects) {
                super.onQuitFullscreen(url, objects);
                Log.d("MainActivityPlayer", "onQuitFullscreen");
            }
        });
        mPlayer.startPlayLogic();
    }

    /**
     * @return 视频默认显示图片
     */
    protected int getDefaultPlayerImage() {
        return R.mipmap.ic_launcher;
    }

    protected abstract List<GSYVideoModel> getVideoUrls();

    private void initViewPager() {
        PagerAdapter pagerAdapter = new CashierPageAdapter(generateViewPagerData());
        vp.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;//禁止滑动
            }
        });
        vp.setAdapter(pagerAdapter);
    }

    /**
     * @return viewpager需要显示的数据
     */
    protected abstract ArrayList<View> generateViewPagerData();

    @Override
    public void onBackPressed() {
//        if (menuIsShown) {//收起商品列表
//            hideMenuAndDetail();
//            return;
//        }
//        super.onBackPressed();
    }


    @Override
    protected void onPause() {
        super.onPause();
//        isPause = true;
    }

    @Override
    protected void onResume() {
        super.onResume();
//        isPause = false;
    }

    @Override
    protected void onDestroy() {
        GSYVideoPlayer.releaseAllVideos();
        GSYPreViewManager.instance().releaseMediaPlayer();
        zXingView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
//        //如果旋转了就全屏
//        if (isPlay && !isPause) {
//            if (newConfig.orientation == ActivityInfo.SCREEN_ORIENTATION_USER) {
//                if (!mPlayer.isIfCurrentIsFullscreen()) {
//                    mPlayer.startWindowFullscreen(this, true, true);
//                }
//            } else {
//                //新版本isIfCurrentIsFullscreen的标志位内部提前设置了，所以不会和手动点击冲突
//                if (mPlayer.isIfCurrentIsFullscreen()) {
//                    StandardGSYVideoPlayer.backFromWindowFull(this);
//                }
//            }
//        }
    }

    /**
     * 一些视频界面的基本设置
     */
    protected void resolveNormalVideoUI() {
        //增加title
        mPlayer.getTitleTextView().setVisibility(View.GONE);
        mPlayer.getTitleTextView().setText("测试视频");
        mPlayer.getBackButton().setVisibility(View.GONE);
        mPlayer.getFullscreenButton().setVisibility(View.GONE);
        mPlayer.setIsTouchWiget(false);//是否可以滑动进度 声音 亮度
        //关闭自动旋转
        mPlayer.setRotateViewAuto(false);
        mPlayer.setLockLand(false);
        mPlayer.setShowFullAnimation(false);
//        mPlayer.bar
    }

    @Override
    public void onScanQRCodeSuccess(String result) {
        vibrate();
        Log.d("MainActivityPlayer", "onScanQRCodeSuccess,result="+result+" End");
//        zXingView.startSpot();
        hideVipCode();
    }

    @Override
    public void onScanQRCodeOpenCameraError() {
        Log.d("MainActivityPlayer", "MainActivityPlayer");

    }
    private void vibrate() {
        Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        vibrator.vibrate(200);
    }
}
