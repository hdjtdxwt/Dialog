package com.epsit.downloadmanager;

import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.alibaba.fastjson.JSON;
import com.arialyy.annotations.Download;
import com.arialyy.aria.core.Aria;
import com.arialyy.aria.core.download.DownloadTask;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    String TAG ="MainActivity";
    ProgressBar progressBar;
    TextView tvProgress,result;
    Button start,stop;
    String path;
    int totalCount;
    int successedCount;
    int failedCount;
    String url ="http://download.epsit.cn:8088/img/userfiles/9bf3a9cf37c044cda8326e696a251803/images/photo/2018/03/77.jpg";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        path = Environment.getExternalStorageDirectory().getAbsolutePath();

        result= (TextView) findViewById(R.id.result);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        tvProgress = (TextView) findViewById(R.id.progress1);
        start = (Button) findViewById(R.id.start);
        stop = (Button) findViewById(R.id.stop);
        start.setOnClickListener(this);
        stop.setOnClickListener(this);
        Aria.download(this).register();

        loadData();
    }
    public void loadData(){
        try {
            InputStream is = getAssets().open("download.json");
            StringBuffer stringBuffer = new StringBuffer();

            byte[]b = new byte[1024];
            int count =0;
            while((count=(is.read(b)))>0){
                stringBuffer.append(new String(b,0,count));
            }
            MapLoadResponse response = JSON.parseObject(stringBuffer.toString(),MapLoadResponse.class);
            List<MapImgData> list = response.getData();
            Log.e(TAG,"加载数据成功-->ok啦");
            startDownload(list);
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*Aria.download(this).load(url)     //读取下载地址
                .setFilePath(path+"/epsit/file/face_image_77.png") //设置文件保存的完整路径
                .start();   //启动下载*/
    }

    private void startDownload(List<MapImgData> list) {
        if(list==null){
            totalCount=0;
            return;
        }
        totalCount = list.size();
        for(int i=0;i<list.size();i++){
            String url = list.get(i).getUrl();
            String realName = getFileNameFromUrl(url);
            if(!TextUtils.isEmpty(realName)){
                Aria.download(this).load(url)     //读取下载地址
                        .setFilePath(path+"/epsit/file/"+realName) //设置文件保存的完整路径
                        .start();   //启动下载*/
            }else{
                Aria.download(this).load(url)     //读取下载地址
                        .setFilePath(path+"/epsit/file/"+ UUID.randomUUID()+".jpg") //设置文件保存的完整路径
                        .start();   //启动下载*/
            }
        }
    }
    public String getFileNameFromUrl(String url){

        if(TextUtils.isEmpty(url)){
            return "";
        }else{
            String realNameUrl = java.net.URLDecoder.decode(url);
            int index = realNameUrl.lastIndexOf("/");
            return url.substring(index,realNameUrl.length());
        }
    }
    public void updateProgress(){
        if(totalCount>0){
            float progress = (successedCount+failedCount)*1.0f / totalCount;
            int intProgress = (int)progress*100;
            progressBar.setProgress(intProgress);
            result.setText("总数:"+totalCount+"  已完成："+successedCount+"  失败："+failedCount);
        }

    }
    //在这里处理任务执行中的状态，如进度进度条的刷新
    @Download.onTaskRunning
    protected void running(DownloadTask task) {
        /*Log.e(TAG,"----thread->"+Thread.currentThread().getName());
        if(task.getKey().equals(url)){
            Log.e(TAG,"同一个key");
            int p = task.getPercent();	//任务进度百分比

            String speed = task.getConvertSpeed();	//转换单位后的下载速度，单位转换需要在配置文件中打开
            long speed1 = task.getSpeed(); //原始byte长度速度
            progressBar.setProgress(task.getPercent());
            tvProgress.setText(task.getPercent()+"%");
            Log.e(TAG,"  "+task.getCurrentProgress()+"   " +task.getConvertCurrentProgress()+"  "+p);
        }*/

    }

    @Download.onTaskComplete
    void taskComplete(DownloadTask task) {
        //在这里处理任务完成的状态
        Log.e(TAG,task.getKey()+" 完成了！ "+task.getPercent());
        successedCount++;
        updateProgress();
    }

    @Download.onTaskStart void taskStart(DownloadTask task) {
        //mAdapter.updateBtState(task.getKey(), false);
    }

    @Download.onTaskResume void taskResume(DownloadTask task) {
        //mAdapter.updateBtState(task.getKey(), false);
    }

    @Download.onTaskStop void taskStop(DownloadTask task) {
        //mAdapter.updateBtState(task.getKey(), true);
        failedCount++;
        updateProgress();
    }

    @Download.onTaskCancel void taskCancel(DownloadTask task) {
        //mAdapter.updateBtState(task.getKey(), true);
        failedCount++;
        updateProgress();
    }

    @Download.onTaskFail void taskFail(DownloadTask task) {
        //mAdapter.updateBtState(task.getKey(), true);
        failedCount++;
        updateProgress();
        Log.e(TAG,task.getKey()+" taskFail ！ "+task.getPercent());
    }
    @Override
    public void onClick(View v) {

    }
}
