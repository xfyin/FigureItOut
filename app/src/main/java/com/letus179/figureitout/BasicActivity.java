package com.letus179.figureitout;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.System.exit;

/**
 * Created by xfyin on 2017/9/24.
 */

public class BasicActivity extends AppCompatActivity {

    private static final String TAG = "BasicActivity";

    private long exitTime = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityController.addActivity(this);
        Log.d(TAG, "onCreate: " + getClass().getSimpleName());
    }

    public void setupBackAsUp(String title, boolean titleShow) {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // 为标题栏设置标题，
            if (titleShow) {
                actionBar.setTitle(title);
            } else {
                actionBar.setTitle("");
            }
            // 加一个返回图标
            actionBar.setDisplayHomeAsUpEnabled(true);
            // 不显示当前程序的图标
            actionBar.setDisplayShowHomeEnabled(false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ActivityController.removeActivity(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && "MainActivity".equals(getClass().getSimpleName())) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                Toast.makeText(getApplicationContext(), "真的要离开我么~", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                ActivityController.finishAll();
                exit(0);
            }
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 退出对话框
     *
     * @param context
     */
    public void exitDialog(final Context context, final Class clz) {
        AlertDialog dialog = new AlertDialog.Builder(context)
                //                .setIcon(R.mipmap.icon)//设置标题的图片
                .setTitle("提示")//设置对话框的标题
                .setMessage("放弃本次挑战？")//设置对话框的内容
                //设置对话框的按钮
                .setNegativeButton("再想一想", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .setPositiveButton("毅然离开", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, clz);
                        startActivity(intent);
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                        finish();
                    }
                }).create();
        dialog.show();
    }


    /**
     * 还剩多少题
     *
     * @param total
     * @param answeredTotal
     * @param left_total
     */
    public void leftTotalMsg(int total, int answeredTotal, TextView left_total) {
        String msg = "还剩：" + String.valueOf(total - answeredTotal) + " 题";
        int last = msg.indexOf("题");
        SpannableStringBuilder spannable = new SpannableStringBuilder(msg);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 3, last, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        left_total.setText(spannable);
    }


    /**
     * 几秒后开始下一题
     *
     * @param millisUntilFinished
     * @param go_to_next
     */
    public void leftSecondsMsg(long millisUntilFinished, TextView go_to_next) {
        String msg = millisUntilFinished / 1000 + "s后进入下一题";
        int last = msg.indexOf("s");
        SpannableStringBuilder spannable = new SpannableStringBuilder(msg);
        spannable.setSpan(new ForegroundColorSpan(Color.RED), 0, last + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        go_to_next.setText(spannable);
    }

    public void cleanAndNext(){
        
    }

    class MyCountDownTimer extends CountDownTimer {

        TextView go_to_next;

        public MyCountDownTimer(long millisInFuture, long countDownInterval, TextView go_to_next) {
            super(millisInFuture, countDownInterval);
            this.go_to_next = go_to_next;
        }

        @Override
        public void onTick(long millisUntilFinished) {
            leftSecondsMsg(millisUntilFinished, go_to_next);
        }

        @Override
        public void onFinish() {
            cleanAndNext();
        }
    }

}