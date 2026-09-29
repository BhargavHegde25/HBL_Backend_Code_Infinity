package com.temenos.infinity.api.accountsweeps.resource.impl;

import com.dbp.core.api.factory.BusinessDelegateFactory;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.dbp.core.fabric.extn.DBPServiceExecutorBuilder;
import com.dbp.core.util.JSONUtils;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.kony.dbputilities.util.EnvironmentConfigurationsHandler;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.JSONToResult;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.product.commons.businessdelegate.api.AccountBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commons.dto.FilterDTO;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.infinity.api.accountsweeps.businessdelegate.api.AccountSweepsBusinessDelegate;
import com.temenos.infinity.api.accountsweeps.constants.Constants;
import com.temenos.infinity.api.accountsweeps.constants.ErrorCodeEnum;
import com.temenos.infinity.api.accountsweeps.dto.AccountSweepsDTO;
import com.temenos.infinity.api.accountsweeps.resource.api.AccountSweepsResource;
import com.temenos.infinity.api.srmstransactions.dto.SessionMap;
import com.temenos.infinity.api.srmstransactions.utils.MemoryManagerUtils;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import static com.temenos.infinity.api.accountsweeps.constants.Constants.*;

/**
 * @author naveen.yerra
 */
public class AccountSweepsResourceImpl implements AccountSweepsResource {

