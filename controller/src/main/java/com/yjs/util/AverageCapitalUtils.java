package com.yjs.util;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/** 等额本金计算工具类
 * 等额本金，每月偿还同等数额的本金和剩余贷款在该月所产生的利息，这样由于每月的还款本金额固定，而利息越来越少，借款人起初还款压力较大，但是随时间的推移每月还款数也越来越少。
 * author : yjs
 * createTime : 2018/9/30
 * description :
 * version : 1.0
 */
public class AverageCapitalUtils {

    /**
     * 获取等额本金的每月偿还本金和利息
     * 公式：每月月供额=(贷款本金÷还款月数)+(贷款本金-已归还本金累计额)×月利率
     * @param invest  总借款额（贷款本金）
     * @param yearRate    年利率
     * @param totalMonth  还款总月数
     * @return 每月偿还本金和利息，四舍五入
     */
    public static Map<Integer, BigDecimal> getPerMonthPrincipalInterest(double invest, double yearRate, int totalMonth){
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        double monthPrincipal = getPerMonthPrincipal(invest, totalMonth).doubleValue();
        double monthRate = yearRate / 12;
        for(int i=1;i<=totalMonth;i++){
            BigDecimal monthIncome = new BigDecimal(monthPrincipal).add(new BigDecimal(invest - monthPrincipal*(i-1)).multiply(new BigDecimal(monthRate)));
            monthIncome = monthIncome.setScale(2, BigDecimal.ROUND_HALF_UP);
            map.put(i,monthIncome);
        }
        return map;
    }

    /**
     * 获取等额本金的每月偿还本金
     * @param invest  总借款额（贷款本金）
     * @param totalMonth  还款总月数
     * @return 每月偿还本金
     */
    public static BigDecimal getPerMonthPrincipal(double invest, int totalMonth) {
        BigDecimal monthIncome = new BigDecimal(invest).divide(new BigDecimal(totalMonth), 2, BigDecimal.ROUND_HALF_UP);
        return monthIncome;
    }

    /**
     *  获取等额本金的每月偿还利息
     *  公式：每月应还利息=剩余本金×月利率=(贷款本金-已归还本金累计额)×月利率
     * @param invest  总借款额（贷款本金）
     * @param yearRate    年利率
     * @param totalMonth  还款总月数
     * @return 每月偿还利息
     */
    public static Map<Integer, BigDecimal> getPerMonthInterest(double invest, double yearRate, int totalMonth) {
        Map<Integer, BigDecimal> map = new HashMap<Integer, BigDecimal>();
        double monthPrincipal = getPerMonthPrincipal(invest, totalMonth).doubleValue();
        double monthRate = yearRate / 12;
        for(int i=1;i<=totalMonth;i++){
            BigDecimal monthInterest = new BigDecimal(invest - monthPrincipal*(i-1)).multiply(new BigDecimal(monthRate));
            monthInterest = monthInterest.setScale(2, BigDecimal.ROUND_HALF_UP);
            map.put(i,monthInterest);
        }
        return map;
    }

    /**
     * 获取等额本金的总利息
     * @param invest  总借款额（贷款本金）
     * @param yearRate    年利率
     * @param totalMonth  还款总月数
     * @return
     */
    public static double getInterestCount(double invest, double yearRate, int totalMonth) {
        BigDecimal count = new BigDecimal(0);
        Map<Integer, BigDecimal> mapInterest = getPerMonthInterest(invest, yearRate, totalMonth);
        for (Map.Entry<Integer, BigDecimal> entry : mapInterest.entrySet()) {
            count = count.add(entry.getValue());
        }
        return count.doubleValue();
    }

}
