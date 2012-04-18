package net.kirie.whois.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static InetAddress toAddress(String ipv4) {
		InetAddress result = null;
		String regex = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(ipv4);

		if (match.find()) {
			try {
				String[] splitAddr = ipv4.split("\\.");
				byte[] address = new byte[4];
				for (int i = 0; i < splitAddr.length; i++) {
					address[i] = Integer.valueOf(splitAddr[i]).byteValue();
				}
				result = InetAddress.getByAddress(address);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public static String toHostname(String ipv4) {
		return Utils.toHostname(Utils.toAddress(ipv4));
	}

	public static String toHostname(InetAddress inetAddr) {
		if (inetAddr == null) {
			return null;
		}
		return inetAddr.getCanonicalHostName();
	}
	
	public static String removeHostname(String domain) {
		String result = null;
		int index = domain.indexOf('.');
		result = domain.substring(index+1);
		return result;
	}

}
