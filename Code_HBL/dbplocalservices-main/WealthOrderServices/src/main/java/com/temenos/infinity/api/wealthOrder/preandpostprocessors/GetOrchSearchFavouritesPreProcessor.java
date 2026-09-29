package com.temenos.infinity.api.wealthOrder.preandpostprocessors;
import java.util.HashMap;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.konylabs.middleware.common.DataPreProcessor2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Result;
/**
 * (INFO) Builds the Result in the desired format for the Refinitiv service.
 * 
 * @author madhumathi
 */
public class GetOrchSearchFavouritesPreProcessor implements DataPreProcessor2 {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
    @SuppressWarnings({ "rawtypes" })
    @Override
    public boolean execute(HashMap inputMap, DataControllerRequest request, DataControllerResponse response,
            Result result) throws Exception {
        try {
            if (request.getParameter("isFavouriteSearch") != null && request.getParameter("isFavouriteSearch").equalsIgnoreCase("true")) {
                return true ;
            } else {
                alert.prepareError("Invalid request").log();
                result.addParam("status", "Failure");
                result.addParam("error", "Unauthorized Access");
                return false;
            }
        } catch (Exception e) {
            alert.prepareError("Error in GetOrchSearchFavouritesPreProcessor" + e).log();
            e.getMessage();
            return false;
        }
    }
}