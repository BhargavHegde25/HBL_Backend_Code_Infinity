/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2023. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.preprocessor;

import com.kony.memorymgmt.CorporateManager;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.tradefinanceservices.constants.ErrorCodeEnum;
import com.kony.dbputilities.util.Log4j2Configurator;

import java.util.Arrays;
import java.util.HashMap;

/**
 * @author k.meiyazhagan
 */
public class MessageEligibilityRecordValidation implements DataPreProcessor2 {
    @Override
    public boolean execute(HashMap hashMap, DataControllerRequest request, DataControllerResponse response, Result result) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            String[] subjectAndRecord = request.getParameter("requestsubject").split("-");
            if (subjectAndRecord.length != 2
                    || !Arrays.asList("RCID_TF_LETTEROFCREDIT", "RCID_TF_GUARANTEES", "RCID_TF_COLLECTIONS").contains(request.getParameter("requestcategory_id"))) {
                ErrorCodeEnum.ERR_12002.setErrorCode(result);
                return false;
            }

            CorporateManager corpManager = new CorporateManager(request);
            if (!corpManager.isRecordMessageEligible(subjectAndRecord[1].trim())) {
                ErrorCodeEnum.ERR_10117.setErrorCode(result);
                return false;
            }
        } catch (Exception e) {
            ErrorCodeEnum.ERR_12004.setErrorCode(result);
            return false;
        }
        return true;
    }
}
