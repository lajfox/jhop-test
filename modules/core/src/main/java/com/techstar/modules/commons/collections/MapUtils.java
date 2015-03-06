package com.techstar.modules.commons.collections;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

import com.techstar.modules.commons.lang3.time.DateUtils;

public final class MapUtils extends org.apache.commons.collections.MapUtils {

	public static Date getDate(final Map<String, ?> map, final String key) {

		Object obj = getObject(map, key);
		if (obj == null) {
			return null;
		} else {
			if (Date.class.isAssignableFrom(obj.getClass())) {
				return (Date) obj;
			} else if (String.class.isAssignableFrom(obj.getClass())) {
				return DateUtils.parseDate(obj.toString());
			} else if (long.class.isAssignableFrom(obj.getClass()) || Long.class.isAssignableFrom(obj.getClass())) {
				Calendar cal = Calendar.getInstance();
				cal.setTimeInMillis((Long) obj);
				return cal.getTime();
			} else {
				throw new IllegalArgumentException("不支持的类型转换：" + obj);
			}
		}
	}
}
