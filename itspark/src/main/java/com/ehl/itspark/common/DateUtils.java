package com.ehl.itspark.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateUtils {
	
	/**
	 * yyyyMMddHHmmss
	 */
	public static final String MY_STANDARD_DATE_FORMAT = "yyyyMMddHHmmss";
	/**
	 * yyyy-MM-dd HH:mm:ss SSS
	 */
	public static final String MILLISECOND_FORMAT = "yyyy-MM-dd HH:mm:ss SSS";
	
	public static final String SECOND_FORMAT = "yyyy-MM-dd HH:mm:ss";
	
	public static String getNowTime(){
		SimpleDateFormat sdf = new SimpleDateFormat(SECOND_FORMAT);
		return sdf.format(new Date());
	}
	public static String getNowTime(String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(new Date());
	}
	public static String getNowDateWithMinute(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
		return sdf.format(new Date());
	}
	public static String getNowDateWithHour(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		return sdf.format(new Date());
	}
	/**
	 * 
	 * @Title: getNowDateWithMillisecond 
	 * @Description: 获取带有毫秒级的当前时间
	 * @return     
	 * String     
	 * @throws
	 */
	public static String getNowDateWithMillisecond(){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		return sdf.format(new Date());
	}
	/**
	 * 
	 *���ڼ����ٷ���
	 *@param time ����
	 *@param frmT ��ʽ
	 *@param minute ����
	 *@return String
	 *@author chuanhl
	 *@date May 19, 2014
	 *@throws
	 */
	public static String subTimeMinute(String time,String frmT,Object minute){
		SimpleDateFormat sdf = new SimpleDateFormat(frmT); 
		Date date = null;
		try {
			date = sdf.parse(time);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		String times = sdf.format(date);
		date.setMinutes(date.getMinutes()-Integer.parseInt(minute.toString()));
		times=sdf.format(date);
		return times;
	}
	/**
	 * �ַ����͵�����ת����Date���͵�����
	 * @param string
	 * @param withtime
	 *  withtime=true,���ڸ�ʽ��yyyy-MM-dd HH:mm:ss��withtime=false,���ڸ�ʽ��yyyy-MM-dd 
	 * @return    Date
	 */
	public static Date StringToDate(String string, boolean withtime) {
		Date date1 = null;
		if (string == null || string.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if (withtime) {
			sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		} else {
			sdf = new SimpleDateFormat("yyyy-MM-dd");
		}
		try {
			date1 = sdf.parse(string);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date1;
	}
	public static java.sql.Date convertToSqlDate(Date date)
	{
		if(date==null){
			return null;
		}
	    try
	    {
	      java.sql.Date sqlDate = new java.sql.Date(date.getTime());
	      return sqlDate;
	    }
	    catch(Exception ex)
	    {
	      ex.printStackTrace();
	      return null;
	    }
	}
	public static java.sql.Timestamp convertToTimestamp(Date date)
	{
		if(date==null){
			return null;
		}
	    try
	    {
	      java.sql.Timestamp time = new java.sql.Timestamp(date.getTime());
	      return time;
	    }
	    catch(Exception ex)
	    {
	      ex.printStackTrace();
	      return null;
	    }
	}
	public static String getDateBeforeNow(int modifyHours) {
		Calendar cal=Calendar.getInstance();
		cal.add(Calendar.HOUR_OF_DAY, -1*modifyHours);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH");
		return sdf.format(cal.getTime());
	}
	/**
	 * 计算 second 秒后的时间
	 * 
	 * @param date
	 * @param second
	 * @return
	 */
	public static Date addSecond(Date date, int second) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.SECOND, second);
		return calendar.getTime();
	}
	
	public static Date addMinute(Date now,int minutes) {
		Calendar cal=Calendar.getInstance();
		cal.setTime(now);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}
	/**
	 * 计算 hour 小时后的时间
	 * 
	 * @param date
	 * @param hour
	 * @return
	 */
	public static Date addHour(Date date, int hour) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.HOUR, hour);
		return calendar.getTime();
	}
	/**
	 * 计算 day 天后的时间
	 * 
	 * @param date
	 * @param day
	 * @return
	 */
	public static Date addDay(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	public static Date addYear(Date date, int year) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_YEAR, 365 * year);
		return calendar.getTime();
	}
	/**
	 * 得到day的起始时间点。
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 得到day的终止时间点.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	/**
	 * 得到当月起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	/**
	 * 得到month的终止时间点.
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	/**
	 * 得到当前年起始时间
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYearStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * 得到当前年最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getYearEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	public static String DateToString(Date date){
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(date);
	}
	/**
	 * 字符串转换成日期
	 * @param str
	 * @param format
	 * @return
	 */
	public static Date StringToDate(String str, String format) {
		Date date = null;
		if (str == null || str.equals("")) {
			return null;
		}
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		try {
			date = sdf.parse(str);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return date;
	}
	/**
	 * 日期转换成字符串
	 * @param date
	 * @param format
	 * @return
	 */
	public static String DateToString(Date date,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}
	//获取时间差
	public static Long getTimeInterval(String begTime,String endTime,String format){
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		Date startDate;
		Long interval=null;
		try {
			startDate = sdf.parse(begTime);
			Date endDate=sdf.parse(endTime);
			interval=endDate.getTime()-startDate.getTime();
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
		return interval/1000;
	}
	/**
	 * 将字符串型日期转换成long型时间
	 * @param str   
	 * @param t_def
	 * @return 
	 */
	public static  long parse(String str){
	    return parse(str,MILLISECOND_FORMAT);
    }
	public static  long parse(String str,String parrten){
        try{
            SimpleDateFormat format = new SimpleDateFormat(parrten);
            long ts = format.parse(str).getTime();
            return ts;
        }catch (ParseException e){
            e.printStackTrace();
        }
        return -1;
    }
	/**
	 * 
	 * 将long型时间转成 yyyy-MM-dd HH:mm:ss SSS形式的日期
	 * 
	 * @param t
	 *              the timestamp
	 * @return the natural format string value
	 */
	public static  String format(long t){
		SimpleDateFormat format = new SimpleDateFormat(MILLISECOND_FORMAT);
		return format.format(new Date(t));
	}
	/**
	 * 
	 * @Title: crateTimeDir 
	 * @Description: 创建日期型路径
	 * @param pattern
	 * @param date
	 * @return     
	 * String     
	 * @throws
	 */
	public static String crateDateDir(DirPatten pattern,String date){

		StringBuffer dir = new StringBuffer();
		switch (pattern) {
		case YMDHM:
			
			dir.append(date.substring(0, 4)).append("/");//��
			dir.append(date.substring(5, 7)).append("/");//��
			dir.append(date.substring(8, 10)).append("/");//��
			dir.append(date.substring(11, 13)).append("/");//ʱ
			dir.append(date.substring(14, 16));//��
			break;
		case YMDH:
			
			dir.append(date.substring(0, 4)).append("/");//��
			dir.append(date.substring(5, 7)).append("/");//��
			dir.append(date.substring(8, 10)).append("/");//��
			dir.append(date.substring(11, 13));//ʱ
			break;
		case YMD:
			
			dir.append(date.substring(0, 4)).append("/");//��
			dir.append(date.substring(5, 7)).append("/");//��
			dir.append(date.substring(8, 10));//��
			break;
		}
		return dir.toString();
	}
	public static enum DirPatten{
		YMDHM,YMDH,YMD
	}
	/**
	 * @Title: cutMillisecond 
	 * @Description: 
	 * @param millisecondTime
	 * @return     
	 * String     
	 * @throws
	 */
	public static String cutMillisecond(String millisecondTime){
		if(millisecondTime!=null&&millisecondTime.length()>19){
			return millisecondTime.substring(0, 19);
		}
		return millisecondTime;
	}
}