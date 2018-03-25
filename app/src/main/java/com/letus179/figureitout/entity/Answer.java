package com.letus179.figureitout.entity;

import java.io.Serializable;

/**
 * Created by xfyin on 2018/1/14.
 */

public class Answer implements Serializable {
    /**
     * 第几题
     */
    private int number;
    /**
     * 运算表达式
     */
    private String expression;

    /**
     * 运算符（对单种运算符有效）
     */
    private String operate;

    /**
     * 输入的结果
     */
    private String inputResult;

    /**
     * 回答正确还是错误
     */
    private boolean rightOrWrong;


    /**
     * 正确的答案
     */
    private String rightResult;

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getOperate() {
        return operate;
    }

    public void setOperate(String operate) {
        this.operate = operate;
    }


    public boolean isRightOrWrong() {
        return rightOrWrong;
    }

    public void setRightOrWrong(boolean rightOrWrong) {
        this.rightOrWrong = rightOrWrong;
    }

    public String getInputResult() {
        return inputResult;
    }

    public void setInputResult(String inputResult) {
        this.inputResult = inputResult;
    }

    public String getRightResult() {
        return rightResult;
    }

    public void setRightResult(String rightResult) {
        this.rightResult = rightResult;
    }
}
