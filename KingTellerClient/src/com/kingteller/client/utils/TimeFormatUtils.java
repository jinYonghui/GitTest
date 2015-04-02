package com.kingteller.client.utils;

import java.math.RoundingMode;
import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.annotation.SuppressLint;

@SuppressLint("SimpleDateFormat")
public class TimeFormatUtils {

	public static String dateFormatDate(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy/M/d EEEE HH:mm");

		return s.format(date);
	}

	public static Date getDateByString(String time) {
		Date date = null;
		if (time == null)
			return date;
		String date_format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public static String getDateByString(String time, String dformat) {
		Date date = null;
		if (time == null)
			return time;
		String date_format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		try {
			date = format.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		SimpleDateFormat s = new SimpleDateFormat(dformat);
		return s.format(date);

	}

	public static String getShortTime(Date date) {
		String shortstring = null;
		// String time = timestampToStr(dateline);
		// Date date = getDateByString(time);
		if (date == null)
			return shortstring;

		long now = Calendar.getInstance().getTimeInMillis();
		long deltime = (now - date.getTime()) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分钟前";
		} else {
			shortstring = "刚刚";
		}
		return shortstring;
	}

	public static String getShortTime(long deltime) {
		String shortstring = null;
		// String time = timestampToStr(dateline);
		// Date date = getDateByString(time);
		if (deltime == 0)
			return shortstring;

		long now = Calendar.getInstance().getTimeInMillis();
		deltime = (now - deltime) / 1000;
		if (deltime > 365 * 24 * 60 * 60) {
			shortstring = (int) (deltime / (365 * 24 * 60 * 60)) + "年前";
		} else if (deltime > 24 * 60 * 60) {
			shortstring = (int) (deltime / (24 * 60 * 60)) + "天前";
		} else if (deltime > 60 * 60) {
			shortstring = (int) (deltime / (60 * 60)) + "小时前";
		} else if (deltime > 60) {
			shortstring = (int) (deltime / (60)) + "分前";
		} else {
			shortstring = "刚刚";
		}
		return shortstring;
	}

	// Timestamp转化为String:
	public static String timestampToStr(long dateline) {
		Timestamp timestamp = new Timestamp(dateline * 1000);
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 定义格式，不显示毫秒
		return df.format(timestamp);
	}

	public static String getNowTime() {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

		return s.format(new Date());
	}

	public static String getNowTime(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, min);// 对小时数进行+2操作,同理,减2为-2
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(calendar.getTime());
	}

	public static Date getNowDate(int min) {
		Calendar calendar = Calendar.getInstance();
		calendar.add(Calendar.MINUTE, min);// 对小时数进行+2操作,同理,减2为-2
		return calendar.getTime();
	}

	public static String getAddMinDate(String sdate, int min) {
		Date date = null;
		if (sdate == null)
			return sdate;
		String date_format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		try {
			date = format.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MINUTE, min);// 对小时数进行+2操作,同理,减2为-2
		return format.format(calendar.getTime());
	}
	
	
	public static long getTimeStampFromString(String sdate) {
		Date date = null;
		String date_format = "yyyy-MM-dd HH:mm:ss";
		SimpleDateFormat format = new SimpleDateFormat(date_format);
		try {
			date = format.parse(sdate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);

		return calendar.getTimeInMillis();
	}
	
	

	public static String getStringFromDate(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return s.format(date);
	}
	
	
	public static String getStringFromSimpleDate(Date date) {
		SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
		return s.format(date);
	}

	public static String getHourFormat(int min) {
		String timeH = "";
		if (min < 60)
			timeH = min + "分钟";
		else {
			timeH = String.valueOf(min / 60) + "小时";

			if (min % 60 > 0)
				timeH += String.valueOf(min % 60) + "分钟";
		}
		return timeH;
	}
	
	//精确到小数点后两位
	public static String getDecimalFormat(float number){
		 DecimalFormat formater = new DecimalFormat("#0.##");
		 return formater.format(number);
	}
	
	//得到分钟数
	public static float getMinute(String startTime,String endTime){
		Date end = TimeFormatUtils.getDateByString(endTime);
		Date start = TimeFormatUtils.getDateByString(startTime);
		return (end.getTime()-start.getTime())/(1000*60);
	}
}