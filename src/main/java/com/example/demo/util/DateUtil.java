package com.example.demo.util;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateUtil {

	/**
	 * 曜日を取得する
	 *
	 * @param baseDate 基準日
	 * @return 基準日の曜日
	 */
	public static String getDayOfWeek(LocalDate baseDate) {
		if (baseDate == null) {
			return "";
		}

		DayOfWeek dayOfWeek = baseDate.getDayOfWeek();
		
		return dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.JAPANESE);

	}
}
