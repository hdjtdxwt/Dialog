package com.epsit.timercountdownview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by Administrator on 2018/6/22/022.
 */

public class CustomView extends View {
    float mRadius;
    int mTotalProgress;//总的进度
    int countdownTime;//当前进度
    int mCircleColor; //里层圆圈的颜色
    int mRingColor;//稍微比圆圈大一点的圈圈进度条的颜色
    Paint mCirclePaint;//画里层圆圈的画笔
    Paint mTextPaint;
    int mTextColor;
    // 字的长度
    private float mTxtWidth;
    // 字的高度
    private float mTxtHeight;

    private float drgeePercent;

    String progressDesc;

    float textSize;
    int mXCenter,mYCenter;
    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttrs(context, attrs);
        initPaint();
    }
    //属性
    private void initAttrs(Context context, AttributeSet attrs) {
        TypedArray typeArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomView, 0, 0);
        mRadius = typeArray.getDimension(R.styleable.CustomView_cv_radius, 80);
        textSize = typeArray.getDimension(R.styleable.CustomView_cv_text_size, 16);
        mTotalProgress = typeArray.getInt(R.styleable.CustomView_cv_progress, 60);
        mTextColor = typeArray.getColor(R.styleable.CustomView_cv_textColor, 0xFFFFFFFF);
        mCircleColor = typeArray.getColor(R.styleable.CustomView_cv_circleColor, 0xFFFFFFFF);
        mRingColor = typeArray.getColor(R.styleable.CustomView_cv_ringColor, 0xFFFFFFFF);
    }
    public void initPaint(){
        //初始化画笔,画圆圈的
        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(mCircleColor);
        mCirclePaint.setStyle(Paint.Style.FILL);

        //中间字
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mRadius / 2);

        //字体
        String txt = countdownTime + "分";
        mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());

    }
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mXCenter = getWidth() / 2;
        mYCenter = getHeight() / 2;

        canvas.drawCircle(mXCenter, mYCenter, mRadius, mCirclePaint);

        //字体
        String txt = countdownTime + "";
        mTxtWidth = mTextPaint.measureText(txt, 0, txt.length());
        canvas.drawText(txt, mXCenter - mTxtWidth / 2, mYCenter + mTxtHeight / 4, mTextPaint);

    }
    //设置进度
    public void setProgress(int progress) {
        countdownTime = progress;
        postInvalidate();//重绘
    }
    /**
     * 开始倒计时任务
     */
    public void startCountdown(final OnCountDownFinishListener countDownFinishListener) {
        setClickable(false);
        final ValueAnimator valA = getValA(mTotalProgress * 1000);
        valA.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                drgeePercent = Float.valueOf(valA.getAnimatedValue().toString());
                invalidate();
            }
        });
        valA.start();
        valA.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                if (null != countDownFinishListener) {
                    countDownFinishListener.countDownFinished();
                }
                super.onAnimationEnd(animation);
                if (countdownTime > 0) {
                    setClickable(true);
                } else {

                    setClickable(false);
                }
            }
        });
        startCountDownTaskByRxAndroid();
    }
    private ValueAnimator getValA(long countdownTime) {
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 1.F);
        valueAnimator.setDuration(countdownTime);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setRepeatCount(0);
        return valueAnimator;
    }
    public void setCountdownTime(int countdownTime) {
        this.countdownTime = countdownTime;
        progressDesc = countdownTime + "";
    }

    private void startCountDownTaskByRxAndroid() {
        Observable.interval(0, 1, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {
                        countdownTime = 0;
                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(Long aLong) {
                        if (countdownTime < -1) {
                            this.unsubscribe();
                        }
                        --countdownTime;
                        if (countdownTime < 0) {
                            mTextPaint.setTextSize(textSize / 2);
                            progressDesc = "时间到";
                            onCompleted();
                            return;
                        } else {
                            mTextPaint.setTextSize(textSize);
                            progressDesc = countdownTime + "";
                        }
                        invalidate();
                    }
                });
    }


    public interface OnCountDownFinishListener {
        void countDownFinished();
    }
}
