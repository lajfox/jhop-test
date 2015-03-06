package com.techstar.modules.commons.lang3.time;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;

public final class DateUtils extends org.apache.commons.lang3.time.DateUtils {

	public static final String PARSEPATTERNS[] = new String[] { "yyyy", "yyyy-MM", "yyyy-MM-dd", "yyyy-MM-dd HH",
			"yyyy-MM-dd HH:mm", "yyyy-MM-dd HH:mm:ss", "yyyy/MM", "yyyy/MM/dd", "yyyy/MM/dd HH", "yyyy/MM/dd HH:mm",
			"yyyy/MM/dd HH:mm:ss","HH:mm","HH:mm:ss" };

	private static final Pattern DATERANGEPARRERN = Pattern
			.compile("([\\d]{4}[/|-]+[\\d]{1,2}[/|-]+[\\d]{1,2})(\\s*-\\s*)([\\d]{4}[/|-]+[\\d]{1,2}[/|-]+[\\d]{1,2})");

	/**
	 * 
	 * @param value
	 *            日期字符串
	 * @return java.util.Date
	 */
	public static Date parseDate(String value) {
		return StringUtils.isEmpty(value) ? null : parseDate(value, true);
	}

	public static Date parseDate(String value, String parsepattern) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		try {
			return parseDate(value, new String[] { parsepattern });
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * 
	 * @param value
	 *            日期字符串
	 * @param isthrow
	 *            　日期转化错误时是否抛出异常 true :抛出,false :返回null
	 * @return java.util.Date
	 */
	public static Date parseDate(String value, boolean isthrow) {
		if (StringUtils.isEmpty(value)) {
			return null;
		}

		try {
			return parseDate(value, PARSEPATTERNS);
		} catch (ParseException e) {
			if (isthrow) {
				throw new IllegalArgumentException(e);
			} else {
				return null;
			}
		}
	}

	/**
	 * 将dateRange范围日期(2013/01/12-2013/03/23)转为Date
	 * 
	 * @param dateRange
	 *            如：2013/01/12-2013/03/23
	 * @return
	 */
	public static Date[] getDateRanges(final String dateRange) {
		Date[] dateRanges = null;
		Matcher m = DATERANGEPARRERN.matcher(dateRange);
		if (m.find()) {
			dateRanges = new Date[2];
			dateRanges[0] = parseDate(m.group(1));
			dateRanges[1] = parseDate(m.group(3));
		}
		return dateRanges;
	}

	/**
	 * 判断是dateRange是否范围日期，如2013/01/12-2013/03/23
	 * 
	 * @param dateRange
	 *            如：2013/01/12-2013/03/23
	 * @return
	 */
	public static boolean isDateRange(final String dateRange) {
		if (StringUtils.isEmpty(dateRange)) {
			return false;
		} else {
			Matcher m = DATERANGEPARRERN.matcher(dateRange);
			return m.find();
		}
	}

	/**
	 * 根据日期取得对应周周几日期
	 * 
	 * @param cal
	 * @param value
	 *            星期几
	 * @return
	 */
	public static Date getDateOfWeek(final Calendar cal, final int value) {
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		cal.set(Calendar.DAY_OF_WEEK, value);
		return cal.getTime();
	}

	/**
	 * 根据日期取得对应周周几日期
	 * 
	 * @param date
	 * @param value
	 *            星期几
	 * @return
	 */
	public static Date getDateOfWeek(final Date date, final int value) {		
		return getDateOfWeek(toCalendar(date), value);
	}

	/**
	 * 根据日期取得对应周周一日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(final Calendar cal) {
		return getDateOfWeek(cal, Calendar.MONDAY);
	}

	/**
	 * 根据日期取得对应周周一日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getMondayOfWeek(Date date) {	
		return getMondayOfWeek(toCalendar(date));
	}

	/**
	 * 根据日期取得对应周周日日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(Calendar cal) {
		return getDateOfWeek(cal, Calendar.SUNDAY);
	}

	/**
	 * 根据日期取得对应周周日日期
	 * 
	 * @param date
	 * @return
	 */
	public static Date getSundayOfWeek(Date date) {		
		return getSundayOfWeek(toCalendar(date));
	}

	/**
	 * 根据日期取得对应季度： 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param cal
	 * @return
	 */
	public static int getSeason(final Calendar cal) {

		int season = 0;

		int month = cal.get(Calendar.MONTH);
		switch (month) {
		case Calendar.JANUARY:
		case Calendar.FEBRUARY:
		case Calendar.MARCH:
			season = 1;
			break;
		case Calendar.APRIL:
		case Calendar.MAY:
		case Calendar.JUNE:
			season = 2;
			break;
		case Calendar.JULY:
		case Calendar.AUGUST:
		case Calendar.SEPTEMBER:
			season = 3;
			break;
		case Calendar.OCTOBER:
		case Calendar.NOVEMBER:
		case Calendar.DECEMBER:
			season = 4;
			break;
		default:
			break;
		}
		return season;
	}

	/**
	 * 根据日期取得对应季度： 1 第一季度 2 第二季度 3 第三季度 4 第四季度
	 * 
	 * @param date
	 * @return
	 */
	public static int getSeason(Date date) {
		return getSeason(toCalendar(date));
	}

	/**
	 * 取得月第一天
	 * 
	 * @param cal
	 * @return
	 */
	public static Date getFirstDateOfMonth(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMinimum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * 取得月第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfMonth(Date date) {		
		return getFirstDateOfMonth(toCalendar(date));
	}

	/**
	 * 取得月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Calendar cal) {
		cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH));
		return cal.getTime();
	}

	/**
	 * 取得月最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfMonth(Date date) {		
		return getLastDateOfMonth(toCalendar(date));
	}

	/**
	 * 取得一年的第几周
	 * 
	 * @param cal
	 * @return
	 */
	public static int getWeekOfYear(Calendar cal) {
		cal.setFirstDayOfWeek(Calendar.MONDAY);
		int week_of_year = cal.get(Calendar.WEEK_OF_YEAR);
		return week_of_year;
	}

	/**
	 * 取得一年的第几周
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekOfYear(Date date) {		
		return getWeekOfYear(toCalendar(date));
	}

	/**
	 * 根据日期取得对应月份包含的周数
	 * 
	 * @param cal
	 * @return
	 */
	public static int[] getWeeksOfMonth(Calendar cal) {

		cal.set(Calendar.DATE, 1);

		int start = getWeekOfYear(cal);
		int weeks[] = new int[] { start };

		cal.add(Calendar.MONTH, 1);
		cal.add(Calendar.DATE, -1);

		int end = getWeekOfYear(cal);

		for (int i = start + 1; i <= end; i++) {
			weeks = ArrayUtils.add(weeks, i);
		}

		return weeks;
	}

	/**
	 * 根据日期取得对应月份包含的周数
	 * 
	 * @param date
	 * @return
	 */
	public static int[] getWeeksOfMonth(Date date) {
		return getWeeksOfMonth(toCalendar(date));
	}

	/**
	 * 取得月的剩余天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfMonth(Calendar cal) {
		int dayOfMonth = getDayOfMonth(cal);
		int day = getPassDayOfMonth(cal);
		return dayOfMonth - day;
	}

	/**
	 * 取得月的剩余天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfMonth(Date date) {
		int dayOfMonth = getDayOfMonth(date);
		int day = getPassDayOfMonth(date);
		return dayOfMonth - day;
	}

	/**
	 * 取得月已经过的天数
	 * 
	 * @param cal
	 * @return
	 */
	public static int getPassDayOfMonth(Calendar cal) {
		return cal.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月已经过的天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getPassDayOfMonth(Date date) {
		return getPassDayOfMonth(toCalendar(date));
	}

	/**
	 * 取得月天数
	 * 
	 * @param cal
	 * @return
	 */
	public static int getDayOfMonth(Calendar cal) {
		return cal.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 取得月天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfMonth(Date date) {
		return getDayOfMonth(toCalendar(date));
	}

	/**
	 * 取得季度第一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getFirstDateOfSeason(Date date) {
		return getFirstDateOfMonth(getSeasonDate(date)[0]);
	}

	/**
	 * 取得季度最后一天
	 * 
	 * @param date
	 * @return
	 */
	public static Date getLastDateOfSeason(Date date) {
		return getLastDateOfMonth(getSeasonDate(date)[2]);
	}

	/**
	 * 取得季度天数
	 * 
	 * @param cal
	 * @return
	 */
	public static int getDayOfSeason(Calendar cal) {
		int day = 0;
		Date[] seasonDates = getSeasonDate(cal);
		for (Date date2 : seasonDates) {
			day += getDayOfMonth(date2);
		}
		return day;
	}

	/**
	 * 取得季度天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getDayOfSeason(Date date) {
		int day = 0;
		Date[] seasonDates = getSeasonDate(date);
		for (Date date2 : seasonDates) {
			day += getDayOfMonth(date2);
		}
		return day;
	}

	/**
	 * 取得季度剩余天数
	 * 
	 * @param cal
	 * @return
	 */
	public static int getRemainDayOfSeason(Calendar cal) {
		return getDayOfSeason(cal) - getPassDayOfSeason(cal);
	}

	/**
	 * 取得季度剩余天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getRemainDayOfSeason(Date date) {
		return getDayOfSeason(date) - getPassDayOfSeason(date);
	}

	/**
	 * 取得季度已过天数
	 * 
	 * @param cal
	 * @return
	 */
	public static int getPassDayOfSeason(Calendar cal) {
		int day = 0;

		Date[] seasonDates = getSeasonDate(cal);
		int month = cal.get(Calendar.MONTH);

		if (month == Calendar.JANUARY || month == Calendar.APRIL || month == Calendar.JULY || month == Calendar.OCTOBER) {// 季度第一个月
			day = getPassDayOfMonth(seasonDates[0]);
		} else if (month == Calendar.FEBRUARY || month == Calendar.MAY || month == Calendar.AUGUST
				|| month == Calendar.NOVEMBER) {// 季度第二个月
			day = getDayOfMonth(seasonDates[0]) + getPassDayOfMonth(seasonDates[1]);
		} else if (month == Calendar.MARCH || month == Calendar.JUNE || month == Calendar.SEPTEMBER
				|| month == Calendar.DECEMBER) {// 季度第三个月
			day = getDayOfMonth(seasonDates[0]) + getDayOfMonth(seasonDates[1]) + getPassDayOfMonth(seasonDates[2]);
		}
		return day;
	}

	/**
	 * 取得季度已过天数
	 * 
	 * @param date
	 * @return
	 */
	public static int getPassDayOfSeason(Date date) {
		return getPassDayOfSeason(toCalendar(date));
	}

	/**
	 * 取得季度月
	 * 
	 * @param cal
	 * @return
	 */
	public static Date[] getSeasonDate(Calendar cal) {
		Date[] season = new Date[3];

		int nSeason = getSeason(cal);
		if (nSeason == 1) {// 第一季度
			cal.set(Calendar.MONTH, Calendar.JANUARY);
			season[0] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.FEBRUARY);
			season[1] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.MARCH);
			season[2] = cal.getTime();
		} else if (nSeason == 2) {// 第二季度
			cal.set(Calendar.MONTH, Calendar.APRIL);
			season[0] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.MAY);
			season[1] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.JUNE);
			season[2] = cal.getTime();
		} else if (nSeason == 3) {// 第三季度
			cal.set(Calendar.MONTH, Calendar.JULY);
			season[0] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.AUGUST);
			season[1] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.SEPTEMBER);
			season[2] = cal.getTime();
		} else if (nSeason == 4) {// 第四季度
			cal.set(Calendar.MONTH, Calendar.OCTOBER);
			season[0] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.NOVEMBER);
			season[1] = cal.getTime();
			cal.set(Calendar.MONTH, Calendar.DECEMBER);
			season[2] = cal.getTime();
		}
		return season;
	}

	/**
	 * 取得季度月
	 * 
	 * @param date
	 * @return
	 */
	public static Date[] getSeasonDate(Date date) {
		return getSeasonDate(toCalendar(date));

	}

	/**
	 * 取得当天日期是周几
	 * 
	 * @param cal
	 * @return
	 */
	public static int getWeekDay(Calendar cal) {
		int week_of_year = cal.get(Calendar.DAY_OF_WEEK);
		return week_of_year - 1;
	}

	/**
	 * 取得当天日期是周几
	 * 
	 * @param date
	 * @return
	 */
	public static int getWeekDay(Date date) {
		return getWeekDay(toCalendar(date));
	}

	/**
	 * 取得日期：年
	 * 
	 * @param cal
	 * @return
	 */
	public static int getYear(Calendar cal) {
		int year = cal.get(Calendar.YEAR);
		return year;
	}

	/**
	 * 取得日期：年
	 * 
	 * @param date
	 * @return
	 */
	public static int getYear(Date date) {
		return getYear(toCalendar(date));
	}

	/**
	 * 取得日期：月
	 * 
	 * @param cal
	 * @return
	 */
	public static int getMonth(Calendar cal) {
		int month = cal.get(Calendar.MONTH);
		return month + 1;
	}

	/**
	 * 取得日期：月
	 * 
	 * @param date
	 * @return
	 */
	public static int getMonth(Date date) {
		return getMonth(toCalendar(date));
	}

	/**
	 * 取得日期：日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Calendar cal) {
		int da = cal.get(Calendar.DAY_OF_MONTH);
		return da;
	}

	/**
	 * 取得日期：日
	 * 
	 * @param date
	 * @return
	 */
	public static int getDay(Date date) {
		return getDay(toCalendar(date));
	}

	/**
	 * 取得日期：小时
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(Calendar cal) {
		int da = cal.get(Calendar.HOUR_OF_DAY);
		return da;
	}

	/**
	 * 取得日期：小时
	 * 
	 * @param date
	 * @return
	 */
	public static int getHour(Date date) {
		return getHour(toCalendar(date));
	}

	/**
	 * 取得日期：分
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinute(Calendar cal) {
		int da = cal.get(Calendar.MINUTE);
		return da;
	}

	/**
	 * 取得日期：分
	 * 
	 * @param date
	 * @return
	 */
	public static int getMinute(Date date) {
		return getMinute(toCalendar(date));
	}

	/**
	 * 取得日期：秒
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecond(Calendar cal) {
		int da = cal.get(Calendar.SECOND);
		return da;
	}

	/**
	 * 取得日期：秒
	 * 
	 * @param date
	 * @return
	 */
	public static int getSecond(Date date) {
		return getSecond(toCalendar(date));
	}

}
