package com.temenos.infinity.api.QRPayments.dto;


import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class QRPaymentDTO implements DBPDTO {

	private static final long serialVersionUID = -1974351165510782554L;
	public String fromAccountNumber;
	public String toAccountNumber;
	public String date;
	public String referenceId;
	public String amount;
	public String notes;
	public String fromAccountName;
	public String toAccountName;
	public String fromAccountCurrency;
	public String transactionCurrency;
	public String dbpErrCode;
	public String dbpErrMsg;
	public String errorDetails;
	

	public QRPaymentDTO() {
		super();
		// TODO Auto-generated constructor stub
	}

	public QRPaymentDTO(String fromAccountNumber, String toAccountNumber, String date, String referenceId,
			String amount, String notes, String fromAccountName, String toAccountName, String fromAccountCurrency,
			String transactionCurrency, String dbpErrCode, String dbpErrMsg, String errorDetails) {
		super();
		this.fromAccountNumber = fromAccountNumber;
		this.toAccountNumber = toAccountNumber;
		this.date = date;
		this.referenceId = referenceId;
		this.amount = amount;
		this.notes = notes;
		this.fromAccountName = fromAccountName;
		this.toAccountName = toAccountName;
		this.fromAccountCurrency = fromAccountCurrency;
		this.transactionCurrency = transactionCurrency;
		this.dbpErrCode = dbpErrCode;
		this.dbpErrMsg = dbpErrMsg;
		this.errorDetails = errorDetails;
	}

	public String getFromAccountNumber() {
		return fromAccountNumber;
	}

	public void setFromAccountNumer(String fromAccountNumer) {
		this.fromAccountNumber = fromAccountNumer;
	}

	public String getToAccountNumber() {
		return toAccountNumber;
	}

	public void setToAccountNumer(String toAccountNumber) {
		this.toAccountNumber = toAccountNumber;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getReferenceId() {
		return referenceId;
	}

	public void setReferenceId(String referenceId) {
		this.referenceId = referenceId;
	}

	public String getAmount() {
		return amount;
	}

	public void setAmount(String amount) {
		this.amount = amount;
	}
	
	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}

	public String getFromAccountName() {
		return fromAccountName;
	}

	public void setFromAccountName(String fromAccountName) {
		this.fromAccountName = fromAccountName;
	}
	
	public String getToAccountName() {
		return toAccountName;
	}

	public void setToAccountName(String toAccountName) {
		this.toAccountName = toAccountName;
	}
	
	public String getFromAccountCurrency() {
		return fromAccountCurrency;
	}

	public void setFromAccountCurrency(String fromAccountCurrency) {
		this.fromAccountCurrency = fromAccountCurrency;
	}
	
	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}
	
	public String getErrorDetails() {
		return errorDetails;
	}

	public void setErrorDetails(String errorDetails) {
		this.errorDetails = errorDetails;
	}
	
	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	public String getDbpErrCode() {
		return dbpErrCode;
	}

	public void setDbpErrCode(String dbpErrCode) {
		this.dbpErrCode = dbpErrCode;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((fromAccountNumber == null) ? 0 : fromAccountNumber.hashCode());
		result = prime * result + ((toAccountNumber == null) ? 0 : toAccountNumber.hashCode());
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + ((referenceId == null) ? 0 : referenceId.hashCode());
		result = prime * result + ((amount == null) ? 0 : amount.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((fromAccountName == null) ? 0 : fromAccountName.hashCode());
		result = prime * result + ((toAccountName == null) ? 0 : toAccountName.hashCode());
		result = prime * result + ((fromAccountCurrency == null) ? 0 : fromAccountCurrency.hashCode());
		result = prime * result + ((transactionCurrency == null) ? 0 : transactionCurrency.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrCode == null) ? 0 : dbpErrCode.hashCode());
		result = prime * result + ((errorDetails == null) ? 0 : errorDetails.hashCode());
		
		return result;
}
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		QRPaymentDTO other = (QRPaymentDTO) obj;
		if (referenceId != other.referenceId)
			return false;
		return true;
	}
}

