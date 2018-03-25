package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.letus179.figureitout.common.Constants;

public class ModelThreeActivity extends AppCompatActivity {

    private Button start_answer_one, go_back_index;
    private String whichType = "两种", whichRange = "10", whichTotal = "3";
    private Toolbar toolbar;
    private RadioGroup type_radio_group, range_radio_group, total_radio_group, type_radio_group1;
    private TextView model_tip2, model_tip3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_three);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        init();

    }

    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelThreeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });


        model_tip2 = (TextView) findViewById(R.id.model_tip2);
        model_tip3 = (TextView) findViewById(R.id.model_tip3);
        model_tip2.setVisibility(View.GONE);
        model_tip3.setVisibility(View.VISIBLE);

        start_answer_one = (Button) findViewById(R.id.start_answer_one);
        go_back_index = (Button) findViewById(R.id.go_back_index);
        start_answer_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelThreeActivity.this, ModelThreePlayActivity.class);
                if(Constants.TWO_OPERATOR.equals(whichType)) {
                    intent.putExtra("type", 2);
                } else if (Constants.THREE_OPERATOR.equals(whichType)) {
                    intent.putExtra("type", 3);
                } else if (Constants.FOUR_OPERATOR.equals(whichType)) {
                    intent.putExtra("type", 4);
                } else {
                    // 随机
                    intent.putExtra("type", -1);
                }

                intent.putExtra("range", Integer.valueOf(whichRange));
                intent.putExtra("total", Integer.valueOf(whichTotal));
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        go_back_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelThreeActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        type_radio_group = (RadioGroup) findViewById(R.id.type_radio_group);
        range_radio_group = (RadioGroup) findViewById(R.id.range_radio_group);
        total_radio_group = (RadioGroup) findViewById(R.id.total_radio_group);
        type_radio_group1 = (RadioGroup) findViewById(R.id.type_radio_group1);
        type_radio_group.setVisibility(View.GONE);
        type_radio_group1.setVisibility(View.VISIBLE);

        type_radio_group1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton radio = (RadioButton) findViewById(buttonId);
                whichType = radio.getText().toString();
            }
        });
        range_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton radio = (RadioButton) findViewById(buttonId);
                whichRange = radio.getText().toString();
            }
        });
        total_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton radio = (RadioButton) findViewById(buttonId);
                whichTotal = radio.getText().toString();
            }
        });

    }
}
