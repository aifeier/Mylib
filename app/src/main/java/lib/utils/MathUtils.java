package lib.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 * Created by n-240 on 2016/5/5.
 * 保留小数点
 *
 * @author cwf
 * @email 237142681@qq.com
 */
public class MathUtils {

    /*保留位数*/
    public static int points = 2;

    /**
     * 保留两位小数，四舍五入的一个老土的方法
     *
     * @param d
     * @return
     */
    public static double formatDouble1(double d) {
        return (double) Math.round(d * Math.pow(10, points)) / Math.pow(10, points);
    }


    /**
     * The BigDecimal class provides operations for arithmetic, scale manipulation, rounding, comparison, hashing, and format conversion.
     *
     * @param d
     * @return
     */
    public static double formatDouble2(double d) {
        // 旧方法，已经不再推荐使用
//        BigDecimal bg = new BigDecimal(d).setScale(2, BigDecimal.ROUND_HALF_UP);


        // 新方法，如果不需要四舍五入，可以使用RoundingMode.DOWN
        BigDecimal bg = new BigDecimal(d).setScale(points, RoundingMode.UP);


        return bg.doubleValue();
    }

    /**
     * NumberFormat is the abstract base class for all number formats.
     * This class provides the interface for formatting and parsing numbers.
     *
     * @param d
     * @return
     */
    public static String formatDouble3(double d) {
        NumberFormat nf = NumberFormat.getNumberInstance();


        // 保留两位小数
        nf.setMaximumFractionDigits(points);


        // 如果不需要四舍五入，可以使用RoundingMode.DOWN
        nf.setRoundingMode(RoundingMode.UP);


        return nf.format(d);
    }


    /**
     * 这个方法挺简单的。
     * DecimalFormat is a concrete subclass of NumberFormat that formats decimal numbers.
     *
     * @param d
     * @return
     */
    public static String formatDouble4(double d) {
        String str = "#.";
        for (int i = 0; i < points; i++) {
            str += "0";
        }
        DecimalFormat df = new DecimalFormat(str);


        return df.format(d);
    }


    /**
     * 如果只是用于程序中的格式化数值然后输出，那么这个方法还是挺方便的。
     * 应该是这样使用：System.out.println(String.format("%.2f", d));
     *
     * @param d
     * @return
     */
    public static String formatDouble5(double d) {
        return String.format("%." + points + "f", d);
    }
}
