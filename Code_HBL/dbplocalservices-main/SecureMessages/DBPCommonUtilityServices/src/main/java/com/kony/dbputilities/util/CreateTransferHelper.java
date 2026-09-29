package com.kony.dbputilities.util;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.security.SecureRandom;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.dbp.core.util.JSONUtils;
import com.kony.dbputilities.exceptions.HttpCallException;
import com.konylabs.middleware.api.ServicesManagerHelper;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;
import com.konylabs.middleware.ehcache.ResultCache;
import com.konylabs.middleware.exceptions.MiddlewareException;


public class CreateTransferHelper {
	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
    @SuppressWarnings("rawtypes")
    public boolean validateOnDeposit(Map inputParams, DataControllerRequest dcRequest, Result result) {
        String amount = (String) inputParams.get(DBPInputConstants.AMOUNT);
        if (DBPUtilitiesConstants.MAX_DEPOSIT_AMOUNT.compareTo(new BigDecimal(amount)) < 0) {
            HelperMethods.setValidationMsg("Amount Greater than Allowed Maximum Deposit", dcRequest, result);
            return false;
        }
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean createNewDeposit(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {

        if (validateOnDeposit(inputParams, dcRequest, result)) {

            String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
            String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_DEPOSIT);
            String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
            String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
            String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);

            inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
            inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
            inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
            inputParams.put("isScheduled", "false");
            inputParams.put("checkNumber", String.valueOf(getDepositCheckNumber()));

            if (StringUtils.isBlank(frmAccountNumber)) {
                inputParams.put(DBPUtilitiesConstants.FRM_ACCONT, null);
            }
            Map toAccountDetails = getAccountDetails(dcRequest, toAccountNumber);
            BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
            BigDecimal availBal = new BigDecimal((String) toAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            BigDecimal newBal = availBal.add(transAmnt);
            inputParams.put(DBPUtilitiesConstants.TO_ACCOUNT_BALANCE, newBal.toPlainString());
            inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
            inputParams.put("description", DBPUtilitiesConstants.MOBILE_DEPOSIT_MESSAGE
                    + (String) toAccountDetails.get(DBPUtilitiesConstants.N_NAME));
            updateAccountBalance(dcRequest, toAccountNumber, newBal.toPlainString());
            HelperMethods.removeNullValues(inputParams);
        }
        return true;
    }

    public static long getDepositCheckNumber() {
        SecureRandom rnd = new SecureRandom();
        long generatedValue;
        generatedValue = 100000L + rnd.nextInt(90000);
        return generatedValue;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean createNewInternalTransfer(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {

        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_INTERNAL_TRANSFER);
        String frequencyType = (String) inputParams.get(DBPUtilitiesConstants.FREQUENCY_TYPE);
        String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String payeeId = (String) inputParams.get("payeeId");
        Map toAccountDetails = getAccountDetails(dcRequest, toAccountNumber);
        Map frmAccountDetails = getAccountDetails(dcRequest, frmAccountNumber);

        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("Payee_id", payeeId);

        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }

        if (!"Once".equalsIgnoreCase(frequencyType)) {
            String noOfRecurr = (String) inputParams.get(DBPUtilitiesConstants.NO_OF_RECURRENCES);
            if (StringUtils.isNotBlank(noOfRecurr)) {
                inputParams.put(DBPUtilitiesConstants.RECUR_DESC, "1 of " + noOfRecurr);
            }

        }

        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);

        String statusDesc = StringUtils.isNotEmpty(inputParams.get("status")+"") ? inputParams.get("status")+"" : DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, statusDesc);
        
