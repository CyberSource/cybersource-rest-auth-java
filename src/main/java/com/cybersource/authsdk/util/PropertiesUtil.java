package com.cybersource.authsdk.util;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;

public final class PropertiesUtil {
	public static String date;

	private PropertiesUtil() {

	}
	
	public static String getNewDate() {
		PropertiesUtil.date = DateTimeFormatter.RFC_1123_DATE_TIME.format(ZonedDateTime.now(ZoneId.of("GMT")));
		return PropertiesUtil.date;
	}
}
