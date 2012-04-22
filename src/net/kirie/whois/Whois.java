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

import net.kirie.whois.utils.Utils;

import org.apache.commons.net.whois.*;

public class Whois {
	public static final String DEFAULT_SERVER = "whois.crsnic.net";
	private static WhoisClient whois = new WhoisClient();
	
	public static String whois(String domain) {
		String result = null;
		if(Utils.isIpAddress(domain)) {
			domain = Utils.toHostname(domain);
		}
		String whoisServer = Utils.getWhoisServer(domain);
		if (whoisServer == null) {
			whoisServer = DEFAULT_SERVER;
		}
		
		String charset = Utils.getWhoisCharset(whoisServer);
		
		result = search(domain, whoisServer, charset);

		return result;
	}
	
	private static String search(String domain,
			String whoisServer, String charset) {
		String result = null;
		
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
		
		if (!Utils.isSuccess(result)) {
			domain = Utils.removeHostname(domain);
			result = search(domain,whoisServer,charset);
		}
		
		return result;
	}


}
