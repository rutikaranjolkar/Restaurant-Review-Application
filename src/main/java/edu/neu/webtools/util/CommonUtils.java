package edu.neu.webtools.util;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class CommonUtils {
	private static String salt = "WebToolsIsAwesome832212";

	public static String md5Hash(String input) {
		String md5 = null;
		if (null == input)
			return null;
		try {
			String saltedString = input + salt;
			MessageDigest digest = MessageDigest.getInstance("MD5");
			digest.update(saltedString.getBytes(), 0, saltedString.length());
			md5 = new BigInteger(1, digest.digest()).toString(16);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			return null;
		}
		return md5;
	}

	public static String randomString() {
		SecureRandom random = new SecureRandom();
		BigInteger number = new BigInteger(130, random);
		return number.toString();
	}
}
