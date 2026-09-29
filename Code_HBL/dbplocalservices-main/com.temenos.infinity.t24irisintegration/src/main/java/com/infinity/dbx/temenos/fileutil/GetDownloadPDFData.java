package com.infinity.dbx.temenos.fileutil;

import java.util.HashMap;
import java.util.Map;

import org.apache.http.HttpHeaders;
import org.apache.http.HttpStatus;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */
public class GetDownloadPDFData implements JavaService2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");

    @SuppressWarnings("unchecked")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest request,
            DataControllerResponse response) throws Exception {
        TemenosUtils temenosUtils = TemenosUtils.getInstance();
        String fileId = request.getParameter("fileId");
        byte[] bytes = (byte[]) temenosUtils.getDataFromCache(request, fileId);
        if (bytes == null) {
            alert.prepareError("SECURITY EXCEPTION - UNAUTHORIZED ACCESS").log();
            response.setAttribute("dbpErrMsg", "SECURITY EXCEPTION - UNAUTHORIZED ACCESS");
            response.setAttribute("dbpErrCode", "12403");
            return new Result();
        }
        temenosUtils.removeFromCache(fileId);
        response.getHeaders().putAll(getCustomHeaders());
        response.setAttribute("chunkedresults_json", new BufferedHttpEntity(new ByteArrayEntity(bytes)));
        response.setStatusCode(HttpStatus.SC_OK);
        return new Result();
    }

    private Map<String, String> getCustomHeaders() {
        Map<String, String> customHeaders = new HashMap<>();
        customHeaders.put(HttpHeaders.CONTENT_TYPE, "application/pdf");
        customHeaders.put("Content-Disposition", "attachment; filename=\"Transaction Report.pdf\"");
        return customHeaders;
    }

}
