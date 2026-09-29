package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class InternationalFundTransferBackendDTO extends TransferDTO implements DBPDTO{

	private static final long serialVersionUID = -3912113288301575348L;

	private String iban;
	
	private String bicCode;
	private String bankName;
	private String bankId;
	private String feeCurrency;
	private String beneficiaryName;
	
	private String paymentType;
	private String feeAmount;
	private String beneficiaryAddressNickName;
	private String beneficiaryAddressLine1;
	private String beneficiaryCity;
	private String beneficiaryZipcode;
	private String beneficiarycountry;
	private String intermediaryBicCode;
	private String clearingCode;
    private String beneficiaryBankName;
    private String beneficiaryAddressLine2;
    private String beneficiaryPhone;
    private String beneficiaryEmail;
    private String beneficiaryState;
    private String purposeCode;
	private String clearingIdentifierCode;
	private String streetName;
	private String townName;
	private String countryName;
	private String payeeVerificationStatus;
	private String payeeVerificationErrMsg;
	public InternationalFundTransferBackendDTO() {
		super();
	}
	
	public InternationalFundTransferBackendDTO(String transactionId, String dbxtransactionId, String isScheduled,
			String featureActionId, String transactionType, String companyId, String transactionCurrency,
			String fromAccountCurrency, String frequencyType, String frequencyTypeId, String fromAccountNumber,
			String toAccountNumber, double amount, String transactionsNotes, String notes, String transactionts,
			String frequencyEndDate, String numberOfRecurrences, String scheduledDate, String serviceName,	 String deliveryDate, String processingDate, String recipientId, String accountCode,
		 	String nickName, String fromAccountType, String day1, String day2, String toAccountType, String name,
		 	String numberOfPayments, String message, String frequency, String question, String answer, String checkBackImage,
		 	String accountHolderNumber, String payeeName, String profileId, String cardNumber, String cardExpiry, String transferLocator,
		 	String transferId, String paymentId, String noofRecurrences, String payeName, String iban, String swiftCode, String bicCode, String bankName, String bankId, String feeCurrency, 
			String beneficiaryName, String paidBy, String paymentType, String feeAmount, String beneficiaryAddressNickName, 
			String beneficiaryAddressLine1, String beneficiaryCity, String beneficiaryId, String payeeId, String beneficiaryZipcode, String beneficiarycountry, String validate, String overrides, String charges, String exchangeRate, String totalAmount,
			String serviceCharge, String convertedAmount, String transactionAmount, String status, String overrideList, String creditValueDate, String intermediaryBicCode, String clearingCode,
			String beneficiaryBankName, String beneficiaryAddressLine2, String beneficiaryPhone, String beneficiaryEmail, String beneficiaryState,String paymentStatus, String purposeCode,
			String clearingIdentifierCode, String streetName, String townName, String countryName,String payeeVerificationStatus,String payeeVerificationErrMsg ) {
		super();
		this.iban = iban;      
        this.bicCode = bicCode;
        this.bankName = bankName;
        this.bankId = bankId;
        this.feeCurrency = feeCurrency;
        this.beneficiaryName = beneficiaryName;    
        this.paymentType = paymentType;
        this.feeAmount = feeAmount;
        this.beneficiaryAddressNickName = beneficiaryAddressNickName;
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
        this.beneficiaryCity = beneficiaryCity;
        this.beneficiaryZipcode = beneficiaryZipcode;
		this.beneficiarycountry = beneficiarycountry;
		this.intermediaryBicCode = intermediaryBicCode;
		this.clearingCode = clearingCode;
        this.beneficiaryBankName = beneficiaryBankName;
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
        this.beneficiaryPhone = beneficiaryPhone;
        this.beneficiaryEmail = beneficiaryEmail;
        this.beneficiaryState = beneficiaryState;
        this.purposeCode = purposeCode;
        this.clearingIdentifierCode = clearingIdentifierCode;
        this.streetName = streetName;
        this.townName = townName;
        this.countryName = countryName;
        this.payeeVerificationStatus = payeeVerificationStatus;
		this.payeeVerificationErrMsg = payeeVerificationErrMsg;
       // this.paymentStatus=paymentStatus;
	}

	public InternationalFundTransferBackendDTO convert(InternationalFundTransferDTO dto) {
		super.convert(dto);	
        this.iban = dto.getIban();
        this.bicCode = dto.getBicCode();
        this.bankName = dto.getBankName();
        this.bankId = dto.getBankId();
        this.feeCurrency = dto.getFeeCurrency();
        this.beneficiaryName = dto.getBeneficiaryName();
        this.paymentType = dto.getPaymentType();
        this.feeAmount = dto.getFeeAmount();
        this.beneficiaryAddressNickName = dto.getBeneficiaryAddressNickName();
        this.beneficiaryAddressLine1 = dto.getBeneficiaryAddressLine1();
        this.beneficiaryCity = dto.getBeneficiaryCity();
        this.beneficiaryZipcode = dto.getBeneficiaryZipcode();
		this.beneficiarycountry = dto.getBeneficiarycountry();
		this.intermediaryBicCode = dto.getIntermediaryBicCode();
		this.clearingCode = dto.getClearingCode();
        this.beneficiaryAddressLine2 = dto.getBeneficiaryAddressLine2();
        this.beneficiaryPhone = dto.getBeneficiaryPhone();
        this.beneficiaryEmail = dto.getBeneficiaryEmail();
        this.beneficiaryState = dto.getBeneficiaryState();
        this.purposeCode = dto.getPurposeCode();
        this.clearingIdentifierCode = dto.getClearingIdentifierCode();
        this.streetName = dto.getStreetName();
        this.townName = dto.getTownName();
        this.countryName = dto.getCountryName();
       // this.paymentStatus=dto.getPaymentStatus();
		return this;
	}
	
	public String getIban() {
		return iban;
	}

	public void setIban(String iban) {
		this.iban = iban;
	}

	public String getBicCode() {
		return bicCode;
	}

	public void setBicCode(String bicCode) {
		this.bicCode = bicCode;
	}

	public String getBankName() {
		return bankName;
	}

	public void setBankName(String bankName) {
		this.bankName = bankName;
	}

	public String getBankId() {
		return bankId;
	}

	public void setBankId(String bankId) {
		this.bankId = bankId;
	}

	public String getFeeCurrency() {
		return feeCurrency;
	}

	public void setFeeCurrency(String feeCurrency) {
		this.feeCurrency = feeCurrency;
	}

	public String getBeneficiaryName() {
		return beneficiaryName;
	}

	public void setBeneficiaryName(String beneficiaryName) {
		this.beneficiaryName = beneficiaryName;
	}


	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public String getFeeAmount() {
		return feeAmount;
	}

	public void setFeeAmount(String feeAmount) {
		this.feeAmount = feeAmount;
	}

	public String getBeneficiaryAddressNickName() {
		return beneficiaryAddressNickName;
	}

	public void setBeneficiaryAddressNickName(String beneficiaryAddressNickName) {
		this.beneficiaryAddressNickName = beneficiaryAddressNickName;
	}

	public String getBeneficiaryAddressLine1() {
		return beneficiaryAddressLine1;
	}

	public void setBeneficiaryAddressLine1(String beneficiaryAddressLine1) {
		this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
	}

	public String getBeneficiaryCity() {
		return beneficiaryCity;
	}

	public void setBeneficiaryCity(String beneficiaryCity) {
		this.beneficiaryCity = beneficiaryCity;
	}

	public String getBeneficiaryZipcode() {
		return beneficiaryZipcode;
	}

	public void setBeneficiaryZipcode(String beneficiaryZipcode) {
		this.beneficiaryZipcode = beneficiaryZipcode;
	}

	public String getBeneficiarycountry() {
		return beneficiarycountry;
	}

	public void setBeneficiarycountry(String beneficiarycountry) {
		this.beneficiarycountry = beneficiarycountry;
	}
    
    public String getIntermediaryBicCode() {
		return intermediaryBicCode;
	}

	public void setIntermediaryBicCode(String intermediaryBicCode) {
		this.intermediaryBicCode = intermediaryBicCode;
	}
	
	public String getClearingCode() {
		return clearingCode;
	}

	public void setClearingCode(String clearingCode) {
		this.clearingCode = clearingCode;
	}

    public String getBeneficiaryBankName() {
        return beneficiaryBankName;
    }

    public void setBeneficiaryBankName(String beneficiaryBankName) {
        this.beneficiaryBankName = beneficiaryBankName;
    }

    public String getBeneficiaryAddressLine2() {
        return beneficiaryAddressLine2;
    }

    public void setBeneficiaryAddressLine2(String beneficiaryAddressLine2) {
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
    }

    public String getBeneficiaryPhone() {
        return beneficiaryPhone;
    }

    public void setBeneficiaryPhone(String beneficiaryPhone) {
        this.beneficiaryPhone = beneficiaryPhone;
    }

    public String getBeneficiaryEmail() {
        return beneficiaryEmail;
    }

    public void setBeneficiaryEmail(String beneficiaryEmail) {
        this.beneficiaryEmail = beneficiaryEmail;
    }

    public String getBeneficiaryState() {
        return beneficiaryState;
    }

    public void setBeneficiaryState(String beneficiaryState) {
        this.beneficiaryState = beneficiaryState;
    }
    
    public String getPurposeCode() {
        return purposeCode;
    }

    public void setPurposeCode(String purposeCode) {
        this.purposeCode = purposeCode;
    }

    public String getClearingIdentifierCode() {
        return clearingIdentifierCode;
    }

    public void setClearingIdentifierCode(String clearingIdentifierCode) {
        this.clearingIdentifierCode = clearingIdentifierCode;
    }

    public String getStreetName() {
        return streetName;
    }

    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    public String getTownName() {
        return townName;
    }

    public void setTownName(String townName) {
        this.townName = townName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }
    
    public String getPayeeVerificationErrMsg() {
		return payeeVerificationErrMsg;
	}

	public void setPayeeVerificationErrMsg(String payeeVerificationErrMsg) {
		this.payeeVerificationErrMsg = payeeVerificationErrMsg;
	}

	public String getPayeeVerificationStatus() {
		return payeeVerificationStatus;
	}
	
	public void setPayeeVerificationStatus(String payeeVerificationStatus) {
		this.payeeVerificationStatus = payeeVerificationStatus;
	}


    @Override
	public int hashCode() {
    	super.hashCode();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((iban == null) ? 0 : iban.hashCode());
		result = prime * result + ((bicCode == null) ? 0 : bicCode.hashCode());
		result = prime * result + ((bankName == null) ? 0 : bankName.hashCode());
		result = prime * result + ((bankId == null) ? 0 : bankId.hashCode());
		result = prime * result + ((feeCurrency == null) ? 0 : feeCurrency.hashCode());
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((paymentType == null) ? 0 : paymentType.hashCode());
		result = prime * result + ((feeAmount == null) ? 0 : feeAmount.hashCode());
		result = prime * result + ((beneficiaryAddressNickName == null) ? 0 : beneficiaryAddressNickName.hashCode());
		result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
		result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
		result = prime * result + ((beneficiaryZipcode == null) ? 0 : beneficiaryZipcode.hashCode());
		result = prime * result + ((beneficiarycountry == null) ? 0 : beneficiarycountry.hashCode());
		result = prime * result + ((intermediaryBicCode == null) ? 0 : intermediaryBicCode.hashCode());
		result = prime * result + ((clearingCode == null) ? 0 : clearingCode.hashCode());
        result = prime * result + ((beneficiaryBankName == null) ? 0 : beneficiaryBankName.hashCode());
        result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
        result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
        result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
        result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
        result = prime * result + ((purposeCode == null) ? 0 : purposeCode.hashCode());
        result = prime * result + ((clearingIdentifierCode == null) ? 0 : clearingIdentifierCode.hashCode());
        result = prime * result + ((streetName == null) ? 0 : streetName.hashCode());
        result = prime * result + ((townName == null) ? 0 : townName.hashCode());
        result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
        return result;
	}

}