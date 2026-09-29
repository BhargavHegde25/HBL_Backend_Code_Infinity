package com.temenos.dbx.product.achservices.dto;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.dbp.core.api.factory.impl.DBPAPIAbstractFactoryImpl;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.temenos.dbx.product.commons.businessdelegate.api.ApplicationBusinessDelegate;
import com.temenos.dbx.product.constants.ACHConstants;

/**
 * @author KH2638
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHFileRecordDTO implements DBPDTO{

	private static final long serialVersionUID = 3220389719004237744L;
	
	private String achFileId;
	private String achFileRecordId;
	private String offsetAccountNumber;
	private double offsetAmount;
	private String offsetTransactionType;
	private String effectiveDate;
	private String requestType;
	private double totalCreditAmount;
	private double totalDebitAmount;
	private String transactionType;
	
	List<ACHFileSubrecordDTO> subRecords;

	public ACHFileRecordDTO() {
		super();
	}

	public ACHFileRecordDTO(String achFileId, String achFileRecordId, String offsetAccountNumber, double offsetAmount,
			String offsetTransactionType, String effectiveDate, String requestType, double totalCreditAmount,
			double totalDebitAmount, String transactionType, List<ACHFileSubrecordDTO> subRecords) {
		super();
		this.achFileId = achFileId;
		this.achFileRecordId = achFileRecordId;
		this.offsetAccountNumber = offsetAccountNumber;
		this.offsetAmount = offsetAmount;
		this.offsetTransactionType = offsetTransactionType;
		this.effectiveDate = effectiveDate;
		this.requestType = requestType;
		this.totalCreditAmount = totalCreditAmount;
		this.totalDebitAmount = totalDebitAmount;
		this.transactionType = transactionType;
		this.subRecords = subRecords;
	}

	public String getAchFileId() {
		return achFileId;
	}

	public void setAchFileId(String achFileId) {
		this.achFileId = achFileId;
	}

	public String getAchFileRecordId() {
		return achFileRecordId;
	}

	public void setAchFileRecordId(String achFileRecordId) {
		this.achFileRecordId = achFileRecordId;
	}

	public String getOffsetAccountNumber() {
		return offsetAccountNumber;
	}

	public void setOffsetAccountNumber(String offsetAccountNumber) {
		this.offsetAccountNumber = offsetAccountNumber;
	}

	public double getOffsetAmount() {
		return offsetAmount;
	}

	public void setOffsetAmount(double offsetAmount) {
		this.offsetAmount = offsetAmount;
	}

	public String getOffsetTransactionType() {
		return offsetTransactionType;
	}

	public void setOffsetTransactionType(String offsetTransactionType) {
		this.offsetTransactionType = offsetTransactionType;
	}

	public String getEffectiveDate() {
		return effectiveDate;
	}

	public void setEffectiveDate(String effectiveDate) {
		this.effectiveDate = getValidEffectiveDate(effectiveDate);
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public double getTotalCreditAmount() {
		return totalCreditAmount;
	}

	public void setTotalCreditAmount(double totalCreditAmount) {
		this.totalCreditAmount = totalCreditAmount;
	}

	public double getTotalDebitAmount() {
		return totalDebitAmount;
	}

	public void setTotalDebitAmount(double totalDebitAmount) {
		this.totalDebitAmount = totalDebitAmount;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public List<ACHFileSubrecordDTO> getSubRecords() {
		return subRecords;
	}

	public void setSubRecords(List<ACHFileSubrecordDTO> subRecords) {
		this.subRecords = subRecords;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achFileId == null) ? 0 : achFileId.hashCode());
		result = prime * result + ((achFileRecordId == null) ? 0 : achFileRecordId.hashCode());
		result = prime * result + ((effectiveDate == null) ? 0 : effectiveDate.hashCode());
		result = prime * result + ((offsetAccountNumber == null) ? 0 : offsetAccountNumber.hashCode());
		long temp;
		temp = Double.doubleToLongBits(offsetAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((offsetTransactionType == null) ? 0 : offsetTransactionType.hashCode());
		result = prime * result + ((requestType == null) ? 0 : requestType.hashCode());
		result = prime * result + ((subRecords == null) ? 0 : subRecords.hashCode());
		temp = Double.doubleToLongBits(totalCreditAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalDebitAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((transactionType == null) ? 0 : transactionType.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		ACHFileRecordDTO other = (ACHFileRecordDTO) obj;
		if (achFileRecordId == null) {
			if (other.achFileRecordId != null)
				return false;
		} else if (!achFileRecordId.equals(other.achFileRecordId))
			return false;
		return true;
	}
	
	/***
	 * This method validates whether effective date mentioned in ACH File batches are valid or not
	 * @param String effectiveDate - contains effective date value found after parsing the ach file
	 * @return String - returns date in yyyy-mm-dd format if valid or returns null if invalid
	 */
	private String getValidEffectiveDate(String effectiveDate) {

		if(effectiveDate == null || effectiveDate.isEmpty()) {
			return null;
		}

		SimpleDateFormat fileFormat = new SimpleDateFormat(ACHConstants.ACH_FILE_RECORD_TIMESTAMP_FORMAT);
		SimpleDateFormat backendFormat = new SimpleDateFormat(ACHConstants.ACH_FILE_TIMESTAMP_FORMAT);

		try {
			ApplicationBusinessDelegate application = DBPAPIAbstractFactoryImpl.getBusinessDelegate(ApplicationBusinessDelegate.class);
			Date batchDate = backendFormat.parse(backendFormat.format(fileFormat.parse(effectiveDate)));
			Date currentDate = backendFormat.parse(application.getServerTimeStamp());

			if (currentDate.before(batchDate)) {
				return backendFormat.format(fileFormat.parse(effectiveDate));
			}
			else {
				return null;
			}

		} catch (Exception e) {
			return null;
		}
	}
}