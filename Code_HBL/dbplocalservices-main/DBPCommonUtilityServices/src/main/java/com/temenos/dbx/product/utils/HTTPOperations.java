package com.temenos.dbx.product.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;

import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.temenos.dbx.product.dto.DBXResult;

public class HTTPOperations
{

    private static Alert alert;
    private static Diagnostic diagnostic;

    public static enum operations{
        POST,PUT,GET,DELETE
    }

    public static DBXResult sendHttpRequest(operations operation, String requestUrl, String payload, Map<String,Object> headers) {
        alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
        diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

        DBXResult dbxResult = new DBXResult();
        diagnostic.prepareDebug("REQUEST_URL in request is : "+requestUrl).log();
        
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
                    if(StringUtils.isBlank((String)entry.getValue())) {
                        continue;
                    }
                    if(entry.getKey().equals("Authorization")) {
                        diagnostic.prepareDebug("Authorization in request is : "+entry.getValue()).log();
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equals("channelName")) {
                        diagnostic.prepareDebug("channel Name in request is : "+entry.getValue()).log();
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equalsIgnoreCase("companyid")) {
                        diagnostic.prepareDebug("companyid in request is : "+entry.getValue()).log();
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equals("X-Kony-Authorization")) {
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equals("x-pay-token")) {
                        diagnostic.prepareDebug("x-pay-token in request is : "+entry.getValue()).log();
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equals("x-api-key")) {
                        diagnostic.prepareDebug("x-api-key in request is : "+entry.getValue()).log();
                        connection.setRequestProperty(entry.getKey(), (String)entry.getValue());
                    }
                    if(entry.getKey().equals("x-functions-key")) {
                        diagnostic.prepareDebug("x-functions-key in request is : "+entry.getValue()).log();
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

            diagnostic.prepareDebug("successResponse is : "+successResponse).log();
            
            dbxResult.setResponse(successResponse);

            connection.disconnect();
            return dbxResult;
        } catch (Exception e) {
            alert.prepareError("Caught exception while Sending HTTP Request: for URL -> "+requestUrl +" ", e).log();
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
            
            diagnostic.prepareDebug("errorResponse is : "+errorResponse).log();

            if(StringUtils.isNotBlank(errorResponse)) {
                dbxResult.setResponse(errorResponse);
            }
            connection.disconnect();
            alert.prepareError("Caught exception while Sending HTTP Request: for URL -> "+requestUrl +" ", e).log();
        } 


        return dbxResult;
    }
}
