package com.kony.dbputilities.transservices;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.kony.dbputilities.dbutil.QueryFormer;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.CreateTransferHelper;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.HelperMethods;
import com.kony.dbputilities.util.MWConstants;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.common.JavaService2;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.controller.DataControllerResponse;
import com.konylabs.middleware.dataobject.Param;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.commonsutils.CustomerSession;

public class CreateTransfer implements JavaService2 {
	ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);

    @Override
    public Object invoke(String methodID, Object[] inputArray, DataControllerRequest dcRequest,
            DataControllerResponse dcResponse) throws Exception {
        Result result = new Result();
        Map<String, String> inputParams = HelperMethods.getInputParamMap(inputArray);
        boolean status = true;
        status = preProcess(inputParams, dcRequest, result);
        if (inputParams.containsKey(DBPUtilitiesConstants.FILTER)) {
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.TRANSACTION_GET);
            postProcess(dcRequest, inputParams, result);
        }
        if (status) {
        	if("MSSQL".equalsIgnoreCase(QueryFormer.getDBType(dcRequest)))
        	{
        		result=TransOperations.createDataSetInsert(dcRequest, inputParams);
        	}
        	else
            result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNT_TRANSACTION_CREATE);
        	/* result = HelperMethods.callApi(dcRequest, inputParams, HelperMethods.getHeaders(dcRequest),TransactionUtils.getCreateTransactionDBService()); */
            status = !HelperMethods.hasError(result);
            
            if (status) {
            	result = postProcess(dcRequest, result);
            }
        }

        return result;
    }

    private void postProcess(DataControllerRequest dcRequest, Map<String, String> inputParams, Result result)
            throws HttpCallException {
        if (HelperMethods.hasRecords(result)) {
            List<Record> transactions = result.getAllDatasets().get(0).getAllRecords();
            for (Record transaction : transactions) {
                updateFromAccountDetails(dcRequest, transaction);
                updateToAccountDetails(dcRequest, transaction);
                updatePayPersonDetails(dcRequest, transaction);
                updateBillDetails(dcRequest, transaction);
                updatePayeeDetails(dcRequest, transaction);
                updateDateFormat(transaction);
            }
        }
    }

    private Result postProcess(DataControllerRequest request, Result result) {
        Result retResult = new Result();
        updateValidDate(retResult, result);
        retResult.addParam(new Param("cashlessPersonName", HelperMethods.getFieldValue(result, "cashlessPersonName"),
                DBPUtilitiesConstants.STRING_TYPE));
        retResult.addParam(new Param("otp", HelperMethods.getFieldValue(result, "cashlessOTP"),
                DBPUtilitiesConstants.STRING_TYPE));
        retResult.addParam(
                new Param("referenceId", HelperMethods.getFieldValue(result, "Id"), DBPUtilitiesConstants.STRING_TYPE));
        retResult.addParam(new Param("success", "success", DBPUtilitiesConstants.STRING_TYPE));
        retResult.addParam(new Param("status", "success", DBPUtilitiesConstants.STRING_TYPE));
        return retResult;
    }
	
    private void updateValidDate(Result retResult, Result ipResult) {
        String otpValidDate = HelperMethods.getFieldValue(ipResult, "cashlessOTPValidDate");
        if (StringUtils.isNotBlank(otpValidDate)) {
            long timeDiff = HelperMethods.getFormattedTimeStamp(otpValidDate).getTime() - new Date().getTime();
            timeDiff = timeDiff / 1000;
            long m = (timeDiff / 60) % 60;
            long h = (timeDiff / (60 * 60)) % 24;
            retResult.addParam(new Param("validDate", (String.valueOf(h) + "h:" + String.valueOf(m) + "m"),
                    DBPUtilitiesConstants.STRING_TYPE));
        }
    }

    private boolean preProcess(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        boolean status = false;
        boolean isSchedulingEngine = false;
        String transactionType = inputParams.get(DBPUtilitiesConstants.TRANSACTION_TYPE);
        Map<String, String> map = HelperMethods.getCustomerFromIdentityService(dcRequest);
        Map<String, Object> customer = CustomerSession.getCustomerMap(dcRequest);
        String legalEntityId = (String) customer.get("legalEntityId");
		inputParams.put("legalEntityId", legalEntityId);
        if (map.containsKey("isSchedulingEngine") && map.get("isSchedulingEngine") != null
                && map.get("isSchedulingEngine").equals("true")) {
            isSchedulingEngine = true;
        }

        if (!isSchedulingEngine) {

            status = validations(inputParams, dcRequest, result);

        }

        if (status || isSchedulingEngine) {
            CreateTransferHelper helper = new CreateTransferHelper();
            inputParams.put(DBPUtilitiesConstants.CREATED_DATE, application.getServerTimeStamp());
            
            if (DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT.equalsIgnoreCase(transactionType)) {
                status = helper.createNewDeposit(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_INTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
                status = helper.createNewInternalTransfer(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_EXTERNAL_TRANSFER.equalsIgnoreCase(transactionType)) {
                status = helper.createNewExternalTransfer(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_PAY_BILL.equalsIgnoreCase(transactionType)) {
                status = helper.payNewBill(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_P2P.equalsIgnoreCase(transactionType)) {
                status = helper.payPerson(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN.equalsIgnoreCase(transactionType)) {
                status = helper.payLoan(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_CARDLESS.equalsIgnoreCase(transactionType)) {
                status = helper.cardlessCash(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_REQUEST.equalsIgnoreCase(transactionType)) {
                status = helper.createP2PRequestMoney(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_WIRE.equalsIgnoreCase(transactionType)) {
                status = helper.wireTransfer(inputParams, dcRequest, result);
            } else if (DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST
                    .equalsIgnoreCase(transactionType)) {
                status = helper.stopCheckPaymentRequest(inputParams, dcRequest, result);
            }
        }
        return status;
    }

    private boolean validations(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        boolean status = true;
        String transactionType = inputParams.get(DBPUtilitiesConstants.TRANSACTION_TYPE);

        if (!StringUtils.isNotBlank(transactionType)) {
            HelperMethods.setValidationMsg("Edit operation not permitted for this Transaction type", dcRequest, result);
            status = false;
        }
        return status;
    }

    private boolean accountValidation(Map<String, String> inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        Result userResult = new Result();
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String fromAccountNum = inputParams.get("fromAccountNumber");
        String userId = user.get("customer_id");
        String userType = user.get("customerType");
        if (StringUtils.isNotBlank(fromAccountNum) && StringUtils.isNotBlank(userId)) {
            if (HelperMethods.isBusinessUserType(userType)) {
                String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
                        + "Customer_id" + DBPUtilitiesConstants.EQUAL + userId;
                userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.CUSTOMERACCOUNTS_GET);
            } else {
                String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + fromAccountNum + DBPUtilitiesConstants.AND
                        + DBPUtilitiesConstants.USER_ID + DBPUtilitiesConstants.EQUAL + userId;
                userResult = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                        URLConstants.ACCOUNTS_GET);
            }
            return HelperMethods.hasRecords(userResult);
        }
        return true;
    }

    private void updateFromAccountDetails(DataControllerRequest dcRequest, Record transaction)
            throws HttpCallException {
        String frmAccountNum = HelperMethods.getFieldValue(transaction, "fromAccountNumber");
        if (StringUtils.isNotBlank(frmAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + frmAccountNum;
            Result frmAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String type = HelperMethods.getFieldValue(frmAccount, "typeDescription");
            transaction.addParam(new Param("fromAccountType", type, MWConstants.STRING));
            String accountName = HelperMethods.getFieldValue(frmAccount, "accountName");
            transaction.addParam(new Param("fromAccountName", accountName, MWConstants.STRING));
            String nickName = HelperMethods.getFieldValue(frmAccount, "nickName");
            if (StringUtils.isBlank(nickName)) {
                nickName = accountName;
            }
            transaction.addParam(new Param("fromAccountNickName", nickName, MWConstants.STRING));
        }
    }

    private void updateToAccountDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String toAccountNum = HelperMethods.getFieldValue(transaction, "toAccountNumber");
        if (StringUtils.isNotBlank(toAccountNum)) {
            String filter = "Account_id" + DBPUtilitiesConstants.EQUAL + toAccountNum;
            Result toAccount = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.ACCOUNTS_GET);
            String type = HelperMethods.getFieldValue(toAccount, "typeDescription");
            transaction.addParam(new Param("toAccountType", type, MWConstants.STRING));
            String accountName = HelperMethods.getFieldValue(toAccount, "accountName");
            transaction.addParam(new Param("toAccountName", accountName, MWConstants.STRING));
        }
    }

    private void updatePayPersonDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payPersonId = HelperMethods.getFieldValue(transaction, "Person_Id");
        if (StringUtils.isNotBlank(payPersonId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + payPersonId;
            Result payPerson = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYPERSON_GET);
            if (HelperMethods.hasRecords(payPerson)) {
                Record person = payPerson.getAllDatasets().get(0).getRecord(0);
                transaction
                        .addParam(new Param("phone", HelperMethods.getFieldValue(person, "phone"), MWConstants.STRING));
                transaction
                        .addParam(new Param("email", HelperMethods.getFieldValue(person, "email"), MWConstants.STRING));
                transaction
                        .addParam(new Param("name", HelperMethods.getFieldValue(person, "name"), MWConstants.STRING));
            }
        }
    }

    private void updateBillDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String billId = HelperMethods.getFieldValue(transaction, "Bill_id");
        if (StringUtils.isNotBlank(billId)) {
            String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
            Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.BILL_GET);
            if (HelperMethods.hasRecords(biller)) {
                Record bill = biller.getAllDatasets().get(0).getRecord(0);
                String payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
                fetchAndUpdatePayee(dcRequest, payeeId, transaction);
            }
        }
    }

    private void fetchAndUpdatePayee(DataControllerRequest dcRequest, String payeeId, Record transaction)
            throws HttpCallException {
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(
                        new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"), MWConstants.STRING));
            }
        }
    }

    private void updatePayeeDetails(DataControllerRequest dcRequest, Record transaction) throws HttpCallException {
        String payeeId = HelperMethods.getFieldValue(transaction, "Payee_Id");
        if (StringUtils.isNotBlank(payeeId)) {
            String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
            Result payees = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                    URLConstants.PAYEE_GET);
            if (HelperMethods.hasRecords(payees)) {
                Record payee = payees.getAllDatasets().get(0).getRecord(0);
                transaction.addParam(
                        new Param("payeeNickName", HelperMethods.getFieldValue(payee, "nickName"), MWConstants.STRING));
            }
        }
    }

    private void updateDateFormat(Record transaction) {
        String scheduledDate = HelperMethods.getFieldValue(transaction, "scheduledDate");
        String transactionDate = HelperMethods.getFieldValue(transaction, "transactionDate");
        try {
            if (StringUtils.isNotBlank(scheduledDate)) {
                transaction.addParam(new Param("scheduledDate",
                        HelperMethods.convertDateFormat(scheduledDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            if (StringUtils.isNotBlank(transactionDate)) {
                transaction.addParam(new Param("transactionDate",
                        HelperMethods.convertDateFormat(transactionDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            String frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyEndDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyEndDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
            frequencyDate = HelperMethods.getFieldValue(transaction, "frequencyStartDate");
            if (StringUtils.isNotBlank(frequencyDate)) {
                transaction.addParam(new Param("frequencyStartDate",
                        HelperMethods.convertDateFormat(frequencyDate, "yyyy-MM-dd'T'hh:mm:ss'Z'"), "String"));
            }
        } catch (Exception e) {
        }
    }

}