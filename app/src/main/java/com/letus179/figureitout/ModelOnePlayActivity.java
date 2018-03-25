package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ModelOnePlayActivity extends BasicActivity implements View.OnClickListener {

    private static final String TAG = "ModelOnePlayActivity";
    private TextView expression, left_total_one, go_to_next_one, input_result_one, right_or_wrong_one, right_result_one, exit;
    private Button submit_one;
    private String type;
    private int range, total, firstNum, secondNum, inputResult, rightResult, answeredTotal, rightScore, wrongScore;
    private double wrongRate;
    private boolean wrongRateTipShowed;
    private long startTime;
    private Toolbar toolbar;
    private List<Answer> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_one_play);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        exit = (TextView) findViewById(R.id.exit);
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Intent intent = getIntent();
        type = intent.getStringExtra("type");
        range = Integer.valueOf(intent.getStringExtra("range"));
        total = Integer.valueOf(intent.getStringExtra("total"));
        init();
        setGenerateNum(range);
        leftTotalMsg(total, answeredTotal, left_total_one);
        startTime = System.currentTimeMillis();
    }

    private void init() {
        left_total_one = (TextView) findViewById(R.id.left_total_one);
        go_to_next_one = (TextView) findViewById(R.id.go_to_next_one);
        expression = (TextView) findViewById(R.id.expression);
        input_result_one = (TextView) findViewById(R.id.input_result_one);
        right_or_wrong_one = (TextView) findViewById(R.id.right_or_wrong_one);
        right_result_one = (TextView) findViewById(R.id.right_result_one);
        submit_one = (Button) findViewById(R.id.submit_one);
        submit_one.setOnClickListener(this);
        answerList = new ArrayList<>();
    }


    /**
     * 根据传入的范围产生数字
     *
     * @param range 10,100,1000,1000
     * @return
     */
    private int generateNum(int range) {
        return new Random().nextInt(range);
    }

    private void setGenerateNum(int range) {
        firstNum = generateNum(range);
        secondNum = generateNum(range);

        if (("-").equals(type)) {
            if (firstNum < secondNum) {
                int temp = firstNum;
                firstNum = secondNum;
                secondNum = temp;
            }
        } else if ("÷".equals(type)) {
            if (secondNum == 0) {
                int temp = firstNum;
                firstNum = secondNum;
                secondNum = temp;
            }

            if (!reFigure(firstNum, secondNum)) {
                setGenerateNum(range);
            }
        }

        expression.setText(firstNum + "  " + type + "  " + secondNum);

    }

    private boolean reFigure(int firstNum, int secondNum) {
        double i = (double) firstNum / secondNum;
        return i == Math.floor(i);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_one:
                if (TextUtils.isEmpty(input_result_one.getText())) {
                    ToastUtil.showShort(ModelOnePlayActivity.this, "请输入运算结果...");
                    return;
                }

                try {
                    inputResult = Integer.valueOf(input_result_one.getText().toString());
                } catch (NumberFormatException e) {
                    Log.d(TAG, "int range is  -2147483648～2147483647");
                    inputResult = -1;
                }
                switch (type) {
                    case "+":
                        rightResult = firstNum + secondNum;
                        break;
                    case "-":
                        rightResult = firstNum - secondNum;
                        break;
                    case "*":
                        rightResult = firstNum * secondNum;
                        break;
                    case "÷":
                        rightResult = firstNum / secondNum;
                        break;
                }
                right_or_wrong_one.setVisibility(View.VISIBLE);
                go_to_next_one.setVisibility(View.VISIBLE);

                Answer answer = new Answer();
                answer.setNumber(++answeredTotal);
                answer.setOperate(type);
                answer.setExpression(firstNum + "  " + type + "  " + secondNum);
                answer.setInputResult(String.valueOf(inputResult));
                if (inputResult == rightResult) {
                    answer.setRightOrWrong(true);
                    rightScore++;
                    right_or_wrong_one.setBackgroundResource(R.drawable.right);
                    new MyCountDownTimer(3 * 1000, 1000, go_to_next_one).start();
                } else {
                    answer.setRightOrWrong(false);
                    answer.setRightResult(String.valueOf(rightResult));
                    wrongScore++;
                    right_or_wrong_one.setBackgroundResource(R.drawable.wrong);
                    right_result_one.setVisibility(View.VISIBLE);
                    right_result_one.setText("正确答案：" + rightResult);
                    new MyCountDownTimer(5 * 1000, 1000, go_to_next_one).start();
                }
                answerList.add(answer);
                submit_one.setEnabled(false);
                // 全部答完了 分析结果
                if (answeredTotal == total) {

                    // TODO: 2018/1/12
                    // 进度条对话框，显示正在分析

                    long endTime = System.currentTimeMillis();
                    Intent intent = new Intent(ModelOnePlayActivity.this, ResultAnalyseActivity.class);
                    intent.putExtra("answer_data", (Serializable) answerList);
                    intent.putExtra("model", 1);
                    intent.putExtra("range", range);
                    intent.putExtra("used_time", (endTime - startTime - 3 * 1000 * rightScore - 5 * 1000 * wrongScore) * 1.000 / 1000);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.exit:
                exitDialog(this, ModelOneActivity.class);
                break;
            default:
                break;
        }
    }

    public void cleanAndNext() {
        input_result_one.setText("");
        right_or_wrong_one.setVisibility(View.INVISIBLE);
        go_to_next_one.setVisibility(View.INVISIBLE);
        right_result_one.setVisibility(View.INVISIBLE);
        submit_one.setEnabled(true);
        setGenerateNum(range);
        leftTotalMsg(total, answeredTotal, left_total_one);
        wrongRate = (double) wrongScore / total;
        if (wrongRate >= 0.5 && !wrongRateTipShowed) {
            wrongRateTipShowed = true;
            Toast.makeText(this, "答错一半了亲， 用点心哦。", Toast.LENGTH_SHORT).show();
        }
    }

}
