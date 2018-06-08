package com.epsit.hiddenvirtualkey;

/**
 * Created by Administrator on 2018/6/7/007.
 */

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.WindowManager;

/**
 * 加载提醒对话框
 */
public class CustomDialog extends ProgressDialog
{
    public CustomDialog(Context context)
    {
        super(context);
    }

    public CustomDialog(Context context, int theme)
    {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        init(getContext());
    }

    private void init(Context context)
    {
        //设置不可取消，点击其他区域不能取消，实际中可以抽出去封装供外包设置
        setCancelable(true);
        setCanceledOnTouchOutside(false);

        setContentView(R.layout.load_dialog);
        WindowManager.LayoutParams params = getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.WRAP_CONTENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        getWindow().setAttributes(params);

        /*android.view.WindowManager.LayoutParams p = dialog.getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = 720;
        p.width = 1080; //这个是dialog的宽高，设置合适的值看得舒服就好
        dialog.getWindow().setAttributes(p);     //设置生效*/
    }

    @Override
    public void show()
    {
        super.show();
    }
}