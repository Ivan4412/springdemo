package util;

import com.yjs.SpringbootMybaitsApplication;
import com.yjs.util.AverageCapitalPlusInterestUtils;
import com.yjs.util.AverageCapitalUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Map;

/**
 * author : yjs
 * createTime : 2018/9/30
 * description :
 * version : 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = SpringbootMybaitsApplication.class)
public class TestInterestUtils {

    /**
     * 测试等额本息
     */
    @Test
    public void testAverageCapitalPlusInterest() {
        double invest = 10000; // 本金
        int month = 3;
        double yearRate = 0.065; // 年利率
        double perMonthPrincipalInterest = AverageCapitalPlusInterestUtils.getPerMonthPrincipalInterest(invest, yearRate, month).doubleValue();
        System.out.println("等额本息---每月还款本息：" + perMonthPrincipalInterest);
        Map<Integer, BigDecimal> mapInterest = AverageCapitalPlusInterestUtils.getPerMonthInterestByFormula(invest, yearRate, month);
        System.out.println("等额本息---每月还款利息(公式)：" + mapInterest);
        Map<Integer, BigDecimal> mapPrincipal = AverageCapitalPlusInterestUtils.getPerMonthPrincipalBySub(invest, yearRate, month);
        System.out.println("等额本息---每月还款本金（相减）：" + mapPrincipal);
        Map<Integer, BigDecimal> mapInterest2 = AverageCapitalPlusInterestUtils.getPerMonthInterestBySub(invest, yearRate, month);
        System.out.println("等额本息---每月还款利息(相减)：" + mapInterest2);
        Map<Integer, BigDecimal> mapPrincipal2 = AverageCapitalPlusInterestUtils.getPerMonthPrincipalByFormula(invest, yearRate, month);
        System.out.println("等额本息---每月还款本金（公式）：" + mapPrincipal2);
        double count = AverageCapitalPlusInterestUtils.getInterestCount(invest, yearRate, month);
        System.out.println("等额本息---总利息：" + count);
        double principalInterestCount = AverageCapitalPlusInterestUtils.getPrincipalAndInterestCount(invest, yearRate, month);
        System.out.println("等额本息---应还本息总和：" + principalInterestCount);

    }

    /**
     * 测试等额本金
     */
    @Test
    public void testAverageCapital() {
        double invest = 10000; // 本金
        int month = 3;
        double yearRate = 0.065; // 年利率
        Map<Integer, BigDecimal> getPerMonthPrincipalInterest = AverageCapitalUtils.getPerMonthPrincipalInterest(invest, yearRate, month);
        System.out.println("等额本金---每月本息：" + getPerMonthPrincipalInterest);
        BigDecimal benjin = AverageCapitalUtils.getPerMonthPrincipal(invest, month);
        System.out.println("等额本金---每月本金:" + benjin);
        Map<Integer, BigDecimal> mapInterest = AverageCapitalUtils.getPerMonthInterest(invest, yearRate, month);
        System.out.println("等额本金---每月利息:" + mapInterest);
        double count = AverageCapitalUtils.getInterestCount(invest, yearRate, month);
        System.out.println("等额本金---总利息：" + count);

    }

}
