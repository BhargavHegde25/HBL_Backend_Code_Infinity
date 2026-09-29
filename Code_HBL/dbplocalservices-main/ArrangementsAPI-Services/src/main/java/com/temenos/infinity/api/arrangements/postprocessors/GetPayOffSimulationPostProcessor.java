package com.temenos.infinity.api.arrangements.postprocessors;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import com.kony.dbputilities.util.Log4j2Configurator;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.infinity.dbx.temenos.TemenosBasePostProcessor;
import com.infinity.dbx.temenos.utils.TemenosUtils;
import com.kony.dbx.util.CommonUtils;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.constants.UserAccountSettingConstants;

/**
 * TODO: Document me!
 *
 * @author smugesh
 *
 */

public class GetPayOffSimulationPostProcessor extends TemenosBasePostProcessor implements UserAccountSettingConstants {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    
    @Override
    public Result execute(Result result, DataControllerRequest request, DataControllerResponse response)
            throws Exception {
		Log4j2Configurator.getInstance();
        Dataset propertiesDS = result.getDatasetById("properties");
        String ErrMsg = result.getParamValueByName(UserAccountSettingConstants.PARAM_ERROR_MESSAGE) != ""
                ? result.getParamValueByName(UserAccountSettingConstants.PARAM_ERROR_MESSAGE) : "";

        List<Record> propertiesRecords = propertiesDS != null ? propertiesDS.getAllRecords() : null;
        
        
        if ((propertiesRecords != null && !propertiesRecords.isEmpty())){
            double penaltyInterest=0,principalDecreaseFee=0;
             for (Record property : propertiesRecords) {
                String propertyName = CommonUtils.getParamValue(property, "propertyName");
                String propertyAmount = CommonUtils.getParamValue(property, "propertyAmount");
            
                switch(propertyName) {
                case "Account":
                	result.addParam(new Param("totalOutstandingPricipal",propertyAmount));
                	break;
                case "Principal Interest":
                	result.addParam(new Param("principalInterest",propertyAmount));
                	break;
                case "Tax":
                	result.addParam(new Param("tax",propertyAmount));
                	break;
                case "Payoff Fee":
                	result.addParam(new Param("earlyPayoffFee",propertyAmount));
                	break;
                case "Penalty interest":
                	try {
						penaltyInterest= Double.parseDouble(propertyAmount);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						alert.prepareError(e.getMessage()).log();
					}
                	break;
                case "Principal Decrease Fee":
                	try {
                		principalDecreaseFee= Double.parseDouble(propertyAmount);
					} catch (NumberFormatException e) {
						// TODO Auto-generated catch block
						alert.prepareError(e.getMessage()).log();
					}
                	
                }

            }
             double otherCharges = penaltyInterest+principalDecreaseFee;
             result.addParam(new Param("otherCharges",otherCharges+""));
        }else {
                diagnostic.prepareDebug(ERR_EMPTY_RESPONSE).log();
        }

		return result;
    }
    
    public String convertDate(String Date){
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            try {
                date = (Date) formatter.parse(Date);
            } catch (ParseException e) {
                alert.prepareError("Unable to parse Date" + e).log();
            }
        SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
        String processedDate = DATE_FORMAT.format(date);
        return processedDate;
    }
}