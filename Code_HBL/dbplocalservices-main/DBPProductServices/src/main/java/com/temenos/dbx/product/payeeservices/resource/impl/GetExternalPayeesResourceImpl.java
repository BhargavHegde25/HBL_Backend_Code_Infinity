package com.temenos.dbx.product.payeeservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.payeeservices.businessdelegate.api.GetExternalPayeesBusinessDelegate;
import com.temenos.dbx.product.payeeservices.dto.ExternalPayeeListDTO;
import com.temenos.dbx.product.payeeservices.resource.api.GetExternalPayeesResource;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author naveen.yerra
 */
public class GetExternalPayeesResourceImpl implements GetExternalPayeesResource {

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private String otherBankLogoUrl="";
	private String sameBankLogoUrl="";
    GetExternalPayeesBusinessDelegate businessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(
            GetExternalPayeesBusinessDelegate.class);

    public Result fetchPayees(Object[] inputArray, DataControllerRequest dcRequest) {
    	sameBankLogoUrl = EnvironmentConfigurationsHandler.getServerProperty("BENEFICIARY_SAME_BANK_LOGO_URL");
    	otherBankLogoUrl = EnvironmentConfigurationsHandler.getServerProperty("BENEFICIARY_OTHER_BANK_LOGO_URL");
        Map<String, Object> requestParameters = new HashMap<>();
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
        if(customer == null){
            alert.prepareError("Unable to fetch customer from the session").log();
            return ErrorCodeEnum.ERR_10020.setErrorCode(new Result());
        }
        requestParameters.put("userId", CustomerSession.getCustomerId(customer));
        requestParameters.put("legalEntityId", customer.get("legalEntityId") != null ? customer.get("legalEntityId") : "" );
        diagnostic.prepareInfo(requestParameters.toString()).log();

        List<ExternalPayeeListDTO> backendDTOs = businessDelegate.fetchPayees(requestParameters, dcRequest);

        Map<String, Object> inputParams = (HashMap<String, Object>)inputArray[1];
        _getUniquePayees(backendDTOs);
        FilterDTO filterDTO;
        try {
            filterDTO = JSONUtils.parse(new JSONObject(inputParams).toString(), FilterDTO.class);
        } catch (IOException e) {
            alert.prepareError("Exception occurred while fetching params: ",e).log();
            return ErrorCodeEnum.ERR_21220.setErrorCode(new Result());
        }
        if(filterDTO != null)
            backendDTOs = filterDTO.filter(backendDTOs);

        return JSONToResult.convert(new JSONObject().put("ExternalAccounts", backendDTOs).toString());
    }

    private void _getUniquePayees(List<ExternalPayeeListDTO> ExternalPayeeDTOs) {
        Map<String, HashMap<String,String>> payeeMap = new HashMap<>();

        for(ExternalPayeeListDTO payee: ExternalPayeeDTOs){
            HashMap<String, String> cifMap;
            if(payeeMap.containsKey(payee.getPayeeId())) {
                cifMap = payeeMap.get(payee.getPayeeId());
                String cifIds = payee.getCif();
                if(cifMap.containsKey(payee.getContractId())) {
                    cifIds = cifMap.get(payee.getContractId()) + "," + payee.getCif();
                }
                cifMap.put(payee.getContractId(), cifIds);
            }
            else {
                cifMap = new HashMap<String, String>();
                cifMap.put(payee.getContractId(), payee.getCif());
            }
            payeeMap.put(payee.getPayeeId(), cifMap);
        }

        for(int i = 0; i < ExternalPayeeDTOs.size(); i++){
            ExternalPayeeListDTO payee = ExternalPayeeDTOs.get(i);
            if(payeeMap.containsKey(payee.getPayeeId())) {
                HashMap<String,String> cifMap = payeeMap.get(payee.getPayeeId());
                JSONArray cifArray = new JSONArray();
                int noOfCustomersLinked = 0;
                for (Map.Entry<String,String> entry : cifMap.entrySet()) {
                    JSONObject cifObj = new JSONObject();
                    cifObj.put("contractId", entry.getKey());
                    cifObj.put("coreCustomerId", entry.getValue());
                    noOfCustomersLinked += entry.getValue().split(",").length;
                    cifArray.put(cifObj);
                }
                payee.setCif(cifArray.toString());
                payee.setNoOfCustomersLinked(String.valueOf(noOfCustomersLinked));
                String bankId=payee.getIban();
                String isSameBankAccount= payee.getIsSameBankAccount();
    			if(StringUtils.isNotBlank(isSameBankAccount)&& payee.getIsSameBankAccount().equalsIgnoreCase("true")) {
    				payee.setTownName(sameBankLogoUrl);
    			}else if(StringUtils.isNotBlank(bankId)){
    				payee.setTownName(otherBankLogoUrl+bankId+".png");
    			}
                payeeMap.remove(payee.getPayeeId());
            }
            else {
                ExternalPayeeDTOs.remove(i);
                i--;
            }
        }

    }

}
