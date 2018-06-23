package com.epsit.timercountdownview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class TestActivity extends AppCompatActivity {
    ProgressView mATProgressView;

    private int mTotalProgress = 40;
    private int mCurrentProgress = 0;
    //进度条
    private CompletedView mTasksView;

    CustomView customView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        mATProgressView = (ProgressView) findViewById(R.id.progressView);
        mATProgressView.setCountdownTime(10);
        mATProgressView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mATProgressView.startCountdown(new ProgressView.OnCountDownFinishListener() {
                    @Override
                    public void countDownFinished() {
                        Toast.makeText(getApplicationContext(), "倒计时结束了--->该UI处理界面逻辑了", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });


        mTasksView = (CompletedView) findViewById(R.id.tasks_view);
        new Thread(new ProgressRunable()).start();

        customView = (CustomView) findViewById(R.id.CustomView);
        customView.setCountdownTime(30);
        customView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customView.startCountdown(new CustomView.OnCountDownFinishListener() {
                    @Override
                    public void countDownFinished() {
                        Toast.makeText(getApplicationContext(), "倒计时结束了--->该UI处理界面逻辑了", Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    class ProgressRunable implements Runnable {
        @Override
        public void run() {
            while (mCurrentProgress < mTotalProgress) {
                mCurrentProgress += 1;
                mTasksView.setProgress(mCurrentProgress);
                try {
                    Thread.sleep(90);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
