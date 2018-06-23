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

import java.io.File;
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

        /*url = "http://download.epsit.cn:8088/img/userfiles/1/mapimg/knowledge/knowledgeMapLine/2018/06/F1%E9%97%A8%E8%AF%8A%E4%BE%BF%E6%B0%91%E6%9C%8D%E5%8A%A1%E4%B8%AD%E5%BF%83.png";
        totalCount=1;
        String realNameUrl = java.net.URLDecoder.decode(url);
        String realName = getFileNameFromUrl(realNameUrl);
        Log.e(TAG,"名字："+realName);
        Aria.download(this).load(url)     //读取下载地址
                .setFilePath(path+"/epsit/file/"+realName) //设置文件保存的完整路径
                .start();   //启动下载*/

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
            final List<MapImgData> list = response.getData();
            totalCount = list!=null ? list.size():0;
            Log.e(TAG,"加载数据成功-->ok啦");
            if(list!=null){
                new Thread(){
                    @Override
                    public void run() {
                        startDownload(list);
                    }
                }.start();
            }


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startDownload(List<MapImgData> list) {
        Log.e(TAG,"totalCount="+totalCount);
        for(int i=0;i<list.size();i++){
            String url = list.get(i).getUrl();
            String realName = getFileNameFromUrl(url);
            if(!TextUtils.isEmpty(realName)){
                String savePath = path+"/epsit/file/"+realName;
                File saveFile = new File(savePath);
                if(saveFile.exists()){
                    saveFile.delete();
                }
                Log.e(TAG,"开始下载---1");
                Aria.download(this).load(url)     //读取下载地址
                        .setFilePath(saveFile.getAbsolutePath()) //设置文件保存的完整路径
                        .start();   //启动下载*/
            }else{
                Log.e(TAG,"开始下载---2");
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
            Log.e(TAG,"realNameUrl="+realNameUrl);
            int index = realNameUrl.lastIndexOf("/");
            return realNameUrl.substring(index+1,realNameUrl.length());
        }
    }
    public void updateProgress(){
        Log.e(TAG,"updateProgress-->"+totalCount);
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
        Log.e(TAG,task.getKey()+" 完成了！ "+task.getPercent()+"  "+Thread.currentThread().getName());
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
