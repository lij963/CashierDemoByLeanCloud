package com.etam.cashier.activity;

import android.os.Bundle;
import android.os.Environment;
import android.text.format.Formatter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.etam.cashier.BaseActivity;
import com.etam.cashier.Constant;
import com.etam.cashier.R;
import com.etam.cashier.model.VideoModel;
import com.etam.cashier.view.NumberProgressBar;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.DownloadService;
import com.lzy.okserver.listener.DownloadListener;
import com.lzy.okserver.task.ExecutorWithListener;

import java.util.List;

import butterknife.BindView;


public class DownloadManagerActivity extends BaseActivity implements View.OnClickListener, ExecutorWithListener.OnAllTaskEndListener {

    private List<DownloadInfo> allTask;
    private MyAdapter adapter;
    private DownloadManager downloadManager;

    @BindView(R.id.listView)
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download_manager);

        downloadManager = DownloadService.getDownloadManager();
        downloadManager.setTargetFolder(Environment.getExternalStorageDirectory().getAbsolutePath() + "/EtamVideo/");
        allTask = downloadManager.getAllTask();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);

        downloadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(this);
        if (getIntent().getBooleanExtra(Constant.needUpdate, false)) {
            startUpdateVideo();
        }
    }

    @Override
    public void onAllTaskEnd() {
        for (DownloadInfo downloadInfo : allTask) {
            if (downloadInfo.getState() != DownloadManager.FINISH) {
                Toast.makeText(DownloadManagerActivity.this, "所有下载线程结束，部分下载未完成", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //记得移除，否者会回调多次
        downloadManager.removeAllTask();
        downloadManager.getThreadPool().getExecutor().removeOnAllTaskEndListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }

    public void onClick(View v) {
        startUpdateVideo();//开始下载
    }

    private void startUpdateVideo() {
        VideoModel videoModel = new VideoModel("Video1", "http://baobab.wdjcdn.com/14564977406580.mp4", "1");
        GetRequest request = OkGo.get(videoModel.getUrl());
        VideoModel videoModel2 = new VideoModel("Video2", "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4", "1");
        GetRequest request2 = OkGo.get(videoModel2.getUrl());
        downloadManager.addTask(videoModel.getUrl(), videoModel, request, null);
        downloadManager.addTask(videoModel2.getUrl(), videoModel2, request2, null);
        adapter.notifyDataSetChanged();
    }

    private class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return allTask.size();
        }

        @Override
        public DownloadInfo getItem(int position) {
            return allTask.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            DownloadInfo downloadInfo = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(DownloadManagerActivity.this, R.layout.item_download_manager, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.refresh(downloadInfo);

            //对于非进度更新的ui放在这里，对于实时更新的进度ui，放在holder中
            VideoModel videoModel = (VideoModel) downloadInfo.getData();
            if (videoModel != null) {
                holder.name.setText(videoModel.getName());
            } else {
                holder.name.setText(downloadInfo.getFileName());
            }
            holder.restart.setOnClickListener(holder);

            DownloadListener downloadListener = new MyDownloadListener();
            downloadListener.setUserTag(holder);
            downloadInfo.setListener(downloadListener);
            return convertView;
        }
    }

    private class ViewHolder implements View.OnClickListener {
        private DownloadInfo downloadInfo;
        private TextView name;
        private TextView downloadSize;
        private TextView tvProgress;
        private TextView netSpeed;
        private NumberProgressBar pbProgress;
        private Button restart;

        public ViewHolder(View convertView) {
            name = (TextView) convertView.findViewById(R.id.name);
            downloadSize = (TextView) convertView.findViewById(R.id.downloadSize);
            tvProgress = (TextView) convertView.findViewById(R.id.tvProgress);
            netSpeed = (TextView) convertView.findViewById(R.id.netSpeed);
            pbProgress = (NumberProgressBar) convertView.findViewById(R.id.pbProgress);
            restart = (Button) convertView.findViewById(R.id.restart);
        }

        public void refresh(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            refresh();
        }

        //对于实时更新的进度ui，放在这里，例如进度的显示，而图片加载等，不要放在这，会不停的重复回调
        //也会导致内存泄漏
        private void refresh() {
            String downloadLength = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getDownloadLength());
            String totalLength = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getTotalLength());
            downloadSize.setText(downloadLength + "/" + totalLength);
            if (downloadInfo.getState() == DownloadManager.NONE) {
                netSpeed.setText("停止");
            } else if (downloadInfo.getState() == DownloadManager.PAUSE) {
                netSpeed.setText("暂停中");
            } else if (downloadInfo.getState() == DownloadManager.ERROR) {
                netSpeed.setText("下载出错");
            } else if (downloadInfo.getState() == DownloadManager.WAITING) {
                netSpeed.setText("等待中");
            } else if (downloadInfo.getState() == DownloadManager.FINISH) {
                netSpeed.setText("下载完成");
                restart.setVisibility(View.INVISIBLE);
            } else if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                String networkSpeed = Formatter.formatFileSize(DownloadManagerActivity.this, downloadInfo.getNetworkSpeed());
                netSpeed.setText(networkSpeed + "/s");
            }
            tvProgress.setText((Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
            pbProgress.setMax((int) downloadInfo.getTotalLength());
            pbProgress.setProgress((int) downloadInfo.getDownloadLength());
            pbProgress.setProgressTextVisibility(NumberProgressBar.ProgressTextVisibility.Invisible);
        }

        @Override
        public void onClick(View v) {
            downloadManager.restartTask(downloadInfo.getUrl());
        }
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(DownloadInfo downloadInfo) {
            if (getUserTag() == null) return;
            ViewHolder holder = (ViewHolder) getUserTag();
            holder.refresh();  //这里不能使用传递进来的 DownloadInfo，否者会出现条目错乱的问题
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
//            Toast.makeText(DownloadManagerActivity.this, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null)
                Toast.makeText(DownloadManagerActivity.this, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
