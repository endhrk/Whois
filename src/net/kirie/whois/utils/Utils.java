package net.kirie.whois.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	public static boolean isIpAddress(String ipv4) {
		String regex = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(ipv4);

		if (match.find()) {
			return true;
		} else {
			return false;
		}
	}

	static InetAddress toInetAddress(String ipv4) {
		InetAddress result = null;
		if (isIpAddress(ipv4)) {
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
		return Utils.toHostname(Utils.toInetAddress(ipv4));
	}

	static String toHostname(InetAddress inetAddr) {
		if (inetAddr == null) {
			return null;
		}
		return inetAddr.getCanonicalHostName();
	}

	public static String removeHostname(String domain) {
		if (domain == null) {
			return null;
		}
		String result = null;
		int index = domain.indexOf('.');
		result = domain.substring(index + 1);
		return result;
	}

	public static String getWhoisServer(String domain) {
		String result = null;
		HashMap<String, String> serverList = Utils.createServerList();
		int firstIndex = domain.lastIndexOf('.');
		String firstTld = domain.substring(firstIndex);
		int secondIndex = domain.lastIndexOf('.', firstIndex - 1);
		String secondTld;
		if (secondIndex < 0) {
			secondTld = domain;
		} else {
			secondTld = domain.substring(secondIndex);
		}
		if ((result = serverList.get(secondTld)) != null) {
		} else if ((result = serverList.get(firstTld)) != null) {
		}

		return result;
	}

	public static String getWhoisCharset(String whoisServer) {
		HashMap<String, String> charsetList = Utils.createCharsetList();
		return charsetList.get(whoisServer);
	}

	private static HashMap<String, String> createServerList() {
		String filePath = "data/whois-server-list";
		return createWhoisList(filePath);
	}

	private static HashMap<String, String> createCharsetList() {
		String filePath = "data/whois-charset-list";
		return createWhoisList(filePath);
	}

	private static HashMap<String, String> createWhoisList(String filePath) {
		HashMap<String, String> serverList = new HashMap<String, String>();
		File file = new File(filePath);
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String server;
			String[] serverArray;
			while ((server = br.readLine()) != null) {
				serverArray = server.split(" ");
				serverList.put(serverArray[0], serverArray[1]);
			}
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return serverList;
	}

	public static boolean isSuccess(String result) {
		String regex = "No match|no match";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(result);

		if (match.find()) {
			return false;
		} else {
			return true;
		}

	}
}
