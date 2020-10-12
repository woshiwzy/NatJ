package com.nat.test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.alibaba.fastjson.util.IOUtils;

public class GetGlobalIP {

	public static String getV4IPFromChinaz(String defaultIp) {
		
		String ip = "";
		String chinaz = "http://ip.chinaz.com";
		StringBuilder inputLine = new StringBuilder();
		String read = "";
		URL url = null;
		HttpURLConnection urlConnection = null;
		BufferedReader in = null;
		try {
			url = new URL(chinaz);
			urlConnection = (HttpURLConnection) url.openConnection();
			in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "UTF-8"));
			while ((read = in.readLine()) != null) {
				inputLine.append(read + "\r\n");
			}
		} catch (Exception e) {
			return defaultIp;
		} finally {
			IOUtils.close(in);
		}

		Pattern p = Pattern.compile("\\<dd class\\=\"fz24\">(.*?)\\<\\/dd>");
		Matcher m = p.matcher(inputLine.toString());
		if (m.find()) {
			String ipstr = m.group(1);
			ip = ipstr;
		}
		return ip;
	}

	public static void main(String[] args) {
		System.out.println(getV4IPFromChinaz("123"));
	}
}
