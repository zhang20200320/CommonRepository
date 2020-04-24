package com.zhang.demo.common.utils;

import org.springframework.util.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * 日期处理
 *
 * @author zhang
 * @date 2020-04-24 13:46:45
 */
public class DateUtils {

	/** 时间格式(yyyy-MM-dd) */
	public final static String DATE_PATTERN = "yyyy-MM-dd";
	/** 时间格式(yyyy-MM-dd HH:mm:ss) */
	public final static String DATE_TIME_PATTERN = "yyyy-MM-dd HH:mm:ss";

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @return  返回yyyy-MM-dd格式日期
     */
	public static String format(Date date) {
        return format(date, DATE_PATTERN);
    }

    /**
     * 日期格式化 日期格式为：yyyy-MM-dd
     * @param date  日期
     * @param pattern  格式，如：DateUtils.DATE_TIME_PATTERN
     * @return  返回yyyy-MM-dd格式日期
     */
    public static String format(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            return df.format(date);
        }
        return null;
    }

    public static String formatGMT8(Date date, String pattern) {
        if(date != null){
            SimpleDateFormat df = new SimpleDateFormat(pattern);
            TimeZone destTimeZone = TimeZone.getTimeZone("GMT+8");
            df.setTimeZone(destTimeZone);
            return df.format(date);
        }
        return null;
    }

    /**
     * 字符串转换成日期
     * @param strDate 日期字符串
     * @param pattern 日期的格式，如：DateUtils.DATE_TIME_PATTERN
     */
    public static Date stringToDate(String strDate, String pattern) {
        if (StringUtils.isEmpty(strDate)){
            return null;
        }
        DateTimeFormatter fmt = DateTimeFormat.forPattern(pattern);
        return fmt.parseLocalDateTime(strDate).toDate();
    }

    /**
     *日期格式字符串转换
     */
    public static String convertDatePattern(String strDate, String pattern){
        if (StringUtils.isEmpty(strDate)){
            return null;
        }
        Date stringToDate = stringToDate(strDate, DATE_TIME_PATTERN);
        return format(stringToDate, pattern);
    }

    /**
     * 根据周数，获取开始日期、结束日期
     * @param week  周期  0本周，-1上周，-2上上周，1下周，2下下周
     * @return  返回date[0]开始日期、date[1]结束日期
     */
    public static Date[] getWeekStartAndEnd(int week) {
        DateTime dateTime = new DateTime();
        LocalDate date = new LocalDate(dateTime.plusWeeks(week));

        date = date.dayOfWeek().withMinimumValue();
        Date beginDate = date.toDate();
        Date endDate = date.plusDays(6).toDate();
        return new Date[]{beginDate, endDate};
    }

    /**
     * 对日期的【秒】进行加/减
     *
     * @param date 日期
     * @param seconds 秒数，负数为减
     * @return 加/减几秒后的日期
     */
    public static Date addDateSeconds(Date date, int seconds) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusSeconds(seconds).toDate();
    }

    /**
     * 对日期的【分钟】进行加/减
     *
     * @param date 日期
     * @param minutes 分钟数，负数为减
     * @return 加/减几分钟后的日期
     */
    public static Date addDateMinutes(Date date, int minutes) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMinutes(minutes).toDate();
    }

    /**
     * 对日期的【小时】进行加/减
     *
     * @param date 日期
     * @param hours 小时数，负数为减
     * @return 加/减几小时后的日期
     */
    public static Date addDateHours(Date date, int hours) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusHours(hours).toDate();
    }

    /**
     * 对日期的【天】进行加/减
     *
     * @param date 日期
     * @param days 天数，负数为减
     * @return 加/减几天后的日期
     */
    public static Date addDateDays(Date date, int days) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusDays(days).toDate();
    }

    /**
     * 对日期的【周】进行加/减
     *
     * @param date 日期
     * @param weeks 周数，负数为减
     * @return 加/减几周后的日期
     */
    public static Date addDateWeeks(Date date, int weeks) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusWeeks(weeks).toDate();
    }

    /**
     * 对日期的【月】进行加/减
     *
     * @param date 日期
     * @param months 月数，负数为减
     * @return 加/减几月后的日期
     */
    public static Date addDateMonths(Date date, int months) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusMonths(months).toDate();
    }

    /**
     * 对日期的【年】进行加/减
     *
     * @param date 日期
     * @param years 年数，负数为减
     * @return 加/减几年后的日期
     */
    public static Date addDateYears(Date date, int years) {
        DateTime dateTime = new DateTime(date);
        return dateTime.plusYears(years).toDate();
    }

    /**
     * long转时间格式字符串
     */
    public static String longToDateStr(long time,String pattern){
    	Date stringToDate = stringToDate(String.valueOf(time), "yyyyMMddHHmmss");
    	return format(stringToDate, pattern);
    }

    public static boolean isSameDate(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);

        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
                .get(Calendar.YEAR);
        boolean isSameMonth = isSameYear
                && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
        boolean isSameDate = isSameMonth
                && cal1.get(Calendar.DAY_OF_MONTH) == cal2
                        .get(Calendar.DAY_OF_MONTH);

        return isSameDate;
    }

    public static boolean isSameMonth(Date date1, Date date2) {
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(date1);

    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(date2);

    	boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
    			.get(Calendar.YEAR);
    	boolean isSameMonth = isSameYear
    			&& cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    	return isSameMonth;
    }

    /**
     * 是否相同月，忽略年份
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isSameMonthIngoreYear(Date date1, Date date2) {
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(date1);

    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(date2);

    	boolean isSameMonth = cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH);
    	return isSameMonth;
    }

    public static boolean isSameYear(Date date1, Date date2) {
    	Calendar cal1 = Calendar.getInstance();
    	cal1.setTime(date1);

    	Calendar cal2 = Calendar.getInstance();
    	cal2.setTime(date2);

    	boolean isSameYear = cal1.get(Calendar.YEAR) == cal2
    			.get(Calendar.YEAR);
    	return isSameYear;
    }
    public static Date getUTCE8Date() {
		Date date = null;
		// 1、取得本地时间：
        Calendar cal = Calendar.getInstance();
        // 2、取得时间偏移量：
        int zoneOffset = cal.get(Calendar.ZONE_OFFSET);
        // 3、取得夏令时差：
        int dstOffset = cal.get(Calendar.DST_OFFSET);
        // 4、从本地时间里扣除这些差量，即可以取得UTC时间：
        cal.add(Calendar.MILLISECOND, -(zoneOffset + dstOffset));
        //加8小时变成东8区时间
        cal.set(Calendar.HOUR, cal.get(Calendar.HOUR) + 8);
        date = cal.getTime();

        return date;
	}

    /**
	 * 获取指定日期所在月份开始的时间戳
	 * @param date 指定日期
	 * @return
	 */
	public static Long getMonthBegin(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);

		//设置为1号,当前日期既为本月第一天
		c.set(Calendar.DAY_OF_MONTH, 1);
		//将小时至0
		c.set(Calendar.HOUR_OF_DAY, 0);
		//将分钟至0
		c.set(Calendar.MINUTE, 0);
		//将秒至0
		c.set(Calendar.SECOND,0);
		//将毫秒至0
		c.set(Calendar.MILLISECOND, 0);
		// 获取本月第一天的时间戳
		return c.getTimeInMillis();
	}

	/**
	 * 当前时间转换时区加/减小时（GTM+8/GTM-8）
	 * @param date 当前时区的时间（要处理的时间）
	 * @param timeZone 获取更改时间规则————配置文件中更改该（timeZone）值
	 * @return
	 */
