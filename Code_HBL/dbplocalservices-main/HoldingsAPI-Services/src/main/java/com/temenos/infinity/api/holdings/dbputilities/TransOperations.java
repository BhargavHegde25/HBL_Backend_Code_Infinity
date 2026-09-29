package com.temenos.infinity.api.holdings.dbputilities;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import com.temenos.logger.Logger;
import com.temenos.logger.alert.Alert;
import com.temenos.logger.diagnostics.Diagnostic;

import com.kony.dbputilities.dbutil.DBManager;
import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.URLConstants;
import com.kony.dbputilities.util.URLFinder;
import com.konylabs.middleware.controller.DataControllerRequest;
import com.konylabs.middleware.dataobject.Dataset;
import com.konylabs.middleware.dataobject.Record;
import com.konylabs.middleware.dataobject.Result;

public class TransOperations {
	
	private TransOperations() {
	}

	private static final Alert alert = Logger.forAlert().forModule("Infinity", "DIGITALBANKING");
	private static final Diagnostic diagnostic = Logger.forDiagnostic().forModule("Infinity", "DIGITALBANKING");
	private static final String DATASET_NAME = "transaction";
	private static String inssertTransactionQuery = "INSERT INTO [?1].[transaction] ([isScheduled],[Customer_id],[ExpenseCategory_id],[Payee_id],[Bill_id],[Type_id],[Reference_id],[fromAccountNumber],[fromAccountBalance],[toAccountNumber],[toAccountBalance],[amount],[convertedAmount],[transactionCurrency],[baseCurrency],[Status_id],[statusDesc],[notes],[checkNumber],[imageURL1],[imageURL2],[hasDepositImage],[description],[scheduledDate],[transactionDate],[postedDate],[createdDate],[transactionComments],[toExternalAccountNumber],[Person_Id],[frequencyType],[numberOfRecurrences],[frequencyStartDate],[frequencyEndDate],[checkImage],[checkImageBack],[cashlessOTPValidDate],[cashlessOTP],[cashlessPhone],[cashlessEmail],[cashlessPersonName],[cashlessMode],[cashlessSecurityCode],[cashWithdrawalTransactionStatus],[cashlessPin],[category],[billCategory],[recurrenceDesc],[deliverBy],[p2pContact],[p2pRequiredDate],[requestCreatedDate],[penaltyFlag],[payoffFlag],[viewReportLink],[isPaypersonDeleted],[fee],[feeCurrency],[feePaidByReceipent],[frontImage1],[frontImage2],[backImage1],[backImage2],[checkDesc],[checkNumber1],[checkNumber2],[bankName1],[bankName2],[withdrawlAmount1],[withdrawlAmount2],[cashAmount],[payeeCurrency],[billid],[isDisputed],[disputeDescription],[disputeReason],[disputeStatus],[disputeDate],[payeeName],[checkDateOfIssue],[checkReason],[isPayeeDeleted],[amountRecieved],[requestValidity],[statementReference],[transCreditDebitIndicator],[bookingDateTime],[valueDateTime],[transactionInformation],[addressLine],[transactionAmount],[chargeAmount],[chargeCurrency],[sourceCurrency],[targetCurrency],[unitCurrency],[exchangeRate],[contractIdentification],[quotationDate],[instructedAmount],[instructedCurrency],[transactionCode],[transactionSubCode],[proprietaryTransactionCode],[proprietaryTransactionIssuer],[balanceCreditDebitIndicator],[balanceType],[balanceAmount],[balanceCurrency],[merchantName],[merchantCategoryCode],[creditorAgentSchemeName],[creditorAgentIdentification],[creditorAgentName],[creditorAgentaddressType],[creditorAgentDepartment],[creditorAgentSubDepartment],[creditorAgentStreetName],[creditorAgentBuildingNumber],[creditorAgentPostCode],[creditorAgentTownName],[creditorAgentCountrySubDivision],[creditorAgentCountry],[creditorAgentAddressLine],[creditorAccountSchemeName],[creditorAccountIdentification],[creditorAccountName],[creditorAccountSeconIdentification],[debtorAgentSchemeName],[debtorAgentIdentification],[debtorAgentName],[debtorAgentAddressType],[debtorAgentDepartment],[debtorAgentSubDepartment],[debtorAgentStreetName],[debtorAgentBuildingNumber],[dedtorAgentPostCode],[debtorAgentTownName],[debtorAgentCountrySubDivision],[debtorAgentCountry],[debtorAgentAddressLine],[debtorAccountSchemeName],[debtorAccountIdentification],[debtorAccountName],[debtorAccountSeconIdentification],[cardInstrumentSchemeName],[cardInstrumentAuthorisationType],[cardInstrumentName],[cardInstrumentIdentification],[IBAN],[sortCode],[FirstPaymentDateTime],[NextPaymentDateTime],[FinalPaymentDateTime],[StandingOrderStatusCode],[FP_Amount],[FP_Currency],[NP_Amount],[NP_Currency],[FPA_Amount],[FPA_Currency],[ConsentId],[Initiation_InstructionIdentification],[Initiation_EndToEndIdentification],[RI_Reference],[RI_Unstructured],[RiskPaymentContextCode],[MerchantCustomerIdentification],[beneficiaryName],[bankName],[swiftCode],[DomesticPaymentId],[linkSelf],[StatusUpdateDateTime],[dataStatus],[serviceName],[payPersonName],[payPersonNickName],[p2pAlternateContact],[billerId],[bicCode],[paidBy],[paymentType],[feeAmount],[beneficiaryAddressNickName],[beneficiaryAddressLine1],[beneficiaryCity],[beneficiaryZipcode],[beneficiarycountry],[bankId]) VALUES (:isScheduled,:Customer_id,:ExpenseCategory_id,:Payee_id,:Bill_id,:Type_id,:Reference_id,:fromAccountNumber,:fromAccountBalance,:toAccountNumber,:toAccountBalance,:amount,:convertedAmount,:transactionCurrency,:baseCurrency,:Status_id,:statusDesc,:notes,:checkNumber,:imageURL1,:imageURL2,:hasDepositImage,:description,:scheduledDate,:transactionDate,:postedDate,:CreatedDate,:transactionComments,:toExternalAccountNumber,:Person_Id,:frequencyType,:numberOfRecurrences,:frequencyStartDate,:frequencyEndDate,:checkImage,:checkImageBack,:cashlessOTPValidDate,:cashlessOTP,:cashlessPhone,:cashlessEmail,:cashlessPersonName,:cashlessMode,:cashlessSecurityCode,:cashWithdrawalTransactionStatus,:cashlessPin,:category,:billCategory,:recurrenceDesc,:deliverBy,:p2pContact,:p2pRequiredDate,:requestCreatedDate,:penaltyFlag,:payoffFlag,:viewReportLink,:isPaypersonDeleted,:fee,:feeCurrency,:feePaidByReceipent,:frontImage1,:frontImage2,:backImage1,:backImage2,:checkDesc,:checkNumber1,:checkNumber2,:bankName1,:bankName2,:withdrawlAmount1,:withdrawlAmount2,:cashAmount,:payeeCurrency,:billid,:isDisputed,:disputeDescription,:disputeReason,:disputeStatus,:disputeDate,:payeeName,:checkDateOfIssue,:checkReason,:isPayeeDeleted,:amountRecieved,:requestValidity,:statementReference,:transCreditDebitIndicator,:bookingDateTime,:valueDateTime,:transactionInformation,:addressLine,:transactionAmount,:chargeAmount,:chargeCurrency,:sourceCurrency,:targetCurrency,:unitCurrency,:exchangeRate,:contractIdentification,:quotationDate,:instructedAmount,:instructedCurrency,:transactionCode,:transactionSubCode,:proprietaryTransactionCode,:proprietaryTransactionIssuer,:balanceCreditDebitIndicator,:balanceType,:balanceAmount,:balanceCurrency,:merchantName,:merchantCategoryCode,:creditorAgentSchemeName,:creditorAgentIdentification,:creditorAgentName,:creditorAgentaddressType,:creditorAgentDepartment,:creditorAgentSubDepartment,:creditorAgentStreetName,:creditorAgentBuildingNumber,:creditorAgentPostCode,:creditorAgentTownName,:creditorAgentCountrySubDivision,:creditorAgentCountry,:creditorAgentAddressLine,:creditorAccountSchemeName,:creditorAccountIdentification,:creditorAccountName,:creditorAccountSeconIdentification,:debtorAgentSchemeName,:debtorAgentIdentification,:debtorAgentName,:debtorAgentAddressType,:debtorAgentDepartment,:debtorAgentSubDepartment,:debtorAgentStreetName,:debtorAgentBuildingNumber,:dedtorAgentPostCode,:debtorAgentTownName,:debtorAgentCountrySubDivision,:debtorAgentCountry,:debtorAgentAddressLine,:debtorAccountSchemeName,:debtorAccountIdentification,:debtorAccountName,:debtorAccountSeconIdentification,:cardInstrumentSchemeName,:cardInstrumentAuthorisationType,:cardInstrumentName,:cardInstrumentIdentification,:IBAN,:sortCode,:FirstPaymentDateTime,:NextPaymentDateTime,:FinalPaymentDateTime,:StandingOrderStatusCode,:FP_Amount,:FP_Currency,:NP_Amount,:NP_Currency,:FPA_Amount,:FPA_Currency,:ConsentId,:Initiation_InstructionIdentification,:Initiation_EndToEndIdentification,:RI_Reference,:RI_Unstructured,:RiskPaymentContextCode,:MerchantCustomerIdentification,:beneficiaryName,:bankName,:swiftCode,:DomesticPaymentId,:linkSelf,:StatusUpdateDateTime,:dataStatus,:serviceName,:payPersonName,:payPersonNickName,:p2pAlternateContact,:billerId,:bicCode,:paidBy,:paymentType,:feeAmount,:beneficiaryAddressNickName,:beneficiaryAddressLine1,:beneficiaryCity,:beneficiaryZipcode,:beneficiarycountry,:bankId)";
	private static String updateTransactionQuery="UPDATE [?1].[transaction] SET ?3 WHERE [Id] = \'?2\'";
	private static String deleteTransactionQuery="DELETE FROM [?1].[transaction] WHERE Id=\'?2\'";

