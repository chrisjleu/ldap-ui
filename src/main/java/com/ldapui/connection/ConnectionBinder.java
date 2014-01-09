/**
 * 
 */
package com.ldapui.connection;

/*******************************************************************************
 * $Novell: CheckBind.java,v 1.8 2003/08/21 11:29:57 $
 * Copyright (c) 2001 Novell, Inc. All Rights Reserved.
 *
 * THIS WORK IS SUBJECT TO U.S. AND INTERNATIONAL COPYRIGHT LAWS AND
 * TREATIES. USE AND REDISTRIBUTION OF THIS WORK IS SUBJECT TO THE LICENSE
 * AGREEMENT ACCOMPANYING THE SOFTWARE DEVELOPMENT KIT (SDK) THAT CONTAINS
 * THIS WORK. PURSUANT TO THE SDK LICENSE AGREEMENT, NOVELL HEREBY GRANTS TO
 * DEVELOPER A ROYALTY-FREE, NON-EXCLUSIVE LICENSE TO INCLUDE NOVELL'S SAMPLE
 * CODE IN ITS PRODUCT. NOVELL GRANTS DEVELOPER WORLDWIDE DISTRIBUTION RIGHTS
 * TO MARKET, DISTRIBUTE, OR SELL NOVELL'S SAMPLE CODE AS A COMPONENT OF
 * DEVELOPER'S PRODUCTS. NOVELL SHALL HAVE NO OBLIGATIONS TO DEVELOPER OR
 * DEVELOPER'S CUSTOMERS WITH RESPECT TO THIS CODE.
 *
 * $name:         CheckBind.java
 * $description:  Sometimes a user can not bind to a directory although he
 *                is using a correct password. This is because that there
 *                are a number of bind restrictions to prevent the user from
 *                binding to the directory.
 *
 *                CheckBind.java verifies user's pass word and checks user's
 *                'LoginDisabled', 'loginExpirationTime', 'passwordExpira-
 *                tionTime', 'loginAllowedTimeMap', and 'lockedByIntruder'
 *                attributes to determine if the user can bind to the directory
 *                at the current time.
 *
 *                CheckBind.java does not check 'networkAddressRestriction' and
 *                'loginMaximumSimultaneous' attributes. The settings of
 *                those attributes can still prevent the user from binding
 *                to the directory.
 *
 *                In order to access all those attributes, the login dn should
 *                have admin or admin equivalent rights.
 ******************************************************************************/
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.TimeZone;

import javax.inject.Inject;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.ldap.core.LdapTemplate;
import org.springframework.security.authentication.encoding.LdapShaPasswordEncoder;
import org.springframework.stereotype.Component;

import com.novell.ldap.LDAPAttribute;
import com.novell.ldap.LDAPAttributeSet;
import com.novell.ldap.LDAPConnection;
import com.novell.ldap.LDAPEntry;
import com.novell.ldap.LDAPException;
import com.novell.ldap.connectionpool.PoolManager;

/**
 * 
 * @author cleung
 * @deprecated Not required if using Spring's {@link LdapTemplate}.See {@link PersonDao} instead.
 */
//@Component
public class ConnectionBinder {

	public static final int LENGTH = 42;
	public static final int BITS = 336;
	public static final int BITSOFFSET = 14;
	public static Date date = new Date();
	public static String locale = date.toString();

	private static final String USER_DN_PREFIX = "cn=";
	private static final String USER_DN_SUFFIX = ",ou=Users,o=YourOrg";

	@Inject
	LdapShaPasswordEncoder encoder;

	@Inject
	PoolManager poolManager;

	@Value("${loginDN}")
	String loginDN;
	
	@Value("${password}")
	String password;
	
	public boolean canUserBind(String userDN, String userPassword) throws UnsupportedEncodingException,
			LDAPException, InterruptedException {

		LDAPConnection lc = poolManager.getBoundConnection(loginDN, password.getBytes("UTF8"));

		String completeUserDN = USER_DN_PREFIX + userDN + USER_DN_SUFFIX;
		String password = hashPassword(userPassword, null);

		System.out.println(lc.getAuthenticationDN());

		boolean canBind = canBind(lc, completeUserDN, password);

		// Put the connection back in the pool
		poolManager.makeConnectionAvailable(lc);

		return canBind;
	}

