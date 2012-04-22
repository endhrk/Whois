package net.kirie.whois.utils;

import static org.junit.Assert.*;

import java.net.InetAddress;
import java.net.UnknownHostException;

import org.junit.Test;

public class UtilsTest {

	@Test
	public void testToInetAddress() {
		assertNotNull(Utils.toInetAddress("59.106.174.85"));
		assertNull(Utils.toInetAddress("hogehoge"));
	}

	@Test
	public void testToHostnameString() {
		assertNotNull(Utils.toHostname("59.106.174.85"));
		assertNull(Utils.toHostname("hogehoge"));
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
		String input = "www.kirie.net";
		String result = "kirie.net";
		assertEquals(Utils.removeHostname(input), result);
		input = "hoge.www.kirie.net";
		result = "www.kirie.net";
		assertEquals(Utils.removeHostname(input), result);
		input = "kirie.net";
		result = "net";
		assertEquals(Utils.removeHostname(input), result);
		input = "net";
		result = "net";
		assertEquals(Utils.removeHostname(input), result);
		input = "";
		result = "";
		assertEquals(Utils.removeHostname(input), result);
		input = null;
		result = null;
		assertEquals(Utils.removeHostname(input), result);
	}

	@Test
	public void testIsSuccess() {
		assertTrue(Utils.isSuccess("no match"));
		assertTrue(Utils.isSuccess("No match"));
		assertTrue(Utils.isSuccess("No Match"));
		assertFalse(Utils.isSuccess("www.kirie.net"));
	}

}
