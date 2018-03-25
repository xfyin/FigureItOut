package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.letus179.figureitout.common.FourMixedOpeExp;
import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class ModelTwoPlayActivity extends BasicActivity implements View.OnClickListener {


    private static final String TAG = "ModelTwoPlayActivity";
    private TextView expression, left_total_one, go_to_next_one, right_or_wrong_one, right_result_one, exit;
    private Button submit_one;
    private String type;
    private int range, total, firstNum, secondNum, chooseResult, rightResult, answeredTotal, rightScore, wrongScore, numOne, numTwo, numThree, numFour;
    private double wrongRate;
    private boolean wrongRateTipShowed;
    private long startTime;
    private Toolbar toolbar;
    private RadioGroup choose_result_radio_group;
    private RadioButton num1_two, num2_two, num3_two, num4_two;
    private Set<Integer> numSet = new HashSet<>(4);
    private List<Answer> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_two_play);

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
        expression.setText(firstNum + "  " + type + "  " + secondNum);
        num1_two.setText(String.valueOf(numOne));
        num2_two.setText(String.valueOf(numTwo));
        num3_two.setText(String.valueOf(numThree));
        num4_two.setText(String.valueOf(numFour));
        leftTotalMsg(total, answeredTotal, left_total_one);
        startTime = System.currentTimeMillis();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit_one:
                right_or_wrong_one.setVisibility(View.VISIBLE);
                go_to_next_one.setVisibility(View.VISIBLE);
                Answer answer = new Answer();
                answer.setNumber(++answeredTotal);
                answer.setOperate(type);
                answer.setExpression(firstNum + "  " + type + "  " + secondNum);
                answer.setInputResult(String.valueOf(chooseResult));
                answer.setRightResult(String.valueOf(rightResult));
                if (chooseResult == rightResult) {
                    answer.setRightOrWrong(true);
                    rightScore++;
                    right_or_wrong_one.setBackgroundResource(R.drawable.right);
                    new MyCountDownTimer(3 * 1000, 1000, go_to_next_one).start();
                } else {
                    answer.setRightOrWrong(false);
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
                    Intent intent = new Intent(ModelTwoPlayActivity.this, ResultAnalyseActivity.class);
                    intent.putExtra("answer_data", (Serializable) answerList);
                    intent.putExtra("model", 2);
                    intent.putExtra("range", range);
                    intent.putExtra("used_time", (endTime - startTime - 3 * 1000 * rightScore - 5 * 1000 * wrongScore) * 1.000 / 1000);
                    startActivity(intent);
                    finish();
                } else {
                    setGenerateNum(range);
                }
                break;
            case R.id.exit:
                exitDialog(this, ModelTwoActivity.class);
                break;
            default:
                break;
        }
    }

    private void init() {
        answerList = new ArrayList<>();
        left_total_one = (TextView) findViewById(R.id.left_total_one);
        go_to_next_one = (TextView) findViewById(R.id.go_to_next_one);
        expression = (TextView) findViewById(R.id.expression);
        right_or_wrong_one = (TextView) findViewById(R.id.right_or_wrong_one);
        right_result_one = (TextView) findViewById(R.id.right_result_one);
        choose_result_radio_group = (RadioGroup) findViewById(R.id.choose_result_radio_group);
        num1_two = (RadioButton) findViewById(R.id.num1_two);
        num2_two = (RadioButton) findViewById(R.id.num2_two);
        num3_two = (RadioButton) findViewById(R.id.num3_two);
        num4_two = (RadioButton) findViewById(R.id.num4_two);
        submit_one = (Button) findViewById(R.id.submit_one);
        submit_one.setOnClickListener(this);
        choose_result_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int radioButtonId = group.getCheckedRadioButtonId();
                RadioButton radioButton = (RadioButton) findViewById(radioButtonId);
                if (radioButton != null) {
                    chooseResult = Integer.valueOf(radioButton.getText().toString());
                    submit_one.setEnabled(true);
                }
            }
        });
    }


    private void setGenerateNum(int range) {
        firstNum = FourMixedOpeExp.getRanNum(0, range);
        secondNum = FourMixedOpeExp.getRanNum(0, range);
        if (("-").equals(type)) {
            if (firstNum < secondNum) {
                int temp = firstNum;
                firstNum = secondNum;
                secondNum = temp;
            }

        } else if ("÷".equals(type)) {
            divNum(range);
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

        arithmetic(type, range, rightResult, firstNum > secondNum ? firstNum : secondNum, firstNum > secondNum ? secondNum : firstNum);

        for (int i = 0; i < numSet.size(); i++) {
            Object[] obj = numSet.toArray();
            numOne = (Integer) obj[0];
            numTwo = (Integer) obj[1];
            numThree = (Integer) obj[2];
            numFour = (Integer) obj[3];
        }
    }


    /**
     * 通过 求接近 range的最大的平方根，在1~平方更内随机产生数据，作为除数（secondNum）,
     * <p>
     * 然后 通过range/secondNum 产生商的最大值，在0~商内，随机数产生真正的商，求出被除数
     *
     * @param range
     */
    private void divNum(int range) {
        double sqrt = Math.sqrt(range);
        int max = (int) Math.floor(sqrt);
        secondNum = FourMixedOpeExp.getRanNum(1, max);
        int resultNum = FourMixedOpeExp.getRanNum(0, (int) Math.floor(range / secondNum));
        while (true) {
            if (resultNum == 0) {
                resultNum = FourMixedOpeExp.getRanNum(0, (int) Math.floor(range / secondNum));
            } else {
                break;
            }
        }

        firstNum = secondNum * resultNum;
    }

    /**
     * 提交后清除记录，再次初始化
     */
    public void cleanAndNext() {
        right_or_wrong_one.setVisibility(View.INVISIBLE);
        go_to_next_one.setVisibility(View.INVISIBLE);
        right_result_one.setVisibility(View.INVISIBLE);
        choose_result_radio_group.clearCheck();
        submit_one.setEnabled(false);
        expression.setText(firstNum + "  " + type + "  " + secondNum);
        num1_two.setText(String.valueOf(numOne));
        num2_two.setText(String.valueOf(numTwo));
        num3_two.setText(String.valueOf(numThree));
        num4_two.setText(String.valueOf(numFour));
        leftTotalMsg(total, answeredTotal, go_to_next_one);
        wrongRate = (double) wrongScore / total;
        if (wrongRate >= 0.5 && !wrongRateTipShowed) {
            wrongRateTipShowed = true;
            Toast.makeText(this, "答错一半了亲， 用点心哦。", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 四则运算 随机产生和标准答案接近的3个数
     *
     * @param type
     * @param range
     * @param rightResult
     * @param maxNum
     * @param minNum
     */
    private void arithmetic(String type, int range, int rightResult, int maxNum, int minNum) {
        numSet.clear();
        numSet.add(rightResult);
        while (true) {
            if (numSet.size() < 4) {
                int ran;
                if (range == 10) {
                    ran = rightResult + FourMixedOpeExp.getRanNum(-4, 4);
                } else if (range == 100) {
                    ran = rightResult + FourMixedOpeExp.getRanNum(-3, 3) * 10;
                } else {
                    ran = rightResult + FourMixedOpeExp.getRanNum(-3, 3) * range / 100;
                }

                if (("+".equals(type) && ran < maxNum)
                        || ("-".equals(type) && ran > maxNum)
                        || ("*".equals(type) && ran < maxNum)
                        || ("÷".equals(type) && ran > maxNum || (ran + minNum) < 0)) {
                    continue;
                } else {
                    numSet.add(ran);
                }
            } else {
                break;
            }
        }
    }
}
