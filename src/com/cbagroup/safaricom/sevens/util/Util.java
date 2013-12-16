package com.cbagroup.safaricom.sevens.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Util {
	private static NetworkInfo networkInfo;
	private static final String VALID_EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*(\\.[A-Za-z]{2,})$";
	private static final String  VALID_PHONE_NUMBER_PATTERN="^[0-9]{10}$";

	private static Pattern pattern;
	private static Matcher matcher;

	/**
	 * Kuna internet connection?
	 */
	public static boolean isConnected(Context context) {

		ConnectivityManager connectivity = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);

		networkInfo = connectivity.getActiveNetworkInfo();

		if (networkInfo == null || !networkInfo.isConnected()) {
			return false;
		}
		return true;

	}

	/**
	 * Validates an email address Credits:
	 * 
	 * @param String
	 *            - email address to be validated
	 * @return boolean
	 */
	public static boolean validateEmail(String emailAddress) {
		if (!emailAddress.equals("")) {
			pattern = Pattern.compile(VALID_EMAIL_PATTERN);
			matcher = pattern.matcher(emailAddress);
			return matcher.matches();
		}
		return true;
	}
	
	/**
	 * Since the SMS gateway requires +254 prefix on every number,lets substring
	 * the initial 0 from mobile number and add +254 to the result
	 * @param s
	 * @return
	 */
	
	public static String CorrectPhoneNumber (String s){
		String phonenumber = "+254"+s.substring(1);
		   return phonenumber;
		}
	
	
	/**
	 * Checks if the Phone Number provided is an Actual Kenyan Phone Number
	 * @param phone
	 * @return Boolean
	 */
	public static boolean validatePhoneNumber(String phone) {
		if (!phone.equals("")) {
			pattern = Pattern.compile(VALID_PHONE_NUMBER_PATTERN);
			matcher = pattern.matcher(phone);
			return matcher.matches();
		}
		return true;
	}

}
