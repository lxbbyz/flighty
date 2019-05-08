package com.flighty.projectDirectory.util;

import com.flighty.projectDirectory.user.dao.UserDao;
import com.flighty.projectDirectory.user.service.UserService;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Author:
 * Date:
 */
public class num {
    //用来判断是否有解
    private static boolean flag = false;
    //存放操作符
    private static char[] operator = { '+', '-', '*', '/' };
    //存放所有结果
    private static ArrayList<String> results = new ArrayList<String>();
    //存放阶段性结果
    private static ArrayList<String> resultsStage = new ArrayList<String>();

    /**
     * 保存公式进数据库
     * @param args
     */
    @RequestMapping(value = "saveNum", produces = "application/json")
    public Object main(String[] args) {
        //所有正确的解的个数
        int rightCount = 0;
        //所有情况的个数
        int allCount = 0;
        //存放4个数字
        double[] number = new double[4];

        long startTime = System.currentTimeMillis();

	/*	第1次去重，过滤掉可能产生的重复的情况，比如1,2,3,4  和4,3,2,1
		因为后面是通过排列组合来找出所有情况，1,2,3,4可以组合成4,3,2,1
		这样就重复了，这里为了过滤掉这些重复的*/
        for (int i = 1; i <= 13; i++) {
            for (int j = i; j <= 13; j++) {
                for (int k = j; k <= 13; k++) {
                    for (int m = k; m <= 13; m++) {
                        number[0] = i;
                        number[1] = j;
                        number[2] = k;
                        number[3] = m;
                        //由于过滤掉重复的，这里重新计算重复的次数（在计算所有情况的个数时需要）
                        //如果你不需要计算所有情况的个数，可以不需要
                        resultsStage.clear();
                        int count = times(i, j , k ,m);
                        System.out.println("******************************************************");
                        System.out.println("所有数字"+i+"、"+j+"、"+k+"、"+m);
                        allCount += count;
                        duplicateRemoval(number);
                        //判断是否有解
                        if(flag == true){
                            rightCount += count;
                            flag = false;
                        }

//                        for (int y = 0; y < resultsStage.size(); y++) {
//                            System.out.println(resultsStage.get(y));
//                        }

                        int numAll = resultsStage.size();

                        String numSuang = JSONArray.fromObject(resultsStage).toString();

                        String num = "[\""+i+"\",\""+j+"\",\""+k+"\",\""+m+"\"]";

                        Map params = new HashMap();
                        params.put("reckon_all",numAll);
                        params.put("nums",num);
                        params.put("reckon_formula",numSuang);
                        Map adviserMap = new HashMap<>();
                        adviserMap.put("params", params);

                        System.out.println(numSuang);
                        System.out.println(numAll);
                        System.out.println(num);

                    }
                }
            }
        }
        long endTime = System.currentTimeMillis();

//        for (int i = 0; i < results.size(); i++) {
//            System.out.println(results.get(i));
//        }
        System.out.println("共耗费时间：" + (endTime - startTime) + "ms");
        System.out.println("所有可能的个数：" + allCount);
        System.out.println("有解的个数：" + rightCount);
        System.out.println("有解的几率" + (double)rightCount/allCount);

        return "aaa";
    }

    /**
     * 由于最开始过滤掉一部分重复的情况，但这些重复情况是存在的
     * 这里是为了计算每种重复情况有多少次数，如当3张牌相同，另一张牌不同时，
     * 如3,3,3,5  抽牌时有16种不同的情况（根据花色的不同）
     * 而在计算时为了去重把这些过滤掉了，这里是为了重新计算这些情况
     * 如果你不需要计算所有情况的个数，可以不需要次方法
     */
    private static int times(int i,int j,int k,int m){
        //判断有多少种重复
        Set<Integer> set = new HashSet<Integer>();
        set.add(i);
        set.add(j);
        set.add(k);
        set.add(m);
        if(set.size() == 1){
            //当4个数的数字全部一样时（不同花色），只可能有一种组合
            return 1;
        } else if(set.size() == 3){
            //当4个数中，有两个数相同，其余的数都不相同时
            return 96;
        } else if(set.size() == 4){
            //当4个数全部不同时
            return 256;
        } else{
            if((i == j && k == m)||(i == k && j == m)||(i == m && k == j)){
                //当4个数中，两两相同时
                return 36;
            } else {
                //当4个数中有三个数相同，另外一个数不同时
                return 16;
            }
        }
    }

