package com.fcbox.lib.wxpay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author: zhaoxiang
 * @create: 2018/5/24 20:34
 * @description:
 */
public class FormatUtil {
    /**
     * yyyy-MM-dd HH:mm
     */
    public static final String FORMAT_DATE_TIME = "yyyy-MM-dd HH:mm";
    /**
     * yyyy-MM-dd HH:mm:ss
     */
    public static final String FORMAT_DATE_TIME_02 = "yyyy-MM-dd HH:mm:ss";
    /**
     * yyyy-MM-dd HH:mm:ss.SSS
     */
    public static final String FORMAT_DATE_TIME_03 = "yyyy-MM-dd HH:mm:ss.SSS";

    /**
     * hump transform to underline
     *
     * @param para
     * @return
     */
    public static String humpToUnderline(String para) {
        StringBuilder sb = new StringBuilder(para);
        int temp = 0;
        for (int i = 0; i < para.length(); i++) {
            if (Character.isUpperCase(para.charAt(i))) {
                sb.insert(i + temp, "_");
                temp += 1;
            }
        }
        return sb.toString().toLowerCase();
    }

    /**
     * Time format transform
     * @param date
     * @param format
     * @return
     */
    public static String date2StrFormat(Date date, String format) {
        SimpleDateFormat simpleDate = new SimpleDateFormat(format,
                Locale.getDefault());
        return simpleDate.format(date);
    }
}
