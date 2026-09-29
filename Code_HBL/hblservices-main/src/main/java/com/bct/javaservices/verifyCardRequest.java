package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.bct.custom.constants.HBLURLConstants;
import com.bct.utilities.HBLCommonUtility;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ErrorCodeEnum;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbx.util.CommonUtils;
import com.kony.dbx.util.Constants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.infinity.api.arrangements.utils.ArrangementsUtils;

public class verifyCardRequest implements JavaService2 {
	private static final Logger LOG = LogManager.getLogger(verifyCardRequest.class);

	@Override
	public Object invoke(String methodId, Object[] inputArray, DataControllerRequest request,
			DataControllerResponse response) throws Exception {
		Result result = new Result();
		try {
			String customerId = ArrangementsUtils.getUserAttributeFromIdentity(request, "customer_id");
			String coreIdentifier = HBLCommonUtility.getCoreBackendId(request);
			Map<String, Object> inputParams = HelperMethods.getInputParamObjectMap(inputArray);
			LOG.debug("HBL:RequestNewCardService: processPayment:inputParams:" + inputParams);

			LOG.debug("CustomerId ##: " + customerId);
			LOG.debug("coreIdentifier ##: " + coreIdentifier);

			boolean isCardRequestExists = getCardRequestData(request, customerId, coreIdentifier);
			if (isCardRequestExists) {
				result.setParam(new Param("status",
						"Your card request is already in progress. For more details, please contact the bank."));
				result.setParam(new Param("isCardReqExists", "true"));
			} else if (!isCardRequestExists) {
				result.setParam(new Param("status", "No card requests found for the selected data."));
				result.setParam(new Param("isCardReqExists", "false"));
			} else {
				result.setParam(new Param("ErrMsg", "Request check failed!"));
				result.setParam(new Param("errmsg", "Request check failed!"));
			}

			result.setParam(new Param("opstatus", "0"));
			result.setParam(new Param("httpStatusCode", "200"));
		} catch (Exception e) {
			LOG.error("Exception occured in verifyCardRequest:::" + e.getMessage(), e);
			ErrorCodeEnum.ERR_10021.setErrorCode(result);
			result.addParam(new Param("dbpErrMsg", e.getLocalizedMessage()));
			result.addParam(new Param("success", "false"));
		}
		return result;
	}

	
	
	private static boolean getCardRequestData(DataControllerRequest request, String customerId, String coreIdentifier) {

		boolean IsCardAlreadyRequested = false;
		try {
			LOG.debug("customerId  getCardRequestData##:" + customerId);
			LOG.debug("coreIdentifier  getCardRequestData##:" + coreIdentifier);
			String cardType = request.getParameter("cardType");
			String debitAccount = request.getParameter("debitAccount");
			String serviceProvider = request.getParameter("serviceProvider");
			
			String filter =	"customerId" + DBPUtilitiesConstants.EQUAL + customerId + DBPUtilitiesConstants.AND + 
					"coreIdentifier" + DBPUtilitiesConstants.EQUAL + coreIdentifier + DBPUtilitiesConstants.AND + 
					"cardType" + DBPUtilitiesConstants.EQUAL + cardType + DBPUtilitiesConstants.AND + 
					"debitAccount" + DBPUtilitiesConstants.EQUAL + debitAccount + DBPUtilitiesConstants.AND + 
					"serviceProvider" + DBPUtilitiesConstants.EQUAL + serviceProvider + DBPUtilitiesConstants.AND + 
					"status" + DBPUtilitiesConstants.EQUAL + "PENDING" ;
			LOG.debug("filter##:" + filter);
			HashMap<String, Object> svcHeaders = new HashMap<String, Object>();
			HashMap<String, Object> svcParams = new HashMap<String, Object>();

			svcParams.put(Constants.PARAM_DOLLAR_FILTER, filter);
			Result result = CommonUtils.callIntegrationService(request, svcParams, svcHeaders,
					Constants.DBX_DB_SERVICE_NAME, HBLURLConstants.REQUEST_NEW_CARD_GET, false);
			Dataset cardrequest = result.getDatasetById(HBLURLConstants.REQUEST_NEW_CARD_DATASET);
			if (null != cardrequest && cardrequest.getAllRecords().size()>0) {

				IsCardAlreadyRequested = true;
			} else {
				LOG.debug("Else IsCardAlreadyRequested:");
				IsCardAlreadyRequested = false;
			}

			LOG.debug("IsCardAlreadyRequested:" + IsCardAlreadyRequested);

		} catch (Exception e) {

			LOG.error("Error while retrieving getCardRequestData for Customer ");
		}
		return IsCardAlreadyRequested;

	}
}
