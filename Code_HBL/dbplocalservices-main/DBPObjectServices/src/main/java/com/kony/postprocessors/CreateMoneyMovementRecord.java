package com.kony.postprocessors;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.UUID;

import com.google.gson.JsonObject;
import com.kony.utilities.HelperMethods;
import com.kony.utilities.HikariConfiguration;
import com.kony.utilities.ObjectServiceHelperMethods;
import com.konylabs.middleware.api.processor.PayloadHandler;
import com.konylabs.middleware.api.processor.manager.FabricRequestManager;
import com.konylabs.middleware.api.processor.manager.FabricResponseManager;

public class CreateMoneyMovementRecord implements ObjectServicesConstants {

	private static HashMap<String, Integer> transactionTypes = new HashMap<>();

	void create(FabricRequestManager requestManager, FabricResponseManager responseManager, String statusId)
			throws SQLException {
		PayloadHandler responsePayloadHandler = responseManager.getPayloadHandler();
		PayloadHandler requestPayloadHandler = requestManager.getPayloadHandler();
		JsonObject responsePayload = responsePayloadHandler.getPayloadAsJson().getAsJsonObject();
		JsonObject requestPayload = requestPayloadHandler.getPayloadAsJson().getAsJsonObject();

		String isScheduled = "";
		String frequencyType = "";
		String uuid = "";
		String referenceId = "";
		String customerId = HelperMethods.getCustomerIdFromSession(requestManager);
		String payeeId = HelperMethods.getStringFromJsonObject(requestPayload, "payeeId");
		String personId = HelperMethods.getStringFromJsonObject(requestPayload, "personId");
		String p2pContact = HelperMethods.getStringFromJsonObject(requestPayload, "p2pContact");
		JsonObject mfaAttributes = requestPayload.getAsJsonObject(PARAM_MFA_ATRIBUTES);
		String serviceId = HelperMethods.getStringFromJsonObject(mfaAttributes, "serviceName");
		String payeeCurrency = HelperMethods.getStringFromJsonObject(requestPayload, "payeeCurrency");
		String recipientId;
		transactionTypes.put("internaltransfer", 1);
		transactionTypes.put("externaltransfer", 3);
		transactionTypes.put("wire", 15);
		transactionTypes.put("p2p", 5);
		recipientId = (payeeId != null) ? payeeId : personId;
		if (requestPayload.has(PARAM_IS_SCHEDULED))
			isScheduled = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_IS_SCHEDULED, true);

