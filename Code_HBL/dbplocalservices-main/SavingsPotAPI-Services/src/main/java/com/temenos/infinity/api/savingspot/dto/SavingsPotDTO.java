package com.temenos.infinity.api.savingspot.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;


public class SavingsPotDTO implements Serializable, DBPDTO {
	
    private static final long serialVersionUID = 2483378820155469411L;
	private String savingsPotId;
    private String partyId;
    private String portfolioId;
    private String productId;
    private String currency;
    private String fundingAccountId;
    private String fundingAccountHoldingsId;
    private String potAccountId;
    private String potType;
    private String potName;
    private String savingsType;
    private String targetAmount;
    private String periodicContribution;
    private String targetPeriod;
    private String frequency;
    private String creationDate;
    private String startDate;
    private String endDate;
    private String availableBalance;
    private String amountWithdrawn;
    private String status;
    private String lastModifiedDate;
    private String frequencyDay;
    private String potCurrentStatus;
    private String potAmountPercentage;
    private String monthsLeftForCompletion;
    
    /**
	 * @return the opstatus
	 */
	public String getOpstatus() {
		return opstatus;
	}

	/**
	 * @param opstatus the opstatus to set
	 */
	public void setOpstatus(String opstatus) {
		this.opstatus = opstatus;
	}

	/**
	 * @return the httpStatusCode
	 */
	public String getHttpStatusCode() {
		return httpStatusCode;
	}

	/**
	 * @param httpStatusCode the httpStatusCode to set
	 */
	public void setHttpStatusCode(String httpStatusCode) {
		this.httpStatusCode = httpStatusCode;
	}

	private String remainingSavings;
    private String message;
    private String errMessage;
    private String errCode;
    private String errmsg;
	private String opstatus;
    private String httpStatusCode;
	private String legalEntityId;
    
    /**
   	 * @return the errMsg
   	 */
    public String getErrmsg() {
		return errmsg;
	}

	public void setErrmsg(String errmsg) {
		this.errmsg = errmsg;
	}
	
	
    /**
	 * @return the errMessage
	 */
	public String getErrMessage() {
		return errMessage;
	}

	/**
	 * @param errMessage the errMessage to set
	 */
	public void setErrMessage(String errMessage) {
		this.errMessage = errMessage;
	}

	/**
	 * @return the errCode
	 */
	public String getErrCode() {
		return errCode;
	}

	/**
	 * @param errCode the errCode to set
	 */
	public void setErrCode(String errCode) {
		this.errCode = errCode;
	}

	public SavingsPotDTO() {
        super();

    }

    public SavingsPotDTO(String savingsPotId, String partyId, String portfolioId,
            String productId, String currency, String fundingAccountId, String fundingAccountHoldingsId,
            String potAccountId, String potType, String potName, String savingsType, String targetAmount,
            String periodicContribution, String targetPeriod, String frequency,String creationDate, String startDate, String endDate, 
            String availableBalance, String amountWithdrawn,String status, String lastModifiedDate, String frequencyDay,
            String potCurrentStatus, String potAmountPercentage,String monthsLeftForCompletion, String remainingSavings, String message, String legalEntityId) {
        super();
        this.savingsPotId = savingsPotId;
        this.partyId = partyId;
        this.portfolioId = portfolioId;
        this.productId = productId;
        this.currency = currency;
        this.fundingAccountId = fundingAccountId;
        this.fundingAccountHoldingsId = fundingAccountHoldingsId;
        this.potAccountId = potAccountId;
        this.potType = potType;
        this.potName = potName;
        this.savingsType = savingsType;
        this.targetAmount = targetAmount;
        this.periodicContribution = periodicContribution;
        this.targetPeriod = targetPeriod;
        this.frequency = frequency;
        this.creationDate = creationDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.availableBalance = availableBalance;
        this.amountWithdrawn = amountWithdrawn;
        this.status = status;
        this.lastModifiedDate = lastModifiedDate;
        this.frequencyDay = frequencyDay;
        this.potCurrentStatus = potCurrentStatus;
        this.potAmountPercentage = potAmountPercentage;
        this.monthsLeftForCompletion = monthsLeftForCompletion;
        this.remainingSavings = remainingSavings;
        this.message = message;
        this.legalEntityId = legalEntityId;
        
    }

	public String getSavingsPotId() {
		return savingsPotId;
	}

	public void setSavingsPotId(String savingsPotId) {
		this.savingsPotId = savingsPotId;
	}

	public String getPartyId() {
		return partyId;
	}

