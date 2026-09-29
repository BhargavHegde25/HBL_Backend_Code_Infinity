package com.temenos.dbx.product.businessdelegate.api;

import java.util.Map;

import com.dbp.core.api.BusinessDelegate;
import com.google.gson.JsonObject;
import com.temenos.dbx.product.dto.UpdateProspectDTO;

public interface UpdateProspectBusinessDelegate extends BusinessDelegate {

    JsonObject updateProspect(UpdateProspectDTO updateProspectDTO, String customerId, String urlToUpdatePersonalInfo,
            String urlToUpdateCommInfo, String urlToGetCommInfo, Map<String, ? extends Object> headerMap);
}