	public static String parameterSubstitute(Map<String, String> params, String query) {

		for (int i = 0; i < query.length(); i++) {
			char c = query.charAt(i);
			if (c == ':') {
				for (int j = i; j < query.length(); j++) {
					char d = query.charAt(j);
					if (d == ',' || d == ')') {
						String abc = query.substring(i + 1, j).trim();
						if (!params.containsKey(abc)) {
							params.put(abc, "NULL");
						}
						break;
					}
				}
			}
		}
		for (Map.Entry<String, String> set : params.entrySet()) {
			if (set.getValue() == "NULL")
				{query = query.replace(":" + set.getKey()+",", set.getValue()+",");
				query = query.replace(":" + set.getKey()+")", set.getValue()+")");}
			else
				{query = query.replace(":" + set.getKey()+",", "\'" + set.getValue() + "\'"+",");
				query = query.replace(":" + set.getKey()+")", "\'" + set.getValue() + "\'"+")");}
		}
		
		return query;

	}
	
	private static void updateDefaultValues(Map<String, String> inputParams) {
		if(StringUtils.isBlank(inputParams.get("withdrawlAmount1"))) {
			inputParams.put("withdrawlAmount1", "0.0");
		}
		if(StringUtils.isBlank(inputParams.get("withdrawlAmount2"))) {
			inputParams.put("withdrawlAmount2", "0.0");
		}
		if(StringUtils.isBlank(inputParams.get("cashAmount"))) {
			inputParams.put("cashAmount", "0.0");
		}
		if(StringUtils.isBlank(inputParams.get("payeeCurrency"))) {
			inputParams.put("payeeCurrency", "INR");
		}
		if(StringUtils.isBlank(inputParams.get("numberOfRecurrences"))) {
			inputParams.put("numberOfRecurrences", "0");
		}
		inputParams.put("postedDate",inputParams.get(DBPUtilitiesConstants.CREATED_DATE));
	}
	
