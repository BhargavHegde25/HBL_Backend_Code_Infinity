package com.temenos.infinity.api.arrangements.dto;

import com.dbp.core.api.DBPDTO;

public class AccountClosureDTO implements DBPDTO {

	
	/**
	 * Unique ID for Serialization
	 */
	private static final long serialVersionUID = 887984530375346133L;
	
	
	public AccountClosureDTO() {
		super();
	}
	
	public AccountClosureDTO(String accountName, String accountNumber, String accountType, String currentBalance, String closingReason,
			String IBAN, String SWIFTCode, String supportingDocumentData, String code, String message, String Id, String status,String errorMessage) {
		super();
		this.accountName = accountName;
		this.accountNumber = accountNumber;
		this.accountType = accountType;
		this.currentBalance = currentBalance;
		this.closingReason = closingReason;
		this.IBAN = IBAN;
		this.SWIFTCode = SWIFTCode;
		this.supportingDocumentData = supportingDocumentData;
		this.code = code;
		this.message = message;
		this.status = status;
		this.Id = Id;
		this.errorMessage = errorMessage;
	}
	private String accountName;
	private String accountNumber;
	private String accountType;
	private String currentBalance;
	private String closingReason;
	private String IBAN;
	private String SWIFTCode;
	private String supportingDocumentData;
	private String code;
	private String message;
	private String status;
	private String Id;
	private String errorMessage;
	private String customerid;
	

    public String getAccountName() {
		return accountName;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public String getAccountNumber() {
		return accountNumber;
	}

	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}

	public String getCurrentBalance() {
		return currentBalance;
	}

	public void setCurrentBalance(String currentBalance) {
		this.currentBalance = currentBalance;
	}

	public String getClosingReason() {
		return closingReason;
	}

	public void setClosingReason(String closingReason) {
		this.closingReason = closingReason;
	}

	public String getIBAN() {
		return IBAN;
	}

	public void setIBAN(String iBAN) {
		IBAN = iBAN;
	}

	public String getSWIFTCode() {
		return SWIFTCode;
	}

	public void setSWIFTCode(String sWIFTCode) {
		SWIFTCode = sWIFTCode;
	}

	public String getSupportingDocumentData() {
		return supportingDocumentData;
	}

	public void setSupportingDocumentData(String supportingDocumentData) {
		this.supportingDocumentData = supportingDocumentData;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getId() {
		return Id;
	}

	public void setId(String id) {
		Id = id;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	@Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((accountName == null) ? 0 : accountName.hashCode());
        result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
        result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
        result = prime * result + ((currentBalance == null) ? 0 : currentBalance.hashCode());
        result = prime * result + ((closingReason == null) ? 0 : closingReason.hashCode());
        result = prime * result + ((IBAN == null) ? 0 : IBAN.hashCode());
        result = prime * result + ((SWIFTCode == null) ? 0 : SWIFTCode.hashCode());
        result = prime * result + ((supportingDocumentData == null) ? 0 : supportingDocumentData.hashCode());
        result = prime * result + ((code == null) ? 0 : code.hashCode());
        result = prime * result + ((message == null) ? 0 : message.hashCode());
        result = prime * result + ((status == null) ? 0 : status.hashCode());
        result = prime * result + ((Id == null) ? 0 : Id.hashCode());
        result = prime * result + ((errorMessage == null) ? 0 : errorMessage.hashCode());
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
        AccountClosureDTO other = (AccountClosureDTO) obj;
        if (accountName == null) {
            if (other.accountName != null)
                return false;
        } else if (!accountName.equals(other.accountName))
            return false;
        if (accountNumber == null) {
            if (other.accountNumber != null)
                return false;
        } else if (!accountNumber.equals(other.accountNumber))
            return false;
        if (currentBalance == null) {
            if (other.currentBalance != null)
                return false;
        } else if (!currentBalance.equals(other.currentBalance))
            return false;
        if (accountType == null) {
            if (other.accountType != null)
                return false;
        } else if (!accountType.equals(other.accountType))
            return false;
        if (closingReason == null) {
            if (other.closingReason != null)
                return false;
        } else if (!closingReason.equals(other.closingReason))
            return false;
        if (IBAN == null) {
            if (other.IBAN != null)
                return false;
        } else if (!IBAN.equals(other.IBAN))
            return false;
        if (SWIFTCode == null) {
            if (other.SWIFTCode != null)
                return false;
        } else if (!SWIFTCode.equals(other.SWIFTCode))
            return false;
        if (supportingDocumentData == null) {
            if (other.supportingDocumentData != null)
                return false;
        } else if (!supportingDocumentData.equals(other.supportingDocumentData))
            return false;
        if (code == null) {
            if (other.code != null)
                return false;
        } else if (!code.equals(other.code))
            return false;
        if (message == null) {
            if (other.message != null)
                return false;
        } else if (!message.equals(other.message))
            return false;
        if (status == null) {
            if (other.status != null)
                return false;
        } else if (!status.equals(other.status))
            return false;
        if (Id == null) {
            if (other.Id != null)
                return false;
        } else if (!Id.equals(other.Id))
            return false;
        if (errorMessage == null) {
            if (other.errorMessage != null)
                return false;
        } else if (!errorMessage.equals(other.errorMessage))
            return false;
        return true;
    }

	public String getCustomerid() {
		return customerid;
	}

	public void setCustomerid(String customerid) {
		this.customerid = customerid;
	}	
	
}