    /**
     * 第2次去重，由于排列组合可能导致数字组合的重复
     * 这里进行第2次过滤，只计算给定4个数的所有不同的排列
     */
    private static void duplicateRemoval(double[] number){
        Map<Double, Integer> map = new HashMap<Double, Integer>();
        //存放数字，用来判断输入的4个数字中有几个重复的，和重复的情况
        for (int i = 0; i < number.length; i++) {
            if(map.get(number[i]) == null){
                map.put(number[i], 1);
            } else {
                map.put(number[i], map.get(number[i]) + 1);
            }
        }
        if(map.size() == 1){
            //如果只有一种数字（4个不同花色的），此时只有一种排列组合，如6,6,6,6
            calculation(number[0], number[1],number[2],number[3]);
        } else if(map.size() == 2){
            //如果只有2种数字，有2种情况，如1,1,2,2和1,1,1,2
            int index = 0;//用于数据处理
            int state = 0;//判断是那种情况
            for (Double key : map.keySet()) {
                if(map.get(key) == 1){
                    //如果是有1个数字和其他3个都不同，将number变为 number[0]=number[1]=number[2]，
                    //将不同的那个放到number[3]，方便计算
                    number[3] = key;
                    state = 1;
                } else if(map.get(key) == 2){
                    //两两相同的情况，将number变为number[0]=number[1],number[2]=number[3]的情况，方便计算
                    number[index++] = key;
                    number[index++] = key;
                } else {
                    number[index++] = key;
                }
            }
            //列出2种情况的所有排列组合，并分别计算
            if(state == 1){
                calculation(number[3], number[1], number[1], number[1]);
                calculation(number[1], number[3], number[1], number[1]);
                calculation(number[1], number[1], number[3], number[1]);
                calculation(number[1], number[1], number[1], number[3]);
            }
            if(state == 0){
                calculation(number[1], number[1], number[3], number[3]);
                calculation(number[1], number[3], number[1], number[3]);
                calculation(number[1], number[3], number[3], number[1]);
                calculation(number[3], number[1], number[1], number[3]);
                calculation(number[3], number[3], number[1], number[1]);
                calculation(number[3], number[1], number[3], number[1]);
            }
        } else if(map.size() == 3){
            //有3种数字的情况
            int index = 0;
            for (Double key : map.keySet()) {
                if(map.get(key) == 2){
                    //将相同的2个数字放到number[2]=number[3]，方便计算
                    number[2] = key;
                    number[3] = key;
                } else {
                    number[index++] = key;
                }
            }
            //排列组合，所有情况
            calculation(number[0], number[1], number[3], number[3]);
            calculation(number[0], number[3], number[1], number[3]);
            calculation(number[0], number[3], number[3], number[1]);
            calculation(number[1], number[0], number[3], number[3]);
            calculation(number[1], number[3], number[0], number[3]);
            calculation(number[1], number[3], number[3], number[0]);
            calculation(number[3], number[0], number[1], number[3]);
            calculation(number[3], number[0], number[3], number[1]);
            calculation(number[3], number[1], number[0], number[3]);
            calculation(number[3], number[1], number[3], number[0]);
            calculation(number[3], number[3], number[0], number[1]);
            calculation(number[3], number[3], number[1], number[0]);
        } else if(map.size() == 4){
            //4个数都不同的情况
            getNumber(number);
        }
    }

