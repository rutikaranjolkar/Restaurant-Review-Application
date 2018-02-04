package edu.neu.webtools.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailUtil {
	private static Pattern pattern;
	private static Matcher matcher;

	private static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	static {
		pattern = Pattern.compile(VALID_EMAIL_PATTERN);
	}

	public static boolean validate(String email) {
		matcher = pattern.matcher(email);
		return matcher.matches();
	}
}