	public void setPartyId(String partyId) {
		this.partyId = partyId;
	}

	public String getPortfolioId() {
		return portfolioId;
	}

	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	public String getProductId() {
		return productId;
	}

	public void setProductId(String productId) {
		this.productId = productId;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public String getFundingAccountId() {
		return fundingAccountId;
	}

	public void setFundingAccountId(String fundingAccountId) {
		this.fundingAccountId = fundingAccountId;
	}

	public String getFundingAccountHoldingsId() {
		return fundingAccountHoldingsId;
	}

	public void setfundingAccountHoldingsId(String fundingAccountHoldingsId) {
		this.fundingAccountHoldingsId = fundingAccountHoldingsId;
	}

	public String getPotAccountId() {
		return potAccountId;
	}

	public void setPotAccountId(String potAccountId) {
		this.potAccountId = potAccountId;
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

	public String getSavingsType() {
		return savingsType;
	}

	public void setSavingsType(String savingsType) {
		this.savingsType = savingsType;
	}

	public String getTargetAmount() {
		return targetAmount;
	}

	public void setTargetAmount(String targetAmount) {
		this.targetAmount = targetAmount;
	}

	public String getPeriodicContribution() {
		return periodicContribution;
	}

	public void setPeriodicContribution(String periodicContribution) {
		this.periodicContribution = periodicContribution;
	}

	public String getTargetPeriod() {
		return targetPeriod;
	}

	public void setTargetPeriod(String targetPeriod) {
		this.targetPeriod = targetPeriod;
	}

	public String getFrequency() {
		return frequency;
	}

	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}

	public String getCreationDate() {
		return creationDate;
	}

	public void setCreationDate(String creationDate) {
		this.creationDate = creationDate;
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

	public String getAvailableBalance() {
		return availableBalance;
	}

	public void setAvailableBalance(String availableBalance) {
		this.availableBalance = availableBalance;
	}

	public String getAmountWithdrawn() {
		return amountWithdrawn;
	}

	public void setAmountWithdrawn(String amountWithdrawn) {
		this.amountWithdrawn = amountWithdrawn;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getLastModifiedDate() {
		return lastModifiedDate;
	}

	public void setLastModifiedDate(String lastModifiedDate) {
		this.lastModifiedDate = lastModifiedDate;
	}

	public String getFrequencyDay() {
		return frequencyDay;
	}

	public void setFrequencyDay(String frequencyDay) {
		this.frequencyDay = frequencyDay;
	}

	public String getPotCurrentStatus() {
		return potCurrentStatus;
	}

	public void setPotCurrentStatus(String potCurrentStatus) {
		this.potCurrentStatus = potCurrentStatus;
	}

	public String getPotAmountPercentage() {
		return potAmountPercentage;
	}

	public void setPotAmountPercentage(String potAmountPercentage) {
		this.potAmountPercentage = potAmountPercentage;
	}

	public String getMonthsLeftForCompletion() {
		return monthsLeftForCompletion;
	}

	public void setMonthsLeftForCompletion(String monthsLeftForCompletion) {
		this.monthsLeftForCompletion = monthsLeftForCompletion;
	}

	public String getRemainingSavings() {
		return remainingSavings;
	}

	public void setRemainingSavings(String remainingSavings) {
		this.remainingSavings = remainingSavings;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((amountWithdrawn == null) ? 0 : amountWithdrawn.hashCode());
		result = prime * result + ((availableBalance == null) ? 0 : availableBalance.hashCode());
		result = prime * result + ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result + ((currency == null) ? 0 : currency.hashCode());
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime * result + ((frequency == null) ? 0 : frequency.hashCode());
		result = prime * result + ((frequencyDay == null) ? 0 : frequencyDay.hashCode());
		result = prime * result + ((fundingAccountHoldingsId == null) ? 0 : fundingAccountHoldingsId.hashCode());
		result = prime * result + ((fundingAccountId == null) ? 0 : fundingAccountId.hashCode());
		result = prime * result + ((lastModifiedDate == null) ? 0 : lastModifiedDate.hashCode());
		result = prime * result + ((monthsLeftForCompletion == null) ? 0 : monthsLeftForCompletion.hashCode());
		result = prime * result + ((partyId == null) ? 0 : partyId.hashCode());
		result = prime * result + ((periodicContribution == null) ? 0 : periodicContribution.hashCode());
		result = prime * result + ((portfolioId == null) ? 0 : portfolioId.hashCode());
		result = prime * result + ((potAccountId == null) ? 0 : potAccountId.hashCode());
		result = prime * result + ((potAmountPercentage == null) ? 0 : potAmountPercentage.hashCode());
		result = prime * result + ((potCurrentStatus == null) ? 0 : potCurrentStatus.hashCode());
		result = prime * result + ((potName == null) ? 0 : potName.hashCode());
		result = prime * result + ((potType == null) ? 0 : potType.hashCode());
		result = prime * result + ((productId == null) ? 0 : productId.hashCode());
		result = prime * result + ((remainingSavings == null) ? 0 : remainingSavings.hashCode());
		result = prime * result + ((savingsPotId == null) ? 0 : savingsPotId.hashCode());
		result = prime * result + ((savingsType == null) ? 0 : savingsType.hashCode());
		result = prime * result + ((startDate == null) ? 0 : startDate.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((targetAmount == null) ? 0 : targetAmount.hashCode());
		result = prime * result + ((targetPeriod == null) ? 0 : targetPeriod.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((legalEntityId == null) ? 0 : legalEntityId.hashCode());
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
		if (amountWithdrawn == null) {
			if (other.amountWithdrawn != null)
				return false;
		} else if (!amountWithdrawn.equals(other.amountWithdrawn))
			return false;
		if (availableBalance == null) {
			if (other.availableBalance != null)
				return false;
		} else if (!availableBalance.equals(other.availableBalance))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (currency == null) {
			if (other.currency != null)
				return false;
		} else if (!currency.equals(other.currency))
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
		if (frequencyDay == null) {
			if (other.frequencyDay != null)
				return false;
		} else if (!frequencyDay.equals(other.frequencyDay))
			return false;
		if (fundingAccountHoldingsId == null) {
			if (other.fundingAccountHoldingsId != null)
				return false;
		} else if (!fundingAccountHoldingsId.equals(other.fundingAccountHoldingsId))
			return false;
		if (fundingAccountId == null) {
			if (other.fundingAccountId != null)
				return false;
		} else if (!fundingAccountId.equals(other.fundingAccountId))
			return false;
		if (lastModifiedDate == null) {
			if (other.lastModifiedDate != null)
				return false;
		} else if (!lastModifiedDate.equals(other.lastModifiedDate))
			return false;
		if (monthsLeftForCompletion == null) {
			if (other.monthsLeftForCompletion != null)
				return false;
		} else if (!monthsLeftForCompletion.equals(other.monthsLeftForCompletion))
			return false;
		if (partyId == null) {
			if (other.partyId != null)
				return false;
		} else if (!partyId.equals(other.partyId))
			return false;
		if (periodicContribution == null) {
			if (other.periodicContribution != null)
				return false;
		} else if (!periodicContribution.equals(other.periodicContribution))
			return false;
		if (portfolioId == null) {
			if (other.portfolioId != null)
				return false;
		} else if (!portfolioId.equals(other.portfolioId))
			return false;
		if (potAccountId == null) {
			if (other.potAccountId != null)
				return false;
		} else if (!potAccountId.equals(other.potAccountId))
			return false;
		if (potAmountPercentage == null) {
			if (other.potAmountPercentage != null)
				return false;
		} else if (!potAmountPercentage.equals(other.potAmountPercentage))
			return false;
		if (potCurrentStatus == null) {
			if (other.potCurrentStatus != null)
				return false;
		} else if (!potCurrentStatus.equals(other.potCurrentStatus))
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
		if (productId == null) {
			if (other.productId != null)
				return false;
		} else if (!productId.equals(other.productId))
			return false;
		if (remainingSavings == null) {
			if (other.remainingSavings != null)
				return false;
		} else if (!remainingSavings.equals(other.remainingSavings))
			return false;
		if (savingsPotId == null) {
			if (other.savingsPotId != null)
				return false;
		} else if (!savingsPotId.equals(other.savingsPotId))
			return false;
		if (savingsType == null) {
			if (other.savingsType != null)
				return false;
		} else if (!savingsType.equals(other.savingsType))
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
		if (targetPeriod == null) {
			if (other.targetPeriod != null)
				return false;
		} else if (!targetPeriod.equals(other.targetPeriod))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (legalEntityId == null) {
			if (other.legalEntityId != null)
				return false;
		} else if (!legalEntityId.equals(other.legalEntityId))
			return false;
		return true;
	}

	public String getLegalEntityId() {
		return legalEntityId;
	}

	public void setLegalEntityId(String legalEntityId) {
		this.legalEntityId = legalEntityId;
	}


}