	private String hashPassword(String password, String salt) {
		// LdapShaPasswordEncoder encoder = new LdapShaPasswordEncoder();
		encoder.setForceLowerCasePrefix(true);

		return encoder.encodePassword(password, salt);
	}

	// canBind() checks bind restrictions and returns 'true' if
	// there is no any bind restriction. Otherwise 'false' is returned.
	public static boolean canBind(LDAPConnection lc, String userDN, String userPWD) {
		int i;
		byte timeMap[] = new byte[0];
		boolean checkResult = true, res;
		String login = null, loginExpTime = null, pwdExpTime = null;
		String locked = null, attrName;
		String value;
		LDAPAttribute attribute;
		// return those attributes only
		String returnAttrs[] = { "LoginDisabled", "loginExpirationTime", "passwordExpirationTime",
				"loginAllowedTimeMap", "lockedByIntruder" };

		try {
			// check user's password
			System.out.print("        user's password: ");
			attribute = new LDAPAttribute("userPassword", userPWD);
			if (lc.compare(userDN, attribute))
				System.out.println("password is correct");
			else {
				System.out.println(attribute + " is not the password");
				checkResult = false;
			}

			// read the entry to get attributes
			LDAPEntry entry = lc.read(userDN, returnAttrs);
			LDAPAttributeSet attributeSet = entry.getAttributeSet();
			Iterator allAttributes = attributeSet.iterator();

			// save the attribute values
			while (allAttributes.hasNext()) {
				attribute = (LDAPAttribute) allAttributes.next();
				attrName = attribute.getName();

				if (attrName.equalsIgnoreCase("loginAllowedTimeMap")) {
					timeMap = attribute.getByteValueArray()[0];
				} else if (attrName.equalsIgnoreCase("LoginDisabled")) {
					if ((value = attribute.getStringValue()) != null)
						login = value;
				} else if (attrName.equalsIgnoreCase("loginExpirationTime")) {
					if ((value = attribute.getStringValue()) != null)
						loginExpTime = value;
				} else if (attrName.equalsIgnoreCase("passwordExpirationTime")) {
					if ((value = attribute.getStringValue()) != null)
						pwdExpTime = value;
				} else if (attrName.equalsIgnoreCase("lockedByIntruder")) {
					if ((value = attribute.getStringValue()) != null)
						locked = value;
				}
			}

			// check 'Logindisabled'
			System.out.print("        Logindisabled: ");
			if ((login != null) && (login.length() != 0)) {
				if (login.equalsIgnoreCase("FALSE"))
					System.out.println(login + " (enabled)");
				else {
					System.out.println(login + " (disabled)");
					checkResult = false;
				}
			} else
				System.out.println("not present (no logindisabled set)");

			// check 'LoginExpirationTime'
			System.out.print("        loginExpirationTime: ");
			if ((loginExpTime != null) && (loginExpTime.length() != 0)) {
				PrintLocalTime(loginExpTime);
				res = CompareTime(loginExpTime);
				if (res)
					System.out.println("");
				else {
					System.out.println(" (login expired)");
					checkResult = false;
				}
			} else
				System.out.println("not present (no expiration set)");

			// check 'passwordExpirationTime'
			System.out.print("        passwordExpirationTime: ");
			if ((pwdExpTime != null) && (pwdExpTime.length() != 0)) {
				PrintLocalTime(pwdExpTime);
				res = CompareTime(pwdExpTime);
				if (res)
					System.out.println("");
				else {
					System.out.println(" (password expired)");
					checkResult = false;
				}
			} else
				System.out.println("not present (no expiration set)");

			// check 'LoginAllowedTimeMap'
			System.out.print("        LoginAllowedTimeMap: ");
			if ((timeMap != null) && (timeMap.length != 0) && (timeMap.length == LENGTH)) {
				res = getTimeRestriction(timeMap, locale);
				if (res) {
					System.out.println(res + " (login time is restricted)");
					checkResult = false;
				} else {
					System.out.println(res + " (no restriction)");
				}
			} else
				System.out.println("not present (no time restriction set)");

			// Check 'lockedByIntruder'
			System.out.print("        lockedByIntruder: ");
			if ((locked != null) && (locked.length() != 0)) {
				if (locked.equalsIgnoreCase("FALSE"))
					System.out.println(locked + " (not locked)");
				else {
					System.out.println(locked + " (locked)");
					checkResult = false;
				}
			} else
				System.out.println("not present (no lock set)");
		} catch (LDAPException e) {
			System.out.println("\n    LDAPException: " + e.toString());
			checkResult = false;
		}

		return checkResult;
	}

