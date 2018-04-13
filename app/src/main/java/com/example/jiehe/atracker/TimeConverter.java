package com.example.jiehe.atracker;

import java.util.Date;

/**
 * Created by jiehe on 14/03/2018.
 */

//Converts time from millisecond to request format
public class TimeConverter {

    long hour_milli;
    long minute_milli;
    long second_milli;
    long day_milli;

    public TimeConverter(){
        hour_milli =3600000;
        minute_milli = 60000;
        second_milli = 1000;
        day_milli = 86400000;
    }

    /**
     * Converts a time from milli seconds to hours
     * will not go pass 24 hours
     * (if more than 24 hours we might need a "day")
     * @param milli
     *  timre to convert in millis
     * @return
     *  return the hour
     */
    public long getHour(long milli){
        long hour = (int)(milli/hour_milli);
        return hour%24;
    }

    /**
     * same as above. for minutes
     * @param milli
     * @return
     */
    public long getMinute(long milli){
        long minute = (int)(milli/minute_milli) ;
        return minute %60;
    }

    /**
     * same as above. for second
     * @param milli
     * @return
     */
    public long getSecond(long milli){
        long second = (int)(milli/second_milli);
        return second %60;
    }

    /**
     * same as above. for day
     * @param milli
     * @return
     */
    public long getDay(long milli){
        long day = (int)(milli / day_milli);
        return day% 365;
    }

    /**
     * return the time in DD...D:HH:MM:SS format
     * @param milli
     *  time to convert from milli
     * @return
     *  string in that format
     */
    public String getTimeString(long milli){
        String duration = "";

        /*if time is more than a day then include day*/
        String day = "";
        if(milli >= day_milli){
            day = getDay(milli) + ":";
        }

        String hour = Long.toString(getHour(milli));
        if(hour.length() == 1){
            hour = "0" + hour;
        }

        String minute = Long.toString(getMinute(milli));
        if(minute.length() == 1){
            minute = "0" + minute;
        }

        String second = Long.toString(getSecond(milli));
        if(second.length() == 1){
            second = "0" + second;
        }


        //add hour minute seconds
         duration = day + hour + ":" + minute + ":" + second;
        return duration;
    }
    public String getDateTimeString(long milli){
        Date date = new Date(milli);

        return  date.getDate() + "/" + (date.getMonth() + 1) + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds();
    }
}