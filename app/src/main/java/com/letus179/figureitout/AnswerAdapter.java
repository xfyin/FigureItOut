package com.letus179.figureitout;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.letus179.figureitout.entity.Answer;

import java.util.List;

/**
 * Created by xfyin on 2018/1/14.
 */

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> {

    private List<Answer> answerList;

    private int range;

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView number, expression, right_or_wrong, right_result;
        EditText input_result;


        public ViewHolder(View itemView) {
            super(itemView);
            number = (TextView) itemView.findViewById(R.id.number);
            expression = (TextView) itemView.findViewById(R.id.expression);
            input_result = (EditText) itemView.findViewById(R.id.input_result);
            right_or_wrong = (TextView) itemView.findViewById(R.id.right_or_wrong);
            right_result = (TextView) itemView.findViewById(R.id.right_result);
        }
    }


    public AnswerAdapter(List<Answer> answerList, int range) {
        this.answerList = answerList;
        this.range = range;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.check_answer_one_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Answer answer = answerList.get(position);
        holder.number.setText(String.valueOf(answer.getNumber()) + ".");
        holder.expression.setText(answer.getExpression());
        holder.input_result.setText(answer.getInputResult());
        if (range == 10) {
            holder.expression.setTextSize(18);
            holder.input_result.setTextSize(18);
        } else if (range == 100) {
            holder.expression.setTextSize(16);
            holder.input_result.setTextSize(16);
        } else if (range == 1000) {
            holder.expression.setTextSize(14);
            holder.input_result.setTextSize(14);
        } else if (range == 10000) {
            holder.expression.setTextSize(12);
            holder.input_result.setTextSize(12);
        }

        if (answer.isRightOrWrong()) {
            holder.right_or_wrong.setBackgroundResource(R.drawable.right);
        } else {
            holder.right_or_wrong.setBackgroundResource(R.drawable.wrong);
            holder.right_result.setVisibility(View.VISIBLE);
            holder.right_result.setText("正确答案：" + answer.getRightResult());
        }
    }

    @Override
    public int getItemCount() {
        return answerList.size();
    }


}
