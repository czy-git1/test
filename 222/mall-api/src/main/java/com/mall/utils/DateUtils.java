package com.mall.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
    private static SimpleDateFormat sdfStandard = new SimpleDateFormat("yyyy-MM-dd");
    private static SimpleDateFormat sdfCn = new SimpleDateFormat("yyyy年MM月dd日");
    private static SimpleDateFormat sdfCnYM = new SimpleDateFormat("yyyy年MM月");

    public static String formatCnDate(Date date){
        if(date!=null){
            return sdfCn.format(date);
        }else{
            return "";
        }
    }

    public static String formatCnYMDate(Date date){
        if(date!=null){
            return sdfCnYM.format(date);
        }else{
            return "";
        }
    }

    public static String formatStandard(Date date){
        if(date!=null){
            return sdfStandard.format(date);
        }else{
            return "";
        }
    }

    public static Date addDays(Date date, int days){
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DAY_OF_MONTH, days);
        return cal.getTime();
    }
}