		if (requestPayload.has(PARAM_FREQUENCY_TYPE))
			frequencyType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_TYPE, true);

		if (responsePayload.has(PARAM_REFERENCE_ID))
			referenceId = HelperMethods.getStringFromJsonObject(responsePayload, PARAM_REFERENCE_ID, true);

		HikariConfiguration.getDataSource(requestManager);

		try (Connection connection = HikariConfiguration.getconnection()) {

			int immediateTransferFlag = 0;
			int futureDatedTransferFlag = 0;
			String transactionNotes = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_NOTES,
					true);
			Float amount = Float.parseFloat(HelperMethods.getStringFromJsonObject(requestPayload, PARAM_AMOUNT, true));
			String fromAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FROM_ACCOUNT_NUMBER,
					true);
			String toAccountNumber = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TO_ACCOUNT_NUMBER,
					true);
			String transactionType = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_TRANSACTION_TYPE,
					true);

			String transactionStatus = PARAM_SID_TRANS_FAILED;
			if (isScheduled.equals("1") && statusId.equalsIgnoreCase(PARAM_SID_EVENT_SUCCESS))
				transactionStatus = PARAM_SID_TRANS_SCHEDULED;
			if (isScheduled.equals("0") && statusId.equalsIgnoreCase(PARAM_SID_EVENT_SUCCESS))
				transactionStatus = PARAM_SID_TRANS_POSTED;

			if (isScheduled.equals("0")) {
				futureDatedTransferFlag = 0;
				immediateTransferFlag = 1;
			} else {
				futureDatedTransferFlag = 1;
				immediateTransferFlag = 0;
			}

			if (!requestPayload.has(PARAM_SE_ID)) {
				String scheduledDate = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_SCHEDULED_DATE,
						true);
				Date scheduledDateObject = Date.valueOf(scheduledDate.substring(0, scheduledDate.indexOf('T')));
				uuid = UUID.randomUUID().toString();
				if (frequencyType.equalsIgnoreCase(PARAM_ONCE)) {

					try (PreparedStatement prepareStatement = connection.prepareStatement(
							"insert into dbxdb.moneymovement (id,customerId,fromAccountNumber,toAccountNumber,amount,"
									+ "transactionType,immediateTransferFlag,futureDatedTransferFlag,notes,transactionStatus"
									+ ",transactionDate,referenceId,createdby,createdDate,recipientId,p2pContact,serviceId,payeeCurrency) values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
						prepareStatement.setString(1, uuid);
						prepareStatement.setString(2, HelperMethods.getCustomerIdFromSession(requestManager));
						prepareStatement.setString(3, fromAccountNumber);
						prepareStatement.setString(4, toAccountNumber);
						prepareStatement.setFloat(5, amount);
						prepareStatement.setInt(6, transactionTypes.get(transactionType.toLowerCase()));
						prepareStatement.setInt(7, immediateTransferFlag);
						prepareStatement.setInt(8, futureDatedTransferFlag);
						prepareStatement.setString(9, transactionNotes);
						prepareStatement.setString(10, transactionStatus);
						prepareStatement.setDate(11, scheduledDateObject);
						prepareStatement.setString(12, referenceId);
						prepareStatement.setString(13, HelperMethods.getCustomerIdFromSession(requestManager));
						prepareStatement.setTimestamp(14, getCurrentTimeStamp());
						prepareStatement.setString(15, recipientId);
						prepareStatement.setString(16, p2pContact);
						prepareStatement.setString(17, serviceId);
						prepareStatement.setString(18, payeeCurrency);
						prepareStatement.executeUpdate();
					}

				} else {

					if (ObjectServiceHelperMethods.hasKey(requestPayload, PARAM_NO_OF_RECURRENCES)) {

						int noOfRecurrences = Integer.parseInt(
								HelperMethods.getStringFromJsonObject(requestPayload, PARAM_NO_OF_RECURRENCES, true));
						try (PreparedStatement prepareStatement = connection
								.prepareStatement("insert into dbxdb.moneymovementrecurringdefinition "
										+ "(id, customerId, fromAccountNumber, toAccountNumber, amount, "
										+ "transactionType, frequency, numberOfRecurrences, notes,startDate,"
										+ "recurrencesRemaining,referenceId,createdby,createdDate,recipientId,p2pContact,serviceId,payeeCurrency) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
							prepareStatement.setString(1, uuid);
							prepareStatement.setString(2, customerId);
							prepareStatement.setString(3, fromAccountNumber);
							prepareStatement.setString(4, toAccountNumber);
							prepareStatement.setFloat(5, amount);
							prepareStatement.setInt(6, transactionTypes.get(transactionType.toLowerCase()));
							prepareStatement.setString(7, frequencyType);
							prepareStatement.setInt(8, noOfRecurrences);
							prepareStatement.setString(9, transactionNotes);
							prepareStatement.setDate(10, scheduledDateObject);
							prepareStatement.setInt(11, noOfRecurrences);
							prepareStatement.setString(12, referenceId);
							prepareStatement.setString(13, HelperMethods.getCustomerIdFromSession(requestManager));
							prepareStatement.setTimestamp(14, getCurrentTimeStamp());
							prepareStatement.setString(15, recipientId);
							prepareStatement.setString(16, p2pContact);
							prepareStatement.setString(17, serviceId);
							prepareStatement.setString(18, payeeCurrency);
							prepareStatement.executeUpdate();
						}
					} else {

						String endDate = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_FREQUENCY_END_DATE,
								true);
						Date endDateObject = Date.valueOf(endDate.substring(0, endDate.indexOf('T')));

						try (PreparedStatement prepareStatement = connection
								.prepareStatement("insert into dbxdb.moneymovementrecurringdefinition "
										+ "(id, customerId, fromAccountNumber, toAccountNumber, amount, transactionType, frequency, startDate, notes,endDate,referenceId,createdby,createdDate,recipientId,p2pContact,serviceId,payeeCurrency) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)")) {
							prepareStatement.setString(1, uuid);
							prepareStatement.setString(2, customerId);
							prepareStatement.setString(3, fromAccountNumber);
							prepareStatement.setString(4, toAccountNumber);
							prepareStatement.setFloat(5, amount);
							prepareStatement.setInt(6, transactionTypes.get(transactionType.toLowerCase()));
							prepareStatement.setString(7, frequencyType);
							prepareStatement.setDate(8, scheduledDateObject);
							prepareStatement.setString(9, transactionNotes);
							prepareStatement.setDate(10, endDateObject);
							prepareStatement.setString(11, referenceId);
							prepareStatement.setString(12, HelperMethods.getCustomerIdFromSession(requestManager));
							prepareStatement.setTimestamp(13, getCurrentTimeStamp());
							prepareStatement.setString(14, recipientId);
							prepareStatement.setString(15, p2pContact);
							prepareStatement.setString(16, serviceId);
							prepareStatement.setString(17, payeeCurrency);
							prepareStatement.executeUpdate();
						}

					}
				}
			} else if (requestPayload.has(PARAM_SE_ID)) {
				if (requestPayload.has(PARAM_SE_ID))
					uuid = HelperMethods.getStringFromJsonObject(requestPayload, PARAM_SE_ID, true);
				try (PreparedStatement prepareStatement = connection.prepareStatement(
						"update dbxdb.moneymovement set transactionStatus = ? ,referenceId = ? where id =  ?")) {
					prepareStatement.setString(1, transactionStatus);
					prepareStatement.setString(2, referenceId);
					prepareStatement.setString(3, uuid);
					prepareStatement.executeUpdate();
				}
			}

		}

	}

	private static java.sql.Timestamp getCurrentTimeStamp() {

		java.util.Date today = new java.util.Date();
		return new java.sql.Timestamp(today.getTime());

	}

}