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

	public static String whois(String domain) {
		String result = null;
		WhoisClient whois = new WhoisClient();
		HashMap<String, String> serverList = Whois.createServerList();
		HashMap<String, String> charsetList = Whois.createCharsetList();

		domain = getDomain(domain);
		String whoisServer = null;
		for (int depth=2; depth > 0;depth--){ 
			String toplevel = getTopLevelDomain(domain,depth);
			whoisServer = serverList.get(toplevel);
			if (whoisServer != null) {
				break;
			}
		}
		if (whoisServer == null) {
			whoisServer = DEFAULT_SERVER;
		}
		String charset = charsetList.get(whoisServer);

		try {
			whois.connect(whoisServer);
			if (charset != null) {
				BufferedReader stream = new BufferedReader(
						new InputStreamReader(whois.getInputStream(domain),
								charset));
				String line;
				result = "";
				while ((line = stream.readLine()) != null) {
					result += line + "\n";
				}
			} else {
				result = whois.query(domain);
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

	private static String getDomain(String input) {
		String result = null;
		String regex = "^\\d+\\.\\d+\\.\\d+\\.\\d+$";
		Pattern pattern = Pattern.compile(regex);
		Matcher match = pattern.matcher(input);

		if (match.find()) {
			try {
				String[] splitAddr = input.split("\\.");
				byte[] address = new byte[4];
				for (int i = 0; i < splitAddr.length; i++) {
					address[i] = Integer.valueOf(splitAddr[i]).byteValue();
				}
				InetAddress inetAddr = InetAddress.getByAddress(address);
				result = getDomain(inetAddr);
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
		} else {
			try {
				InetAddress inetAddr = InetAddress.getByName(input);
				result = getDomain(inetAddr);
			} catch (UnknownHostException e) {
				result = input;
			}

		}

		return result;
	}

	private static String getDomain(InetAddress inetAddr) {
		String hostname = inetAddr.getCanonicalHostName();
		return hostname.substring(hostname.indexOf('.') + 1);
	}

	private static String getTopLevelDomain(String domain, int depth) {
		int string_index = domain.length();
		for(int i=0;i<depth;i++) {
			string_index = domain.lastIndexOf('.',string_index);
		}
		return domain.substring(domain.lastIndexOf('.') + 1);
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
