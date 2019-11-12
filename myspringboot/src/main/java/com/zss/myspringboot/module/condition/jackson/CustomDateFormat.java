package com.zss.myspringboot.module.condition.jackson;

import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class CustomDateFormat extends DateFormat {
    private static final long serialVersionUID = -3161942617506568819L;
    private static Map<String, DateFormat> formats = new HashMap();
    private static final CustomDateFormat instance = new CustomDateFormat();

    private CustomDateFormat() {
        this.setPatterns(new String[]{"yyyy-M-d", "yyyy-M-d HH:mm:ss", "yyyy-MM-dd", "yyyy-MM-dd HH:mm:ss"});
    }

    public static CustomDateFormat getDateFormat() {
        return instance;
    }

    public Object clone() {
        return new CustomDateFormat();
    }

    public StringBuffer format(Date date, StringBuffer toAppendTo, FieldPosition fieldPosition) {
        StringBuffer result = null;
        Iterator i$ = formats.values().iterator();

        while(i$.hasNext()) {
            DateFormat format = (DateFormat)i$.next();

            try {
                result = format.format(date, toAppendTo, fieldPosition);
                if (result != null) {
                    break;
                }
            } catch (Exception var8) {
            }
        }

        return result;
    }

    public Date parse(String source, ParsePosition pos) {
        Date date = null;
        Iterator i$ = formats.values().iterator();

        while(i$.hasNext()) {
            DateFormat format = (DateFormat)i$.next();

            try {
                date = format.parse(source, pos);
                if (date != null) {
                    break;
                }
            } catch (Exception var7) {
            }
        }

        return date;
    }

    public void setPatterns(String[] patterns) {
        String[] arr$ = patterns;
        int len$ = patterns.length;

        for(int i$ = 0; i$ < len$; ++i$) {
            String pattern = arr$[i$];
            if (!formats.containsKey(pattern)) {
                formats.put(pattern, new SimpleDateFormat(pattern));
            }
        }

    }
}