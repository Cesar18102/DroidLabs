package nure.kyrychenko_oleh_volodymyrovych_5.Util;

import android.content.Context;
import android.util.TypedValue;

import java.util.Date;

import nure.kyrychenko_oleh_volodymyrovych_5.Model.Importance;

public final class Util {
    public static int Dp2Px(float dp, Context context) {
        return (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, dp,
                context.getResources().getDisplayMetrics()
        );
    }

    public static String FormatDate(Date date) {
        StringBuilder text = new StringBuilder();

        int day = date.getDate();
        if(day < 10) {
            text.append(0);
        }
        text.append(day).append(".");

        int month = date.getMonth();
        if(month < 10) {
            text.append(0);
        }
        text.append(month).append(".");
        text.append(date.getYear());

        return text.toString();
    }

    public static String FormatTime(Date time) {
        StringBuilder text = new StringBuilder();

        int hour = time.getHours();
        if(hour < 10) {
            text.append(0);
        }
        text.append(hour).append(":");

        int minute = time.getMinutes();
        if(minute < 10) {
            text.append(0);
        }
        text.append(minute);

        return text.toString();
    }

    public static String FormatDateTimeFormDb(Date date) {
        return FormatDateTimeFormDb(date, new Date(date.getTime()));
    }

    public static String FormatDateTimeFormDb(Date date, Date time) {
        String dateString = FormatDate(date);

        if(time == null) {
            return dateString;
        }

        return dateString + "T" + FormatTime(time);
    }

    public static Date ParseDateTime(String dateTime) {
        String[] dateTimeItems = dateTime.split("T");
        String[] dateItems = dateTimeItems[0].split("\\.");

        Date date = new Date(
                Integer.parseInt(dateItems[2]),
                Integer.parseInt(dateItems[1]),
                Integer.parseInt(dateItems[0])
        );

        if(dateTimeItems.length == 1) {
            return date;
        }

        String[] timeItems =  dateTimeItems[1].split(":");

        date.setHours(Integer.parseInt(timeItems[1]));
        date.setMinutes(Integer.parseInt(timeItems[0]));

        return date;
    }

    public static int importanceToInt(Importance importance) {
        if(importance == null) {
            return -1;
        }

        switch (importance) {
            case LOW: return 0;
            case MEDIUM: return 1;
            case HIGH: return 2;
            default: return -1;
        }
    }

    public static Importance intToImportance(int importance) {
        Importance[] values = Importance.values();

        if(importance < 0 || importance >= values.length) {
            return null;
        }

        return values[importance];
    }
}
