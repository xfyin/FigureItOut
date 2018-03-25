package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.letus179.figureitout.common.Calculator;
import com.letus179.figureitout.common.Constants;
import com.letus179.figureitout.common.FourMixedOpeExp;
import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static com.letus179.figureitout.R.string.range;

public class ModelFourPlayActivity extends BasicActivity implements View.OnClickListener {
    private Toolbar toolbar;
    private TextView exit, left_total, go_to_next, num1, num2, num3, num4, num5, result, right_or_wrong;

    private TextView[] nums = new TextView[5];
    private TextView[] operates = new TextView[10];
    private Button submit, check_answer;
    private EditText operate1, operate2, operate3, operate4, operate5, operate6, operate7, operate8, operate9, operate10;
    private String level, num, calculatorResult;
    private int total, operator, rightScore, wrongScore;
    ;
    private List<String> expBank;
    private List<Answer> answerList;
    private long startTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_four_play);

        init();
        exit.setVisibility(View.VISIBLE);
        exit.setOnClickListener(this);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }

        Intent intent = getIntent();
        total = intent.getIntExtra("total", 5);
        level = intent.getStringExtra("level");
        if (Constants.PRIMARY_MODEL.equals(level)) {
            operator = 2;
            num4.setVisibility(View.GONE);
            num5.setVisibility(View.GONE);
            operate7.setVisibility(View.GONE);
            operate8.setVisibility(View.GONE);
            operate9.setVisibility(View.GONE);
            operate10.setVisibility(View.GONE);
        } else if (Constants.MIDDLE_MODEL.equals(level)) {
            operator = 3;
            num5.setVisibility(View.GONE);
            operate9.setVisibility(View.GONE);
            operate10.setVisibility(View.GONE);
        } else if (Constants.SENIOR_MODEL.equals(level)) {
            operator = 4;
        }

        for (int i = 1; i < 10; i++) {
            expBank.addAll(FourMixedOpeExp.genExpBank(total, operator, 0, i, true));
        }

        goToNext();
        leftTotalMsg(total, answerList.size(), left_total);
        startTime = System.currentTimeMillis();
    }


    private void showInPage(String num, String calculatorResult) {
        for (int i = 0; i < nums.length; i++) {
            nums[i].setText(num);
        }
        result.setText(calculatorResult);
    }

    private void goToNext() {
        String ranExpression = FourMixedOpeExp.getRanExpression(expBank);
        String[] split = ranExpression.split(" ");
        if (Constants.LEFT_PARENTHESIS.equals(split[0])) {
            num = split[1];
        } else {
            num = split[0];
        }
        calculatorResult = ranExpression.split("=")[1].replace(" ", "");
        showInPage(num, calculatorResult);
    }


    private void init() {
        answerList = new ArrayList<>();
        expBank = new ArrayList<>();
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        exit = (TextView) findViewById(R.id.exit);
        left_total = (TextView) findViewById(R.id.left_total);
        go_to_next = (TextView) findViewById(R.id.go_to_next);
        num1 = (TextView) findViewById(R.id.num1);
        num2 = (TextView) findViewById(R.id.num2);
        num3 = (TextView) findViewById(R.id.num3);
        num4 = (TextView) findViewById(R.id.num4);
        num5 = (TextView) findViewById(R.id.num5);
        result = (TextView) findViewById(R.id.result);
        right_or_wrong = (TextView) findViewById(R.id.right_or_wrong);
        operate1 = (EditText) findViewById(R.id.operate1);
        operate2 = (EditText) findViewById(R.id.operate2);
        operate3 = (EditText) findViewById(R.id.operate3);
        operate4 = (EditText) findViewById(R.id.operate4);
        operate5 = (EditText) findViewById(R.id.operate5);
        operate6 = (EditText) findViewById(R.id.operate6);
        operate7 = (EditText) findViewById(R.id.operate7);
        operate8 = (EditText) findViewById(R.id.operate8);
        operate9 = (EditText) findViewById(R.id.operate9);
        operate10 = (EditText) findViewById(R.id.operate10);
        submit = (Button) findViewById(R.id.submit);
        check_answer = (Button) findViewById(R.id.check_answer);

        exit.setOnClickListener(this);
        submit.setOnClickListener(this);
        check_answer.setOnClickListener(this);


        nums[0] = num1;
        nums[1] = num2;
        nums[2] = num3;
        nums[3] = num4;
        nums[4] = num5;
        operates[0] = operate1;
        operates[1] = operate2;
        operates[2] = operate3;
        operates[3] = operate4;
        operates[4] = operate5;
        operates[5] = operate6;
        operates[6] = operate7;
        operates[7] = operate8;
        operates[8] = operate9;
        operates[9] = operate10;
    }

    /**
     * 写完完成的表达式
     *
     * @return
     */
    private List<String> oneExp() {

        List<String> expList = new ArrayList<>();

        if (!TextUtils.isEmpty(operate1.getText())) {
            expList.add(operate1.getText().toString());
        }
        expList.add(String.valueOf(num));
        if (!TextUtils.isEmpty(operate2.getText())) {
            expList.add(operate2.getText().toString());
        }
        if (!TextUtils.isEmpty(operate3.getText())) {
            expList.add(operate3.getText().toString());
        }
        expList.add(String.valueOf(num));
        if (!TextUtils.isEmpty(operate4.getText())) {
            expList.add(operate4.getText().toString());
        }
        if (!TextUtils.isEmpty(operate5.getText())) {
            expList.add(operate5.getText().toString());
        }

        expList.add(String.valueOf(num));
        if (!TextUtils.isEmpty(operate6.getText())) {
            expList.add(operate6.getText().toString());
        }

        if (Constants.MIDDLE_MODEL.equals(level)) {

            if (!TextUtils.isEmpty(operate7.getText())) {
                expList.add(operate7.getText().toString());
            }
            expList.add(String.valueOf(num));

            if (!TextUtils.isEmpty(operate8.getText())) {
                expList.add(operate8.getText().toString());
            }

        } else if (Constants.SENIOR_MODEL.equals(level)) {
            if (!TextUtils.isEmpty(operate9.getText())) {
                expList.add(operate9.getText().toString());
            }
            expList.add(String.valueOf(num));
            if (!TextUtils.isEmpty(operate10.getText())) {
                expList.add(operate10.getText().toString());
            }
        }
        return expList;
    }


    public void cleanAndNext() {
        right_or_wrong.setVisibility(View.INVISIBLE);
        go_to_next.setVisibility(View.INVISIBLE);
        submit.setEnabled(true);
        cleanEditView();
        goToNext();
        leftTotalMsg(total, answerList.size(), left_total);
    }

    private void cleanEditView() {
        for (int i = 0; i < nums.length; i++) {
            nums[i].setText("");
        }
        for (int i = 0; i < operates.length; i++) {
            operates[i].setText("");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.submit:

                boolean isAllEmpty = true;
                int leftParenthesis = 0;
                int rightParenthesis = 0;


                for (int i = 0; i < operates.length; i++) {


                    if (!TextUtils.isEmpty(operates[i].getText())) {
                        String ope = operates[i].getText().toString();

                        if (Constants.LEFT_PARENTHESIS.equals(ope)) {
                            leftParenthesis++;
                        } else if (Constants.RIGHT_PARENTHESIS.equals(ope)) {
                            rightParenthesis++;
                        }

                        isAllEmpty = false;
                    }
                }

                if (isAllEmpty) {
                    ToastUtil.showShort(ModelFourPlayActivity.this, "请输入“+”、“-”、“*”、“/”以及“(”、“)”若干,使得等式成立。");
                    return;
                }

                if (leftParenthesis != rightParenthesis) {
                    ToastUtil.showShort(ModelFourPlayActivity.this, "“(”、“)”必须成对出现。");
                    return;
                }

                Answer answer = new Answer();
                answer.setNumber(answerList.size() + 1);

                StringBuilder sb = new StringBuilder();
                List<String> oneExp = oneExp();
                for (String exp : oneExp) {
                    sb.append(exp).append("  ");
                }

                answer.setExpression(sb.toString());
                String calculate = new Calculator().calculate(oneExp);

                int dot = calculate.indexOf(".");
                int dotLen = calculate.substring(dot + 1, calculate.length())
                        .length();
                if (dotLen == 1
                        && "0".equals(calculate.substring(dot + 1,
                        calculate.length()))) {
                    calculate = calculate.substring(0, dot);
                }

                answer.setRightResult(calculatorResult);

                right_or_wrong.setVisibility(View.VISIBLE);
                go_to_next.setVisibility(View.VISIBLE);

                // TODO: 2018/1/21  如果计算的结果
                if (calculatorResult.equals(calculate)) {
                    rightScore++;
                    answer.setRightOrWrong(true);
                    right_or_wrong.setBackgroundResource(R.drawable.right);
                    new MyCountDownTimer(3 * 1000, 1000, go_to_next).start();
                } else {
                    wrongScore++;
                    answer.setRightOrWrong(false);
                    right_or_wrong.setBackgroundResource(R.drawable.wrong);
                    new MyCountDownTimer(5 * 1000, 1000, go_to_next).start();
                }

                answerList.add(answer);
                submit.setEnabled(false);


                if (total == answerList.size()) {
                    long endTime = System.currentTimeMillis();
                    Intent intent = new Intent(ModelFourPlayActivity.this, ResultAnalyseActivity.class);
                    intent.putExtra("answer_data", (Serializable) answerList);
                    intent.putExtra("model", 4);
                    intent.putExtra("range", range);
                    intent.putExtra("used_time", (endTime - startTime - 3 * 1000 * rightScore - 5 * 1000 * wrongScore) * 1.000 / 1000);
                    startActivity(intent);
                    finish();
                }

                break;
            case R.id.check_answer:
                break;
            case R.id.exit:
                exitDialog(this, ModelFourActivity.class);
                break;
            default:
                break;
        }
    }
}
