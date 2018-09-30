package com.yjs.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * author : yjs
 * createTime : 2018/9/29
 * description : 等额本息计算工具类
 * 等额本息还款，借款人每月按相等的金额偿还贷款本息，其中每月贷款利息按月初剩余贷款本金计算并逐月结清。把按揭贷款的本金总额与利息总额相加，
 * 然后平均分摊到还款期限的每个月中。作为还款人，每个月还给银行固定金额，但每月还款额中的本金比重逐月递增、利息比重逐月递减。
 * version : 1.0
 */
public class AverageCapitalPlusInterestUtils {

    /**
     * 获取等额本息的每月偿还本金和利息
     * 公式：每月偿还本息=〔贷款本金×月利率×(1＋月利率)＾还款月数〕÷〔(1＋月利率)＾还款月数-1〕
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return 每月偿还本金和利息, 四舍五入
     */
    public static BigDecimal getPerMonthPrincipalInterest(double invest, double yearRate, int totalmonth) {
        double monthRate = yearRate / 12;
        BigDecimal monthIncome = new BigDecimal(invest)
                .multiply(new BigDecimal(monthRate * Math.pow(1 + monthRate, totalmonth)))
                .divide(new BigDecimal(Math.pow(1 + monthRate, totalmonth) - 1), 2, BigDecimal.ROUND_HALF_UP);
        return monthIncome;
    }

    /**
     * 通过本息扣除每月偿还本金获取等额本息的每月偿还利息
     * 公式： 每月偿还本息 = 每月偿还利息 - 每月应偿还本金
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return 每月偿还利息，直接截取小数点最后两位
     */
    public static Map<Integer, BigDecimal> getPerMonthInterestBySub(double invest, double yearRate, int totalmonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        BigDecimal monthIncome = getPerMonthPrincipalInterest(invest,yearRate,totalmonth);
        Map<Integer, BigDecimal> monthPrincipalMap = getPerMonthPrincipalByFormula(invest,yearRate,totalmonth);
        for(Map.Entry<Integer,BigDecimal> entry : monthPrincipalMap.entrySet()){
            map.put(entry.getKey(),monthIncome.subtract(entry.getValue()));
        }
        return map;
    }

    /**
     * 公式计算获取等额本息的每月偿还利息
     * 公式：每月偿还利息 = 贷款本金×月利率×〔(1+月利率)^还款月数-(1+月利率)^(还款月序号-1)〕÷〔(1+月利率)^还款月数-1〕
     *
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return 每月偿还利息，直接截取小数点最后两位
     */
    public static Map<Integer, BigDecimal> getPerMonthInterestByFormula(double invest, double yearRate, int totalmonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        double monthRate = yearRate / 12;
        for (int i = 1; i <= totalmonth; i++) {
            BigDecimal monthInterest = new BigDecimal(invest).multiply(new BigDecimal(monthRate))
                    .multiply(new BigDecimal(Math.pow(1 + monthRate, totalmonth)).subtract(new BigDecimal(Math.pow(1 + monthRate, i - 1))))
                    .divide(new BigDecimal(Math.pow(1 + monthRate, totalmonth) - 1), 2, BigDecimal.ROUND_DOWN);
            map.put(i, monthInterest);
        }
        return map;
    }


    /**
     * 通过本息扣除每月偿还利息获取等额本息的每月偿还本金
     * 公式： 每月应偿还本金 =  每月偿还本息 - 每月偿还利息
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return 每月偿还本金
     */
    public static Map<Integer, BigDecimal> getPerMonthPrincipalBySub (double invest, double yearRate, int totalmonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        BigDecimal monthIncome = getPerMonthPrincipalInterest(invest,yearRate,totalmonth);
        Map<Integer, BigDecimal> monthInterestMap = getPerMonthInterestByFormula(invest,yearRate,totalmonth);
        for(Map.Entry<Integer,BigDecimal> entry : monthInterestMap.entrySet()){
            map.put(entry.getKey(),monthIncome.subtract(entry.getValue()));
        }
        return map;
    }


    /**
     * 公式计算获取等额本息的每月偿还本金
     * 公式： 每月应偿还本金 = 贷款本金×月利率×(1+月利率)^(还款月序号-1)÷〔(1+月利率)^还款月数-1〕
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return 每月偿还本金
     */
    public static Map<Integer, BigDecimal> getPerMonthPrincipalByFormula (double invest, double yearRate, int totalmonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        double monthRate = yearRate / 12;
        for (int i = 1; i <= totalmonth; i++) {
            BigDecimal monthPrincipal = new BigDecimal(invest).multiply(new BigDecimal(monthRate))
                    .multiply(new BigDecimal(Math.pow(1 + monthRate, i - 1)))
                    .divide(new BigDecimal(Math.pow(1 + monthRate, totalmonth) - 1), 2, BigDecimal.ROUND_DOWN);
            map.put(i, monthPrincipal);
        }
        return map;
    }

    /**
     *获取等额本息的总利息
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return
     */
    public static double getInterestCount(double invest, double yearRate, int totalmonth) {
        BigDecimal count = new BigDecimal(0);
        Map<Integer, BigDecimal> mapInterest = getPerMonthInterestByFormula(invest, yearRate, totalmonth);
        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            count = count.add(entry.getValue());
        }
        return count.doubleValue();
    }

    /**
     * 获取等额本息的应还获取等额本息的本息总和
     * @param invest     总借款额（贷款本金）
     * @param yearRate   年利率
     * @param totalmonth 还款总月数
     * @return
     */
    public static double getPrincipalAndInterestCount(double invest, double yearRate, int totalmonth) {
        BigDecimal count =  new BigDecimal(totalmonth).multiply(getPerMonthPrincipalInterest(invest,yearRate,totalmonth));
        count = count.setScale(2, BigDecimal.ROUND_DOWN);
        return count.doubleValue();
    }

}
