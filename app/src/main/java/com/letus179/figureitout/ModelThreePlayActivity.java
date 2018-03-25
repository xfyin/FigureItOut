package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.letus179.figureitout.common.FourMixedOpeExp;
import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ModelThreePlayActivity extends BasicActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView expression, input_result, right_or_wrong, exit, left_total, go_to_next, right_result;
    private int type, range, total, rightScore, wrongScore;
    private String rightResult;
    private List<String> expressionBank;
    private List<Answer> answerList;
    private Button submit;
    private long startTime;
    private double wrongRate;
    private boolean wrongRateTipShowed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_three_play);
        init();
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Intent intent = getIntent();
        type = intent.getIntExtra("type", 2);
        range = intent.getIntExtra("range", 10);
        total = intent.getIntExtra("total", 10);
        if (type == -1) {
            expressionBank = new ArrayList<>();
            List<String> bank1 = expressionBank = FourMixedOpeExp.genExpBank(total / 3, 2, range, 0, false);
            List<String> bank2 = expressionBank = FourMixedOpeExp.genExpBank(total / 3, 3, range, 0, false);
            List<String> bank3 = expressionBank = FourMixedOpeExp.genExpBank(total / 3, 4, range, 0, false);
            expressionBank.addAll(bank1);
            expressionBank.addAll(bank2);
            expressionBank.addAll(bank3);
        } else {
            expressionBank = FourMixedOpeExp.genExpBank(total, type, range, 0, false);
        }
        String exp = FourMixedOpeExp.getRanExpression(expressionBank);
        String[] expAndResult = exp.split("=");
        expression.setText(expAndResult[0]);
        if (range == 10) {
            expression.setTextSize(18);
            input_result.setTextSize(18);
        } else if (range == 100) {
            expression.setTextSize(16);
            input_result.setTextSize(16);
        } else if (range == 1000) {
            expression.setTextSize(14);
            input_result.setTextSize(14);
        } else if (range == 10000) {
            expression.setTextSize(12);
            input_result.setTextSize(12);
        }
        rightResult = expAndResult[1].replace(" ", "");
        leftTotalMsg(total, answerList.size(), left_total);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                exitDialog(this, ModelThreeActivity.class);
                break;
            case R.id.submit:
                if (TextUtils.isEmpty(input_result.getText())) {
                    ToastUtil.showShort(ModelThreePlayActivity.this, "请输入运算结果...");
                    return;
                }

                Answer answer = new Answer();
                answer.setNumber(answerList.size() + 1);
                answer.setExpression(expression.getText().toString());
                answer.setInputResult(input_result.getText().toString());
                answer.setRightResult(rightResult);

                right_or_wrong.setVisibility(View.VISIBLE);
                go_to_next.setVisibility(View.VISIBLE);
                if (input_result.getText().toString().equals(rightResult.replace(" ", ""))) {
                    rightScore++;
                    answer.setRightOrWrong(true);
                    right_or_wrong.setBackgroundResource(R.drawable.right);
                    new MyCountDownTimer(3 * 1000, 1000, go_to_next).start();
                } else {
                    wrongScore++;
                    answer.setRightOrWrong(false);
                    right_or_wrong.setBackgroundResource(R.drawable.wrong);
                    right_result.setVisibility(View.VISIBLE);
                    right_result.setText("正确答案：" + rightResult);
                    new MyCountDownTimer(5 * 1000, 1000, go_to_next).start();
                }
                answerList.add(answer);
                submit.setEnabled(false);

                if (total == answerList.size()) {
                    long endTime = System.currentTimeMillis();
                    Intent intent = new Intent(ModelThreePlayActivity.this, ResultAnalyseActivity.class);
                    intent.putExtra("answer_data", (Serializable) answerList);
                    intent.putExtra("model", 3);
                    intent.putExtra("range", range);
                    intent.putExtra("used_time", (endTime - startTime - 3 * 1000 * rightScore - 5 * 1000 * wrongScore) * 1.000 / 1000);
                    startActivity(intent);
                    finish();
                }

                break;

        }
    }

    private void init() {
        answerList = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        exit = (TextView) findViewById(R.id.exit);
        expression = (TextView) findViewById(R.id.expression);
        input_result = (TextView) findViewById(R.id.input_result);
        right_or_wrong = (TextView) findViewById(R.id.right_or_wrong);
        left_total = (TextView) findViewById(R.id.left_total);
        go_to_next = (TextView) findViewById(R.id.go_to_next);
        right_result = (TextView) findViewById(R.id.right_result);
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);
    }

    public void cleanAndNext() {
        input_result.setText("");
        right_or_wrong.setVisibility(View.INVISIBLE);
        go_to_next.setVisibility(View.INVISIBLE);
        right_result.setVisibility(View.INVISIBLE);
        submit.setEnabled(true);

        String exp = FourMixedOpeExp.getRanExpression(expressionBank);
        String[] expAndResult = exp.split("=");
        expression.setText(expAndResult[0]);
        rightResult = expAndResult[1].replace(" ", "");

        leftTotalMsg(total, answerList.size(), left_total);
        wrongRate = (double) wrongScore / total;
        if (wrongRate >= 0.5 && !wrongRateTipShowed) {
            wrongRateTipShowed = true;
            Toast.makeText(this, "答错一半了亲， 用点心哦。", Toast.LENGTH_SHORT).show();
        }
    }


}
