package com.temenos.infinity.api.chequemanagement.dto;

import java.io.Serializable;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class ChequeBook implements Serializable, DBPDTO {

    private static final long serialVersionUID = 653792502420487768L;

    private String chequeIssueId;
    private String chequeStatus;
    private String accountID;
    private String validate;
    private String status;
    private String fees;
    private String currencyId;
    private String note;
    private String chequeNumberStart;
    private String description;
    private String requestDate;
    private String issueDate;
    private String fee;
    private String code;
    private String message;
    private String signatoryApprovalRequired;
    private String requestId;
    
    private String numberOfLeaves;
    private String numberOfChequeBooks;
    private String deliveryType;
    private String address;
    private String recordVersion;
    
    /**
     * Constructor using super class
     */
    public ChequeBook() {
        super();
    }


    public ChequeBook(String chequeIssueId, String chequeStatus, String accountID, String validate, String status,
            String fees, String currencyId, String note, String chequeNumberStart, String description,
            String requestDate, String issueDate, String fee, String code, String message, String signatoryApprovalRequired, String requestId, String recordVersion) {
        super();
        this.chequeIssueId = chequeIssueId;
        this.chequeStatus = chequeStatus;
        this.accountID = accountID;
        this.validate = validate;
        this.status = status;
        this.fees = fees;
        this.currencyId = currencyId;
        this.note = note;
        this.chequeNumberStart = chequeNumberStart;
        this.description = description;
        this.requestDate = requestDate;
        this.issueDate = issueDate;
        this.fee = fee;
        this.code = code;
        this.message = message;
        this.signatoryApprovalRequired = signatoryApprovalRequired;
        this.requestId = requestId;
        this.recordVersion = recordVersion;
    }


    public String getChequeIssueId() {
        return chequeIssueId;
    }


    public void setChequeIssueId(String chequeIssueId) {
        this.chequeIssueId = chequeIssueId;
    }


    public String getChequeStatus() {
        return chequeStatus;
    }


    public void setChequeStatus(String chequeStatus) {
        this.chequeStatus = chequeStatus;
    }


    public String getAccountID() {
        return accountID;
    }


    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }


    public String getValidate() {
        return validate;
    }


    public void setValidate(String validate) {
        this.validate = validate;
    }


    public String getStatus() {
        return status;
    }


    public void setStatus(String status) {
        this.status = status;
    }


    public String getFees() {
        return fees;
    }


    public void setFees(String fees) {
        this.fees = fees;
    }


    public String getCurrencyId() {
        return currencyId;
    }


    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }


    public String getNote() {
        return note;
    }


    public void setNote(String note) {
        this.note = note;
    }


    public String getChequeNumberStart() {
        return chequeNumberStart;
    }


    public void setChequeNumberStart(String chequeNumberStart) {
        this.chequeNumberStart = chequeNumberStart;
    }


    public String getDescription() {
        return description;
    }


    public void setDescription(String description) {
        this.description = description;
    }


    public String getRequestDate() {
        return requestDate;
    }


    public void setRequestDate(String requestDate) {
        this.requestDate = requestDate;
    }


    public String getIssueDate() {
        return issueDate;
    }


    public void setIssueDate(String issueDate) {
        this.issueDate = issueDate;
    }


    public String getFee() {
        return fee;
    }


    public void setFee(String fee) {
        this.fee = fee;
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


    public String getNumberOfLeaves() {
		return numberOfLeaves;
	}


	public void setNumberOfLeaves(String numberOfLeaves) {
		this.numberOfLeaves = numberOfLeaves;
	}


	public String getNumberOfChequeBooks() {
		return numberOfChequeBooks;
	}


	public void setNumberOfChequeBooks(String numberOfChequeBooks) {
		this.numberOfChequeBooks = numberOfChequeBooks;
	}


	public String getDeliveryType() {
		return deliveryType;
	}


	public void setDeliveryType(String deliveryType) {
		this.deliveryType = deliveryType;
	}


	public String getAddress() {
		return address;
	}


	public void setAddress(String address) {
		this.address = address;
	}

	public String getSignatoryApprovalRequired() {
		return signatoryApprovalRequired;
	}

	public void setSignatoryApprovalRequired(String signatoryApprovalRequired) {
		this.signatoryApprovalRequired = signatoryApprovalRequired;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRecordVersion() {
		return recordVersion;
	}

	public void setRecordVersion(String recordVersion) {
		this.recordVersion = recordVersion;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((chequeIssueId == null) ? 0 : chequeIssueId.hashCode());
		result = prime * result + ((chequeNumberStart == null) ? 0 : chequeNumberStart.hashCode());
		result = prime * result + ((chequeStatus == null) ? 0 : chequeStatus.hashCode());
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((currencyId == null) ? 0 : currencyId.hashCode());
		result = prime * result + ((deliveryType == null) ? 0 : deliveryType.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((fee == null) ? 0 : fee.hashCode());
		result = prime * result + ((fees == null) ? 0 : fees.hashCode());
		result = prime * result + ((issueDate == null) ? 0 : issueDate.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + ((numberOfChequeBooks == null) ? 0 : numberOfChequeBooks.hashCode());
		result = prime * result + ((numberOfLeaves == null) ? 0 : numberOfLeaves.hashCode());
		result = prime * result + ((requestDate == null) ? 0 : requestDate.hashCode());
		result = prime * result + ((requestId == null) ? 0 : requestId.hashCode());
		result = prime * result + ((signatoryApprovalRequired == null) ? 0 : signatoryApprovalRequired.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((validate == null) ? 0 : validate.hashCode());
		result = prime * result + ((recordVersion == null) ? 0 : recordVersion.hashCode());
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
		ChequeBook other = (ChequeBook) obj;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (chequeIssueId == null) {
			if (other.chequeIssueId != null)
				return false;
		} else if (!chequeIssueId.equals(other.chequeIssueId))
			return false;
		if (chequeNumberStart == null) {
			if (other.chequeNumberStart != null)
				return false;
		} else if (!chequeNumberStart.equals(other.chequeNumberStart))
			return false;
		if (chequeStatus == null) {
			if (other.chequeStatus != null)
				return false;
		} else if (!chequeStatus.equals(other.chequeStatus))
			return false;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (currencyId == null) {
			if (other.currencyId != null)
				return false;
		} else if (!currencyId.equals(other.currencyId))
			return false;
		if (deliveryType == null) {
			if (other.deliveryType != null)
				return false;
		} else if (!deliveryType.equals(other.deliveryType))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (fee == null) {
			if (other.fee != null)
				return false;
		} else if (!fee.equals(other.fee))
			return false;
		if (fees == null) {
			if (other.fees != null)
				return false;
		} else if (!fees.equals(other.fees))
			return false;
		if (issueDate == null) {
			if (other.issueDate != null)
				return false;
		} else if (!issueDate.equals(other.issueDate))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (numberOfChequeBooks == null) {
			if (other.numberOfChequeBooks != null)
				return false;
		} else if (!numberOfChequeBooks.equals(other.numberOfChequeBooks))
			return false;
		if (numberOfLeaves == null) {
			if (other.numberOfLeaves != null)
				return false;
		} else if (!numberOfLeaves.equals(other.numberOfLeaves))
			return false;
		if (requestDate == null) {
			if (other.requestDate != null)
				return false;
		} else if (!requestDate.equals(other.requestDate))
			return false;
		if (requestId == null) {
			if (other.requestId != null)
				return false;
		} else if (!requestId.equals(other.requestId))
			return false;
		if (signatoryApprovalRequired == null) {
			if (other.signatoryApprovalRequired != null)
				return false;
		} else if (!signatoryApprovalRequired.equals(other.signatoryApprovalRequired))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (validate == null) {
			if (other.validate != null)
				return false;
		} else if (!validate.equals(other.validate))
			return false;
		if (recordVersion == null) {
			if (other.recordVersion != null)
				return false;
		} else if (!recordVersion.equals(other.recordVersion))
			return false;
		return true;
	}

}