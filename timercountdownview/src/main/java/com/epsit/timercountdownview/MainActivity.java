package com.epsit.timercountdownview;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Context mContext;

    TimerCountdownView view ;
    TextView mTimer ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.go).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), TestActivity.class));
            }
        });
        mContext =this;
        view = (TimerCountdownView) findViewById(R.id.view2);

        mTimer = (TextView) findViewById(R.id.timer);

        view.setMaxTime(1);
        view.updateView();
        view.addCountdownTimerListener(litener);
    }
    TimerCountdownView.CountdownTimerListener litener = new TimerCountdownView.CountdownTimerListener() {

        @Override
        public void onCountDown(String time) {
            mTimer.setText(time);
        }

        @Override
        public void onTimeArrive(boolean isArrive) {
            if(isArrive){
                Toast.makeText(mContext, "时间到", Toast.LENGTH_SHORT).show();
            }
        }
    };
}
