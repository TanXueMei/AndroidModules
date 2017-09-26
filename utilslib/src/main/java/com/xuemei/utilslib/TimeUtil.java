package com.xuemei.utilslib;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * <pre>
 *     author : ${xuemei}
 *     e-mail : 1840494174@qq.com
 *     time   : 2017/09/26
 *     desc   : 计算时间工具类
 *     version: 1.0
 * </pre>
 */

public class TimeUtil {
  /**
   * 获取当前时间
   *
   * @param format "yyyy-MM-dd HH:mm:ss"
   * @return 当前时间
   */
  public static String getCurrentTime(String format) {
    Date date = new Date();
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
    return simpleDateFormat.format(date);
  }

  /**
   * 获取当前时间为本月的第几周
   *
   * @return WeekOfMonth
   */
  public static int getWeekOfMonth() {
    Calendar calendar = Calendar.getInstance();
    int week = calendar.get(Calendar.WEEK_OF_MONTH);
    return week - 1;
  }

  /**
   * 获取当前时间为本周的第几天
   *
   * @return DayOfWeek
   */
  public static int getDayOfWeek() {
    Calendar calendar = Calendar.getInstance();
    int day = calendar.get(Calendar.DAY_OF_WEEK);
    if (day == 1) {
      day = 7;
    } else {
      day = day - 1;
    }
    return day;
  }

  /**
   * 获取当前时间的年份
   *
   * @return 年份
   */
  public static int getYear() {
    Calendar calendar = GregorianCalendar.getInstance();
    return calendar.get(Calendar.YEAR);
  }

  /**
   * 获取当前时间的月份
   *
   * @return 月份
   */
  public static int getMonth() {
    Calendar calendar = GregorianCalendar.getInstance();
    return calendar.get(Calendar.MONTH);
  }

  /**
   * 获取当前时间是哪天
   *
   * @return 哪天
   */
  public static int getDay() {
    Calendar calendar = GregorianCalendar.getInstance();
    return calendar.get(Calendar.DATE);
  }

  /**
   * 时间比较大小
   *
   * @param date1 date1
   * @param date2 date2
   * @param format "yyyy-MM-dd HH:mm:ss"
   * @return 1:date1大于date2；
   * -1:date1小于date2
   */
  public static int compareDate(String date1, String date2, String format) {
    DateFormat df = new SimpleDateFormat(format, Locale.getDefault());
    try {
      Date dt1 = df.parse(date1);
      Date dt2 = df.parse(date2);
      if (dt1.getTime() > dt2.getTime()) {
        return 1;
      } else if (dt1.getTime() < dt2.getTime()) {
        return -1;
      } else {
        return 0;
      }
    } catch (Exception exception) {
      exception.printStackTrace();
    }
    return 0;
  }

  /**
   * 时间加减
   *
   * @param day       如"2015-09-22"
   * @param dayAddNum 加减值
   * @return 结果
   */
  public static String timeAddSubtract(String day, int dayAddNum) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    try {
      Date newDate = new Date(simpleDateFormat.parse(day).getTime() + dayAddNum * 24 * 60 * 60 * 1000);
      return simpleDateFormat.format(newDate);
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;

  }

  /**
   * 毫秒格式化
   * 使用unixTimestamp2BeijingTime方法
   *
   * @param millisecond 如"1449455517602"
   * @param format      如"yyyy-MM-dd HH:mm:ss"
   * @return 格式化结果
   */
  @Deprecated
  public static String millisecond2String(Object millisecond, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
    return simpleDateFormat.format(millisecond);
  }

  /**
   * 时间戳转北京时间
   *
   * @param millisecond 如"1449455517602"
   * @param format      如"yyyy-MM-dd HH:mm:ss"
   * @return 格式化结果
   */
  public static String unixTimestamp2BeijingTime(Object millisecond, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
    return simpleDateFormat.format(millisecond);
  }

