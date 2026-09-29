package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class OneTimePayeeDTO implements DBPDTO{

	private static final long serialVersionUID = -6143143234401688198L;
	
	private long onetime_id;
	private String payeeName;
	private String payeeNickName;
	private String payeeType;
	private String wireAccountType;
	private String swiftCode;
	private String routingNumber;
	private String zipCode;
	private String cityName;
	private String state;
	private String country;
	private String payeeAddressLine1;
	private String payeeAddressLine2;
	private String bankName;
	private String internationalRoutingCode;
	private String bankAddressLine1;
	private String bankAddressLine2;
	private String bankCity;
	private String bankState;
	private String bankZip;
	
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private boolean softdeleteflag;

	public OneTimePayeeDTO() {
		super();
	}

	public OneTimePayeeDTO(long onetime_id, String payeeName, String payeeNickName, String payeeType,
			String wireAccountType, String swiftCode, String routingNumber, String zipCode, String cityName,
			String state, String country, String payeeAddressLine1, String payeeAddressLine2, String bankName,
			String internationalRoutingCode, String bankAddressLine1, String bankAddressLine2, String bankCity,
			String bankState, String bankZip, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, boolean softdeleteflag) {
		super();
		this.onetime_id = onetime_id;
		this.payeeName = payeeName;
		this.payeeNickName = payeeNickName;
		this.payeeType = payeeType;
		this.wireAccountType = wireAccountType;
		this.swiftCode = swiftCode;
		this.routingNumber = routingNumber;
		this.zipCode = zipCode;
		this.cityName = cityName;
		this.state = state;
		this.country = country;
		this.payeeAddressLine1 = payeeAddressLine1;
		this.payeeAddressLine2 = payeeAddressLine2;
		this.bankName = bankName;
		this.internationalRoutingCode = internationalRoutingCode;
		this.bankAddressLine1 = bankAddressLine1;
		this.bankAddressLine2 = bankAddressLine2;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.softdeleteflag = softdeleteflag;
	}

	public long getOnetime_id() {
		return onetime_id;
	}

	public void setOnetime_id(long onetime_id) {
		this.onetime_id = onetime_id;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}

	public boolean getSoftdeleteflag() {
		return softdeleteflag;
	}

	public void setSoftdeleteflag(boolean softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public String getPayeeNickName() {
		return payeeNickName;
	}

	public void setPayeeNickName(String payeeNickName) {
		this.payeeNickName = payeeNickName;
	}

	public String getPayeeType() {
		return payeeType;
	}

	public void setPayeeType(String payeeType) {
		this.payeeType = payeeType;
	}

	public String getWireAccountType() {
		return wireAccountType;
	}

	public void setWireAccountType(String wireAccountType) {
		this.wireAccountType = wireAccountType;
	}

	public String getSwiftCode() {
		return swiftCode;
	}

	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}

	public String getRoutingNumber() {
		return routingNumber;
	}

	public void setRoutingNumber(String routingNumber) {
		this.routingNumber = routingNumber;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getPayeeAddressLine1() {
		return payeeAddressLine1;
	}

	public void setPayeeAddressLine1(String payeeAddressLine1) {
		this.payeeAddressLine1 = payeeAddressLine1;
	}

	public String getPayeeAddressLine2() {
		return payeeAddressLine2;
	}

	public void setPayeeAddressLine2(String payeeAddressLine2) {
		this.payeeAddressLine2 = payeeAddressLine2;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getInternationalRoutingCode() {
		return internationalRoutingCode;
	}

	public void setInternationalRoutingCode(String internationalRoutingCode) {
		this.internationalRoutingCode = internationalRoutingCode;
	}

	public String getBankAddressLine1() {
		return bankAddressLine1;
	}

	public void setBankAddressLine1(String bankAddressLine1) {
		this.bankAddressLine1 = bankAddressLine1;
	}

	public String getBankAddressLine2() {
		return bankAddressLine2;
	}

	public void setBankAddressLine2(String bankAddressLine2) {
		this.bankAddressLine2 = bankAddressLine2;
	}

	public String getBankCity() {
		return bankCity;
	}

	public void setBankCity(String bankCity) {
		this.bankCity = bankCity;
	}

	public String getBankState() {
		return bankState;
	}

	public void setBankState(String bankState) {
		this.bankState = bankState;
	}

	public String getBankZip() {
		return bankZip;
	}

	public void setBankZip(String bankZip) {
		this.bankZip = bankZip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + (int) (onetime_id ^ (onetime_id >>> 32));
		result = prime * result + ((payeeAddressLine1 == null) ? 0 : payeeAddressLine1.hashCode());
		result = prime * result + ((payeeAddressLine2 == null) ? 0 : payeeAddressLine2.hashCode());
		result = prime * result + ((payeeName == null) ? 0 : payeeName.hashCode());
		result = prime * result + ((payeeNickName == null) ? 0 : payeeNickName.hashCode());
		result = prime * result + ((payeeType == null) ? 0 : payeeType.hashCode());
		result = prime * result + ((routingNumber == null) ? 0 : routingNumber.hashCode());
		result = prime * result + (softdeleteflag ? 1231 : 1237);
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
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
		OneTimePayeeDTO other = (OneTimePayeeDTO) obj;
		if (onetime_id != other.onetime_id)
			return false;
		return true;
	}
	
}