    /**
     * 排列组合，用来处理4个数都不同的情况
     * 如1,2,3,4  可以转化为1,3,2,4   2,3,1,4    1,4,2,3等
     * 并计算每种的结果
     */
    public static void getNumber(double[] number){
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if(i == j){
                    continue;
                }
                for (int k = 0; k < 4; k++) {
                    if(k == j || k == i){
                        continue;
                    }
                    for (int m = 0; m < 4; m++) {
                        if(m == k || m == j || m == i){
                            continue;
                        }
                        calculation(number[i], number[j], number[k], number[m]);
                    }
                }
            }
        }
    }

    /**
     * 给定4个数，当这4个数位置不变时，只改变操作符号，计算所有的可能性
     * 如1+2+3+4  ，1*2*3*4 ， 1-2+3*4 等
     * 如果能得到24点，就将表达式添加到结果集
     */
    public static boolean calculation(double num1, double num2, double num3, double num4){
        for (int i = 0; i < 4; i++) {
		/*	第一次计算，保存此时的操作符和计算结果
			此时有3中情况，相当于从4个数中选择2个相邻的数来计算
			如（1-2）-3-4， 1-（2-3）-4， 1-2-（3-4）
			则保存此时第一次计算的结果和操作符*/
            char operator1 = operator[i];
            //根据操作符，先计算第1,2两个数，如输入数字是1,2,3,4  则计算1+2（1-2,1*2,1/2等），
            //这里通过循环来改变操作符，下同
            double firstResult = calcute(num1, num2, operator1);
            //根据操作符，先计算第2,3两个数，如输入数字是1,2,3,4  则计算2+3
            double midResult = calcute(num2, num3, operator1);
            //根据操作符，先计算第3,4两个数，如输入数字是1,2,3,4  则计算3+4
            double tailResult = calcute(num3, num4, operator1);
            for (int j = 0; j < 4; j++) {
				/*	第2次计算，保存此时的操作符和计算结果
				此时有5中情况，相当于从4个数中选择2个相邻的数来计算
				如（（1-2）-3）-4， （1-（2-3））-4， （1-2）-（3-4），1-（（2-3）-4），1-（2-（3-4））
				则保存此时第2次计算的结果和操作符*/
                char operator2 = operator[j];
                //根据操作符和第1次计算的结果，计算第2次的情况，如第一次计算是（1-2）-3-4，
                //就计算（（1-2）-3）-4 ，则第一次计算结果为1-2=-1  -->   即计算-1-3，即firstResult-3
                //下面的原理类似
                double firstMidResult = calcute(firstResult, num3, operator2);
                double firstTailResult = calcute(num3, num4, operator2);
                double midFirstResult = calcute(num1, midResult, operator2);
                double midTailResult = calcute(midResult, num4, operator2);
                double tailMidResult = calcute(num2, tailResult, operator2);
                for (int k = 0; k < 4; k++) {
                    //最后1次计算，得出结果，如果是24则保存表达式，原理同上
                    char operator3 = operator[k];
                    if(calcute(firstMidResult, num4, operator3) == 24){
                        String expression = "((" + (int)num1 + operator1 + (int)num2 + ")" + operator2 + (int)num3 + ")" + operator3 + (int)num4;
                        results.add(expression);
                        //System.out.println(expression);
                        resultsStage.add(expression);
                        flag = true;
                    }
                    if(calcute(firstResult, firstTailResult, operator3) == 24){
                        String expression = "(" + (int)num1 + operator1 + (int)num2 + ")" + operator3 + "(" + (int)num3 + operator2 + (int)num4 + ")";
                        results.add(expression);
                        //System.out.println(expression);
                        resultsStage.add(expression);
                        flag = true;
                    }
                    if(calcute(midFirstResult, num4, operator3) == 24){
                        String expression = "(" + (int)num1 + operator2 + "(" + (int)num2 + operator1 + (int)num3 + "))" + operator3 + (int)num4;
                        results.add(expression);
                        //System.out.println(expression);
                        resultsStage.add(expression);
                        flag = true;
                    }
                    if(calcute(num1, midTailResult, operator3) == 24){
                        String expression = "" + (int)num1 + operator3 + "((" + (int)num2 + operator1 + (int)num3 + ")" + operator2 + (int)num4 + ")";
                        results.add(expression);
                        // System.out.println(expression);
                        resultsStage.add(expression);
                        flag = true;
                    }
                    if(calcute(num1, tailMidResult, operator3) == 24){
                        String expression = "" + (int)num1 + operator3 + "(" + (int)num2 + operator2 + "(" + (int)num3 + operator1 + (int)num4 + "))";
                        results.add(expression);
//                        System.out.println(expression);
                        resultsStage.add(expression);
                        flag = true;
                    }
                }
            }
        }
        return flag;
    }

    /**
     * 给定2个数和指定操作符的计算
     * @date 2017年12月22日 下午2:47:49
     */
    private static double calcute(double number1, double number2, char operator) {
        if (operator == '+') {
            return number1 + number2;
        } else if (operator == '-') {
            return number1 - number2;
        } else if (operator == '*') {
            return number1 * number2;
        } else if (operator == '/' && number2 != 0) {
            return number1 / number2;
        } else {
            return -1;
        }
    }

}