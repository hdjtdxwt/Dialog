package com.epsit.timedown;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TimeDownView tdvDownTime = (TimeDownView) findViewById(R.id.tv);
        tdvDownTime.downSecond(30);//从10开始倒计时，一秒倒一次
        tdvDownTime.setOnTimeDownListener(new TimeDownView.DownTimeWatcher() {
                                              public void onTime(int num) {
                                              }

                                              public void onLastTime(int num) {
                                              }

                                              public void onLastTimeFinish(int num) {
                                              }
                                          }

        );
    }
}
