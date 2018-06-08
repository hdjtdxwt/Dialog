package com.epsit.hiddenvirtualkey;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by Administrator on 2018/6/7/007.
 */

public class LoadingDialog extends Dialog {
    String TAG = "LoadingDialog";

    public LoadingDialog(Context context) {
        super(context);
    }
    static Builder builder;

    public static class Builder {
        TextView titleTv,messageTv;
        ImageView imageView;
        ProgressBar progressBar;
        Button submitBtn,cancelBtn;
        Params params;
        LoadingDialog dialog;

        public Builder(Context context){
            builder = this;
            params = new Params();
            params.context = context;
        }
        public Builder setImageId(int imageId){
            params.imageId = imageId;
            return this;
        }
        public Builder setTitle(String title){
            params.title = title;
            return this;
        }
        public Builder setMessage(String message){
            params.message = message;
            return this;
        }
        public Builder setStyle( int style){
            params.style = style;
            return this;
        }
        public Builder setPositiveButton(String text,Builder.OnClickListener listener){
            params.positiveButtonText = text;
            params.positiveButtonListener = listener;
            return this;
        }
        public Builder setNegativeButton(String text,Builder.OnClickListener listener){
            params.negativeButtonText = text;
            params.negativeButtonListener = listener;
            return this;
        }
        public Builder showProgress(){
            if(dialog!=null){
                progressBar.setVisibility(View.VISIBLE);
                imageView.setVisibility(View.GONE);
            }
            return this;
        }
        public Builder showImage(){
            if(dialog!=null){
                progressBar.setVisibility(View.GONE);
                imageView.setVisibility(View.VISIBLE);
            }
            return this;
        }
        public LoadingDialog create(){
            dialog = new LoadingDialog(params.context, params.style);
            dialog.setCanceledOnTouchOutside(false);
            dialog.setCancelable(false);

            LayoutInflater inflater = LayoutInflater.from(params.context);
            View layout = inflater.inflate(R.layout.loading, null);
            titleTv = (TextView) layout.findViewById(R.id.title);
            messageTv = (TextView) layout.findViewById(R.id.tv_load_dialog);
            submitBtn = (Button) layout.findViewById(R.id.submit);
            cancelBtn = (Button) layout.findViewById(R.id.cancel);
            imageView = (ImageView) layout.findViewById(R.id.error_img);
            progressBar = (ProgressBar) layout.findViewById(R.id.pb_load);
            dialog.setContentView(layout);

            if(!TextUtils.isEmpty(params.title)){
                titleTv.setText(params.title);
            }
            if(!TextUtils.isEmpty(params.message)){
                messageTv.setText(params.message);
            }
            if(!TextUtils.isEmpty(params.positiveButtonText)) {
                submitBtn.setVisibility(View.VISIBLE);
                submitBtn.setText(params.positiveButtonText);
            }
            submitBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(params.positiveButtonListener!=null) {
                        params.positiveButtonListener.onClick(v, dialog);
                    }else {
                        dialog.dismiss();
                    }
                }
            });
            if(!TextUtils.isEmpty(params.negativeButtonText)) {
                cancelBtn.setVisibility(View.VISIBLE);
                cancelBtn.setText(params.negativeButtonText);
            }
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (params.negativeButtonListener != null) {
                        params.negativeButtonListener.onClick(v, dialog);
                    }else{
                        dialog.dismiss();
                    }
                }
            });
            return dialog;
        }

        public static interface OnClickListener{
            void onClick(View view,Dialog dialog);
        }
    }
    public static class Params{
        private Context context;
        private String title;
        private String message;
        private int style;
        private int imageId;
        private String positiveButtonText;
        private String negativeButtonText;
        private Builder.OnClickListener positiveButtonListener;
        private Builder.OnClickListener negativeButtonListener;
    }
    public LoadingDialog(Context context, int theme) {
        super(context, theme);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    public void setMessage(String message){
        builder.setMessage(message);
    }
}
