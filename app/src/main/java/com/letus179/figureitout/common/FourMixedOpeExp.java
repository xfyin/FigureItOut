package com.letus179.figureitout.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class FourMixedOpeExp {

    /**
     * 获取指定范围内的随机数，闭区间
     *
     * @param min 最小值
     * @param max 最大值
     * @return 随机数
     */
    public static int getRanNum(int min, int max) {
        if (min > max) {
            throw new IllegalArgumentException(
                    "The minimum must be less than or equal to the maximum");
        }
        return new Random().nextInt(max - min + 1) + min;
    }

    /**
     * 查找结果result是否在集合的表达式计算结果expression中存在
     *
     * @param expression 表达式集合
     * @param result     表达式计算结果
     * @return 存在返回true，不存在返回false
     */
    public static boolean expressionInMap(Map<List<String>, String> expression,
                                          String result) {
        for (String value : expression.values()) {
            if (result.equals(value)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 随机获取list中的某个数
     *
     * @param bank 集合
     * @return 集合中的随机数
     */
    public static String getRanExpression(List<String> bank) {
        int size = bank.size();
        if (size == 0) {
            throw new IllegalArgumentException("the size is zero");
        }
        return bank.get(getRanNum(0, size - 1));
    }

    /**
     * 根据指定的加、减、乘、除操作符数目，产生操作符，可以重复
     *
     * @param operator 操作符数目
     * @return 操作符集合
     */
    public static List<String> getOperator(int operator) {
        List<String> ops = new ArrayList<>();
        String[] operators = new String[]{Calculator.Operator.ADD.getOperator(),
                Calculator.Operator.SUBTRACT.getOperator(),
                Calculator.Operator.MULTIPLY.getOperator(),
                Calculator.Operator.DIVIDE.getOperator()};
        Random ran = new Random();
        int nextInt = ran.nextInt(operators.length);
        while (ops.size() < operator) {
            ops.add(operators[nextInt]);
            nextInt = ran.nextInt(operators.length);
        }
        return ops;
    }


    /**
     * 获取四则混合表达式集合
     *
     * @param total    需要多少个表达式， 总共产生total*100
     * @param operator 运算符的数量,如2个运算符需要3个数
     * @param range    产生操作符的范围
     * @param num      操作符具体是哪个数
     * @param flag     true表示第三个参数其作用，false表示第二个参数起作用
     * @return
     */
    public static List<String> genExpBank(int total, int operator,
                                          int range, int num, boolean flag) {
        List<String> bank = new ArrayList<>();
        while (bank.size() < total * 100) {
            Map<List<String>, String> expression;
            if (flag) {
                expression = getExpBank(operator, 0, num, true);
            } else {
                expression = getExpBank(operator, range, 0, false);
            }
            for (Map.Entry<List<String>, String> map : expression.entrySet()) {

                List<String> key = map.getKey();
                StringBuilder sb = new StringBuilder();
                for (String o : key) {
                    sb.append(o).append(" ");
                }
                String result = map.getValue();

                int dot = result.indexOf(".");
                // 结果非整数不合格，因为计算都是double类型 所以3.0这样的才算合格
                int dotLen = result.substring(dot + 1, result.length())
                        .length();
                if (flag) {
                    // 合格
                    if (dotLen == 1
                            && "0".equals(result.substring(dot + 1,
                            result.length()))) {
                        result = result.substring(0, dot);
                        // 整数部分
                        int integPart = Integer.valueOf(result);
                        // 0 < result < num的5次方
                        if (integPart < Math.round(Math.pow(9, 5))
                                && integPart > 0) {
                            bank.add(sb.toString() + "= " + result);
                        }
                    }
                } else {
                    if (dotLen <= 3) {
                        // 合格
                        if (dotLen == 1
                                && "0".equals(result.substring(dot + 1,
                                result.length()))) {
                            result = result.substring(0, dot);
                        }

                        // 整数部分
                        int integPart = 0;
                        try {
                            integPart = Integer.valueOf(result
                                    .substring(0, dot));
                        } catch (Exception e) {
                            // value = [83, -, (, 7, -, 7, ), /, (, 39, *, 0,
                            // )]=NaN
                            continue;
                        }
                        // 结果的整数部分，不能超过范围的100倍
                        if (integPart < range * 100 && integPart > -range * 100) {
                            bank.add(sb.toString() + "= " + result);
                        }
                    }
                }

            }
        }
        return bank;
    }


    /**
     * @param operator 运算符数
     * @param range    范围 （flag为false有效）
     * @param n        具体的数 1~10
     * @param flag     true 如表示 5 5 5 5 5 = 125
     * @return
     */
    public static Map<List<String>, String> getExpBank(int operator,
                                                       int range, int n, boolean flag) {
        Calculator cal = new Calculator();
        // 运算数数目
        int num = operator + 1;
        // 随机具体操作符
        List<String> opeList = getOperator(operator);
        // 括号最大数目
        int parenthesis = num / 2;
        // 具体括号数0~max之间
        int parenthesisNum = getRanNum(0, parenthesis);
        // 每一个表达式
        List<String> exp = new ArrayList<>();
        // 表达式及结集合
        Map<List<String>, String> exps = new HashMap<>();
        // 随机运算数

        // 随机运算数
        int[] nums = new int[num];
        for (int i = 0; i < nums.length; i++) {
            if (flag) {
                nums[i] = n;
            } else {
                nums[i] = getRanNum(0, range);
            }
        }

        if (!flag) {
            nums[1] = handleDivide(opeList.get(0), nums[1]);
            nums[2] = handleDivide(opeList.get(1), nums[2]);
        }
        // 2个运算符
        if (operator == 2) {

            // 没有括号
            if (parenthesisNum == 0) {
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();
            }
            // 有一个括号
            else if (parenthesisNum == 1) {
                // 左括号的位置
                for (int i = 1; i <= 3 - 1; i++) {
                    // 右括号的位置
                    for (int j = i + 1; j <= 3; j++) {
                        // 左括号在最左，右括号在最右，也即所有数在括号内，不需要考虑这种情况
                        if (i == 1 && j == 3) {
                            continue;
                        }

                        // 左括号在最左，第一个数前
                        if (i == 1) {
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            // 右括号在第2个数右
                            if (j == 2) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                            }

                        }
                        // 左括号在第二个数左
                        else if (i == 2) {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[1]));
                            // 右括号在第3个数右
                            if (j == 3) {
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                            }
                        }
                        if (!expressionInMap(exps, cal.calculate(exp))) {
                            exps.put(exp, cal.calculate(exp));
                        }
                        exp = new ArrayList<>();
                    }
                }
            }
        } else if (operator == 3) {
            if (!flag) {
                nums[3] = handleDivide(opeList.get(2), nums[3]);
            }

            if (parenthesisNum == 0) {
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                exp.add(opeList.get(2));
                exp.add(String.valueOf(nums[3]));
                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();
            } else if (parenthesisNum == 1) {

                // 左括号的位置
                for (int i = 1; i <= 4 - 1; i++) {
                    // 右括号的位置
                    for (int j = i + 1; j <= 4; j++) {
                        // 左括号在最左，右括号在最右，也即所有数在括号内，不需要考虑这种情况
                        if (i == 1 && j == 4) {
                            continue;
                        }
                        // 第1个数在左括号内
                        if (i == 1) {
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            // 右括号在第2个数右
                            if (j == 2) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                            } else if (j == 3) {
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                            }
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        } else if (i == 2) {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[1]));
                            exp.add(opeList.get(1));
                            exp.add(String.valueOf(nums[2]));
                            if (j == 3) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                            } else if (j == 4) {
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                            }
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        } else {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            exp.add(opeList.get(1));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[2]));
                            exp.add(opeList.get(2));
                            exp.add(String.valueOf(nums[3]));
                            exp.add(Constants.RIGHT_PARENTHESIS);
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        }
                    }
                }
            } else if (parenthesisNum == 2) {
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(1));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[2]));
                exp.add(opeList.get(2));
                exp.add(String.valueOf(nums[3]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();
            }
        } else if (operator == 4) {

            if (!flag) {
                nums[3] = handleDivide(opeList.get(2), nums[3]);
                nums[4] = handleDivide(opeList.get(3), nums[4]);
            }

            if (parenthesisNum == 0) {
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                exp.add(opeList.get(2));
                exp.add(String.valueOf(nums[3]));
                exp.add(opeList.get(3));
                exp.add(String.valueOf(nums[4]));
                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();
            } else if (parenthesisNum == 1) {
                // 左括号的位置
                for (int i = 1; i <= 5 - 1; i++) {
                    // 右括号的位置
                    for (int j = i + 1; j <= 5; j++) {
                        // 左括号在最左，右括号在最右，也即所有数在括号内，不需要考虑这种情况
                        if (i == 1 && j == 5) {
                            continue;
                        }
                        // 第1个数在左括号内
                        if (i == 1) {
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            // 右括号在第2个数右
                            if (j == 2) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            } else if (j == 3) {
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            } else if (j == 4) {
                                exp.add(opeList.get(1));
                                exp.add(String.valueOf(nums[2]));
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            }
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        } else if (i == 2) {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[1]));
                            exp.add(opeList.get(1));
                            exp.add(String.valueOf(nums[2]));
                            if (j == 3) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            } else if (j == 4) {
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            } else if (j == 5) {
                                exp.add(opeList.get(2));
                                exp.add(String.valueOf(nums[3]));
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                            }
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        } else if (i == 3) {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            exp.add(opeList.get(1));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[2]));
                            exp.add(opeList.get(2));
                            exp.add(String.valueOf(nums[3]));

                            if (j == 4) {
                                exp.add(Constants.RIGHT_PARENTHESIS);
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                            } else {
                                exp.add(opeList.get(3));
                                exp.add(String.valueOf(nums[4]));
                                exp.add(Constants.RIGHT_PARENTHESIS);
                            }
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        } else {
                            exp.add(String.valueOf(nums[0]));
                            exp.add(opeList.get(0));
                            exp.add(String.valueOf(nums[1]));
                            exp.add(opeList.get(1));
                            exp.add(String.valueOf(nums[2]));
                            exp.add(opeList.get(2));
                            exp.add(Constants.LEFT_PARENTHESIS);
                            exp.add(String.valueOf(nums[3]));
                            exp.add(opeList.get(3));
                            exp.add(String.valueOf(nums[4]));
                            exp.add(Constants.RIGHT_PARENTHESIS);
                            if (!expressionInMap(exps, cal.calculate(exp))) {
                                exps.put(exp, cal.calculate(exp));
                            }
                            exp = new ArrayList<>();
                        }
                    }
                }
            } else if (parenthesisNum == 2) {
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(1));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[2]));
                exp.add(opeList.get(2));
                exp.add(String.valueOf(nums[3]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(3));
                exp.add(String.valueOf(nums[4]));
                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();

                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                exp.add(opeList.get(2));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[3]));
                exp.add(opeList.get(3));
                exp.add(String.valueOf(nums[4]));
                exp.add(Constants.RIGHT_PARENTHESIS);

                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();

                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[1]));
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(2));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[3]));
                exp.add(opeList.get(3));
                exp.add(String.valueOf(nums[4]));
                exp.add(Constants.RIGHT_PARENTHESIS);

                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();

                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[0]));
                exp.add(opeList.get(0));
                exp.add(String.valueOf(nums[1]));
                exp.add(opeList.get(1));
                exp.add(String.valueOf(nums[2]));
                exp.add(Constants.RIGHT_PARENTHESIS);
                exp.add(opeList.get(2));
                exp.add(Constants.LEFT_PARENTHESIS);
                exp.add(String.valueOf(nums[3]));
                exp.add(opeList.get(3));
                exp.add(String.valueOf(nums[4]));
                exp.add(Constants.RIGHT_PARENTHESIS);

                if (!expressionInMap(exps, cal.calculate(exp))) {
                    exps.put(exp, cal.calculate(exp));
                }
                exp = new ArrayList<>();
            }
        }

        return exps;
    }

    /**
     * 在除法运算中，分母不能为0；当是0时，分母设置为固定值2
     *
     * @param operator
     * @param num
     * @return
     */
    private static int handleDivide(String operator, int num) {
        if ("÷".equals(operator) && num == 0) {
            return 2;
        }
        return num;
    }

}