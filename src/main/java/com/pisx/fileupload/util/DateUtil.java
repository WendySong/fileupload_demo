package com.pisx.fileupload.util;

import org.springframework.util.Assert;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by pisx on 2014/12/23.
 */
public class DateUtil {

    public static Timestamp date2Timestamp(Date date){
        Assert.notNull(date);
        return new Timestamp(date.getTime());
    }

    public static String date2String(Date date, String pattern) {
        if (date == null) {
            return null;
        }
        try {
            return new SimpleDateFormat(pattern).format(date);
        } catch (Exception e) {
            return null;
        }
    }

    public static String date2String(Date date) {
        return date2String(date, "yyyy-MM-dd HH:mm");
    }
}
