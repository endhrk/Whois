package net.kirie.whois;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class WhoisData {
	private String domainName;
	private String organization;
	private String org_phonetic;
	private String org_alternative;
	private String[] nameServer;
	private String careteDate;
	private String lastUpdate;
	private String status;
	private String expirationDate;
	
	WhoisData() {
		
	}
	
	public static WhoisData createWhoisData(String whoisResult) {
		WhoisData data = new WhoisData();
		String regex[] = {"組織名", "Organization"};

		String[] resultArray = whoisResult.split("\n");
		for(int i=0;i<resultArray.length;i++) {
			for (int j=0;i<regex.length;i++) {
				Pattern pattern = Pattern.compile(regex[j]);
				
				Matcher match = pattern.matcher(resultArray[i]);
				if (match.find()) {
					data.org_alternative = resultArray[i];
				} else {
					
				}
			}
		}
		return data;
	}
	

}
