package com.temenos.dbx.nonproduct.dto;

import com.dbp.core.api.DBPDTO;

public class SavingsPotDTO implements DBPDTO{

	private static final long serialVersionUID = 4575727651985376646L;

	private String accountId;
	private String potType;
	private String potName;
	private String startDate;
	private String endDate;
	private String savingType;
	private String frequency;
	private String targetAmount;
	private String schedulePaymentAmount;
	private String status;
	private String potContractId;
	private String transactionAmount;
	private String message;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public SavingsPotDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public SavingsPotDTO(String accountId, String potType, String potName, String startDate, String endDate,
			String savingType, String frequency, String targetAmount, String schedulePaymentAmount, String status,
			String potContractId, String transactionAmount) {
		super();
		this.accountId = accountId;
		this.potType = potType;
		this.potName = potName;
		this.startDate = startDate;
		this.endDate = endDate;
		this.savingType = savingType;
		this.frequency = frequency;
		this.targetAmount = targetAmount;
		this.schedulePaymentAmount = schedulePaymentAmount;
		this.status = status;
		this.potContractId = potContractId;
		this.transactionAmount = transactionAmount;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getPotType() {
		return potType;
	}

	public void setPotType(String potType) {
		this.potType = potType;
	}

	public String getPotName() {
		return potName;
	}

	public void setPotName(String potName) {
		this.potName = potName;
	}

	public String getStartDate() {
		return startDate;
	}

	public void setStartDate(String startDate) {
		this.startDate = startDate;
	}

	public String getEndDate() {
		return endDate;
	}

	public void setEndDate(String endDate) {
		this.endDate = endDate;
	}

	public String getSavingType() {
		return savingType;
	}

	public void setSavingType(String savingType) {
		this.savingType = savingType;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(String targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getSchedulePaymentAmount() {
		return schedulePaymentAmount;
	}

	public void setSchedulePaymentAmount(String schedulePaymentAmount) {
		this.schedulePaymentAmount = schedulePaymentAmount;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getPotContractId() {
		return potContractId;
	}

	public void setPotContractId(String potContractId) {
		this.potContractId = potContractId;
	}

	public String getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(String transactionAmount) {
		this.transactionAmount = transactionAmount;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountId == null) ? 0 : accountId.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((potContractId == null) ? 0 : potContractId.hashCode());
		result = prime * result + ((potName == null) ? 0 : potName.hashCode());
		result = prime * result + ((potType == null) ? 0 : potType.hashCode());
		result = prime * result + ((savingType == null) ? 0 : savingType.hashCode());
		result = prime * result + ((schedulePaymentAmount == null) ? 0 : schedulePaymentAmount.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((targetAmount == null) ? 0 : targetAmount.hashCode());
		result = prime * result + ((transactionAmount == null) ? 0 : transactionAmount.hashCode());
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
		SavingsPotDTO other = (SavingsPotDTO) obj;
		if (accountId == null) {
			if (other.accountId != null)
				return false;
		} else if (!accountId.equals(other.accountId))
			return false;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (frequency == null) {
			if (other.frequency != null)
				return false;
		} else if (!frequency.equals(other.frequency))
			return false;
		if (potContractId == null) {
			if (other.potContractId != null)
				return false;
		} else if (!potContractId.equals(other.potContractId))
			return false;
		if (potName == null) {
			if (other.potName != null)
				return false;
		} else if (!potName.equals(other.potName))
			return false;
		if (potType == null) {
			if (other.potType != null)
				return false;
		} else if (!potType.equals(other.potType))
			return false;
		if (savingType == null) {
			if (other.savingType != null)
				return false;
		} else if (!savingType.equals(other.savingType))
			return false;
		if (schedulePaymentAmount == null) {
			if (other.schedulePaymentAmount != null)
				return false;
		} else if (!schedulePaymentAmount.equals(other.schedulePaymentAmount))
			return false;
		if (startDate == null) {
			if (other.startDate != null)
				return false;
		} else if (!startDate.equals(other.startDate))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (targetAmount == null) {
			if (other.targetAmount != null)
				return false;
		} else if (!targetAmount.equals(other.targetAmount))
			return false;
		if (transactionAmount == null) {
			if (other.transactionAmount != null)
				return false;
		} else if (!transactionAmount.equals(other.transactionAmount))
			return false;
		return true;
	}

}