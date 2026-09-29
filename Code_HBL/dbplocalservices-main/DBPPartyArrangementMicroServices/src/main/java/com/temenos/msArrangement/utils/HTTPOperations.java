package com.temenos.msArrangement.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

public class HTTPOperations {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        
    public static String sendHttpRequest(String method, String requestUrl, String payload, String filter, String claimsToken) {
        String s="";
        try {
            if(!(filter==null || filter.isEmpty())){
                requestUrl+="?$filter="+URLEncoder.encode(filter, StandardCharsets.UTF_8.toString());
            }
            URL url = new URL(requestUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(method);
            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            if(claimsToken!=null && (!claimsToken.isEmpty())){
                connection.setRequestProperty("X-Kony-Authorization", claimsToken);
            }
            
            if(!(payload==null || payload.isEmpty())){
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                writer.write(payload);
                writer.close();
            }
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            while ((line = br.readLine()) != null) {
                stringBuffer.append(line);
            }
            br.close();
            connection.disconnect();
            s=stringBuffer.toString();
            System.out.println("Response From Server : "+s);
        } catch (Exception e) {
            alert.prepareError(e.toString()).log(); 
                throw new RuntimeException(e.getMessage());
        }
        return s;
    }

}
