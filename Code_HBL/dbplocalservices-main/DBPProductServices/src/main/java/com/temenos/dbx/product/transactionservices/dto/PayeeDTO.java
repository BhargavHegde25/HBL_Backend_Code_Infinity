package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class PayeeDTO implements DBPDTO {

	private static final long serialVersionUID = 6945426919103902580L;
	
	@JsonProperty("Id")
	private int id;
	@JsonProperty("Type_id")
	private String type_id;
	private String name;
	private String accountNumber;
	private String companyName;
	private String phone;
	private String email;
	private String firstName;
	private String lastName;
	private int eBillEnable;
	@JsonProperty("Region_id")
	private int region_id;
	@JsonProperty("City_id")
	private int city_id;
	private String cityName;
	private String state;
	private String addressLine1;
	private String addressLine2;
	private String zipCode;
	@JsonProperty("User_Id")
	private String user_Id;
	private String nickName;
	private String softDelete;
	private int billermaster_id;
	private String isAutoPayEnabled;
	private String nameOnBill;
	private String notes;
	private String billerId;
	private String country;
	private String swiftCode;
	private String routingCode;
	private String bankName;
	private String bankAddressLine1;
	private String bankAddressLine2;
	private String bankCity;
	private String bankState;
	private String bankZip;
	private String isWiredRecepient;
	private String internationalAccountNumber;
	private String wireAccountType;
	private String internationalRoutingCode;
	private String isManuallyAdded;
	private String phoneExtension;
	private String phoneCountryCode;
	@JsonProperty("IBAN")
	private String iBAN;
	private String transitDays;
	
	
	
	public PayeeDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
    
	public PayeeDTO(int Id, String Type_id, String name, String accountNumber, String companyName, String phone,
			String email, String firstName, String lastName, int eBillEnable, int Region_id, int City_id,
			String cityName, String state, String addressLine1, String addressLine2, String zipCode, String User_Id,
			String nickName, String softDelete, int billermaster_id, String isAutoPayEnabled, String nameOnBill,
			String notes, String billerId, String country, String swiftCode, String routingCode, String bankName,
			String bankAddressLine1, String bankAddressLine2, String bankCity, String bankState, String bankZip,
			String isWiredRecepient, String internationalAccountNumber, String wireAccountType,
			String internationalRoutingCode, String isManuallyAdded, String phoneExtension, String phoneCountryCode,
			String IBAN, String transitDays) {
		super();
		this.id = Id;
		this.type_id = Type_id;
		this.name = name;
		this.accountNumber = accountNumber;
		this.companyName = companyName;
		this.phone = phone;
		this.email = email;
		this.firstName = firstName;
		this.lastName = lastName;
		this.eBillEnable = eBillEnable;
		this.region_id = Region_id;
		this.city_id = City_id;
		this.cityName = cityName;
		this.state = state;
		this.addressLine1 = addressLine1;
		this.addressLine2 = addressLine2;
		this.zipCode = zipCode;
		this.user_Id = User_Id;
		this.nickName = nickName;
		this.softDelete = softDelete;
		this.billermaster_id = billermaster_id;
		this.isAutoPayEnabled = isAutoPayEnabled;
		this.nameOnBill = nameOnBill;
		this.notes = notes;
		this.billerId = billerId;
		this.country = country;
		this.swiftCode = swiftCode;
		this.routingCode = routingCode;
		this.bankName = bankName;
		this.bankAddressLine1 = bankAddressLine1;
		this.bankAddressLine2 = bankAddressLine2;
		this.bankCity = bankCity;
		this.bankState = bankState;
		this.bankZip = bankZip;
		this.isWiredRecepient = isWiredRecepient;
		this.internationalAccountNumber = internationalAccountNumber;
		this.wireAccountType = wireAccountType;
		this.internationalRoutingCode = internationalRoutingCode;
		this.isManuallyAdded = isManuallyAdded;
		this.phoneExtension = phoneExtension;
		this.phoneCountryCode = phoneCountryCode;
		this.iBAN = IBAN;
		this.transitDays = transitDays;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	public String getType_id() {
		return type_id;
	}
	public void setType_id(String type_id) {
		this.type_id = type_id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getAccountNumber() {
		return accountNumber;
	}
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}
	public String getCompanyName() {
		return companyName;
	}
	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFirstName() {
		return firstName;
	}
	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public void setLastName(String lastName) {
		this.lastName = lastName;
	}
	public int geteBillEnable() {
		return eBillEnable;
	}
	public void seteBillEnable(int eBillEnable) {
		this.eBillEnable = eBillEnable;
	}
	public int getRegion_id() {
		return region_id;
	}
	public void setRegion_id(int region_id) {
		this.region_id = region_id;
	}
	public int getCity_id() {
		return city_id;
	}
	public void setCity_id(int city_id) {
		this.city_id = city_id;
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
	public String getAddressLine1() {
		return addressLine1;
	}
	public void setAddressLine1(String addressLine1) {
		this.addressLine1 = addressLine1;
	}
	public String getAddressLine2() {
		return addressLine2;
	}
	public void setAddressLine2(String addressLine2) {
		this.addressLine2 = addressLine2;
	}
	public String getZipCode() {
		return zipCode;
	}
	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}
	public String getUser_Id() {
		return user_Id;
	}
	public void setUser_Id(String user_Id) {
		this.user_Id = user_Id;
	}
	public String getNickName() {
		return nickName;
	}
	public void setNickName(String nickName) {
		this.nickName = nickName;
	}
	public String getSoftDelete() {
		return softDelete;
	}
	public void setSoftDelete(String softDelete) {
		this.softDelete = softDelete;
	}
	public int getBillermaster_id() {
		return billermaster_id;
	}
	public void setBillermaster_id(int billermaster_id) {
		this.billermaster_id = billermaster_id;
	}
	public String getIsAutoPayEnabled() {
		return isAutoPayEnabled;
	}
	public void setIsAutoPayEnabled(String isAutoPayEnabled) {
		this.isAutoPayEnabled = isAutoPayEnabled;
	}
	public String getNameOnBill() {
		return nameOnBill;
	}
	public void setNameOnBill(String nameOnBill) {
		this.nameOnBill = nameOnBill;
	}
	public String getNotes() {
		return notes;
	}
	public void setNotes(String notes) {
		this.notes = notes;
	}
	public String getBillerId() {
		return billerId;
	}
	public void setBillerId(String billerId) {
		this.billerId = billerId;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getSwiftCode() {
		return swiftCode;
	}
	public void setSwiftCode(String swiftCode) {
		this.swiftCode = swiftCode;
	}
	public String getRoutingCode() {
		return routingCode;
	}
	public void setRoutingCode(String routingCode) {
		this.routingCode = routingCode;
	}
	public String getBankName() {
		return bankName;
	}
	public void setBankName(String bankName) {
		this.bankName = bankName;
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
	public String getIsWiredRecepient() {
		return isWiredRecepient;
	}
	public void setIsWiredRecepient(String isWiredRecepient) {
		this.isWiredRecepient = isWiredRecepient;
	}
	public String getInternationalAccountNumber() {
		return internationalAccountNumber;
	}
	public void setInternationalAccountNumber(String internationalAccountNumber) {
		this.internationalAccountNumber = internationalAccountNumber;
	}
	public String getWireAccountType() {
		return wireAccountType;
	}
	public void setWireAccountType(String wireAccountType) {
		this.wireAccountType = wireAccountType;
	}
	public String getInternationalRoutingCode() {
		return internationalRoutingCode;
	}
	public void setInternationalRoutingCode(String internationalRoutingCode) {
		this.internationalRoutingCode = internationalRoutingCode;
	}
	public String getIsManuallyAdded() {
		return isManuallyAdded;
	}
	public void setIsManuallyAdded(String isManuallyAdded) {
		this.isManuallyAdded = isManuallyAdded;
	}
	public String getPhoneExtension() {
		return phoneExtension;
	}
	public void setPhoneExtension(String phoneExtension) {
		this.phoneExtension = phoneExtension;
	}
	public String getPhoneCountryCode() {
		return phoneCountryCode;
	}
	public void setPhoneCountryCode(String phoneCountryCode) {
		this.phoneCountryCode = phoneCountryCode;
	}
	public String getIBAN() {
		return iBAN;
	}
	public void setIBAN(String iBAN) {
		this.iBAN = iBAN;
	}
	public String getTransitDays() {
		return transitDays;
	}
	public void setTransitDays(String transitDays) {
		this.transitDays = transitDays;
	}


	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((type_id == null) ? 0 : type_id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((accountNumber == null) ? 0 : accountNumber.hashCode());
		result = prime * result + ((companyName == null) ? 0 : companyName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result + ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + eBillEnable;
		result = prime * result + region_id;
		result = prime * result + city_id;
		result = prime * result + ((cityName == null) ? 0 : cityName.hashCode());
		result = prime * result + ((state == null) ? 0 : state.hashCode());
		result = prime * result + ((addressLine1 == null) ? 0 : addressLine1.hashCode());
		result = prime * result + ((addressLine2 == null) ? 0 : addressLine2.hashCode());
		result = prime * result + ((zipCode == null) ? 0 : zipCode.hashCode());
		result = prime * result + ((user_Id == null) ? 0 : user_Id.hashCode());
		result = prime * result + ((nickName == null) ? 0 : nickName.hashCode());
		result = prime * result + ((softDelete == null) ? 0 : softDelete.hashCode());
		result = prime * result + billermaster_id;
		result = prime * result + ((isAutoPayEnabled == null) ? 0 : isAutoPayEnabled.hashCode());
		result = prime * result + ((nameOnBill == null) ? 0 : nameOnBill.hashCode());
		result = prime * result + ((notes == null) ? 0 : notes.hashCode());
		result = prime * result + ((billerId == null) ? 0 : billerId.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((swiftCode == null) ? 0 : swiftCode.hashCode());
		result = prime * result + ((routingCode == null) ? 0 : routingCode.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankAddressLine1 == null) ? 0 : bankAddressLine1.hashCode());
		result = prime * result + ((bankAddressLine2 == null) ? 0 : bankAddressLine2.hashCode());
		result = prime * result + ((bankCity == null) ? 0 : bankCity.hashCode());
		result = prime * result + ((bankState == null) ? 0 : bankState.hashCode());
		result = prime * result + ((bankZip == null) ? 0 : bankZip.hashCode());
		result = prime * result + ((isWiredRecepient == null) ? 0 : isWiredRecepient.hashCode());
		result = prime * result + ((internationalAccountNumber == null) ? 0 : internationalAccountNumber.hashCode());
		result = prime * result + ((wireAccountType == null) ? 0 : wireAccountType.hashCode());
		result = prime * result + ((internationalRoutingCode == null) ? 0 : internationalRoutingCode.hashCode());
		result = prime * result + ((isManuallyAdded == null) ? 0 : isManuallyAdded.hashCode());
		result = prime * result + ((phoneExtension == null) ? 0 : phoneExtension.hashCode());
		result = prime * result + ((phoneCountryCode == null) ? 0 : phoneCountryCode.hashCode());
		result = prime * result + ((iBAN == null) ? 0 : iBAN.hashCode());
		result = prime * result + ((transitDays == null) ? 0 : transitDays.hashCode());

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
		PayeeDTO other = (PayeeDTO) obj;
		
		if (type_id == null) {
			if (other.type_id != null)
				return false;
		} else if (!type_id.equals(other.type_id))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (accountNumber == null) {
			if (other.accountNumber != null)
				return false;
		} else if (!accountNumber.equals(other.accountNumber))
			return false;
		if (companyName == null) {
			if (other.companyName != null)
				return false;
		} else if (!companyName.equals(other.companyName))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		
		if (cityName == null) {
			if (other.cityName != null)
				return false;
		} else if (!cityName.equals(other.cityName))
			return false;
		if (state == null) {
			if (other.state != null)
				return false;
		} else if (!state.equals(other.state))
			return false;
		if (addressLine1 == null) {
			if (other.addressLine1 != null)
				return false;
		} else if (!addressLine1.equals(other.addressLine1))
			return false;
		if (addressLine2 == null) {
			if (other.addressLine2 != null)
				return false;
		} else if (!addressLine2.equals(other.addressLine2))
			return false;
		if (zipCode == null) {
			if (other.zipCode != null)
				return false;
		} else if (!zipCode.equals(other.zipCode))
			return false;
		if (user_Id == null) {
			if (other.user_Id != null)
				return false;
		} else if (!user_Id.equals(other.user_Id))
			return false;
		if (nickName == null) {
			if (other.nickName != null)
				return false;
		} else if (!nickName.equals(other.nickName))
			return false;
		
		if (nameOnBill == null) {
			if (other.nameOnBill != null)
				return false;
		} else if (!nameOnBill.equals(other.nameOnBill))
			return false;
		if (notes == null) {
			if (other.notes != null)
				return false;
		} else if (!notes.equals(other.notes))
			return false;
		if (billerId == null) {
			if (other.billerId != null)
				return false;
		} else if (!billerId.equals(other.billerId))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (swiftCode == null) {
			if (other.swiftCode != null)
				return false;
		} else if (!swiftCode.equals(other.swiftCode))
			return false;
		if (routingCode == null) {
			if (other.routingCode != null)
				return false;
		} else if (!routingCode.equals(other.routingCode))
			return false;
		if (bankName == null) {
			if (other.bankName != null)
				return false;
		} else if (!bankName.equals(other.bankName))
			return false;
		if (bankAddressLine1 == null) {
			if (other.bankAddressLine1 != null)
				return false;
		} else if (!bankAddressLine1.equals(other.bankAddressLine1))
			return false;
		if (bankAddressLine2 == null) {
			if (other.bankAddressLine2 != null)
				return false;
		} else if (!bankAddressLine2.equals(other.bankAddressLine2))
			return false;
		if (bankCity == null) {
			if (other.bankCity != null)
				return false;
		} else if (!bankCity.equals(other.bankCity))
			return false;
		if (bankState == null) {
			if (other.bankState != null)
				return false;
		} else if (!bankState.equals(other.bankState))
			return false;
		if (bankZip == null) {
			if (other.bankZip != null)
				return false;
		} else if (!bankZip.equals(other.bankZip))
			return false;
		
		if (internationalAccountNumber == null) {
			if (other.internationalAccountNumber != null)
				return false;
		} else if (!internationalAccountNumber.equals(other.internationalAccountNumber))
			return false;
		if (wireAccountType == null) {
			if (other.wireAccountType != null)
				return false;
		} else if (!wireAccountType.equals(other.wireAccountType))
			return false;
		if (internationalRoutingCode == null) {
			if (other.internationalRoutingCode != null)
				return false;
		} else if (!internationalRoutingCode.equals(other.internationalRoutingCode))
			return false;
		
		if (phoneExtension == null) {
			if (other.phoneExtension != null)
				return false;
		} else if (!phoneExtension.equals(other.phoneExtension))
			return false;
		if (phoneCountryCode == null) {
			if (other.phoneCountryCode != null)
				return false;
		} else if (!phoneCountryCode.equals(other.phoneCountryCode))
			return false;
		if (iBAN == null) {
			if (other.iBAN != null)
				return false;
		} else if (!iBAN.equals(other.iBAN))
			return false;
		if (transitDays == null) {
			if (other.transitDays != null)
				return false;
		} else if (!transitDays.equals(other.transitDays))
			return false;

		return true;
	}

}