	private static String prepareQueryForUpdatedFields(Map<String, String> inputParams) {
		
		String[] columns = new String[] {"amount",
				"frequencyEndDate",
				"frequencyType",
				"fromAccountNumber",
				"fromAccountBalance",
				"isScheduled",
				"scheduledDate",
				"toAccountNumber",
				"notes",
				"transactionComments",
				"Type_id",
				"transactionCurrency",
				"payeeCurrency",
				"numberOfRecurrences",
				"recurrenceDesc",
				"toExternalAccountNumber",
				"Payee_id",
				"Person_Id",
				"isDisputed",
				"disputeDate",
				"disputeStatus",
				"transactionDate",
				"statusDesc"};
		StringBuilder sb = new StringBuilder();
		for( int i = 0; i < columns.length ; i++ ) {
			if(StringUtils.isNotBlank(inputParams.get(columns[i]))) {
				sb.append(",").append(columns[i]).append(" = '").
				append(inputParams.get(columns[i])).append("'");
			}
		}
		if(sb.length() > 0) {
			sb.deleteCharAt(0);
		}
		return sb.toString();
	}

	public static Result createDataSetInsert(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws SQLException {
		Result result = new Result();
		updateDefaultValues(inputParams);
		String query = TransOperations.parameterSubstitute(inputParams, TransOperations.inssertTransactionQuery).replace("?1",
				URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest));
		
		try (Connection connection = DBManager.getConnection(dcRequest);
				PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
				ResultSet rs = statement.executeQuery();) {
			Record rec = new Record();
			if (rs.next()) {
				rec.addIntParam("Id", rs.getInt(1));
			}
			
			// below are required for cashless transaction creation service
			if(StringUtils.isNotBlank(inputParams.get("cashlessPersonName"))) {
				rec.addStringParam("cashlessPersonName", inputParams.get("cashlessPersonName"));
			}
			if(StringUtils.isNotBlank(inputParams.get("cashlessOTP"))) {
				rec.addStringParam("cashlessOTP", inputParams.get("cashlessOTP"));
			}
			if(StringUtils.isNotBlank(inputParams.get("cashlessOTPValidDate"))) {
				rec.addStringParam("cashlessOTPValidDate", inputParams.get("cashlessOTPValidDate"));
			}

			Dataset transDetails = new Dataset(DATASET_NAME);
			transDetails.addRecord(rec);
			result.addDataset(transDetails);
		} catch (Exception e) {
			alert.prepareError("Exception while inserting transaction :", e).log();
			result.addErrMsgParam("Exception while inserting transaction.");
		}
		
		return result;
	}

