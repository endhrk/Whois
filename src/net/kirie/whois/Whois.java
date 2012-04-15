package net.kirie.whois;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.net.whois.*;

public class Whois {
	public static final String DEFAULT_SERVER = "";
	public static final HashMap<String, String> serverList = Whois.createServerList();
	public static final HashMap<String, String> charsetList = Whois.createCharsetList();

	public static String whois(String domain) {
		String result = null;
		WhoisClient whois = new WhoisClient();

		String query = makeQuery(domain);
		String tld = getTopLevelDomain(query);
		String whoisServer = serverList.get(tld);
		if (whoisServer == null) {
			whoisServer = DEFAULT_SERVER;
		}
		String charset = charsetList.get(whoisServer);

		try {
			whois.connect(whoisServer);
			if (charset != null) {
				BufferedReader stream = new BufferedReader(
						new InputStreamReader(whois.getInputStream(query),
								charset));
				String line;
				result = "";
				while ((line = stream.readLine()) != null) {
					result += line + "\n";
				}
			} else {
				result = whois.query(query);
			}
		} catch (SocketException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return result;
	}

	private static String makeQuery(String input) {
		String result = null;
		InetAddress address = toAddress(input);
		if (address != null) {
			result = toDomain(address);
		} else {
			result = toDomain(input);
		}
		return result;
	}
	
	private static InetAddress toAddress(String ipv4) {
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
	
	private static String toDomain(String input) {
		String result = null;
		String tld = getTopLevelDomain(input);
		int index = input.lastIndexOf('.',input.length() - tld.length() - 1);
		result = input.substring(index+1);
		return result;
	}

	private static String toDomain(InetAddress inetAddr) {
		String hostname = inetAddr.getCanonicalHostName();
		return toDomain(hostname);
	}

	private static String getTopLevelDomain(String domain) {
		String result = null;
		int fromIndex = domain.length();
		int firstIndex = domain.lastIndexOf('.');
		String firstTld = domain.substring(firstIndex);
		int secondIndex = domain.lastIndexOf('.', firstIndex -1);
		String secondTld;
		if (secondIndex < 0) {
			secondTld = domain;
		} else {
			secondTld = domain.substring(secondIndex);
		}
		if (serverList.get(secondTld) != null) {
			result = secondTld;
		} else if (serverList.get(firstTld) != null) {
			result = firstTld;
		}

		return result;
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

}
