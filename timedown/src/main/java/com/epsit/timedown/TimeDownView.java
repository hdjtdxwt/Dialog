package com.epsit.timedown;

import android.content.Context;
import android.graphics.Canvas;
import android.os.Build.VERSION_CODES;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Yue on 2017/7/18.
 */

public class TimeDownView extends android.support.v7.widget.AppCompatTextView {
    private Timer timer;
    private DownTimerTask downTimerTask;
    private int downCount;
    private int lastDown;
    private long intervalMills;
    private long delayMills;
    private AnimationSet animationSet;

    public TimeDownView(Context context) {
        this(context, null);
    }

    public TimeDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TimeDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        if (timer == null) {
            timer = new Timer();
        }
        if (animationSet == null) {
            animationSet = new AnimationSet(true);
        }
        if (downHandler == null){
            downHandler = new DownHandler();
        }
        setGravity(Gravity.CENTER);
    }

    /**
     * 开始计时
     *
     * @param seconds
     */
    public void downSecond(int seconds) {
        downTime(seconds, 0, 0, 1000);
    }

    /**
     * 倒计时开启方法
     *
     * @param downCount     倒计时总数
     * @param lastDown      显示的倒计时的最后一个数
     * @param delayMills    延迟启动倒计时（毫秒数）
     * @param intervalMills 倒计时间隔时间（毫秒数）
     */
    public void downTime(int downCount, int lastDown, long delayMills, long intervalMills) {
        this.downCount = downCount;
        this.lastDown = lastDown;
        this.delayMills = delayMills;
        this.intervalMills = intervalMills;
        initDefaultAnimate();

        if (downTimerTask == null) {
            downTimerTask = new DownTimerTask();
        }
        timer.schedule(downTimerTask, delayMills, intervalMills);

    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (View.GONE == visibility) {
            timer.cancel();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (drawTextFlag == DRAW_TEXT_NO) {
            return;
        }
        super.onDraw(canvas);
    }

    /**
     * 取消
     */
    public void cancel() {
        timer.cancel();
    }

    private class DownTimerTask extends TimerTask {

        @Override
        public void run() {
            if (downCount >= (lastDown - 1)) {
                Message msg = Message.obtain();
                msg.what = 1;
                downHandler.sendMessage(msg);
            }
        }
    }

    public static interface DownTimeWatcher {
        public void onTime(int num);

        public void onLastTime(int num);

        public void onLastTimeFinish(int num);
    }

    private DownTimeWatcher downTimeWatcher = null;

    /**
     * 监听倒计时的变化
     *
     * @param downTimeWatcher
     */
    public void setOnTimeDownListener(DownTimeWatcher downTimeWatcher) {
        this.downTimeWatcher = downTimeWatcher;
    }
    private DownHandler downHandler;

    private class DownHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 1) {
                if (downTimeWatcher != null) {
                    downTimeWatcher.onTime(downCount);
                }
                if (downCount >= (lastDown - 1)) {
                    drawTextFlag = DRAW_TEXT_YES;//默认绘制

                    //未到结束时
                    if (downCount >= lastDown) {
                        setText(downCount + "");
                        startDefaultAnimate();
                        if (downCount == lastDown && downTimeWatcher != null) {
                            downTimeWatcher.onLastTime(downCount);
                        }
                    }

                    //到结束时
                    else if (downCount == (lastDown - 1)) {// 若lastDown为0，downCount == -1时是倒计时真正结束之时。
                        if (downTimeWatcher != null) {
                            downTimeWatcher.onLastTimeFinish(downCount);
                        }
                        //倒计时结束，虽然setText()方法触发onDraw，但重写使之不进行绘制
                        //设置不绘制标记
                        if (afterDownDimissFlag == AFTER_LAST_TIME_DIMISS) {
                            drawTextFlag = DRAW_TEXT_NO;
                        }
                        invalidate();//刷新一下
                        timer.cancel();
                    }
                    downCount--;
                }
                //
            }
        }
    }
    private final int DRAW_TEXT_YES = 1;
    private final int DRAW_TEXT_NO = 0;
    /**
     * 是否执行onDraw的标识，默认绘制
     */
    private int drawTextFlag = DRAW_TEXT_YES;

    private final int AFTER_LAST_TIME_DIMISS = 1;
    private final int AFTER_LAST_TIME_NODIMISS = 0;
    /**
     * 在倒计时结束之后文字是否消失的标志，默认消失
     */
    private int afterDownDimissFlag = AFTER_LAST_TIME_DIMISS;

    /**
     * 设置倒计时结束后文字不消失
     */
    public void setAfterDownNoDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_NODIMISS;
    }

    /**
     * 设置倒计时结束后文字消失
     */
    public void setAferDownDimiss() {
        afterDownDimissFlag = AFTER_LAST_TIME_DIMISS;
    }

    private boolean startDefaultAnimFlag = true;

    //关闭默认动画
    public void closeDefaultAnimate() {
        animationSet.reset();
        startDefaultAnimFlag = false;
    }
    //开启默认动画
    private void startDefaultAnimate() {
        if (startDefaultAnimFlag) {
            animationSet.startNow();
        }
    }

    private void initDefaultAnimate() {
        if (animationSet == null) {
            animationSet = new AnimationSet(true);
        }

        ScaleAnimation scaleAnimation = new ScaleAnimation(1f, 0.5f, 1f, 0.5f, Animation.RESTART, 0.5f, Animation.RESTART, 0.5f);
        scaleAnimation.setDuration(intervalMills);

        AlphaAnimation alphaAnimation = new AlphaAnimation(1f, 0.3f);
        alphaAnimation.setDuration(intervalMills);
        //将AlphaAnimation这个已经设置好的动画添加到 AnimationSet中
        animationSet.addAnimation(scaleAnimation);
        animationSet.addAnimation(alphaAnimation);
        animationSet.setInterpolator(new AccelerateInterpolator());
        setAnimation(animationSet);
    }

}