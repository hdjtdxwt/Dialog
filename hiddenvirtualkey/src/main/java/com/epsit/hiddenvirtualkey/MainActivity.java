package com.epsit.hiddenvirtualkey;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    FrameLayout content;
    private static final String TAG = "MainActivity";
    private boolean mLayoutComplete = false;
    LoadingDialog loadingDialog;
    SelfDialog selfDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>Build.VERSION_CODES.LOLLIPOP){
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
        getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

        setContentView(R.layout.activity_main);
        findViewById(R.id.btn).setOnClickListener(this);
        findViewById(R.id.btn2).setOnClickListener(this);
        findViewById(R.id.btn3).setOnClickListener(this);
        findViewById(R.id.btn4).setOnClickListener(this);
        findViewById(R.id.btn5).setOnClickListener(this);
        findViewById(R.id.btn6).setOnClickListener(this);
        findViewById(R.id.btn7).setOnClickListener(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "super.onPause();");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "super.onResume();");
    }
    int progress;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case -1:
                    Log.e(TAG,"----progress="+progress);
                    progress+=5;
                    if(loadingDialog!=null){
                        loadingDialog.setMessage("当前进度："+progress);
                    }
                    if(selfDialog!=null){
                        selfDialog.setMessage("当前进度："+progress);
                        if(progress>90){
                            selfDialog.showErrorImage();
                        }
                    }
                    if(progress<100){
                        handler.sendEmptyMessageDelayed(-1,1000);
                    }
                    break;
                case 1:
                    break;
                case 2:
                    break;
                case 100:
                    selfDialog.setMessage("当前进度："+progress);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn:
                CustomDialog dialog=  new CustomDialog(this, R.style.CustomDialog);
                dialog.show();
                break;
            case R.id.btn2:
                exit(MainActivity.this);
                break;
            case R.id.btn3:
                showDialog3();
                break;
            case R.id.btn4:
                initDialog();
                showDialog();
                break;
            case R.id.btn5:
                Log.e(TAG,"btn5");
                startActivity(new Intent(this,NewActivity.class));
                break;
            case R.id.btn6:
                Log.e(TAG,"-----button点击了");
                LoadingDialog.Builder builder =  new LoadingDialog.Builder(this).setTitle("通知").setMessage("内容").setStyle(R.style.progress_dialog);
                builder.setPositiveButton("确定",new LoadingDialog.Builder.OnClickListener(){
                    @Override
                    public void onClick(View v,Dialog dialog) {
                        dialog.dismiss();
                    }
                }).showProgress();
                loadingDialog = builder.create();
                loadingDialog.show();
                handler.sendEmptyMessage(-1);
                break;
            case R.id.btn7:
                selfDialog = new SelfDialog(this);
                selfDialog.setMessage("测试");
                selfDialog.setTitle("标题");
                selfDialog.setYesOnclickListener("确定", new SelfDialog.onYesOnclickListener() {
                    @Override
                    public void onYesClick() {
                        selfDialog.dismiss();
                    }
                });
                selfDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
                selfDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                    @Override
                    public void onSystemUiVisibilityChange(int visibility) {
                        int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                                //布局位于状态栏下方
                                View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                                //全屏
                                View.SYSTEM_UI_FLAG_FULLSCREEN |
                                //隐藏导航栏
                                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                                View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                        if (Build.VERSION.SDK_INT >= 19) {
                            uiOptions |= 0x00001000;
                        } else {
                            uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                        }
                        selfDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                    }
                });
                selfDialog.setNoShowNavi(true);
                selfDialog.show();
                handler.sendEmptyMessage(-1);
                break;
        }
    }
    AlertDialog alertDialog;
    /*
    初始化AlertDialog
     */
    public void initDialog()
    {
        //创建AlertDialog的构造器的对象
        AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
        //设置构造器标题
        builder.setTitle("提示");
        //构造器对应的图标
        builder.setIcon(R.mipmap.ic_launcher);
        //构造器内容,为对话框设置文本项(之后还有列表项的例子)
        builder.setMessage("你是否要狠心离我而去？");
        //为构造器设置确定按钮,第一个参数为按钮显示的文本信息，第二个参数为点击后的监听事件，用匿名内部类实现
        builder.setPositiveButton("是呀", new DialogInterface.OnClickListener()
        {
            @Override
            public void onClick(DialogInterface dialog, int which)
            {
                //第一个参数dialog是点击的确定按钮所属的Dialog对象,第二个参数which是按钮的标示值
                finish();//结束当前Activity
            }
        });
        //为构造器设置取消按钮,若点击按钮后不需要做任何操作则直接为第二个参数赋值null
        builder.setNegativeButton("不呀",null);
        //为构造器设置一个比较中性的按钮，比如忽略、稍后提醒等
        builder.setNeutralButton("稍后提醒",null);
        //利用构造器创建AlertDialog的对象,实现实例化
        alertDialog=builder.create();
    }

    /*
    实现Button监听器的除了内部类外的方法
    点击Button时弹出AlertDialog
     */
    public void showDialog( )
    {
        //当AlertDialog存在实例对象并且没有在展示时
        if(alertDialog!=null&&!alertDialog.isShowing()) {
            alertDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
            alertDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
                @Override
                public void onSystemUiVisibilityChange(int visibility) {
                    int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                            //布局位于状态栏下方
                            View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                            //全屏
                            View.SYSTEM_UI_FLAG_FULLSCREEN |
                            //隐藏导航栏
                            View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                            View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                    if (Build.VERSION.SDK_INT >= 19) {
                        uiOptions |= 0x00001000;
                    } else {
                        uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                    }
                    alertDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
                }
            });
            alertDialog.show();
        }
    }

    public void exit(final Context context) {
        final ConfirmDialog confirmDialog = new ConfirmDialog(context, "确定要退出吗?", "退出", "取消");
        confirmDialog.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
        confirmDialog.getWindow().getDecorView().setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener() {
            @Override
            public void onSystemUiVisibilityChange(int visibility) {
                int uiOptions = View.SYSTEM_UI_FLAG_LAYOUT_STABLE |
                        //布局位于状态栏下方
                        View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION |
                        //全屏
                        View.SYSTEM_UI_FLAG_FULLSCREEN |
                        //隐藏导航栏
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN;
                if (Build.VERSION.SDK_INT >= 19) {
                    uiOptions |= 0x00001000;
                } else {
                    uiOptions |= View.SYSTEM_UI_FLAG_LOW_PROFILE;
                }
                confirmDialog.getWindow().getDecorView().setSystemUiVisibility(uiOptions);
            }
        });
        confirmDialog.show();
        confirmDialog.setClicklistener(new ConfirmDialog.ClickListenerInterface() {
            @Override
            public void doConfirm() {
                // TODO Auto-generated method stub
                confirmDialog.dismiss();
                MainActivity.this.finish();
            }
            @Override
            public void doCancel() {
                confirmDialog.dismiss();
            }
        });
    }
    public void showDialog3(){
        new CommomDialog(MainActivity.this, R.style.common_dialog, "您确定删除此信息？", new CommomDialog.OnCloseListener() {
            @Override
            public void onClick(Dialog dialog, boolean confirm) {
                Toast.makeText(getApplicationContext(),"点击确定", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        }).setTitle("提示").show();
    }
}