	// PrintLocalTime() turns UTCTime into local
	// time and then prints it out in text format
	public static void PrintLocalTime(String UTCTime) {
		Date date = null;
		// setup x.208 generalized time formatter
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		// LDAP time in UTC - must set formatter
		TimeZone tz = TimeZone.getTimeZone("UTC");
		formatter.setTimeZone(tz);

		try {
			// parse into Date - converted to locale time zone
			date = formatter.parse(UTCTime);
		} catch (ParseException pe) {
			System.out.println("\n    Error: " + pe.toString());
		}
		System.out.print(date);
	}

	// CompareTime() parses UTCTime into locale Date, and then
	// compare it with syetem Date. It returns true if UTCTime
	// is after system time. Otherwise false is returned.
	public static boolean CompareTime(String UTCTime) {
		Date date = null, currentDate = new Date();
		// setup x.208 generalized time formatter
		DateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss'Z'");
		// all time in UTC - must set formatter
		TimeZone tz = TimeZone.getTimeZone("UTC");
		formatter.setTimeZone(tz);

		try {
			// parse into Date - converted to locale time zone
			date = formatter.parse(UTCTime);
		} catch (ParseException pe) {
			System.out.println("\n   Error: " + pe.toString());
		}

		return date.after(currentDate);
	}

	// getTimeRestriction() returns 'true' if there is a time restriction
	// for the current 30-minute period. Otherwise 'false' is returned
	public static boolean getTimeRestriction(byte[] tm, String localTime) {
		int i, temp, index = 0;
		boolean flags[] = new boolean[BITS];
		boolean temp1[] = new boolean[BITSOFFSET];

		// tm has 336 bits(42 bytes). The following operations must performed
		// in order to interpret loginAllowedTimeMap value:
		// 1. swap the bit order in all the bytes;
		// 2. shift the 336 bits left 14 times;
		// 3. replace the vacated bits at right
		// with the 14 bits shifted off the left;
		// Then the 336 bits represent 336 thirty-minute time intervals in
		// a week, starting from the period of Sun 12:00AM-12:30AM.
		// Bit value '1' indicates time restriction while bit value '0'
		// means no restriction.

		// reverse the bits order and set flags
		for (i = 0; i < LENGTH; i++) {
			if ((tm[i] & 0x01) != 0)
				flags[i * 8 + 0] = true;
			if ((tm[i] & 0x02) != 0)
				flags[i * 8 + 1] = true;
			if ((tm[i] & 0x04) != 0)
				flags[i * 8 + 2] = true;
			if ((tm[i] & 0x08) != 0)
				flags[i * 8 + 3] = true;
			if ((tm[i] & 0x10) != 0)
				flags[i * 8 + 4] = true;
			if ((tm[i] & 0x20) != 0)
				flags[i * 8 + 5] = true;
			if ((tm[i] & 0x40) != 0)
				flags[i * 8 + 6] = true;
			if ((tm[i] & 0x80) != 0)
				flags[i * 8 + 7] = true;
		}
		// shift the first 14 elements to the end of flags
		for (i = 0; i < BITSOFFSET; i++)
			temp1[i] = flags[i];
		for (i = BITSOFFSET; i < BITS; i++)
			flags[i - BITSOFFSET] = flags[i];
		for (i = 0; i < BITSOFFSET; i++)
			flags[BITS - BITSOFFSET + i] = temp1[i];

		// get day, hour, and minute from localTime which is
		// in the format of
		// "Tue Jul 24 12:34:56 MDT 2001"
		String weekDay = localTime.substring(0, 3);
		String clock = localTime.substring(localTime.indexOf((int) ':') - 2, localTime.indexOf((int) ':') + 3);
		int hour = Integer.parseInt(clock.substring(0, 2));
		int minute = Integer.parseInt(clock.substring(3));

		// calculate index
		int clockIndex = hour * 2 + minute / 30;
		if (weekDay.equalsIgnoreCase("Sun"))
			index = 0 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Mon"))
			index = 1 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Tue"))
			index = 2 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Wed"))
			index = 3 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Thu"))
			index = 4 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Fri"))
			index = 5 * 48 + clockIndex;
		else if (weekDay.equalsIgnoreCase("Sat"))
			index = 6 * 48 + clockIndex;

		return !flags[index];
	}
}
