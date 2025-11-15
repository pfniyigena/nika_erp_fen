package com.niwe.erp.common.util;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

public abstract class DataParserUtil {
	private DataParserUtil() {
		throw new IllegalStateException("Utility class SdcUtil");
	}

	public static Instant instantFromDateString(String inputDate) {
		if (inputDate == null || inputDate.isEmpty())
			return Instant.now();
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss").withZone(ZoneId.of("UTC")); // Or
																														// your
																														// timezone

		return Instant.from(formatter.parse(inputDate));
	}

}