    private final AccountSweepsBusinessDelegate sweepsBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountSweepsBusinessDelegate.class);
    private final AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");

    AccountBusinessDelegate accountBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AccountBusinessDelegate.class);

    public Result getSweepByAccountId(HashMap<String, Object> inputParams, DataControllerRequest controllerRequest) {
        Result result = new Result();
        String accountId = (String) inputParams.getOrDefault(ACCOUNT_ID, "");

        if(StringUtils.isBlank(accountId)) {
            return ErrorCodeEnum.ERR_3003.setErrorCode(result);
        }

        HashMap<String, Object> customer = (HashMap<String, Object>) CustomerSession.getCustomerMap(controllerRequest);
        String customerId = CustomerSession.getCustomerId(customer);

        if (!authorizationChecksBusinessDelegate.isOneOfMyAccounts(customerId, accountId)) {
            return ErrorCodeEnum.ERR_3008.setErrorCode(result);
        }
        if(featureActionIdCheckAccountLevel(customerId, Constants.ACCOUNT_SWEEP_VIEW, accountId,
                CustomerSession.IsCombinedUser(customer)))
            return ErrorCodeEnum. ERR_3012.setErrorCode(result);
        
        String ACCOUNTSWEEP_BACKEND = EnvironmentConfigurationsHandler.getValue("ACCOUNTSWEEP_BACKEND");
        AccountSweepsDTO sweepsDTO=null;
		if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("STUB")) {
			return stubGetAccountSweepById(result);
		}else if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("t24")) {
			Set<String> accountsInput = new HashSet<String>();
			accountsInput.add(accountId);
			String sweepsResponse = sweepsBusinessDelegate.getAllAccountSweepsFromT24(accountsInput,controllerRequest);
			result = JSONToResult.convert(sweepsResponse);
			result = postProcessT24Response(result,controllerRequest);
			 return result;
		}else {
         sweepsDTO = sweepsBusinessDelegate.getSweepByAccountId(accountId);
		}
        if(StringUtils.isNotBlank(sweepsDTO.getErrorCode()) || StringUtils.isNotBlank(sweepsDTO.getErrorMessage())) {
            result = JSONToResult.convert(new JSONObject(sweepsDTO).toString());
            return result;
        }

        result = JSONToResult.convert(new JSONObject().put(ACCOUNT_SWEEP, new JSONObject(sweepsDTO)).toString());
        return result;
    }

    public Result createSweep(String methodID, Object[] inputArray, DataControllerRequest request,
                              DataControllerResponse response) {
        Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];
        Result result=new Result();
        AccountSweepsDTO accountSweepsDTO;
        try {
            AccountSweepsDTO sweepsDto = JSONUtils.parse(new JSONObject(inputParams).toString(), AccountSweepsDTO.class);

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            if(customerId == null)
                return ErrorCodeEnum.ERR_3005.setErrorCode(result);
            if(validateInput(sweepsDto))
                return ErrorCodeEnum.ERR_3011.setErrorCode(result);

            String ACCOUNTSWEEP_BACKEND = EnvironmentConfigurationsHandler.getValue("ACCOUNTSWEEP_BACKEND");
            
            String isValidate =  request.getParameter("isValidate");
            if("true".equalsIgnoreCase(isValidate) && !ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("t24")) {
            	return validateSuccess(inputParams,result);
            }
            
    		if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("STUB")) {
    			return stubCreateAccountSweep(inputParams,result);
    		}
            // Validating accountId and account type against session info.
            if(validateAccount(sweepsDto.getPrimaryAccountNumber(), customerId, "Create", true)) {
                alert.prepareError("User is not Authorized for the account number used").log();
                return ErrorCodeEnum.ERR_3008.setErrorCode(result);
            }
            if(validateAccount(sweepsDto.getSecondaryAccountNumber(), customerId, "Create", false)) {
                alert.prepareError("User is not Authorized for the account number used").log();
                return ErrorCodeEnum.ERR_3008.setErrorCode(result);
            }

			
			  if(accountStatusAndCurrencyCheck(sweepsDto.getPrimaryAccountNumber(),
			  sweepsDto.getSecondaryAccountNumber(),customerId)) { alert.
			  prepareError("Account Status of both account is not Active or Closure Pending"
			  ).log(); return ErrorCodeEnum.ERR_3013.setErrorCode(result); }
			 
            
            if(featureActionIdCheckAccountLevel(customerId, Constants.ACCOUNT_SWEEP_CREATE,
                    sweepsDto.getPrimaryAccountNumber(), CustomerSession.IsCombinedUser(customer)))
                return ErrorCodeEnum. ERR_3012.setErrorCode(result);

            if(validateDateAgainstBankDate(sweepsDto.getStartDate(), sweepsDto.getEndDate(), request))
                return ErrorCodeEnum.ERR_3010.setErrorCode(result);
             accountSweepsDTO = sweepsBusinessDelegate.createSweep(sweepsDto,request,ACCOUNTSWEEP_BACKEND);
            accountSweepsDTO.setPrimaryAccountNumber(sweepsDto.getPrimaryAccountNumber());
            accountSweepsDTO.setSecondaryAccountNumber(sweepsDto.getSecondaryAccountNumber());

            JSONObject sweepObj = new JSONObject(accountSweepsDTO);
            result = JSONToResult.convert(sweepObj.toString());
            result.addParam(new Param("isValidate",isValidate));
        }
        catch (Exception e){
            alert.prepareError("Error occurred while creating sweep "+e).log();
            return ErrorCodeEnum.ERR_3007.setErrorCode(result);
        }
        return result;
    }

    

	@Override
    public Result getAccountSweeps(FilterDTO filterDTO, DataControllerRequest request) {

        Result result=new Result();
        JSONObject responseObj = new JSONObject();

        HashMap<String, Object> customer = (HashMap<String, Object>) CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if(customerId == null)
            return ErrorCodeEnum.ERR_3005.setErrorCode(result);

        String coreCustomerId = request.getParameter("coreCustomerId");
        Set<String> accounts = getAccounts(customerId,coreCustomerId);
        if(accounts.size() == 0)
            return ErrorCodeEnum.ERR_3006.setErrorCode(result);
        
        List<AccountSweepsDTO> responseSweeps= new ArrayList<AccountSweepsDTO>();
        String ACCOUNTSWEEP_BACKEND = EnvironmentConfigurationsHandler.getValue("ACCOUNTSWEEP_BACKEND");
		if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("STUB")) {
			responseSweeps = stubGetAccountSweep(result);
		}else if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("t24")) {
			String sweepsResponse = sweepsBusinessDelegate.getAllAccountSweepsFromT24(accounts, request);
			result = JSONToResult.convert(sweepsResponse);
			
			result = postProcessT24Response(result,request);
			
			Dataset sweepsDS = result.getDatasetById("AccountSweep");
			if(sweepsDS!=null) {
		        try {
		            JSONArray responseArray =ResultToJSON.convertDataset(sweepsDS);
		            responseSweeps = responseArray.length() !=0 ?
		                    JSONUtils.parseAsList(responseArray.toString(), AccountSweepsDTO.class) : new ArrayList<>();
		        } catch (IOException e) {
		            alert.prepareError("Unable to get Account Sweep from Backend" + e).log();
		            return null;
		        }
			}

			
		}else {
			responseSweeps = sweepsBusinessDelegate.getAllAccountSweeps(accounts);
		}
        

        if (responseSweeps == null) {
            alert.prepareError("Error occurred while fetching account sweeps from backend").log();
            return ErrorCodeEnum.ERR_3001.setErrorCode(result);
        }

        try {
        	// to support sorting name and account number, manipulate the response before sending it to sort method
        		if(filterDTO.get_sortByParam()!=null && (filterDTO.get_sortByParam().equalsIgnoreCase("primaryAccountName") || filterDTO.get_sortByParam().equalsIgnoreCase("secondaryAccountName"))) {
        			for(AccountSweepsDTO sweepRecord:responseSweeps) {
            			sweepRecord.setPrimaryAccountName(sweepRecord.getPrimaryAccountName()+"@#"+sweepRecord.getPrimaryAccountNumber());
            			sweepRecord.setSecondaryAccountName(sweepRecord.getSecondaryAccountName()+"@#"+sweepRecord.getSecondaryAccountNumber());
            		}
        		}
        	//
            List<AccountSweepsDTO> filteredRecords = filterDTO.filter(responseSweeps);
        	// to support sorting name and account number, manipulate the response before sending it to sort method
    		if(filterDTO.get_sortByParam()!=null && (filterDTO.get_sortByParam().equalsIgnoreCase("primaryAccountName") || filterDTO.get_sortByParam().equalsIgnoreCase("secondaryAccountName"))) {
    			for(AccountSweepsDTO sweepRecord:filteredRecords) {
    				String primaryAccountName = sweepRecord.getPrimaryAccountName().split("@#")[0];
        			sweepRecord.setPrimaryAccountName(primaryAccountName);
        			sweepRecord.setSecondaryAccountName(sweepRecord.getSecondaryAccountName().split("@#")[0]);
        		}
    		}
    	//
            
            responseObj.put(ACCOUNT_SWEEP, filteredRecords);
            result = JSONToResult.convert(responseObj.toString());
        } catch (Exception e) {
            result.addErrMsgParam("Failed to fetch the records");
            alert.prepareError("Error occurred while fetching account sweeps from backend").log();
            return ErrorCodeEnum.ERR_3001.setErrorCode(result);
        }
        return result;
    }

	private Result postProcessT24Response(Result result, DataControllerRequest request) {
		Dataset accountsSweepsDS = result.getDatasetById(ACCOUNT_SWEEP);
		if(accountsSweepsDS!=null ) {
			SimpleDateFormat t24Format = new SimpleDateFormat("dd MMM yyyy");
			SimpleDateFormat endDateFormat = new SimpleDateFormat("yyyyMMdd");
	    	SimpleDateFormat infinityFormat = new SimpleDateFormat("dd/MM/yyyy");
            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
			List<Record> accountsSweeps = accountsSweepsDS.getAllRecords();
	        SessionMap accountMap = getInternalAccountsFromSession(customerId);
			for(Record rec:accountsSweeps) {
				String frequency = rec.getParamValueByName("frequency");
				
				if(frequency!=null) {
					if("Every working day".equalsIgnoreCase(frequency)) {
						rec.addParam("frequency", "Daily");
					}else if (frequency.startsWith("Monthly")) {
						rec.addParam("frequency", "Monthly");
					}else if (frequency.startsWith("Every 6 months")) {
						rec.addParam("frequency", "Every 6 Months");
					}
				}
				
				String sweepType = rec.getParamValueByName("sweepType");
				String newSweepType ="";
				switch(sweepType) {
				case "MAIN":
					newSweepType = "Below";break;
				case "SURP":
					newSweepType = "Above";break;
				case "TWOWAY":
					newSweepType = "Both";break;
				}
				rec.addParam("sweepType", newSweepType);
		    	String startDate = rec.getParamValueByName("startDate");
		    	if(startDate!=null) {
		    		try {
						String formattedStartDate = infinityFormat.format(t24Format.parse(startDate));
						rec.addParam("startDate", formattedStartDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						alert.prepareError(e.getMessage()).log();
					}
		    	}
		    	
		    	String endDate = rec.getParamValueByName("endDate");
		    	if(endDate!=null) {
		    		try {
						String formattedEndDate = infinityFormat.format(endDateFormat.parse(endDate));
						rec.addParam("endDate", formattedEndDate);
					} catch (ParseException e) {
						// TODO Auto-generated catch block
						alert.prepareError(e.getMessage()).log();
					}
		    	}
		    	
		    	String belowSweepAmount = rec.getParamValueByName("belowSweepAmount");
		    	if(belowSweepAmount!=null) {
		    		belowSweepAmount = belowSweepAmount.replaceAll(",", "");
		    		if(belowSweepAmount.equals("0.00")) {
		    			belowSweepAmount = "";
		    		}
		    		rec.addParam("belowSweepAmount", belowSweepAmount);
		    	}
		    	
		    	String aboveSweepAmount = rec.getParamValueByName("aboveSweepAmount");
		    	if(aboveSweepAmount!=null) {
		    		aboveSweepAmount = aboveSweepAmount.replaceAll(",", "");
		    		if(aboveSweepAmount.equals("0.00")) {
		    			aboveSweepAmount = "";
		    		}
		    		rec.addParam("aboveSweepAmount", aboveSweepAmount);
		    	}
		    	

	            String primaryAccountNumber = rec.getParamValueByName("primaryAccountNumber");
	            String secondaryAccountNumber = rec.getParamValueByName("secondaryAccountNumber");
	            

		        String account1Name=accountMap.getAttributeValueForKey(primaryAccountNumber, "accountName");
		        String account2Name=accountMap.getAttributeValueForKey(secondaryAccountNumber, "accountName");
		    //    rec.addParam("serviceRequestId", primaryAccountNumber);
		        rec.addParam("serviceRequestId", "");
		        
		        rec.addParam("primaryAccountName", account1Name);
		        rec.addParam("secondaryAccountName", account2Name);
			}
		}else {
			result.addDataset(new Dataset(ACCOUNT_SWEEP));
		}
		return result;
	}
    public Result deleteSweep(String methodID, Object[] inputArray, DataControllerRequest request, DataControllerResponse response) throws IOException {
        Result result = new Result();
        Map<String, Object> inputParams =  (HashMap<String, Object>)inputArray[1];

        AccountSweepsDTO sweepsDto = JSONUtils.parse(new JSONObject(inputParams).toString(), AccountSweepsDTO.class);

        AccountSweepsBusinessDelegate accountSweepsBusinessDelegate = DBPAPIAbstractFactoryImpl.getInstance()
                .getFactoryInstance(BusinessDelegateFactory.class).getBusinessDelegate(AccountSweepsBusinessDelegate.class);

        Map<String, Object> customer = CustomerSession.getCustomerMap(request);
        String customerId = CustomerSession.getCustomerId(customer);
        if (StringUtils.isBlank(customerId)) {
            alert.prepareError("Failed to fetch Customer ID").log();
            return ErrorCodeEnum.ERR_3005.setErrorCode(result);
        }
        if(validateInput(sweepsDto))
            return ErrorCodeEnum.ERR_3011.setErrorCode(result);
        String isValidate =  request.getParameter("isValidate");
        String ACCOUNTSWEEP_BACKEND = EnvironmentConfigurationsHandler.getValue("ACCOUNTSWEEP_BACKEND");
		if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("STUB")) {
			return stubDeleteAccountSweep(inputParams,result);
		}else if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("t24")){
			sweepsDto.setEndDate(getBankDate(request));
		}
		
        if(validateAccount(sweepsDto.getPrimaryAccountNumber(), customerId, "Delete", true)) {
            alert.prepareError("User is not Authorized for the account number used").log();
            return ErrorCodeEnum.ERR_3008.setErrorCode(result);
        }
        if(validateAccount(sweepsDto.getSecondaryAccountNumber(), customerId, "Delete", false)) {
            alert.prepareError("User is not Authorized for the account number used").log();
            return ErrorCodeEnum.ERR_3008.setErrorCode(result);
        }

        if(featureActionIdCheckAccountLevel(customerId, Constants.ACCOUNT_SWEEP_DELETE,
                sweepsDto.getPrimaryAccountNumber(), CustomerSession.IsCombinedUser(customer)))
            return ErrorCodeEnum. ERR_3012.setErrorCode(result);
        

        try {
        	
        	
            AccountSweepsDTO deleteSweep = accountSweepsBusinessDelegate.deleteSweep(sweepsDto, request,ACCOUNTSWEEP_BACKEND);
            deleteSweep.setPrimaryAccountNumber(sweepsDto.getPrimaryAccountNumber());
            deleteSweep.setSecondaryAccountNumber(sweepsDto.getSecondaryAccountNumber());
            JSONObject sweepObj = new JSONObject(deleteSweep);
            result = JSONToResult.convert(sweepObj.toString());
           
            result.addParam(new Param("isValidate",isValidate));
        }
        catch(Exception e){
            alert.prepareError("Unable to delete account sweeps"+e).log();
            return ErrorCodeEnum.ERR_2000.setErrorCode(result);
        }

        return result;
    }

    public Result editSweep(Map<String, Object> inputParams, DataControllerRequest request) {

        Result result=new Result();
        AccountSweepsDTO accountSweepsDTO;
        try {
            AccountSweepsDTO sweepsDto = JSONUtils.parse(new JSONObject(inputParams).toString(), AccountSweepsDTO.class);

            Map<String, Object> customer = CustomerSession.getCustomerMap(request);
            String customerId = CustomerSession.getCustomerId(customer);
            if(customerId == null)
                return ErrorCodeEnum.ERR_3005.setErrorCode(result);
            if(validateInput(sweepsDto))
                return ErrorCodeEnum.ERR_3011.setErrorCode(result);
            

            
            String ACCOUNTSWEEP_BACKEND = EnvironmentConfigurationsHandler.getValue("ACCOUNTSWEEP_BACKEND");
            String isValidate =  request.getParameter("isValidate");
            
            if("true".equalsIgnoreCase(isValidate) && !ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("t24")) {
            	return validateSuccess(inputParams,result);
            }
            
    		if(ACCOUNTSWEEP_BACKEND.equalsIgnoreCase("STUB")) {
    			return stubEditAccountSweep(inputParams,result);
    		}
    		
    /*        String previousAccountnumber;
            AccountSweepsDTO getSweepById=sweepsBusinessDelegate.getSweepByAccountId(sweepsDto.getPrimaryAccountNumber());
            if(getSweepById.getSecondaryAccountNumber().equals(sweepsDto.getSecondaryAccountNumber())) {
                previousAccountnumber = "";
                if(validateAccount(sweepsDto.getSecondaryAccountNumber(), customerId, "Edit", true))
                    return ErrorCodeEnum.ERR_3008.setErrorCode(result);
            }
            else {
                previousAccountnumber = getSweepById.getSecondaryAccountNumber();
                 if(validateAccount(sweepsDto.getSecondaryAccountNumber(), customerId, "Edit", false))
                     return ErrorCodeEnum.ERR_3008.setErrorCode(result);
            }

            if(!getSweepById.getPrimaryAccountNumber().equals(sweepsDto.getPrimaryAccountNumber()))
                return ErrorCodeEnum.ERR_3008.setErrorCode(result);
	*/
            // Validating accountId and account type against session info.
            if(validateAccount(sweepsDto.getPrimaryAccountNumber(), customerId, "Edit", true)) {
                alert.prepareError("User is not Authorized for the account number used").log();
                return ErrorCodeEnum.ERR_3008.setErrorCode(result);
            }

            if(accountStatusAndCurrencyCheck(sweepsDto.getPrimaryAccountNumber(),sweepsDto.getSecondaryAccountNumber(),customerId)) {
                 alert.prepareError("Either Currency code of both accounts not matching or Account Status of both account is not Active or Closure Pending").log();
                 return ErrorCodeEnum.ERR_3013.setErrorCode(result);
            }

            if(featureActionIdCheckAccountLevel(customerId, Constants.ACCOUNT_SWEEP_EDIT,
                    sweepsDto.getPrimaryAccountNumber(), CustomerSession.IsCombinedUser(customer)))
                return ErrorCodeEnum. ERR_3012.setErrorCode(result);

            if(validateDateAgainstBankDate(sweepsDto.getStartDate(), sweepsDto.getEndDate(), request))
                return ErrorCodeEnum.ERR_3010.setErrorCode(result);

            accountSweepsDTO = sweepsBusinessDelegate.editSweep(sweepsDto,request,ACCOUNTSWEEP_BACKEND);

            accountSweepsDTO.setSecondaryAccountNumber(sweepsDto.getSecondaryAccountNumber());
            accountSweepsDTO.setPrimaryAccountNumber(sweepsDto.getPrimaryAccountNumber());
      /*      if(!previousAccountnumber.equals(""))
                accountSweepsDTO.setPreviousSecondaryAccountNumber(previousAccountnumber); */

            JSONObject sweepObj = new JSONObject(accountSweepsDTO);
            result = JSONToResult.convert(sweepObj.toString());
            result.addParam(new Param("isValidate",isValidate));
        }
        catch (Exception e){
            alert.prepareError("Error occured while creating sweep "+e).log();
            return ErrorCodeEnum.ERR_3007.setErrorCode(result);
        }
        return result;
    }

    private boolean validateInput(AccountSweepsDTO sweepsDto) {
        String startDate = sweepsDto.getStartDate();

        if(StringUtils.isEmpty(startDate))
            return true;

        if((sweepsDto.getBelowSweepAmount()==null || sweepsDto.getBelowSweepAmount().equals("")) && (sweepsDto.getAboveSweepAmount()==null || sweepsDto.getAboveSweepAmount().equals("")))
            return true;

        try {
            double belowSweepAmount = Double.parseDouble(
                    StringUtils.isNotEmpty(sweepsDto.getBelowSweepAmount()) ? sweepsDto.getBelowSweepAmount() : "0.0");
            double aboveSweepAmount = Double.parseDouble(
                    StringUtils.isNotEmpty(sweepsDto.getAboveSweepAmount()) ? sweepsDto.getAboveSweepAmount() : "0.0");
            if(belowSweepAmount < 0.0 || aboveSweepAmount < 0.0  || (aboveSweepAmount!=0.0 && belowSweepAmount>aboveSweepAmount)) {
                return true;
            }
        }
        catch (Exception e) {
            alert.prepareError("Error occurred while parsing and validating amount").log();
            return true;
        }

        if (sweepsDto.getPrimaryAccountNumber().equals(sweepsDto.getSecondaryAccountNumber())) {
            alert.prepareError("From Account Number is same as To Account Number").log();
            return true;
        }

        return StringUtils.isEmpty(sweepsDto.getPrimaryAccountNumber()) || StringUtils.isEmpty(
                sweepsDto.getSecondaryAccountNumber());
    }

    private SessionMap getInternalAccountsFromSession(String customerId) {
        return (SessionMap) MemoryManagerUtils.retrieve(INTERNAL_BANK_ACCOUNTS + customerId);
    }

    private Set<String> getAccounts(String customerId,String coreCustomerId) {
    	Set <String> accounts = new HashSet<>();
    	
        SessionMap internalAccountsMap = getInternalAccountsFromSession(customerId); //Get the accounts from session
         if (StringUtils.isNotBlank(internalAccountsMap.toString())) { //If account exists
        	 JSONObject internalAccountsObj = new JSONObject(internalAccountsMap.toString());
        	if(StringUtils.isNoneBlank(coreCustomerId)) { //If request is coming for multi customer user (need to return only specific core customer accounts
        		
        		Set<String> allAccountIds = internalAccountsObj.keySet();
        		for(String accountId:allAccountIds) {
        			JSONObject accountObj = internalAccountsObj.getJSONObject(accountId);
        			if(accountObj!=null && accountObj.has("coreCustomerId") && coreCustomerId.equals(accountObj.getString("coreCustomerId"))) {
        				accounts.add(accountId);
        			}
        		}
        		
        	}else { //Return all accounts
        		accounts =  internalAccountsObj.keySet();
        	}
            
        }
        return accounts;
    }

    private boolean validateAccount(String accountId, String customerId,String operationType,boolean isPrimaryAccount) {
        SessionMap accountMap = getInternalAccountsFromSession(customerId);
        if(!accountMap.hasKey(accountId))
            return true;
		/*
		 * String isSweepCreated=accountMap.getAttributeValueForKey(accountId,
		 * "isSweepCreated"); if(isSweepCreated!=null) { if
		 * (operationType.equals("Create") && isSweepCreated.equals("true")) return
		 * true; if (operationType.equals("Delete") && isSweepCreated.equals("false"))
		 * return true; if(operationType.equals("Edit")){ if(isPrimaryAccount &&
		 * isSweepCreated.equals("false")) return true; if(!isPrimaryAccount &&
		 * isSweepCreated.equals("true")) return true; } }
		 */
        String accountType = accountMap.getAttributeValueForKey(accountId, "accountType");
        return !accountType.equalsIgnoreCase("Savings") && !accountType.equalsIgnoreCase("Checking");
    }

    private boolean validateDateAgainstBankDate(String startDate, String endDate, DataControllerRequest request) throws ParseException {

        JsonObject dateObj = new JsonObject();
        try {
            String date = DBPServiceExecutorBuilder.builder().
                    withServiceId("TransactionObjects").
                    withObjectId("BankDate").
                    withOperationId("getBankDate").
                    withDataControllerRequest(request).
                    withRequestParameters(new HashMap<>()).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            dateObj = parser.parse(date).getAsJsonObject().getAsJsonArray("date").get(0).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching bank date").log();
        }

        String currentDate = dateObj.has("currentWorkingDate") ? dateObj.get("currentWorkingDate").getAsString() : null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        Date bankDate = sdf2.parse(currentDate);
        boolean isValid = true;
        Date Tempdate;
        if (StringUtils.isNotBlank(startDate)) {
            Tempdate = sdf.parse(startDate);
            isValid = Tempdate.compareTo(bankDate) >= 0;
        }
 
        if (isValid && StringUtils.isNotBlank(endDate) && !"End Manually".equalsIgnoreCase(endDate)) {
            Tempdate = sdf.parse(endDate);
            isValid=(Tempdate.compareTo(sdf.parse(startDate)))>0;
        }
        return !isValid;

    }
    private boolean featureActionIdCheckAccountLevel (String customerId,String requiredActionId,String accountId,boolean isCombined){
        return !authorizationChecksBusinessDelegate.isUserAuthorizedForFeatureAction(customerId, requiredActionId, accountId, isCombined);
    }
    private boolean accountStatusAndCurrencyCheck(String primaryAccountNumber,String secondaryAccountNumber,String customerId){

        SessionMap accountMap = getInternalAccountsFromSession(customerId);
        String accountStatus1=accountMap.getAttributeValueForKey(primaryAccountNumber, "accountStatus");
        String accountStatus2=accountMap.getAttributeValueForKey(secondaryAccountNumber, "accountStatus");
/*        String currencyCode1=accountMap.getAttributeValueForKey(primaryAccountNumber, "currencyCode");
        String currencyCode2=accountMap.getAttributeValueForKey(secondaryAccountNumber, "currencyCode");*/

        if((!accountStatus1.equals("ACTIVE") && !accountStatus1.equals("CLOSURE_PENDING")) ||
                (!accountStatus2.equals("ACTIVE") && !accountStatus2.equals("CLOSURE_PENDING")))
            return true;
        
         return false;
/*        return !currencyCode1.equals(currencyCode2);*/

    }
    
    private List<AccountSweepsDTO> stubGetAccountSweep(Result result) {
    	
    	  List<AccountSweepsDTO> responseSweeps= new ArrayList<AccountSweepsDTO>();
    	  
    	  AccountSweepsDTO accountSweepDTO = new AccountSweepsDTO();
    	  accountSweepDTO.setPrimaryAccountName("Brice Bamford");
    	  accountSweepDTO.setPrimaryAccountNumber("107093");
    	  accountSweepDTO.setSecondaryAccountName("Brice Bamford");
    	  accountSweepDTO.setSecondaryAccountNumber("101834");
    	  accountSweepDTO.setAboveSweepAmount("1000.00");
    	  accountSweepDTO.setBelowSweepAmount("10.00");
    	  accountSweepDTO.setFrequency("Daily");
    	  accountSweepDTO.setStartDate("27/04/2023");
    	  accountSweepDTO.setCurrencyCode("USD");
    	  accountSweepDTO.setEndDate("");
    	  accountSweepDTO.setServiceRequestId("STB12345");
    	  accountSweepDTO.setSweepType("Above");
    	  responseSweeps.add(accountSweepDTO);
    	  
    	  AccountSweepsDTO accountSweepDTO1 = new AccountSweepsDTO();
    	  accountSweepDTO1.setPrimaryAccountName("Brice Bamford");
    	  accountSweepDTO1.setPrimaryAccountNumber("107298");
    	  accountSweepDTO1.setSecondaryAccountName("Brice Bamford");
    	  accountSweepDTO1.setSecondaryAccountNumber("104558");
    	  accountSweepDTO1.setAboveSweepAmount("50.00");
    	  accountSweepDTO1.setBelowSweepAmount("10.00");
    	  accountSweepDTO1.setFrequency("Daily");
    	  accountSweepDTO1.setStartDate("20/04/2023");
    	  accountSweepDTO1.setCurrencyCode("USD");
    	  accountSweepDTO1.setEndDate("");
    	  accountSweepDTO1.setServiceRequestId("STB09876");
    	  accountSweepDTO1.setSweepType("Both");
    	  responseSweeps.add(accountSweepDTO1);
    	  
    	  AccountSweepsDTO accountSweepDTO2 = new AccountSweepsDTO();
    	  accountSweepDTO2.setPrimaryAccountName("Wells Fargo");
    	  accountSweepDTO2.setPrimaryAccountNumber("104167");
    	  accountSweepDTO2.setSecondaryAccountName("Wells Fargo");
    	  accountSweepDTO2.setSecondaryAccountNumber("104008");
    	  accountSweepDTO2.setAboveSweepAmount("200000.00");
    	  accountSweepDTO2.setBelowSweepAmount("500.00");
    	  accountSweepDTO2.setFrequency("Daily");
    	  accountSweepDTO2.setStartDate("10/04/2023");
    	  accountSweepDTO2.setCurrencyCode("USD");
    	  accountSweepDTO2.setEndDate("");
    	  accountSweepDTO2.setServiceRequestId("STB76543");
    	  accountSweepDTO2.setSweepType("Both");
    	  responseSweeps.add(accountSweepDTO2);
    	 return responseSweeps;
    	
    	/*Dataset ds = new Dataset();
		ds.setId("AccountSweep");
		Record record1 = new Record();
		record1.addStringParam("primaryAccountName", "Brice Bamford");
		record1.addStringParam("primaryAccountNumber", "107093");
		record1.addStringParam("secondaryAccountName", "Brice Bamford");
		record1.addStringParam("secondaryAccountNumber", "101834");
		record1.addStringParam("belowSweepAmount", "10");
		record1.addStringParam("aboveSweepAmount", "1000.00");
		record1.addStringParam("frequency", "Daily");
		record1.addStringParam("startDate", "27/04/2023");
		record1.addStringParam("currencyCode", "USD");
		record1.addStringParam("endDate", "");
		record1.addStringParam("serviceRequestId", "STB12345");
		record1.addStringParam("sweepType", "Above");
	

		Record record2 = new Record();
		record2.addStringParam("primaryAccountName", "Brice Bamford");
		record2.addStringParam("primaryAccountNumber", "107298");
		record2.addStringParam("secondaryAccountName", "Brice Bamford");
		record2.addStringParam("secondaryAccountNumber", "104558");
		record2.addStringParam("belowSweepAmount", "10.00");
		record2.addStringParam("aboveSweepAmount", "50.00");
		record2.addStringParam("frequency", "Daily");
		record2.addStringParam("startDate", "20/04/2023");
		record2.addStringParam("currencyCode", "USD");
		record2.addStringParam("endDate", "");
		record2.addStringParam("serviceRequestId", "STB09876");
		record2.addStringParam("sweepType", "Both");
		

		Record record3 = new Record();
		record3.addStringParam("primaryAccountName", "Wells Fargo");
		record3.addStringParam("primaryAccountNumber", "104167");
		record3.addStringParam("secondaryAccountName", "Wells Fargo");
		record3.addStringParam("secondaryAccountNumber", "104008");
		record3.addStringParam("belowSweepAmount", "500.00");
		record3.addStringParam("aboveSweepAmount", "200000.00");
		record3.addStringParam("frequency", "Daily");
		record3.addStringParam("startDate", "18/04/2023");
		record3.addStringParam("currencyCode", "USD");
		record3.addStringParam("endDate", "");
		record3.addStringParam("serviceRequestId", "STB76543");
		record3.addStringParam("sweepType", "Both");
		
				
		ds.addRecord(record1);
		ds.addRecord(record2);
		ds.addRecord(record3);
		
		result.addDataset(ds);

		return result;*/
	}
    
    private Result stubDeleteAccountSweep(Map<String, Object> inputParams, Result result) {
		result.addParam("primaryAccountNumber", inputParams.get("primaryAccountNumber").toString());
		result.addParam("secondaryAccountNumber", inputParams.get("secondaryAccountNumber").toString());
		result.addParam("serviceRequestId", "STB12345");
		result.addParam("message", "Account Sweep cancelled successfully");
		result.addParam("isSweepCreated", "false");
		return result;
	}
    
    private Result stubCreateAccountSweep(Map<String, Object> inputParams, Result result) {
		result.addParam("primaryAccountNumber", inputParams.get("primaryAccountNumber").toString());
		result.addParam("secondaryAccountNumber", inputParams.get("secondaryAccountNumber").toString());
		result.addParam("serviceRequestId", "STB12345");
		result.addParam("message", "Account Sweep created successfully");
		result.addParam("isSweepCreated", "true");
		return result;
		
	}
    
    
    private Result validateSuccess(Map<String, Object> inputParams, Result result) {
		result.addParam("primaryAccountNumber", inputParams.get("primaryAccountNumber").toString());
		result.addParam("secondaryAccountNumber", inputParams.get("secondaryAccountNumber").toString());
		result.addParam("message", "valid");
		result.addParam("opstatus", "0");
		return result;

	}
    
    private Result stubEditAccountSweep(Map<String, Object> inputParams, Result result) {
		result.addParam("primaryAccountNumber", inputParams.get("primaryAccountNumber").toString());
		result.addParam("secondaryAccountNumber", inputParams.get("secondaryAccountNumber").toString());
		result.addParam("serviceRequestId", "STB12345");
		result.addParam("message", "Account Sweep updated successfully");
		result.addParam("isSweepCreated", "true");
		result.addParam("isEdit", "true");
		return result;

	}
    
    private Result stubGetAccountSweepById(Result result) {
    	Dataset ds = new Dataset();
		ds.setId("AccountSweep");
		Record record = new Record();
		record.addStringParam("primaryAccountName", "Brice Bamford");
		record.addStringParam("primaryAccountNumber", "107093");
		record.addStringParam("secondaryAccountName", "Brice Bamford");
		record.addStringParam("secondaryAccountNumber", "101834");
		record.addStringParam("belowSweepAmount", "10");
		record.addStringParam("aboveSweepAmount", "1000.00");
		record.addStringParam("frequency", "Daily");
		record.addStringParam("startDate", "27/04/2023");
		record.addStringParam("currencyCode", "USD");
		record.addStringParam("endDate", "");
		record.addStringParam("serviceRequestId", "STB12345");
		record.addStringParam("sweepType", "Above");
		ds.addRecord(record);
		result.addDataset(ds);
		return result;
	}
    
    private String getBankDate(DataControllerRequest request)  {

        JsonObject dateObj = new JsonObject();
        try {
            String date = DBPServiceExecutorBuilder.builder().
                    withServiceId("TransactionObjects").
                    withObjectId("BankDate").
                    withOperationId("getBankDate").
                    withDataControllerRequest(request).
                    withRequestParameters(new HashMap<>()).
                    build().getResponse();
            JsonParser parser = new JsonParser();
            dateObj = parser.parse(date).getAsJsonObject().getAsJsonArray("date").get(0).getAsJsonObject();
        } catch (Exception e) {
            alert.prepareError("Error occurred while fetching bank date").log();
        }
         String currentDate = dateObj.has("currentWorkingDate") ? dateObj.get("currentWorkingDate").getAsString() : null;
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        String formattedBankDate ="";
        try {
			Date bankDate = sdf2.parse(currentDate);
			formattedBankDate =  sdf.format(bankDate);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			
		}
        
    
        return formattedBankDate;

    }
}
