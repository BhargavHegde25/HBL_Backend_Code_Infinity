/*******************************************************************************
 * Copyright © Temenos Headquarters SA 2022. All rights reserved.
 ******************************************************************************/
package com.temenos.infinity.tradefinanceservices.resource.impl;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.google.gson.Gson;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.businessdelegate.api.CorporatePayeesBusinessDelegate;
import com.temenos.infinity.tradefinanceservices.constants.TradeFinanceConstants;
import com.temenos.infinity.tradefinanceservices.dto.CorporatePayeesDto;
import com.temenos.infinity.tradefinanceservices.resource.api.CorporatePayeesResource;
import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CorporatePayeesResourceImpl implements CorporatePayeesResource {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    private AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
    private CorporatePayeesBusinessDelegate corporatePayeesBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(CorporatePayeesBusinessDelegate.class);

    public Result getCorporatePayees(HashMap inputParams, DataControllerRequest request) throws Exception {
        Result result = new Result();
        String inputCif = (String) inputParams.get("cif");

        List<String> featureActions = new ArrayList<>();
        featureActions.add(TradeFinanceConstants.TRADEFINANCE_PAYEE_VIEW);
        Map<String, List<String>> inputCifMap;
        // Validating contract information attached to the payee.
        if (StringUtils.isBlank(inputCif)) {
            inputCifMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
            if (inputCifMap == null || inputCifMap.isEmpty()) {
                alert.prepareError("The logged in user doesn't have permission to perform this action").log();
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }
        } else {
            inputCifMap = getContractCifMap(inputCif);

            //Authorization for all the cifs present in input for which the payee needs to be shared
            if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(featureActions, inputCifMap, request.getHeaderMap(), request)) {
                alert.prepareError("The logged in user doesn't have permission to perform this action").log();
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }
        }

        CorporatePayeesBusinessDelegate corporatePayeesBusinessDelegate = DBPAPIAbstractFactoryImpl
                .getBusinessDelegate(CorporatePayeesBusinessDelegate.class);
        List<CorporatePayeesDto> payeesDtoList = null;
        try {
            payeesDtoList = corporatePayeesBusinessDelegate.getCorporatePayees(inputCifMap, inputParams, request);
        } catch (Exception e) {
            result.addParam("dbpErrMsg", "Unable to fetch payees, Please try later.");
            return result;
        }
        Set<CorporatePayeesDto> payeesSet = new HashSet<>(payeesDtoList);

        JSONObject responseObj1 = new JSONObject();
        responseObj1.put("Payees", payeesSet);
        result = JSONToResult.convert(responseObj1.toString());
        return result;
    }

    public Result createCorporatePayee(HashMap requestParams, DataControllerRequest request) throws Exception {
        Result result = new Result();
        String inputCif = (String) requestParams.get("cif");
        List<String> featureActions = new ArrayList<>();
        featureActions.add(TradeFinanceConstants.TRADEFINANCE_PAYEE_CREATE);
        Map<String, List<String>> inputCifMap;

        // Validating contract information attached to the payee.
        if (StringUtils.isBlank(inputCif)) {
            inputCifMap = authorizationChecksBusinessDelegate.getAuthorizedCifs(featureActions, request.getHeaderMap(), request);
            if (inputCifMap == null || inputCifMap.isEmpty()) {
                alert.prepareError("The logged in user doesn't have permission to perform this action").log();
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }
        } else {
            inputCifMap = getContractCifMap(inputCif);

            //Authorization for all the cifs present in input have respective permission
            if (!authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(featureActions, inputCifMap, request.getHeaderMap(), request)) {
                alert.prepareError("The logged in user doesn't have permission to perform this action").log();
                return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            }
        }

        HashMap<String, String> inputParams;

        List<CorporatePayeesDto> payeesDtoList = corporatePayeesBusinessDelegate.getCorporatePayees(inputCifMap, requestParams,
                request);
        String extAccounts = request.getParameter("ExternalPayees");
        JSONObject extObj = new JSONObject(extAccounts);
        Set<String> uniquePayeeNames = payeesDtoList.stream().map(CorporatePayeesDto::getName).collect(Collectors.toSet());
        for (String key : extObj.keySet()) {
            JSONObject payee = extObj.getJSONObject(key);
            try {
                String name = String.valueOf(payee.get("beneficiaryName"));
                if (StringUtils.isNotEmpty(name)) {
                    uniquePayeeNames.add(name);
                }
            } catch (Exception e) {
                alert.prepareError("Error while parsing beneficiary name").log();
            }
        }
        //List<ExternalPayeeBackendDTO> externalPayeeBackendDTOs = externalPayeeDelegate.fetchPayeesFromDBXOrch(request.getHeaderMap(), request);
        //uniquePayeeNames.addAll(externalPayeeBackendDTOs.stream().map(ExternalPayeeBackendDTO::getName).distinct().collect(Collectors.toSet()))
        String payeeDetails = (String) requestParams.get("payeeDetails");
        String response = null;
        JSONObject payeeIds = new JSONObject();
        JSONArray payeeArray = new JSONArray(payeeDetails);


        for (int i = 0; i < payeeArray.length(); i++) {
            JSONObject payeeObject = payeeArray.getJSONObject(i);
            inputParams = new Gson().fromJson(payeeObject.toString(), HashMap.class);

            if (!validateBeneficiaryDetails(inputParams)) {
                result.addParam("dbpErrMsg", "Beneficiary details validation failed");
                return result;
            }
            String name = inputParams.getOrDefault("beneficiaryName", "");
            if (uniquePayeeNames.contains(name)) {
                result.addParam("dbpErrMsg", "Payee with same name " + name + " already exists.");
                return result;
            }
            inputParams.put("name", name);
            try {
                StringBuilder payeeString = new StringBuilder();
                for (Map.Entry<String, List<String>> contractCif : inputCifMap.entrySet()) {
                    inputParams.put("contractId", contractCif.getKey());
                    List<String> coreCustomerIds = contractCif.getValue();
                    for (String coreCustomerId : coreCustomerIds) {
                        inputParams.put("cif", coreCustomerId);
                        response = corporatePayeesBusinessDelegate.createCorporatePayee(inputParams, request);
                        if (response.equalsIgnoreCase("failed")) {
                            result.addParam("dbpErrMsg", "Unable to create Payee. Please try again later.");
                            return result;
                        }
                        if (i != 1)
                            payeeString.append(",").append(response);
                        else
                            payeeString.append(response);

                    }
                }
                payeeIds.put(name, payeeString.toString());
                uniquePayeeNames.add(name);
            } catch (Exception e) {
                result.addParam("dbpErrMsg", "Unable to create Payee. Please try again later.");
                return result;
            }

        }

        result.addParam("payeeIds", payeeIds.toString());
        result.addParam("Message", "Payee created successfully");

        return result;
    }

    public Result editPayee(HashMap inputParams, DataControllerRequest request) {
        Result result = new Result();
        if (!validateBeneficiaryDetails(inputParams)) {
            result.addParam("dbpErrMsg", "Beneficiary details validation failed");
            return result;
        }
        if (inputParams.get("id") != null && StringUtils.isBlank(String.valueOf(inputParams.get("id")))) {
            result.addParam("dbpErrMsg", "Beneficiary details validation failed");
            return result;
        }
        String isCorporate = request.getParameter("isCorporate");
        String response = null;
        try {
            response = isCorporate.equalsIgnoreCase("true")
                    ? corporatePayeesBusinessDelegate.editCorporatePayee(inputParams, request)
                    : corporatePayeesBusinessDelegate.editExternalPayee(inputParams, request);
        } catch (Exception e) {
            alert.prepareError("Unable to edit Payee. Please try again later.").log();
            result.addParam("dbpErrMsg", "Unable to edit Payee. Please try again later.");
            return result;
        }
        if (response.equalsIgnoreCase("failed")) {
            result.addParam("dbpErrMsg", "Unable to edit Payee. Please try again later.");
            return result;
        }
        if (response.equalsIgnoreCase("Unauthorized")) {
            ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
            return result;
        }

        result.addParam("Message", "Payee edited successfully");
        return result;
    }

    private boolean validateBeneficiaryDetails(HashMap<String, String> inputParams) {
        String name = inputParams.getOrDefault("name", "");
        if (StringUtils.isBlank(name)) {
            name = inputParams.getOrDefault("beneficiaryName", "");
        }
        String address1 = inputParams.getOrDefault("address1", "");
        String address2 = inputParams.getOrDefault("address2", "");
        String city = inputParams.getOrDefault("city", "");
        String state = inputParams.getOrDefault("state", "");
        String country = inputParams.getOrDefault("country", "");
        String zipcode = inputParams.getOrDefault("zipcode", "");

        String alphaNumericMax300CharsWithSpace = "^[a-zA-Z0-9 ]{0,300}$";
        String alphaNumericMax100CharsWithSpace = "^[a-zA-Z0-9 ]{0,100}$";
        String alphaNumericMax80CharsWithSpace = "^[a-zA-Z0-9 ]{0,80}$";
        String numericMax80Chars = "^[0-9]{0,80}$";
        Pattern alphaNumericMax300CharsWithSpacePattern = Pattern.compile(alphaNumericMax300CharsWithSpace);
        Pattern alphaNumericMax100CharsWithSpacePattern = Pattern.compile(alphaNumericMax100CharsWithSpace);
        Pattern alphaNumericMax80CharsWithSpacePattern = Pattern.compile(alphaNumericMax80CharsWithSpace);
        Pattern numericMax80CharsePattern = Pattern.compile(numericMax80Chars);
        Matcher matcher;

        if (StringUtils.isNotEmpty(name) && name.trim().length() != 0) {
            matcher = alphaNumericMax100CharsWithSpacePattern.matcher(name);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(address1) && address1.trim().length() != 0) {
            matcher = alphaNumericMax300CharsWithSpacePattern.matcher(address1);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(address2) && address2.trim().length() != 0) {
            matcher = alphaNumericMax300CharsWithSpacePattern.matcher(address2);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(city) && city.trim().length() != 0) {
            matcher = alphaNumericMax80CharsWithSpacePattern.matcher(city);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(state) && state.trim().length() != 0) {
            matcher = alphaNumericMax80CharsWithSpacePattern.matcher(state);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(country) && country.trim().length() != 0) {
            matcher = alphaNumericMax80CharsWithSpacePattern.matcher(country);
            if (!matcher.matches())
                return false;
        }
        if (StringUtils.isNotEmpty(zipcode) && zipcode.trim().length() != 0) {
            matcher = numericMax80CharsePattern.matcher(zipcode);
            return matcher.matches();
        }

        return true;
    }

    private Map<String, List<String>> getContractCifMap(String cifs) {
        Map<String, List<String>> contractCifMap = new HashMap<String, List<String>>();
        JSONArray cifArr = new JSONArray(cifs);
        for (int i = 0; i < cifArr.length(); i++) {
            JSONObject contractObject = cifArr.getJSONObject(i);
            if (StringUtils.isNotBlank(contractObject.getString("coreCustomerId"))) {
                List<String> coreCustomerIds = Arrays.asList(contractObject.getString("coreCustomerId").split(","));
                contractCifMap.put(contractObject.getString("contractId"), coreCustomerIds);
            }
        }
        return contractCifMap;
    }


}
