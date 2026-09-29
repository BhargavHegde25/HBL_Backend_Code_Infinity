package com.kony.eum.dbputilities.transservices;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import org.apache.poi.util.IOUtils;

import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class GetEAgreementPdfDownloaded implements JavaService2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        String fileName = "Agreement Document.pdf";
        byte[] bytes = getBytes(fileName);
        dcResponse.getHeaders().putAll(getCustomHeaders(fileName, "application/pdf"));
        dcResponse.setAttribute(MWConstants.CHUNKED_RESULTS_IN_JSON,
                new BufferedHttpEntity(new ByteArrayEntity(bytes)));
        dcResponse.setStatusCode(HttpStatus.SC_OK);
        return result;
    }

    private byte[] getBytes(String fileName) {

        try (InputStream is = URLFinder.class.getClassLoader().getResourceAsStream("Agreement Document.pdf");
                BufferedReader reader = new BufferedReader(new InputStreamReader(is));) {

            byte[] bytes = IOUtils.toByteArray(is);
            return bytes;

        } catch (IOException e) {
            alert.prepareError(e.getMessage()).log();
        }
        return null;
    }

    private Map<String, String> getCustomHeaders(String filename, String contentType) {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, contentType);
        customHeaders.put("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        return customHeaders;
    }

}
