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

import com.letus179.figureitout.common.Constants;

public class ModelFourActivity extends AppCompatActivity {

    private RadioGroup type_radio_group,total_radio_group;
    private Button start_answer, go_back_index;
    private Toolbar toolbar;
    private String level = Constants.PRIMARY_MODEL;
    private int total = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_four);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        init();

    }

    private void init() {
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelFourActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        start_answer = (Button) findViewById(R.id.start_answer);
        start_answer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelFourActivity.this, ModelFourPlayActivity.class);
                intent.putExtra("level", level);
                intent.putExtra("total", total);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                finish();
            }
        });

        go_back_index = (Button) findViewById(R.id.go_back_index);
        go_back_index.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ModelFourActivity.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });

        type_radio_group = (RadioGroup) findViewById(R.id.type_radio_group);
        type_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton radio = (RadioButton) findViewById(buttonId);
                level = radio.getText().toString();
            }
        });

        total_radio_group = (RadioGroup) findViewById(R.id.total_radio_group);
        total_radio_group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                int buttonId = group.getCheckedRadioButtonId();
                RadioButton radio = (RadioButton) findViewById(buttonId);
                total = Integer.valueOf(radio.getText().toString());
            }
        });
    }
}
