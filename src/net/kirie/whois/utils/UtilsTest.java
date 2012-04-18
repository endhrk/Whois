package net.kirie.whois.utils;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testToAddress() {
		InetAddress inetAddr = Utils.toAddress("59.106.174.85");
		assertNotNull(inetAddr);
	}

	@Test
	public void testToHostnameString() {
		assertNotNull(Utils.toHostname("59.106.174.85"));
	}

	@Test
	public void testToHostnameInetAddress() {
		String ipv4 = "59.106.174.85";
		String[] splitAddr = ipv4.split("\\.");
		byte[] address = new byte[4];
		for (int i = 0; i < splitAddr.length; i++) {
			address[i] = Integer.valueOf(splitAddr[i]).byteValue();
		}
		InetAddress inetAddr = null;
		try {
			inetAddr = InetAddress.getByAddress(address);
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertNotNull(Utils.toHostname(inetAddr));
	}

	@Test
	public void testRemoveHostname() {
		String hostname = "www.kirie.net";
		String domain = "kirie.net";
		assertEquals(Utils.removeHostname(hostname), domain);
		hostname = "hoge.www.kirie.net";
		domain = "www.kirie.net";
		assertEquals(Utils.removeHostname(hostname), domain);
		hostname = "kirie.net";
		domain = "net";
		assertEquals(Utils.removeHostname(hostname), domain);
		hostname = "net";
		domain = "";
		assertEquals(Utils.removeHostname(hostname), domain);
	}

}
