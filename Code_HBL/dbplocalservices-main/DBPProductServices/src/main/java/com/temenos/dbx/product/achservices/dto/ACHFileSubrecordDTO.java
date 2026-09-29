package com.temenos.dbx.product.achservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author KH2638
 * @version 1.0
 * **/
@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ACHFileSubrecordDTO implements DBPDTO{

	private static final long serialVersionUID = 4968587990971975268L;
	
	private String achFileSubRecordId;
	private String achFileRecordId;
	private double amount;
	private String receiverAccountNumber;
	private String receiverAccountType;
	private String receiverName;
	private String receiverTransactionType;
	
	public ACHFileSubrecordDTO() {
		super();
	}

	public ACHFileSubrecordDTO(String achFileSubRecordId, String achFileRecordId, double amount,
			String receiverAccountNumber, String receiverAccountType, String receiverName,
			String receiverTransactionType) {
		super();
		this.achFileSubRecordId = achFileSubRecordId;
		this.achFileRecordId = achFileRecordId;
		this.amount = amount;
		this.receiverAccountNumber = receiverAccountNumber;
		this.receiverAccountType = receiverAccountType;
		this.receiverName = receiverName;
		this.receiverTransactionType = receiverTransactionType;
	}

	public String getAchFileSubRecordId() {
		return achFileSubRecordId;
	}

	public void setAchFileSubRecordId(String achFileSubRecordId) {
		this.achFileSubRecordId = achFileSubRecordId;
	}

	public String getAchFileRecordId() {
		return achFileRecordId;
	}

	public void setAchFileRecordId(String achFileRecordId) {
		this.achFileRecordId = achFileRecordId;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getReceiverAccountNumber() {
		return receiverAccountNumber;
	}

	public void setReceiverAccountNumber(String receiverAccountNumber) {
		this.receiverAccountNumber = receiverAccountNumber;
	}

	public String getReceiverAccountType() {
		return receiverAccountType;
	}

	public void setReceiverAccountType(String receiverAccountType) {
		this.receiverAccountType = receiverAccountType;
	}

	public String getReceiverName() {
		return receiverName;
	}

	public void setReceiverName(String receiverName) {
		this.receiverName = receiverName;
	}

	public String getReceiverTransactionType() {
		return receiverTransactionType;
	}

	public void setReceiverTransactionType(String receiverTransactionType) {
		this.receiverTransactionType = receiverTransactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((achFileRecordId == null) ? 0 : achFileRecordId.hashCode());
		result = prime * result + ((achFileSubRecordId == null) ? 0 : achFileSubRecordId.hashCode());
		long temp;
		temp = Double.doubleToLongBits(amount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((receiverAccountNumber == null) ? 0 : receiverAccountNumber.hashCode());
		result = prime * result + ((receiverAccountType == null) ? 0 : receiverAccountType.hashCode());
		result = prime * result + ((receiverName == null) ? 0 : receiverName.hashCode());
		result = prime * result + ((receiverTransactionType == null) ? 0 : receiverTransactionType.hashCode());
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
		ACHFileSubrecordDTO other = (ACHFileSubrecordDTO) obj;
		if (achFileSubRecordId == null) {
			if (other.achFileSubRecordId != null)
				return false;
		} else if (!achFileSubRecordId.equals(other.achFileSubRecordId))
			return false;
		return true;
	}
}