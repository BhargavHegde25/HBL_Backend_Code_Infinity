package com.temenos.dbx.product.payeeservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BulkPaymentPayeeDTO implements DBPDTO {
	
	private static final long serialVersionUID = 2117658987075901245L;

	private String bic;
	private String bankName;
	@JsonAlias({"AccountHolder"})
	private String beneficiaryName;
	private String iban;
	@JsonAlias({"Account_id"})
	private String accountNumber;
	private String swift;
	private String city;
	private String country;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg", "ErrorMessage"})
	private String dbpErrMsg;

	public BulkPaymentPayeeDTO() {
		super();
	}
	
	public BulkPaymentPayeeDTO(String bic, String bankName, String beneficiaryName, String iban, 
			String accountNumber, String swift,ErrorCodeEnum DbpErrorCode, String DbpErrMsg,
			String city, String country) {
		super();
		this.bic = bic;
		this.bankName = bankName;
		this.beneficiaryName = beneficiaryName;
		this.iban = iban;
		this.accountNumber = accountNumber;
		this.swift = swift;
		this.dbpErrorCode = DbpErrorCode;
		this.dbpErrMsg = DbpErrMsg;
		this.city = city;
		this.country = country;
	}

	public String getBIC() {
		return bic;
	}
	public void setBIC(String bic) {
		this.bic = bic;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
	}
	public String getBeneficiaryName() {
		return beneficiaryName;
	}
	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}
	public String getIBAN() {
		return iban;
	}
	public void setIBAN(String iban) {
		this.iban = iban;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getSwift() {
		return swift;
	}
	public void setSwift(String swift) {
		this.swift = swift;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public ErrorCodeEnum getDbpErrorCode() {
		return dbpErrorCode;
	}

	public void setDbpErrorCode(ErrorCodeEnum dbpErrorCode) {
		this.dbpErrorCode = dbpErrorCode;
	}

	public String getDbpErrMsg() {
		return dbpErrMsg;
	}

	public void setDbpErrMsg(String dbpErrMsg) {
		this.dbpErrMsg = dbpErrMsg;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());		
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bic == null) ? 0 : bic.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + ((swift == null) ? 0 : swift.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
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
		BulkPaymentPayeeDTO other = (BulkPaymentPayeeDTO) obj;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		return true;
	}
	

}