  /**
   * 北京时间转时间戳
   * 注意第一个参数和第二个参数格式必须一样
   *
   * @param beijingTime 如"2016-6-26 20:35:9"
   * @param format      如"yyyy-MM-dd HH:mm:ss"
   * @return 时间戳
   */
  public static long beijingTime2UnixTimestamp(String beijingTime, String format) {
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format, Locale.getDefault());
    long unixTimestamp = 0;
    try {
      Date date = simpleDateFormat.parse(beijingTime);
      unixTimestamp = date.getTime() / 1000;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return unixTimestamp;
  }
  /**
   * 获得口头时间字符串，如今天，昨天，昨天，刚刚，分钟前，小时前
   *
   * @param d 时间格式为yyyy-MM-dd HH:mm:ss
   * @return 口头时间字符串
   */
  public static String getTimeInterval(String d) {
    Date date = formatStringByFormat(d, "yyyy-MM-dd HH:mm:ss");
    Calendar now = Calendar.getInstance();
    now.setTime(new Date());
    int nowYear = now.get(Calendar.YEAR);
    int nowMonth = now.get(Calendar.MONTH);
    int nowWeek = now.get(Calendar.WEEK_OF_MONTH);
    int nowDay = now.get(Calendar.DAY_OF_WEEK);
    int nowHour = now.get(Calendar.HOUR_OF_DAY);
    int nowMinute = now.get(Calendar.MINUTE);

    Calendar ca = Calendar.getInstance();
    if(date!=null)
      ca.setTime(date);
    else
      ca.setTime(new Date());
    int year = ca.get(Calendar.YEAR);
    int month = ca.get(Calendar.MONTH);
    int week = ca.get(Calendar.WEEK_OF_MONTH);
    int day = ca.get(Calendar.DAY_OF_WEEK);
    int hour = ca.get(Calendar.HOUR_OF_DAY);
    int minute = ca.get(Calendar.MINUTE);
    if (year != nowYear) {
      SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
      //不同年份
      return sdf.format(date);
    } else {
      if (month != nowMonth) {
        //不同月份
        SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
        return sdf.format(date);
      } else {
        if (week != nowWeek) {
          //不同周
          SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
          return sdf.format(date);
        } else if (day != nowDay) {
          if (day + 1 == nowDay) {
            return "昨天" + formatDateByFormat(date, "HH:mm");
          }
          if (day + 2 == nowDay) {
            return "前天" + formatDateByFormat(date, "HH:mm");
          }
          //不同天
          SimpleDateFormat sdf = new SimpleDateFormat("M月dd日");
          return sdf.format(date);
        } else {
          //同一天
          int hourGap = nowHour - hour;
          if (hourGap == 0)//1小时内
          {
            if (nowMinute - minute < 1) {
              return "刚刚";
            } else {
              return (nowMinute - minute) + "分钟前";
            }
          } else if (hourGap >= 1 && hourGap <= 12) {
            return hourGap + "小时前";
          } else {
            SimpleDateFormat sdf = new SimpleDateFormat("今天 HH:mm");
            return sdf.format(date);
          }
        }
      }
    }
  }/**
   * 获取某天是星期几
   * @param date
   * @return
   */
  public static String getMonthDayWeek(Date date) {
    Calendar c = Calendar.getInstance();
    c.setTime(date);
    int year = c.get(Calendar.YEAR);    //获取年
    int month = c.get(Calendar.MONTH) + 1;   //获取月份，0表示1月份
    int day = c.get(Calendar.DAY_OF_MONTH);    //获取当前天数
    int week = c.get(Calendar.DAY_OF_WEEK);

    String weekStr = null;

    switch (week) {

      case Calendar.SUNDAY:
        weekStr = "周日";
        break;

      case Calendar.MONDAY:
        weekStr = "周一";
        break;

      case Calendar.TUESDAY:
        weekStr = "周二";
        break;

      case Calendar.WEDNESDAY:
        weekStr = "周三";
        break;

      case Calendar.THURSDAY:
        weekStr = "周四";
        break;

      case Calendar.FRIDAY:
        weekStr = "周五";
        break;

      case Calendar.SATURDAY:
        weekStr = "周六";
        break;
    }

    return month + "月" + day + "日"  + "(" + weekStr + ")";
  }


  /**
   * 功能描述：以指定的格式来格式化日期
   *
   * @param date
   *            Date 日期
   * @param format
   *            String 格式
   * @return String 日期字符串
   */
  public static String formatDateByFormat(Date date, String format) {
    String result = "";
    if (date != null) {
      try {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        result = sdf.format(date);
      } catch (Exception ex) {
        ex.printStackTrace();
      }
    }
    return result;
  }
  /**
   * 日期字符串转换为日期
   *
   * @param date 日期字符串
   * @param pattern 格式
   * @return 日期
   */
  public static Date formatStringByFormat(String date, String pattern) {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try {
      return sdf.parse(date);
    } catch (ParseException e) {
      e.printStackTrace();
      return null;
    }
  }
}