//	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
//	public static Date getGMTTime(Date date, String timeZone) {
//		Date gmt = null;
//		System.out.println("获取时区转换规则：" + timeZone);
//
//		try {
//			Calendar cal = Calendar.getInstance(TimeZone.getTimeZone(timeZone), Locale.CHINESE);
//			Calendar day = Calendar.getInstance();
//			day.setTime(date);//设置要处理的时间
//			day.set(Calendar.YEAR, cal.get(Calendar.YEAR));
//			day.set(Calendar.MONTH, cal.get(Calendar.MONTH));
//			day.set(Calendar.DATE, cal.get(Calendar.DATE));
//			day.set(Calendar.HOUR_OF_DAY, cal.get(Calendar.HOUR_OF_DAY));
//			day.set(Calendar.MINUTE, cal.get(Calendar.MINUTE));
//			day.set(Calendar.SECOND, cal.get(Calendar.SECOND));
//			gmt = day.getTime();
//		} catch (Exception e) {
//			System.out.println("获取" + gmt + "时间 getGMTTime() error !");
//		}
//		return gmt;
//	}

	/**
	 * 当前时间转换时区加/减小时（GTM+8/GTM-8）
	 * @param date  当前时区的时间（要处理的时间）
	 * @param timeZone  获取更改时间规则————配置文件中更改该（timeZone）值
	 * @return
	 */
	public static Date getTimeZone(Date date, Integer timeZone) {
		SimpleDateFormat sf = new SimpleDateFormat(DATE_TIME_PATTERN);
		Calendar ca = Calendar.getInstance();
	    try {
			date = sf.parse(sf.format(date));
		} catch (ParseException e) {
		}
	    ca.setTime(date);
	    ca.add(Calendar.HOUR, timeZone);
	    date = ca.getTime();
		return date;
	}

	/**
	 * 动态获取当前时区
	 * @return
	 */
	public static int getTimeZone1() {
		Calendar ca = Calendar.getInstance();
	    int offset = ca.get(Calendar.ZONE_OFFSET);
	    ca.add(Calendar.MILLISECOND, -offset);
	    Long timeStampUTC = ca.getTimeInMillis();
	    Long timeStamp = System.currentTimeMillis();
	    int timeZone = (int) ((timeStamp - timeStampUTC) / (1000 * 3600));
	    System.out.println("当前时区：UTC" + timeZone);
		return timeZone;
	}

    public static void main(String[] args) throws ParseException {
//		long time = 20181113104558L;
//		Date stringToDate = stringToDate(String.valueOf(time), "yyyyMMddHHmmss");
//		System.out.println(stringToDate);
    	Date date1 = new Date();
    	int timeZone = getTimeZone1();
    	int timeOffset = timeZone * 3600 * 1000;
    	System.out.println("当前时区：UTC" + timeZone);
    	System.out.println("偏移量：" + timeOffset);

    	Date date = getTimeZone(new Date(), 8);
//    	Date date = getGMTTime(new Date(), "GTM+8");
    	System.out.println(date);
	}
}
