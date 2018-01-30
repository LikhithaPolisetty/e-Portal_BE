package com.tabner.services;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import org.apache.commons.codec.binary.Base64;

public class SmsService {
	
	public static void sendTextMessage(String phoneNumber, String message) {
		try {      
            String appKey = "3cb3acfd-0c77-45b5-a3fd-9fa73f2daece";
            String appSecret = "9UqSwsnWkUe/8fudy7a5/w==";
            URL url = new URL("https://messagingapi.sinch.com/v1/sms/" + phoneNumber);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            String userCredentials = "application\\" + appKey + ":" + appSecret;
            byte[] encoded = Base64.encodeBase64(userCredentials.getBytes());
            String basicAuth = "Basic " + new String(encoded);
            connection.setRequestProperty("Authorization", basicAuth);
            String postData = "{\"Message\":\"" + message + "\"}";
            OutputStream os = connection.getOutputStream();
            os.write(postData.getBytes());
            StringBuilder response = new StringBuilder();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String line;
            while ( (line = br.readLine()) != null)
                response.append(line);
            br.close();
            os.close();
            System.out.println(response.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
}
