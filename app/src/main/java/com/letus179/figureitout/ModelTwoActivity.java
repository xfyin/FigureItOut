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

public class ModelTwoActivity extends AppCompatActivity {

    private Button start_answer_one, go_back_index;
    private String whichType = "+", whichRange = "10", whichTotal = "3";
    private Toolbar toolbar;
    private RadioGroup type_radio_group, range_radio_group, total_radio_group;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_one);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        init();
    }

    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelTwoActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        start_answer_one = (Button) findViewById(R.id.start_answer_one);
        go_back_index = (Button) findViewById(R.id.go_back_index);
        start_answer_one.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelTwoActivity.this, ModelTwoPlayActivity.class);
                intent.putExtra("type", whichType);
                intent.putExtra("range", whichRange);
                intent.putExtra("total", whichTotal);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });
        go_back_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelTwoActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        type_radio_group = (RadioGroup) findViewById(R.id.type_radio_group);
        range_radio_group = (RadioGroup) findViewById(R.id.range_radio_group);
        total_radio_group = (RadioGroup) findViewById(R.id.total_radio_group);

        type_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
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
