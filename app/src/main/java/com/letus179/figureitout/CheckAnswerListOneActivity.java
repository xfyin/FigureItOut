package com.letus179.figureitout;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.letus179.figureitout.entity.Answer;

import java.io.Serializable;
import java.util.List;

public class CheckAnswerListOneActivity extends AppCompatActivity {

    private List<Answer> answerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_answer_one_list);
        answerList = (List<Answer>) getIntent().getSerializableExtra("answer_data");
        final double usedTime = getIntent().getDoubleExtra("used_time", 0.0);
        int range = getIntent().getIntExtra("range", 10);
        final int model = getIntent().getIntExtra("model", 1);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.answer_recycler_view);
        LinearLayoutManager manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        AnswerAdapter adapter = new AnswerAdapter(answerList, range);
        recyclerView.setAdapter(adapter);

        Toolbar toolbar = (Toolbar) findViewById(R.id.tool_bar);
        TextView tool_bar_title = (TextView) findViewById(R.id.tool_bar_title);
        tool_bar_title.setText("答题列表");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(CheckAnswerListOneActivity.this, ResultAnalyseActivity.class);
                intent.putExtra("model", model);
                intent.putExtra("used_time", usedTime);
                intent.putExtra("answer_data", (Serializable) answerList);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                finish();
            }
        });
    }
}
