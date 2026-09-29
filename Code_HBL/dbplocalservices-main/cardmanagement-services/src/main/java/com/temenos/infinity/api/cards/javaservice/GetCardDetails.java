package com.temenos.infinity.api.cards.javaservice;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbp.exception.ApplicationException;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.LegalEntityUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.api.ConfigurableParametersHelper;
import com.konylabs.middleware.api.OperationData;
import com.konylabs.middleware.api.ServiceRequest;
import com.konylabs.middleware.api.ServicesManager;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.dataobject.ResultToJSON;
import com.temenos.dbx.eum.product.usermanagement.businessdelegate.api.BackendIdentifierBusinessDelegate;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;
import com.temenos.dbx.product.dto.BackendIdentifierDTO;
import com.temenos.dbx.product.dto.DBXResult;
import com.temenos.infinity.api.cards.constants.Constants;

public class GetCardDetails implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(GetCardDetails.class);
	private static final String CUSTOMER_CARDS_SERVICE_ORCH = "CardsOrchestration";
	private static final String CUSTOMER_CARDS_OPERATION = "GetCustomerCards";
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
        Map<String, Object> customerSessionMap = CustomerSession.getCustomerMap(dcRequest);
        String customerUserName = dcRequest.getParameter("userName");//inputParams.get("username")!=null?inputParams.get("username").toString():"";
        LOG.debug("GetCardDetails:customerUserName:"+customerUserName);
        String customerId=getCustomerIdFromUsername(customerUserName, dcRequest);
        String coreCustomer=getCoreCustomerIdFromCustomerId(customerId, dcRequest);
        LOG.debug("GetCardDetails:coreCustomer:"+coreCustomer);
        inputParams.clear();
		inputParams.put("customerId", coreCustomer);
		if (!CustomerSession.IsAPIUser(customerSessionMap)) {
			AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl
					.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
			boolean hasBusinessPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(
					Constants.CARD_MANAGEMENT, "1", dcRequest.getHeaderMap(), dcRequest);
			boolean hasRetailPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(
					Constants.CARD_MANAGEMENT, "0", dcRequest.getHeaderMap(), dcRequest);
			if (!hasBusinessPermission && !hasRetailPermission) {
				return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
			}
			
			if (StringUtils.isNotBlank(coreCustomer)) {
				result= getCustomerCards(inputParams, dcRequest, dcRequest.getHeaderMap());
			}
		} else {
			if (StringUtils.isNotBlank(coreCustomer)) {
				result= getCustomerCards(inputParams, dcRequest, dcRequest.getHeaderMap());
				  LOG.debug("GetCardDetails:getCustomerCards:result:"+ResultToJSON.convert(result));
			}
				/*inputParams.put("$filter", "User_id eq '" + id + "'");
				result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
						URLConstants.CARDS_GET);
						*/
			}
		 Dataset ds = result.getAllDatasets().get(0);
		if (null != ds && null != ds.getAllRecords() && ds.getAllRecords().size() > 0) {
			postProcess(dcRequest, result);
		}
		LOG.debug("GetCardDetails:final card result:"+ResultToJSON.convert(result));
        return result;
    }
    public String getCustomerIdFromUsername(String customerUserName, DataControllerRequest dcRequest) {
    	String id=null;
    	Map inputParams = new HashMap<String, Object>();
    	inputParams.put("$filter","UserName eq '"+customerUserName+"'");
    	try {
    	Result cusResult = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
				URLConstants.CUSTOMER_GET);
		if (null != cusResult && null != cusResult.getDatasetById("customer")
				&& cusResult.getDatasetById("customer").getAllRecords().size() > 0) {
			id= cusResult.getDatasetById("customer").getAllRecords().get(0).getParamValueByName("id");
		}
    	}catch (Exception e) {
			return null;
		}
    	return id;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException, ParseException {
        List<Record> cards = result.getAllDatasets().get(0).getAllRecords();
        JSONObject response= new JSONObject();
       JSONArray Cards  = new JSONArray();
       
       LOG.debug("GetCardDetails:final card result:"+ResultToJSON.convert(result));
       
        for (Record card : cards) {
        	if(card.isEmpty()==false ) {
        	LOG.debug("GetCardDetails:card data:"+card.toString());
        	String accountId = HelperMethods.getFieldValue(card, "bankAccNum");
        	card.addParam("account_id", HelperMethods.getFieldValue(card, "bankAccNum"));
        	card.addParam("cardNumber", HelperMethods.getFieldValue(card, "pan"));
        	card.addParam("expirationDate", HelperMethods.getFieldValue(card, "cardExpDate"));
        	card.addParam("cardId", HelperMethods.getFieldValue(card, "pan"));
        	card.addParam("expiryDate", HelperMethods.getFieldValue(card, "cardExpDate"));
        	card.addParam("Reason", HelperMethods.getFieldValue(card, "reason"));
        	String cardType=HelperMethods.getFieldValue(card, "cardType");
        	card.addParam("cardType", cardType.equals("1")?"Debit":cardType.equals("2")?"Credit":cardType.equals("4")?"Prepaid":"");
        	card.addParam("userId", HelperMethods.getFieldValue(card, "customerId"));
        	card.addParam("User_id", HelperMethods.getFieldValue(card, "customerId"));
        	card.addParam("cardProductName", HelperMethods.getFieldValue(card, "Card_Label"));
        	card.addParam("cardHolderName", HelperMethods.getFieldValue(card, "chName"));
        	card.addParam("isTypeBusiness", HelperMethods.getFieldValue(card, "isTypeBusiness"));
        	card.addParam("isExpiring", HelperMethods.getFieldValue(card, "isExpiring"));
        	card.addParam("Id", HelperMethods.getFieldValue(card, "pan"));
        	String cardStatus= HelperMethods.getFieldValue(card, "cardStatus");
        	card.addParam("card_Status", cardStatus.equals("2")?"Active":cardStatus.equals("5")?"Inactive":cardStatus.equals("12")?"Locked":"");
        	card.addParam("cardStatus", cardStatus.equals("2")?"Active":cardStatus.equals("5")?"Inactive":cardStatus.equals("12")?"Locked":"");
        	card.addParam("action", cardStatus.equals("2")?"InActive":cardStatus.equals("5")?"Activate":cardStatus.equals("12")?"Unlock":"");
        	card.addParam("Action", cardStatus.equals("2")?"InActive":cardStatus.equals("5")?"Activate":cardStatus.equals("12")?"Unlock":"");
            setMaskedCardNumber(card);
            setAccountDetails(dcRequest, card);
            setIsExpiringFlag(card);
            JSONObject cardJson = convertRecordToJSONObject(card);
            Cards.put(cardJson);
        	}
        }
        LOG.debug("GetCardDetails:Cards Array:"+Cards);
        response.put("Cards", Cards);
        response.put("opStatus", 0);
        response.put("success","true");
        result.appendJson(response.toString());
    }

    private void setAccountDetails(DataControllerRequest dcRequest, Record card) throws HttpCallException {
        String accountId = HelperMethods.getFieldValue(card, "account_id");
        Result accountDetails = getAccountName(dcRequest, accountId);
        String accountName = HelperMethods.getFieldValue(accountDetails, "accountName");
        String currencyCode = HelperMethods.getFieldValue(accountDetails, "currencyCode");
        card.addParam(
                new Param("maskedAccountNumber", getMaskedAccountNumber(accountId), DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("accountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("transactionCurrency", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("currencyCode", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
    }

    private Result getAccountName(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
    }

    private String getMaskedAccountNumber(String accountNumber) {
    	if(accountNumber.length()>6)
    	{
    		int accLength=accountNumber.length();
        String initialsOfAccount = accountNumber.substring(0, 2);
        String maskedAccountNumber = accountNumber.substring(3, accLength-5).replaceAll("^[0-9]*$", "XXXXXXXXX");
        String lastDigits = accountNumber.substring(accLength-4, accLength);
        return initialsOfAccount.concat(maskedAccountNumber).concat(lastDigits);
    	}
    	return accountNumber;
    }

    private void setMaskedCardNumber(Record card) {
        String cardNumber = HelperMethods.getFieldValue(card, "cardNumber");
        String initialsOfCard = cardNumber.substring(0, 4);
        int initialLength= initialsOfCard.length();
        int length= cardNumber.length();
        String maskedCardNumber = cardNumber.substring(initialLength+1, length-5).replaceAll("^[0-9]*$", "XXXXXXXX");
        String lastDigits = cardNumber.substring(length-4, length);
        String fullCardNumber = initialsOfCard.concat(maskedCardNumber).concat(lastDigits);
        card.addParam(new Param("maskedCardNumber", fullCardNumber, DBPUtilitiesConstants.STRING_TYPE));
    }
    @SuppressWarnings("deprecation")
	private void  setIsExpiringFlag(Record card) throws ParseException
    {
    String expirationDate=HelperMethods.getFieldValue(card, "expirationDate");
    String args[]=expirationDate.trim().split("-");
    String year=args[2].length()==2?"20"+args[2]:"";
    int expYear=Integer.parseInt(year);
    int expMonth=getMonthNumber(args[1]);
    Calendar calendar1 = Calendar.getInstance();
    int currYear=calendar1.get(Calendar.YEAR);
	int currDate = (calendar1.get(Calendar.DATE));
	int currMonth = (calendar1.get(Calendar.MONTH) + 1);
	int numOfDaysInCurrentMonth = new Date(currYear, currMonth, 0).getDate();
	if((expYear==currYear)&&(expMonth==currMonth)&&((numOfDaysInCurrentMonth-currDate)<21))
	{
		card.addParam(new Param("isExpiring", "1", DBPUtilitiesConstants.STRING_TYPE));
	}
	else
	{
		card.addParam(new Param("isExpiring", "0", DBPUtilitiesConstants.STRING_TYPE));
	}
    
    }
    public int getMonthNumber(String month){
    	int monthNum=1;
    	if(StringUtils.isNotBlank(month)) {
    		switch (month.toUpperCase()) {
    		case "JAN":
    			monthNum=1;
    			break;
    		case "FEB":
    			monthNum=2;
    			break;
    		case "MAR":
    			monthNum=3;
    			break;
    		case "APR":
    			monthNum=4;
    			break;
    		case "MAY":
    			monthNum=5;
    			break;
    		case "JUN":
    			monthNum=6;
    			break;
    		case "JUL":
    			monthNum=7;
    			break;
    		case "AUG":
    			monthNum=8;
    			break;
    		case "SEP":
    			monthNum=9;
    			break;
    		case "OCT":
    			monthNum=10;
    			break;
    		case "NOV":
    			monthNum=11;
    			break;
    		case "DEC":
    			monthNum=12;
    			break;
    		default:
    			monthNum=1;
    		}	
    	}
    	return monthNum;
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result, boolean hasRetailPermission, boolean hasBusinessPermission) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        if(hasRetailPermission && !hasBusinessPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "( isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "0" + DBPUtilitiesConstants.OR +  "isTypeBusiness"+ DBPUtilitiesConstants.EQUAL + "null )";
        }
        if(hasBusinessPermission && !hasRetailPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "1";
        }
        filter = filter +  DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
public Result getCustomerCards(Map<String, Object> inputmap, DataControllerRequest dataControllerRequest, Map<String, Object> headermap) throws Exception {
	LOG.debug("getCustomerCards: Object service call inputmap:", inputmap);
	Result result = callObjectService("CardManagementServices", "ListOfCards", "getCustomerCards", inputmap,
			headermap, dataControllerRequest);
	return result;
}
public static Result callObjectService(String serviceID,String objid, String operationID, Map<String, Object> inputmap,
		Map<String, Object> headermap, DataControllerRequest dcRequest) throws Exception {
	Result result = null;
	try
	{
		OperationData operationData = dcRequest.getServicesManager().getOperationDataBuilder().withServiceId(serviceID).withObjectId(objid)
				.withOperationId(operationID).build();
		
		ServiceRequest serviceRequest = dcRequest.getServicesManager().getRequestBuilder(operationData)
				.withInputs(inputmap).withHeaders(headermap).withAuthorizationToken(dcRequest.getParameter("X-Kony-Authorization")).build();
		result = serviceRequest.invokeServiceAndGetResult();
	}
	catch(Exception e)
	{
		LOG.error("Error occurred: ", e);
		throw new Exception(" Exception Occured while invoking the serviceID : " + serviceID + " and operationID : " + operationID + " objectid : " + objid +" ; "+ e.getMessage() );
	}
	return result;
}
public static JSONObject convertRecordToJSONObject(Record record) {

	JSONObject jsonObj = new JSONObject();

	List<Param> arList = record.getAllParams();

	Iterator<Param> it = arList.iterator();
	while (it.hasNext()) {
		Param p = it.next();
		String key = p.getName();
		jsonObj.put(key, p.getValue());
	}
	return jsonObj;
}
private String getCoreCustomerIdFromCustomerId(String customerId, DataControllerRequest request)
		throws ApplicationException, Exception {
	String backendId = "";
	BackendIdentifierDTO backendIdentifierDTO = new BackendIdentifierDTO();
	ServicesManager sm = request.getServicesManager();
	ConfigurableParametersHelper paramHelper = sm.getConfigurableParametersHelper();
	String legalEntityId = paramHelper.getServerProperty(DBPUtilitiesConstants.BRANCH_ID_REFERENCE);
	backendIdentifierDTO.setCustomer_id(customerId);
	backendIdentifierDTO.setCompanyLegalUnit(legalEntityId);
	backendIdentifierDTO.setBackendType("T24");
	if (StringUtils.isNotBlank(backendIdentifierDTO.getCustomer_id())) {
		BackendIdentifierBusinessDelegate backendIdentifierBusinessDelegate = DBPAPIAbstractFactoryImpl
				.getBusinessDelegate(BackendIdentifierBusinessDelegate.class);
		DBXResult backendIdentifiers = backendIdentifierBusinessDelegate.get(backendIdentifierDTO,
				request.getHeaderMap());
		if (backendIdentifiers != null && backendIdentifiers.getResponse() != null) {
			backendIdentifierDTO = (BackendIdentifierDTO) backendIdentifiers.getResponse();
			backendId = backendIdentifierDTO.getBackendId();
		}
	}
	return backendId;
}
}

/*package com.temenos.infinity.api.cards.javaservice;

import java.text.ParseException;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import com.kony.dbputilities.util.Log4j2Configurator;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.temenos.dbx.product.commons.businessdelegate.api.AuthorizationChecksBusinessDelegate;
import com.temenos.infinity.api.cards.constants.Constants;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.kony.dbputilities.util.LegalEntityUtil;

public class GetCardDetails implements JavaService2 {
    @SuppressWarnings("rawtypes")
    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
		Log4j2Configurator.getInstance();
        Result result = new Result();
        Map inputParams = HelperMethods.getInputParamMap(inputArray);
        
        AuthorizationChecksBusinessDelegate authorizationChecksBusinessDelegate = DBPAPIAbstractFactoryImpl.getBusinessDelegate(AuthorizationChecksBusinessDelegate.class);
        boolean hasBusinessPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(Constants.CARD_MANAGEMENT, "1", dcRequest.getHeaderMap(), dcRequest);
        boolean hasRetailPermission = authorizationChecksBusinessDelegate.isUserAuthorizedForPayeeOperations(Constants.CARD_MANAGEMENT, "0", dcRequest.getHeaderMap(), dcRequest);
        if(!hasBusinessPermission && !hasRetailPermission ) {
			return ErrorCodeEnum.ERR_12001.setErrorCode(new Result());
		}
        
        if (preProcess(inputParams, dcRequest, result, hasRetailPermission, hasBusinessPermission)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.CARDS_GET);
        }
        if (HelperMethods.hasRecords(result)) {
            postProcess(dcRequest, result);
        }
        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Result result) throws HttpCallException, ParseException {
        List<Record> cards = result.getAllDatasets().get(0).getAllRecords();
        for (Record card : cards) {
            setMaskedCardNumber(card);
            setAccountDetails(dcRequest, card);
            setIsExpiringFlag(card);
        }
    }

    private void setAccountDetails(DataControllerRequest dcRequest, Record card) throws HttpCallException {
        String accountId = HelperMethods.getFieldValue(card, "account_id");
        Result accountDetails = getAccountName(dcRequest, accountId);
        String accountName = HelperMethods.getFieldValue(accountDetails, "accountName");
        String currencyCode = HelperMethods.getFieldValue(accountDetails, "currencyCode");
        card.addParam(
                new Param("maskedAccountNumber", getMaskedAccountNumber(accountId), DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("accountName", accountName, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("transactionCurrency", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
        card.addParam(new Param("currencyCode", currencyCode, DBPUtilitiesConstants.STRING_TYPE));
    }

    private Result getAccountName(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + accountId;
        return HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
    }

    private String getMaskedAccountNumber(String accountNumber) {
    	if(accountNumber.length()>6)
    	{
    		int accLength=accountNumber.length();
        String initialsOfAccount = accountNumber.substring(0, 2);
        String maskedAccountNumber = accountNumber.substring(3, accLength-5).replaceAll("^[0-9]*$", "XXXXXXXXX");
        String lastDigits = accountNumber.substring(accLength-4, accLength);
        return initialsOfAccount.concat(maskedAccountNumber).concat(lastDigits);
    	}
    	return accountNumber;
    }

    private void setMaskedCardNumber(Record card) {
        String cardNumber = HelperMethods.getFieldValue(card, "cardNumber");
        String initialsOfCard = cardNumber.substring(0, 4);
        String maskedCardNumber = cardNumber.substring(5, 11).replaceAll("^[0-9]*$", "XXXXXXXX");
        String lastDigits = cardNumber.substring(12, 16);
        String fullCardNumber = initialsOfCard.concat(maskedCardNumber).concat(lastDigits);
        card.addParam(new Param("maskedCardNumber", fullCardNumber, DBPUtilitiesConstants.STRING_TYPE));
    }
    @SuppressWarnings("deprecation")
	private void  setIsExpiringFlag(Record card) throws ParseException
    {
    String expirationDate=HelperMethods.getFieldValue(card, "expirationDate");
    String args[]=expirationDate.trim().split("-");
    int expYear=Integer.parseInt(args[0]);
    int expMonth=Integer.parseInt(args[1]);
    Calendar calendar1 = Calendar.getInstance();
    int currYear=calendar1.get(Calendar.YEAR);
	int currDate = (calendar1.get(Calendar.DATE));
	int currMonth = (calendar1.get(Calendar.MONTH) + 1);
	int numOfDaysInCurrentMonth = new Date(currYear, currMonth, 0).getDate();
	if((expYear==currYear)&&(expMonth==currMonth)&&((numOfDaysInCurrentMonth-currDate)<21))
	{
		card.addParam(new Param("isExpiring", "1", DBPUtilitiesConstants.STRING_TYPE));
	}
	else
	{
		card.addParam(new Param("isExpiring", "0", DBPUtilitiesConstants.STRING_TYPE));
	}
    
    }
    @SuppressWarnings({ "unchecked", "rawtypes" })
    private boolean preProcess(Map inputParams, DataControllerRequest dcRequest, Result result, boolean hasRetailPermission, boolean hasBusinessPermission) {
        String userId = HelperMethods.getUserIdFromSession(dcRequest);
        String filter = "User_id" + DBPUtilitiesConstants.EQUAL + userId;
        if(hasRetailPermission && !hasBusinessPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "( isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "0" + DBPUtilitiesConstants.OR +  "isTypeBusiness"+ DBPUtilitiesConstants.EQUAL + "null )";
        }
        if(hasBusinessPermission && !hasRetailPermission) {
        	filter = filter + DBPUtilitiesConstants.AND + "isTypeBusiness" + DBPUtilitiesConstants.EQUAL + "1";
        }
        filter = filter +  DBPUtilitiesConstants.AND + "legalEntityId" + DBPUtilitiesConstants.EQUAL + LegalEntityUtil.getLegalEntityIdFromSessionOrCache(dcRequest);
        inputParams.put(DBPUtilitiesConstants.FILTER, filter);
        return true;
    }
}
*/