	public static Result createDataSetUpdate(DataControllerRequest dcRequest, Map<String, String> inputParams)
			throws SQLException {
		Result result = new Result();
		String query = TransOperations.updateTransactionQuery.replace("?1",
				URLFinder.getPathUrl(URLConstants.SCHEMA_NAME, dcRequest))
				.replace("?3", prepareQueryForUpdatedFields(inputParams)).
				replace("?2", inputParams.get("transactionId"));
		try (Connection connection = DBManager.getConnection(dcRequest);
				PreparedStatement statement = connection.prepareStatement(query);
				) {
			statement.executeUpdate();
			result.addStringParam("referenceId", inputParams.get("transactionId"));
		} catch (Exception e) {
			alert.prepareError("Exception while updating transaction :", e).log();
			result.addErrMsgParam("Exception while updating transaction.");
		}
		return result;
	}
	
	public static Result createDataSetDelete(DataControllerRequest dcRequest,Map<String, String> inputParams) throws SQLException
	{
		Result result=new Result();
		String query = TransOperations.deleteTransactionQuery.replace("?1", URLFinder.getPathUrl(URLConstants.SCHEMA_NAME,dcRequest)).replace("?2", inputParams.get("transactionId").toString());
		try(Connection connection = DBManager.getConnection(dcRequest);
				PreparedStatement statement= connection.prepareStatement(query);
				) {
			statement.executeUpdate();
			result.addStringParam("deletedRecords", "1");
		} catch (Exception e) {
			alert.prepareError("Exception while updating transaction :", e).log();
			result.addErrMsgParam("Exception while updating transaction.");
		}
		return result;
		
	}
	public static String parameterSubCreate(Map<String,String> params,String query) {
		for (int i=0;i<query.length();i++) {
			char c=query.charAt(i);
			if(c==',') { 
				for(int j=i;j<query.length();j++) {
					char d=query.charAt(j);
					if(d=='=') {
						String abc=query.substring(i+1,j);
						if(!params.containsKey(abc)) {
							query= query.substring(0, i)+query.substring(i+abc.length()*2+3, query.length());
						}
						break;
					}
				}
			}
		}
		return query;
	}

	public static String parameterSubUpdate(Map<String, String> params, String query) {
		for (int k = 0; k < query.length(); k++) {
			char f = query.charAt(k);
			if (f == '[') {
				for (int j = k; j < query.length(); j++) {
					char d = query.charAt(j);
					if (d == '=') {
						String abc = query.substring(k + 1, j - 1);
						if (!params.containsKey(abc)) {
							query = query.substring(0, k) + query.substring(j + 2 + abc.length(), query.length());
							break;
						}
						break;
					}

				}
			}

		}
		while (query.contains(",,")) {
			query = query.replace(",,", ",");
		}
		for (Map.Entry<String, String> set : params.entrySet()) {
			if (set.getValue() == "NULL")
				query = query.replace(":" + set.getKey(), set.getValue());
			else
				query = query.replace(":" + set.getKey(), "\'" + set.getValue() + "\'");
		}
		return query;

	}
}
