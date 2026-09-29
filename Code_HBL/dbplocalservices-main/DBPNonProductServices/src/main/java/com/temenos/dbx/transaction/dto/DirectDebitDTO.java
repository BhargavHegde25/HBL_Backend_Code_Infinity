package com.temenos.dbx.transaction.dto;

import com.dbp.core.api.DBPDTO;

public class DirectDebitDTO implements DBPDTO {
	private static final long serialVersionUID = -6570098244266983919L;
	private String accountID;
	private String directDebitId;
	private String orderId;
	private String directDebitStatus;
	private String beneficiaryName;
	private String fromAccountName;
	private String signingDate;
	private String lastPaymentDate;
	private String mandateReference;
	private String status;
	private String message;

	public DirectDebitDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DirectDebitDTO(String accountID, String directDebitId, String directDebitStatus, String beneficiaryName,
			String signingDate, String lastPaymentDate, String mandateReference, String status, String message, String fromAccountName) {
		super();
		this.accountID = accountID;
		this.directDebitId = directDebitId;
		this.fromAccountName = fromAccountName;
		this.directDebitStatus = directDebitStatus;
		this.beneficiaryName = beneficiaryName;
		this.signingDate = signingDate;
		this.lastPaymentDate = lastPaymentDate;
		this.mandateReference = mandateReference;
		this.status = status;
		this.message = message;
		this.orderId = orderId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountID == null) ? 0 : accountID.hashCode());
		result = prime * result + ((directDebitId == null) ? 0 : directDebitId.hashCode());
		result = prime * result + ((directDebitStatus == null) ? 0 : directDebitStatus.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((signingDate == null) ? 0 : signingDate.hashCode());
		result = prime * result + ((lastPaymentDate == null) ? 0 : lastPaymentDate.hashCode());
		result = prime * result + ((mandateReference == null) ? 0 : mandateReference.hashCode());
		result = prime * result + ((status == null) ? 0 : status.hashCode());
		result = prime * result + ((message == null) ? 0 : message.hashCode());
		result = prime * result + ((fromAccountName == null) ? 0 : fromAccountName.hashCode());
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
		DirectDebitDTO other = (DirectDebitDTO) obj;
		if (accountID == null) {
			if (other.accountID != null)
				return false;
		} else if (!accountID.equals(other.accountID))
			return false;
		if (directDebitId == null) {
			if (other.directDebitId != null)
				return false;
		} else if (!directDebitId.equals(other.directDebitId))
			return false;
		if (directDebitStatus == null) {
			if (other.directDebitStatus != null)
				return false;
		} else if (!directDebitStatus.equals(other.directDebitStatus))
			return false;
		if (beneficiaryName == null) {
			if (other.beneficiaryName != null)
				return false;
		} else if (!beneficiaryName.equals(other.beneficiaryName))
			return false;
		if (signingDate == null) {
			if (other.signingDate != null)
				return false;
		} else if (!signingDate.equals(other.signingDate))
			return false;
		if (lastPaymentDate == null) {
			if (other.lastPaymentDate != null)
				return false;
		} else if (!lastPaymentDate.equals(other.lastPaymentDate))
			return false;
		if (mandateReference == null) {
			if (other.mandateReference != null)
				return false;
		} else if (!mandateReference.equals(other.mandateReference))
			return false;
		if (status == null) {
			if (other.status != null)
				return false;
		} else if (!status.equals(other.status))
			return false;
		if (message == null) {
			if (other.message != null)
				return false;
		} else if (!message.equals(other.message))
			return false;
		if (fromAccountName == null) {
			if (other.fromAccountName != null)
				return false;
		} else if (!fromAccountName.equals(other.fromAccountName))
			return false;

		return true;
	}

	public String getDirectDebitId() {
		return directDebitId;
	}

	public void setDirectDebitId(String directDebitId) {
		this.directDebitId = directDebitId;
	}

	public String getDirectDebitStatus() {
		return directDebitStatus;
	}

	public void setDirectDebitStatus(String directDebitStatus) {
		this.directDebitStatus = directDebitStatus;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}

	public String getSigningDate() {
		return signingDate;
	}

	public void setSigningDate(String signingDate) {
		this.signingDate = signingDate;
	}

	public String getLastPaymentDate() {
		return lastPaymentDate;
	}

	public void setLastPaymentDate(String lastPaymentDate) {
		this.lastPaymentDate = lastPaymentDate;
	}

	public String getMandateReference() {
		return mandateReference;
	}

	public void setMandateReference(String mandateReference) {
		this.mandateReference = mandateReference;
	}

	public String getAccountID() {
		return accountID;
	}

	public void setAccountID(String accountID) {
		this.accountID = accountID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}
	
	public String getOrderId() {
		return orderId;
	}

	public void setOrderId(String orderId) {
		this.orderId = orderId;
	}
}
