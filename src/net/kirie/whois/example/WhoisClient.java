package net.kirie.whois.example;

import net.kirie.whois.Whois;
import net.kirie.whois.WhoisData;

public class WhoisClient {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		if (args.length != 1) {
			System.err.println("usage: WhoisClient DOMAIN");
			return;
		}
		String result = Whois.whois(args[0]);
		System.out.println(result);
//		WhoisData data = WhoisData.createWhoisData(result);
		
		System.out.flush();

		// TODO Auto-generated method stub

	}

}
