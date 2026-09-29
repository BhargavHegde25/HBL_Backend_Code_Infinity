package com.kony.adminconsole.utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

public class HTTPOperationsForKeycloak
{

    public static enum operations{
        POST,PUT,GET,DELETE
    }

    public static DBXResult sendHttpRequest(operations operation, String requestUrl, String payload, Map<String,Object> headers) {

        DBXResult dbxResult = new DBXResult();
        
        URL url = null;
        try {
            url = new URL(requestUrl);
        } catch (MalformedURLException e1) {
            dbxResult.setDbpErrMsg("Invalid URL");
            return dbxResult;
        }
        HttpURLConnection connection = null;
        try {
            connection = (HttpURLConnection)url.openConnection();
        } catch (IOException e1) {
            dbxResult.setDbpErrMsg("Unable to open connection");
            return dbxResult;
        }

        try {
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod(operation.toString());

            if(headers != null) {
                for(Entry<String, Object> entry : headers.entrySet()) {
                    if(entry.getValue()==null || entry.getValue()=="") {
                        continue;
                    }
                    if(entry.getKey().equalsIgnoreCase("x-kony-app-key")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("x-kony-app-secret")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("X-Kony-AC-API-Access-Token")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("Content-Type")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("Accept")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("claims_token")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("X-Kony-Authorization")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                }
            }

            connection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");

            if (payload != null && !payload.isEmpty()) {
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

            String successResponse = stringBuffer.toString();
            
            dbxResult.setResponse(successResponse);

            connection.disconnect();
            return dbxResult;
        } catch (Exception e) {
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            StringBuffer stringBuffer = new StringBuffer();
            String line;
            try {
                while ((line = br.readLine()) != null) {
                    stringBuffer.append(line);
                }
                br.close();
            } catch (IOException e1) {

            }


            String errorResponse = stringBuffer.toString();
            

            if(errorResponse!=null && errorResponse!="") {
                dbxResult.setResponse(errorResponse);
            }
            connection.disconnect();
        } 


        return dbxResult;
    }
}