        if (scheduledDate.after(new Date())) {

            inputParams.put("isScheduled", "true");
			inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL,
					frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL,
                    toAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));

        } else {

            inputParams.put("isScheduled", "false");
            BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
            BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            BigDecimal toBal = new BigDecimal((String) toAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, frmBal.subtract(transAmnt).toPlainString());
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, toBal.add(transAmnt).toPlainString());

            if (0 <= frmBal.compareTo(transAmnt)) {

                updateAccountBalance(dcRequest, toAccountNumber, toBal.add(transAmnt).toPlainString());
                updateAccountBalance(dcRequest, frmAccountNumber, frmBal.subtract(transAmnt).toPlainString());
            }
        }

        String nickName = (String) toAccountDetails.get(DBPUtilitiesConstants.N_NAME);

        if (StringUtils.isBlank(nickName)) {
            nickName = (String) inputParams.get("benificiaryName");
        }

        inputParams.put("description", DBPUtilitiesConstants.TRANSFER_MESSAGE + nickName);

        HelperMethods.removeNullValues(inputParams);
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean createNewExternalTransfer(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        boolean status = true;
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_EXTERNAL_TRANSFER);
        String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
        String iBAN = (String) inputParams.get(DBPInputConstants.IBAN);
        if (StringUtils.isBlank(iBAN)) {
        	iBAN = (String) inputParams.get(DBPInputConstants.IBAN_SMALL);
        }
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String frequencyType = (String) inputParams.get(DBPUtilitiesConstants.FREQUENCY_TYPE);
        String fee = (String) inputParams.get(DBPInputConstants.FEE_AMOUNT);
        String payeeId = (String) inputParams.get("payeeId");

        String nickName = "";
        Map frmAccountDetails = getAccountDetails(dcRequest, frmAccountNumber);
        
        inputParams.put("Payee_id", payeeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }
        if (StringUtils.isNotBlank(toAccountNumber)) {
            inputParams.put(DBPUtilitiesConstants.TO_EXT_ACCT_NUM, toAccountNumber);
        } else {
            inputParams.put(DBPInputConstants.IBAN, iBAN);
        }
        inputParams.put(DBPUtilitiesConstants.TO_ACCONT, null);
        if (!"Once".equalsIgnoreCase(frequencyType)) {
            String noOfRecurr = (String) inputParams.get(DBPUtilitiesConstants.NO_OF_RECURRENCES);
            if (StringUtils.isNotBlank(noOfRecurr)) {
                inputParams.put(DBPUtilitiesConstants.RECUR_DESC, "1 of " + noOfRecurr);
            }
        }
        Map extAccountDetails = null;
        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        Date scheduledDate = HelperMethods.getFormattedTimeStamp(scheduledDt);
        if (scheduledDate.after(new Date())) {
            inputParams.put("isScheduled", "true");
            inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL,
                    frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);
            extAccountDetails = getExtAccountDetails(dcRequest, toAccountNumber, iBAN);
        } else {
            inputParams.put("isScheduled", "false");
            String statusDesc = StringUtils.isNotEmpty(inputParams.get("status")+"") ? inputParams.get("status")+"" : DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
            inputParams.put(DBPUtilitiesConstants.STATUS_DESC, statusDesc);
            BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
            BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, frmBal.subtract(transAmnt).toPlainString());
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);
            if (0 <= frmBal.compareTo(transAmnt)) {
                updateAccountBalance(dcRequest, frmAccountNumber, frmBal.subtract(transAmnt).toPlainString());
                extAccountDetails = getExtAccountDetails(dcRequest, toAccountNumber, iBAN);
            } else {
                status = false;
                HelperMethods.setValidationMsg("Amount Greater than Allowed Maximum Deposit", dcRequest, result);
            }
        }

        if (extAccountDetails != null) {
            nickName = (String) extAccountDetails.get(DBPUtilitiesConstants.N_NAME);
        }

        if (StringUtils.isBlank(nickName)) {
            nickName = (String) inputParams.get("beneficiaryName");
        }
        
        if (StringUtils.isNotBlank(fee)) {
            inputParams.put("fee", fee);
        }

        inputParams.put("description", DBPUtilitiesConstants.TRANSFER_MESSAGE + nickName);
        HelperMethods.removeNullValues(inputParams);
        return status;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean payNewBill(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        final String DEFAULT_BILL_ID = "4";
        boolean status = true;
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_PAY_BILL);
        String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String userId = user.get("customer_id");
        Map frmAccountDetails = new HashMap<String, String>();
		try {
			frmAccountDetails = JSONUtils.parseAsMap(fetchMyAccounts(userId).get(frmAccountNumber).toString(), String.class, String.class);
		} catch (JSONException e) {
			alert.prepareError(e).log();
		} catch (IOException e) {
			alert.prepareError(e).log();
		}
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String billId = (String) inputParams.get("billid");
        String payeeId = (String) inputParams.get("payeeId");
        String billerId = (String) inputParams.get("billerId");
        String amount = (String) inputParams.get("amount");
        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }
        if (StringUtils.isNotBlank(billId) && !"0".equals(billId)) {
            inputParams.put("Bill_id", billId);
            Record bill = getBill(dcRequest, billId);
            payeeId = HelperMethods.getFieldValue(bill, "Payee_id");
            BigDecimal totalPaid = new BigDecimal(amount)
                    .add(new BigDecimal(HelperMethods.getFieldValue(bill, "paidAmount")));
            updateBillDetails(dcRequest, billId, totalPaid, currentDate, bill);
        } else if (StringUtils.isNotBlank(payeeId) && !"0".equals(payeeId)) {
            inputParams.put("Bill_id", DEFAULT_BILL_ID);
            inputParams.put("billid", DEFAULT_BILL_ID);
            Record bill = getBill(dcRequest, DEFAULT_BILL_ID);
            BigDecimal totalPaid = new BigDecimal(amount)
                    .add(new BigDecimal(HelperMethods.getFieldValue(bill, "paidAmount")));
            updateBillDetails(dcRequest, DEFAULT_BILL_ID, totalPaid, currentDate, bill);
        } else if (StringUtils.isNotBlank(toAccountNumber)) {
            inputParams.put("toExternalAccountNumber", toAccountNumber);
        }
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("Payee_id", payeeId);

        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        Date scheduledDate = StringUtils.isNotBlank(scheduledDt) ? HelperMethods.getFormattedTimeStamp(scheduledDt)
                : new Date();
        
        String statusDesc = StringUtils.isNotEmpty(inputParams.get("status")+"") ? inputParams.get("status")+"" : DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, statusDesc);
        
        if (scheduledDate.after(new Date())) {
            inputParams.put("isScheduled", "true");
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL,
                    frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);
        } else {
            inputParams.put("isScheduled", "false");
            if(!frmAccountDetails.isEmpty()) {
            BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            BigDecimal transAmnt = new BigDecimal(amount);
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);
            if (0 <= frmBal.compareTo(transAmnt)) {
                String newBal = frmBal.subtract(transAmnt).toPlainString();
                updateAccountBalance(dcRequest, frmAccountNumber, newBal);
                inputParams.put("fromAccountBalance", newBal);
				} else {
                status = false;
                HelperMethods.setValidationMsg("Amount Greater than Allowed Maximum Deposit", dcRequest, result);
            }
            }
        }
        String frequencyType = (String) inputParams.get(DBPUtilitiesConstants.FREQUENCY_TYPE);
        if (!"Once".equalsIgnoreCase(frequencyType)) {
            String noOfRecurr = (String) inputParams.get(DBPUtilitiesConstants.NO_OF_RECURRENCES);
            if (StringUtils.isNotBlank(noOfRecurr)) {
                inputParams.put(DBPUtilitiesConstants.RECUR_DESC, "1 of " + noOfRecurr);
            }
        }
        String payeeNickName = getPayeeNickName(dcRequest, payeeId);
        
        if (StringUtils.isNotBlank(payeeNickName)) {
            inputParams.put("description", "Bill Pay To " + payeeNickName);
        } else {
        	String billerName = getBillerName(dcRequest, billerId);
            inputParams.put("description", "Bill Pay To " + billerName);
        }
        HelperMethods.removeNullValues(inputParams);

        return status;
    }

    private String getPayeeNickName(DataControllerRequest dcRequest, String payeeId) throws HttpCallException {
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        return HelperMethods.getFieldValue(payee, "nickName");
    }

    private String getBillerName(DataControllerRequest dcRequest, String billerId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billerId;

        Result biller = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILLER_MASTER_GET);

        return HelperMethods.getFieldValue(biller, "billerName");
    }

    private void updateBillDetails(DataControllerRequest dcRequest, String billId, BigDecimal totalPaid,
            String currentDate, Record bill) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("id", billId);
        input.put("paidAmount", totalPaid.toString());
        input.put("paidDate", currentDate);
        String billDueDate = HelperMethods.getFieldValue(bill, "billDueDate");
        Calendar cal = Calendar.getInstance();
        cal.setTime(HelperMethods.getFormattedTimeStamp(billDueDate));
        cal.add(Calendar.MONTH, 1);
        input.put("billDueDate", HelperMethods.getFormattedTimeStamp(cal.getTime(), "yyyy-MM-dd"));
        input.put("statusDesc", "TRANSACTION_STATUS_SUCCESSFUL");
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.BILL_UPDATE);
    }

    private Record getBill(DataControllerRequest dcRequest, String billId) throws HttpCallException {
        String filter = "id" + DBPUtilitiesConstants.EQUAL + billId;
        Result bill = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.BILL_GET);
        return bill.getAllDatasets().get(0).getRecord(0);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean payLoan(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        boolean status = true;
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_LOAN);
        String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        Map frmAccountDetails = getAccountDetails(dcRequest, frmAccountNumber);
        Map toAccountDetails = getAccountDetails(dcRequest, toAccountNumber);

        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);

        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }

        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        Date scheduledDate = StringUtils.isNotBlank(scheduledDt) ? HelperMethods.getFormattedTimeStamp(scheduledDt)
                : new Date();
        if (scheduledDate.after(new Date())) {
            inputParams.put("isScheduled", "true");
            inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL,
                    frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);
        } else {
            inputParams.put("isScheduled", "false");
            BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
            BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            BigDecimal toCurrBal = new BigDecimal(
                    (String) toAccountDetails.get(DBPUtilitiesConstants.CURRENT_AMNT_DUE_S));
            BigDecimal lateFees = new BigDecimal((String) toAccountDetails.get("lateFeesDue"));
            BigDecimal newFrmBal = frmBal.subtract(transAmnt);
            String payOffFlag = (String) inputParams.get(DBPInputConstants.PAY_OFF_FLAG);
            String penaltyFlag = (String) inputParams.get(DBPInputConstants.PENALTY_FLAG);
            if (StringUtils.isNotBlank(payOffFlag) && "true".equalsIgnoreCase(payOffFlag)) {
                String payoffCharge = (String) toAccountDetails.get("payOffCharge");
                if (StringUtils.isNotBlank(penaltyFlag) && "true".equalsIgnoreCase(penaltyFlag)) {
                    newFrmBal = newFrmBal.subtract(new BigDecimal(payoffCharge));
                }
                updateAccountBalance(dcRequest, frmAccountNumber, newFrmBal.toPlainString());
                updateAccountDetails(dcRequest, toAccountNumber, "0", "0");
            } else {
                if (StringUtils.isNotBlank(penaltyFlag) && "true".equalsIgnoreCase(penaltyFlag)) {
                    newFrmBal = newFrmBal.subtract(lateFees);
                }
                BigDecimal pricipalBal = new BigDecimal(
                        (String) toAccountDetails.get(DBPUtilitiesConstants.PRINCIPAL_BAL_S));
                if (frmBal.compareTo(transAmnt) > 0) {
                    updateAccountBalance(dcRequest, frmAccountNumber, newFrmBal.toPlainString());
                    updateAccountDetails(dcRequest, toAccountNumber, toCurrBal.subtract(transAmnt).toPlainString(),
                            pricipalBal.subtract(transAmnt).toPlainString());
                    inputParams.put(DBPUtilitiesConstants.STATUS_DESC,
                            DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
                    inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, newFrmBal.toPlainString());
                } else {
                    status = false;
                    HelperMethods.setValidationMsg("Insufficient Balance to make this transfer", dcRequest, result);
                }
            }
        }

        inputParams.put("description", "Loan Payment Successful");
        HelperMethods.removeNullValues(inputParams);
        return status;
    }

    @SuppressWarnings("rawtypes")
    public boolean cardlessCash(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        String overdraft = (String) inputParams.get("overdraft");
        if (StringUtils.isNotBlank(overdraft) && "true".equalsIgnoreCase(overdraft)) {
            String fromAccountBalance = (String) inputParams.get("fromAccountBalance");
            String amount = (String) inputParams.get("amount");
            if (new BigDecimal(fromAccountBalance).subtract(new BigDecimal(amount))
                    .compareTo(new BigDecimal(1000)) <= 0) {
                overdraftCardlessCashHelper(inputParams, dcRequest, result);
                return false;
            } else {
                return cardlessCashHelper(inputParams, dcRequest, result);
            }
        } else {
            return cardlessCashHelper(inputParams, dcRequest, result);
        }
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private void overdraftCardlessCashHelper(Map inputParams, DataControllerRequest dcRequest, Result result) {
        StringBuilder filter = new StringBuilder();
        filter.append("isScheduled").append(DBPUtilitiesConstants.EQUAL).append("1");
        inputParams.put(DBPUtilitiesConstants.FILTER, filter.toString());
        inputParams.put(DBPUtilitiesConstants.SKIP, "1");
        inputParams.put(DBPUtilitiesConstants.TOP, "5");
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    private boolean cardlessCashHelper(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        boolean status = true;
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_CARDLESS);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String toAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.TO_ACCONT);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String personId = (String) inputParams.get(DBPInputConstants.PERSON_ID);
        String cashlessMode = (String) inputParams.get("cashlessMode");
        String scheduledDate = (String) inputParams.get("scheduledDate");
        Map frmAccountDetails = getAccountDetails(dcRequest, frmAccountNumber);

        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }

        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.PERSON_ID, personId);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.CREATED_DATE, currentDate);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("isScheduled", "false");
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL);
        inputParams.put("cashlessOTP", "123456");
        if (StringUtils.isBlank(toAccountNumber)) {
            inputParams.put(DBPUtilitiesConstants.TO_ACCONT, null);
        }
        if (StringUtils.isNotBlank(scheduledDate)) {
            inputParams.put("scheduledDate", convertDateFormat(scheduledDate));
        }
        BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
        BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
        BigDecimal newFrmBal = frmBal.subtract(transAmnt);
        if (newFrmBal.signum() < 0) {
            status = false;
            HelperMethods.setValidationMsg("Insufficient Balance to make this transfer", dcRequest, result);
            return status;
        }
        inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, newFrmBal.toPlainString());
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());
        calender.add(Calendar.HOUR_OF_DAY, 24);
        inputParams.put("cashlessOTPValidDate", HelperMethods.getFormattedTimeStamp(calender.getTime(), null));
        inputParams.put("cashWithdrawalTransactionStatus", "pending");
        if ("others".equalsIgnoreCase(cashlessMode)) {
            inputParams.put("description", "Cardless Withdrawal to " + inputParams.get("cashlessPersonName"));
        } else {
            inputParams.put("description", "Cardless Withdrawal to self");
        }
        updateAccountBalance(dcRequest, frmAccountNumber, newFrmBal.toPlainString());
        HelperMethods.removeNullValues(inputParams);
        return status;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean createP2PRequestMoney(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_REQUEST);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("isScheduled", "true");
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, DBPUtilitiesConstants.TRANSACTION_STATUS_PENDING);
        inputParams.put("description", "Transfer To " + (String) inputParams.get("p2pContact"));
        return true;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean wireTransfer(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException {
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_WIRE);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String payeeId = (String) inputParams.get("payeeId");
        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String userId = user.get("customer_id");
        Map frmAccountDetails = new HashMap<String, String>();
		try {
			frmAccountDetails = JSONUtils.parseAsMap(fetchMyAccounts(userId).get(frmAccountNumber).toString(), String.class, String.class);
		} catch (JSONException e) {
			alert.prepareError(e).log();
		} catch (IOException e) {
			alert.prepareError(e).log();
		}
        String amount = getCurrencyAmount((String) inputParams.get("amount"),
                (String) inputParams.get("payeeCurrency"));
        if (StringUtils.isBlank(payeeId)) {
            payeeId = getPayeeId(inputParams, dcRequest);
            inputParams.put("isPayeeDeleted", "true");
        }

        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }

        //String customerId = HelperMethods.getCustomerIdFromSession(dcRequest);
        String filter = "Id" + DBPUtilitiesConstants.EQUAL + payeeId;
        Result payee = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_GET);
        if (!HelperMethods.hasRecords(payee)) {
            return false;
        }
        /*
        String userId = HelperMethods.getFieldValue(payee, "User_Id");

        if (StringUtils.isBlank(customerId) || !customerId.equals(userId)) {
            return false;
        }
        */
        String statusDesc = StringUtils.isNotEmpty(inputParams.get("status")+"") ? inputParams.get("status")+"" : DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, statusDesc);
        inputParams.put("amountRecieved", inputParams.get("amount"));
        inputParams.put("amount", amount);
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put("Payee_id", payeeId);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("isScheduled", "false");
        inputParams.put("description", "Wire Transfer Successful");
        BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
        BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
        BigDecimal newFrmBal = frmBal.subtract(transAmnt);
        if (newFrmBal.signum() < 0) {
            HelperMethods.setValidationMsg("Insufficient Balance to make this transfer", dcRequest, result);
            return false;
        }
        inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, newFrmBal.toPlainString());
        updateAccountBalance(dcRequest, frmAccountNumber, newFrmBal.toPlainString());
        HelperMethods.removeNullValues(inputParams);
        return true;
    }

    @SuppressWarnings("rawtypes")
    private String getPayeeId(Map inputParams, DataControllerRequest dcRequest) throws HttpCallException {
        Map<String, String> input = new HashMap<>();
        input.put("User_Id", HelperMethods.getUserIdFromSession(dcRequest));
        input.put("name", (String) inputParams.get("payeeName"));
        input.put("accountNumber", (String) inputParams.get("payeeAccountNumber"));
        input.put("nickName", (String) inputParams.get("payeeNickName"));
        input.put("cityName", (String) inputParams.get("cityName"));
        input.put("addressLine1", (String) inputParams.get("payeeAddressLine1"));
        input.put("addressLine2", (String) inputParams.get("addressLine2"));
        input.put("Type_id", (String) inputParams.get("type"));
        input.put("country", (String) inputParams.get("country"));
        input.put("swiftCode", (String) inputParams.get("swiftCode"));
        input.put("routingCode", (String) inputParams.get("routingNumber"));
        input.put("internationalRoutingCode", (String) inputParams.get("internationalRoutingCode"));
        input.put("bankName", (String) inputParams.get("bankName"));
        input.put("bankAddressLine1", (String) inputParams.get("bankAddressLine1"));
        input.put("bankAddressLine2", (String) inputParams.get("bankAddressLine2"));
        input.put("bankCity", (String) inputParams.get("bankCity"));
        input.put("bankState", (String) inputParams.get("bankState"));
        input.put("bankZip", (String) inputParams.get("bankZip"));
        input.put("internationalAccountNumber", (String) inputParams.get("IBAN"));
        input.put("IBAN", (String) inputParams.get("IBAN"));
        input.put("wireAccountType", (String) inputParams.get("wireAccountType"));
        input.put("isWiredRecepient", (String) inputParams.get("true"));
        input.put("softDelete", (String) inputParams.get("true"));
        input.put("Id", HelperMethods.getRandomNumericString(8));
        Result payee = HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYEE_CREATE);
        return HelperMethods.getFieldValue(payee, "Id");
    }

    private String getCurrencyAmount(String amount, String currency) {
        if ("Pound".equalsIgnoreCase(currency)) {
            amount = new BigDecimal(amount).multiply(new BigDecimal("1.40", new MathContext(4))).toString();
        } else if ("Euro".equalsIgnoreCase(currency)) {
            amount = new BigDecimal(amount).multiply(new BigDecimal("1.23", new MathContext(4))).toString();
        } else if ("Rupee".equalsIgnoreCase(currency)) {
            amount = new BigDecimal(amount).multiply(new BigDecimal("0.015", new MathContext(4))).toString();
        }
        return amount;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean stopCheckPaymentRequest(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;
        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_STOPCHECKPAYMENTREQUEST);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String checkDateOfIssue = getCheckDateOfIssue((String) inputParams.get("checkDateOfIssue"));
        String requestValidity = getRequestValidity((String) inputParams.get("requestValidityInMonths"));
        inputParams.put("checkDateOfIssue", checkDateOfIssue);
        inputParams.put("requestValidity", requestValidity);
        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("isScheduled", "false");
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, "In-Progress");
        inputParams.put("description", "Stop Check Payment Request Successful");
        HelperMethods.removeNullValues(inputParams);
        return true;
    }

    private String getRequestValidity(String validMonth) {
        if (StringUtils.isNotBlank(validMonth)) {
            int month = Integer.parseInt(validMonth);
            Calendar c = Calendar.getInstance();
            c.setTime(new Date());
            c.add(Calendar.YEAR, (month / 12));
            c.add(Calendar.MONTH, month % 12);
            return HelperMethods.getFormattedTimeStamp(c.getTime(), null);
        }
        return validMonth;
    }

    private String getCheckDateOfIssue(String checkDate) throws ParseException {
        if (StringUtils.isNotBlank(checkDate)) {
            Date d = HelperMethods.getFormattedTimeStamp(checkDate, "yyyy-dd-MM");
            return HelperMethods.getFormattedTimeStamp(d, null);
        }
        return checkDate;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public boolean payPerson(Map inputParams, DataControllerRequest dcRequest, Result result)
            throws HttpCallException, ParseException {

        boolean status = true;
        String currentDate = inputParams.get(DBPUtilitiesConstants.CREATED_DATE)!= null ? inputParams.get(DBPUtilitiesConstants.CREATED_DATE).toString() : null;

        String typeId = getTransTypeId(dcRequest, DBPUtilitiesConstants.TRANSACTION_TYPE_P2P);
        String frmAccountNumber = (String) inputParams.get(DBPUtilitiesConstants.FRM_ACCONT);
        String frequencyType = (String) inputParams.get(DBPUtilitiesConstants.FREQUENCY_TYPE);
        String notes = (String) inputParams.get(DBPInputConstants.TRANS_NOTES);
        String personId = (String) inputParams.get(DBPInputConstants.PERSON_ID);

        String personName = null;
        if (StringUtils.isNotBlank((String) inputParams.get("payPersonName"))) {
            personName = (String) inputParams.get("payPersonName");
        }

        String personNickName = null;
        if (StringUtils.isNotBlank((String) inputParams.get("payPersonNickName"))) {
            personNickName = (String) inputParams.get("payPersonNickName");
        }

        String p2pAlternateContact = null;
        if (StringUtils.isNotBlank((String) inputParams.get("p2pAlternateContact"))) {
            p2pAlternateContact = (String) inputParams.get("p2pAlternateContact");
        }

        Map<String, String> user = HelperMethods.getCustomerFromIdentityService(dcRequest);
        String userId = user.get("customer_id");
        Map frmAccountDetails = new HashMap<String, String>();
		try {
			frmAccountDetails = JSONUtils.parseAsMap(fetchMyAccounts(userId).get(frmAccountNumber).toString(), String.class, String.class);
		} catch (JSONException e) {
			alert.prepareError(e).log();
		} catch (IOException e) {
			alert.prepareError(e).log();
		}
        Map personDetails = getPayPersonDetails(dcRequest, personId);

        inputParams.put(DBPUtilitiesConstants.TRANS_NOTES, notes);
        inputParams.put(DBPUtilitiesConstants.PERSON_ID, personId);
        inputParams.put(DBPUtilitiesConstants.T_TYPE_ID, typeId);
        inputParams.put(DBPUtilitiesConstants.TRANS_DATE, currentDate);
        inputParams.put("payPersonName", personName);
        inputParams.put("payPersonNickName", personNickName);
        inputParams.put("p2pAlternateContact", p2pAlternateContact);

        if (!inputParams.containsKey("transactionCurrency")) {
            inputParams.put("transactionCurrency", frmAccountDetails.get("transactionCurrency"));
        }

        String scheduledDt = (String) inputParams.get(DBPUtilitiesConstants.SCHEDULED_DATE);
        Date scheduledDate = StringUtils.isNotBlank(scheduledDt) ? HelperMethods.getFormattedTimeStamp(scheduledDt)
                : new Date();

        String statusDesc = StringUtils.isNotEmpty(inputParams.get("status")+"") ? inputParams.get("status")+"" : DBPUtilitiesConstants.TRANSACTION_STATUS_SUCCESSFUL;
        inputParams.put(DBPUtilitiesConstants.STATUS_DESC, statusDesc);
        
        if (scheduledDate.after(new Date())) {

            inputParams.put("isScheduled", "true");
            inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL,
                    frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            inputParams.put(DBPUtilitiesConstants.TO_ACCNT_BAL, null);

        } else {

            inputParams.put("isScheduled", "false");
            BigDecimal transAmnt = new BigDecimal((String) inputParams.get(DBPInputConstants.AMOUNT));
            BigDecimal frmBal = new BigDecimal((String) frmAccountDetails.get(DBPUtilitiesConstants.AVAILABLE_BAL_S));
            BigDecimal newFrmBal = frmBal.subtract(transAmnt);

            if (frmBal.compareTo(transAmnt) >= 0) {

                updateAccountBalance(dcRequest, frmAccountNumber, newFrmBal.toPlainString());
                inputParams.put(DBPUtilitiesConstants.FRM_ACCNT_BAL, newFrmBal.toPlainString());

            } else {

                status = false;
                HelperMethods.setValidationMsg("Insufficient Balance to make this transfer", dcRequest, result);
            }
        }

        if (!"Once".equalsIgnoreCase(frequencyType)) {

            String noOfRecurr = (String) inputParams.get(DBPUtilitiesConstants.NO_OF_RECURRENCES);
            if (StringUtils.isNotBlank(noOfRecurr)) {
                inputParams.put(DBPUtilitiesConstants.RECUR_DESC, "1 of " + noOfRecurr);
            }
        }

        if (StringUtils.isNotBlank((String) personDetails.get("name"))) {
            inputParams.put("description", "Person2PersonTransfer to " + personDetails.get("name"));
        } else {
            inputParams.put("description", "Person2PersonTransfer to " + personName);
        }

        HelperMethods.removeNullValues(inputParams);

        return status;
    }

    public String getTransTypeId(DataControllerRequest dcRequest, String transType) throws HttpCallException {
        String filterQuery = DBPUtilitiesConstants.TRANS_TYPE_DESC + DBPUtilitiesConstants.EQUAL + transType;
        Result rs = HelperMethods.callGetApi(dcRequest, filterQuery, HelperMethods.getHeaders(dcRequest),
                URLConstants.TRANSACTION_TYPE_GET);
        return HelperMethods.getFieldValue(rs, DBPUtilitiesConstants.T_TRANS_ID);
    }

    public String getCurrentTimeStamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'hh:mm:ss");
        return formatter.format(new Date());
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getAccountDetails(DataControllerRequest dcRequest, String accountId) throws HttpCallException {
        Map result = new HashMap();
        String filter = DBPUtilitiesConstants.ACCOUNT_ID + DBPUtilitiesConstants.EQUAL + accountId;
        Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.ACCOUNTS_GET);
        Dataset ds = rs.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            String nickName = HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.N_NAME);
            String avlblBal = HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.AVAILABLE_BAL_S);
            String currdue = HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.CURRENT_AMNT_DUE_S);
            String principalBal = HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.PRINCIPAL_BAL_S);
            String lateFeesDue = HelperMethods.getFieldValue(ds.getRecord(0), "lateFeesDue");
            String payOffCharge = HelperMethods.getFieldValue(ds.getRecord(0), "payOffCharge");
            result.put(DBPUtilitiesConstants.N_NAME, nickName);
            result.put(DBPUtilitiesConstants.AVAILABLE_BAL_S, StringUtils.isBlank(avlblBal) ? "0" : avlblBal);
            result.put(DBPUtilitiesConstants.CURRENT_AMNT_DUE_S, StringUtils.isBlank(currdue) ? "0" : currdue);
            result.put(DBPUtilitiesConstants.PRINCIPAL_BAL_S, StringUtils.isBlank(principalBal) ? "0" : principalBal);
            result.put("lateFeesDue", StringUtils.isBlank(lateFeesDue) ? "0" : lateFeesDue);
            result.put("payOffCharge", StringUtils.isBlank(payOffCharge) ? "0" : payOffCharge);
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getExtAccountDetails(DataControllerRequest dcRequest, String accountNum, String iBAN)
            throws HttpCallException {
        Map result = new HashMap();
        String filter = "";

        if (StringUtils.isNotBlank(accountNum)) {
            filter += DBPUtilitiesConstants.ACCT_NUMBER + DBPUtilitiesConstants.EQUAL + accountNum;
        } else {
            filter += DBPInputConstants.IBAN + DBPUtilitiesConstants.EQUAL + iBAN;

            String userID = HelperMethods.getUserIdFromSession(dcRequest);
            if (StringUtils.isNotBlank(userID)) {
                filter += DBPUtilitiesConstants.AND;

                filter += "User_id" + DBPUtilitiesConstants.EQUAL + userID;
            }
        }

        Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.EXT_ACCOUNTS_GET);
        Dataset ds = rs.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            String nickName = HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.N_NAME);
            if (StringUtils.isNotBlank(nickName)) {
                result.put(DBPUtilitiesConstants.N_NAME, nickName);
            } else {
                nickName = HelperMethods.getFieldValue(ds.getRecord(0), "beneficiaryName");
                result.put(DBPUtilitiesConstants.N_NAME, nickName);
            }
        }
        return result;
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    public Map getPayPersonDetails(DataControllerRequest dcRequest, String personId) throws HttpCallException {
        Map result = new HashMap();
        String filter = DBPUtilitiesConstants.UN_ID + DBPUtilitiesConstants.EQUAL + personId;
        Result rs = HelperMethods.callGetApi(dcRequest, filter, HelperMethods.getHeaders(dcRequest),
                URLConstants.PAYPERSON_GET);
        Dataset ds = rs.getAllDatasets().get(0);
        if (null != ds && !ds.getAllRecords().isEmpty()) {
            result.put(DBPUtilitiesConstants.F_NAME,
                    HelperMethods.getFieldValue(ds.getRecord(0), DBPUtilitiesConstants.F_NAME));
            result.put("name", HelperMethods.getFieldValue(ds.getRecord(0), "name"));
        }
        return result;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void updateAccountBalance(DataControllerRequest dcRequest, String accountId, String balance)
            throws HttpCallException {
        Map input = new HashMap();
        input.put(DBPUtilitiesConstants.ACCOUNT_ID, accountId);
        input.put(DBPUtilitiesConstants.AVAILABLE_BAL, balance);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public void updateAccountDetails(DataControllerRequest dcRequest, String accountId, String currentDue,
            String principalDue) throws HttpCallException {
        Map input = new HashMap();
        input.put(DBPUtilitiesConstants.ACCOUNT_ID, accountId);
        input.put("CurrentAmountDue", currentDue);
        input.put("principalBalance", principalDue);
        HelperMethods.callApi(dcRequest, input, HelperMethods.getHeaders(dcRequest), URLConstants.ACCOUNTS_UPDATE);
    }

    private String convertDateFormat(String dt) throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-mm-dd");
        Date date = formatter.parse(dt);
        return HelperMethods.getFormattedTimeStamp(date, null);
    }
    

	private JSONObject fetchMyAccounts(String createdby) {
		
		JSONObject myAccountsjson = new JSONObject();
		
		try {
			String key = "INTERNAL_BANK_ACCOUNTS" + createdby;
			ResultCache resultCache = ServicesManagerHelper.getServicesManager().getResultCache();
			
			if (resultCache != null && StringUtils.isNotBlank(key)) {
				String myAccounts = (String) resultCache.retrieveFromCache(key);
				if(myAccounts != null) {
					myAccountsjson = new JSONObject(myAccounts);
				}
			}
		}
		catch(MiddlewareException e) {
			return null;
		}
		return myAccountsjson;
	}
}
