package com.kony.adminconsole.preprocessor;

import java.util.HashMap;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;

import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;

public class FetchCountriesAndRegionsPreProcessor implements DataPreProcessor2 {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "SPOTLIGHT");

    private static final String DEFAULT_LOCALE = "en-US";

    @SuppressWarnings("rawtypes")
	@Override
    public boolean execute(HashMap inputMap, DataControllerRequest requestInstance,
            DataControllerResponse responseInstance, Result serviceResult) throws Exception {
		Log4j2Configurator.getInstance();
        try {
            if(inputMap.get("$filter") == null || StringUtils.isBlank(inputMap.get("$filter").toString())) {
            	requestInstance.addRequestParam_("$filter", "LanguageCode eq "+ DEFAULT_LOCALE);
            }
            return true;
        } catch (Exception e) {
            alert.prepareError("Error occured while assigning the language code").log();
            alert.prepareError(e.toString()).log();
            return false;
        }

    }

}
