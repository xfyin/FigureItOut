package com.letus179.figureitout;

import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.letus179.figureitout.common.Constants;
import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.List;

public class ResultAnalyseActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView operate_type, total_num, right_score, used_time, right_rate, per_used_time, analyse_result, check_answer_list;

    private Button do_it_again, re_choose_model;

    private String rightRate, perUsedTime;

    private int model, range;

    private List<Answer> answerList;

    private double usedTime;

    private ImageView result_img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result_analyse);
        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        ActionBar ac = getSupportActionBar();
        if (ac != null) {
            ac.setDisplayHomeAsUpEnabled(false);
            ac.setTitle("结果分析");
        }

        Intent intent = getIntent();
        usedTime = intent.getDoubleExtra("used_time", 0.0);
        model = intent.getIntExtra("model", 1);
        range = intent.getIntExtra("range", 10);
        answerList = (List<Answer>) intent.getSerializableExtra("answer_data");
        int total = 0;
        String type = "+";
        int rightScore = 0;
        if (answerList != null && answerList.size() > 0) {
            total = answerList.size();
            type = answerList.get(0).getOperate();
            for (Answer answer : answerList) {
                if (answer.isRightOrWrong()) {
                    rightScore++;
                }
            }
        }

        total_num = (TextView) findViewById(R.id.total_num);
        right_score = (TextView) findViewById(R.id.right_score);
        used_time = (TextView) findViewById(R.id.used_time);
        operate_type = (TextView) findViewById(R.id.operate_type);
        right_rate = (TextView) findViewById(R.id.right_rate);
        per_used_time = (TextView) findViewById(R.id.per_used_time);
        analyse_result = (TextView) findViewById(R.id.analyse_result);
        check_answer_list = (TextView) findViewById(R.id.check_answer_list);
        result_img = (ImageView) findViewById(R.id.result_img);
        if (!TextUtils.isEmpty(type)) {
            operate_type.setText(transfer(type));
        } else {
            operate_type.setText(Constants.FOUR_MIXED_OPE_EXP);
        }
        total_num.setText(String.valueOf(total) + "题");
        right_score.setText(String.valueOf(rightScore) + "分");
        used_time.setText(String.valueOf(usedTime) + "秒");
        check_answer_list.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
        check_answer_list.getPaint().setAntiAlias(true);//抗锯齿

        DecimalFormat df = new DecimalFormat("#.00");
        df.setRoundingMode(RoundingMode.HALF_UP);
        rightRate = df.format((double) (rightScore * 100 / total)) + "%";
        perUsedTime = df.format(usedTime / total);
        right_rate.setText(rightRate);
        per_used_time.setText(perUsedTime + "秒");
        analyse_result.setText(analyseResult(rightRate));

        do_it_again = (Button) findViewById(R.id.do_it_again);
        re_choose_model = (Button) findViewById(R.id.re_choose_model);
        do_it_again.setOnClickListener(this);
        re_choose_model.setOnClickListener(this);
        check_answer_list.setOnClickListener(this);
    }

    private String transfer(String type) {
        String operateType = "";
        switch (type) {
            case "+":
                operateType = "加法运算";
                break;
            case "-":
                operateType = "减法运算";
                break;
            case "*":
                operateType = "乘法运算";
                break;
            case "÷":
                operateType = "除法运算";
                break;
            default:
                break;
        }
        return operateType;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.do_it_again:
                if (model == 1) {
                    intent = new Intent(this, ModelOneActivity.class);
                } else if (model == 2) {
                    intent = new Intent(this, ModelTwoActivity.class);
                } else if (model == 3) {
                    intent = new Intent(this, ModelThreeActivity.class);
                }
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
            case R.id.re_choose_model:
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
                break;
            case R.id.check_answer_list:
                intent = new Intent(this, CheckAnswerListOneActivity.class);
                intent.putExtra("used_time", usedTime);
                intent.putExtra("model", model);
                intent.putExtra("range", range);
                intent.putExtra("answer_data", (Serializable) answerList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
                break;
            default:
                break;
        }

    }

    private String analyseResult(String rightRate) {
        String rightResult;
        String right = rightRate.substring(0, rightRate.indexOf("."));
        int rightScore = 0;
        if (!TextUtils.isEmpty(right)) {
            rightScore = Integer.valueOf(right);
        }

        if (rightScore < 30) {
            rightResult = "不勤训练，回家养猪";
            result_img.setImageResource(R.drawable.score_0_30);
        } else if (rightScore >= 30 && rightScore < 60) {
            rightResult = "努力努力，天道酬勤。";
            result_img.setImageResource(R.drawable.score_31_59);
        } else if (rightScore >= 60 && rightScore < 85) {
            rightResult = "不错不错，前途无量。";
            result_img.setImageResource(R.drawable.score_60_84);
        } else {
            rightResult = "算法大师，非你莫属。";
            result_img.setImageResource(R.drawable.score_85_100);
        }
        return rightResult;
    }

}