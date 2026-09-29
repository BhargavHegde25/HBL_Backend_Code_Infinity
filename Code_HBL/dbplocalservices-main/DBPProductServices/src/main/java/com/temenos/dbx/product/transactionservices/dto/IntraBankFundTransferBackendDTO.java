package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class IntraBankFundTransferBackendDTO extends TransferDTO implements DBPDTO{

	private static final long serialVersionUID = 7973996816061871938L;
	private String beneficiaryName;
	private String beneficiaryAddressLine1;
    private String beneficiaryBankName;
    private String beneficiaryAddressLine2;
    private String beneficiaryPhone;
    private String beneficiaryEmail;
    private String beneficiaryState;
    private String beneficiaryCity;
    private String beneficiaryZipcode;
    private String beneficiarycountry;
	

	public IntraBankFundTransferBackendDTO() {
		super();
	}
	
	public IntraBankFundTransferBackendDTO(String transactionId, String dbxtransactionId, String isScheduled,
			String featureActionId, String transactionType, String companyId, String transactionCurrency,
			String fromAccountCurrency, String toAccountCurrency, String frequencyType, String frequencyTypeId,
			String fromAccountNumber, String toAccountNumber, double amount, String transactionsNotes, String notes,
			String transactionts, String frequencyEndDate, String numberOfRecurrences, String scheduledDate, String serviceName,	 String deliveryDate, String processingDate, String recipientId, String accountCode,
		 	String nickName, String fromAccountType, String day1, String day2, String toAccountType, String name,
		 	String numberOfPayments, String message, String frequency, String question, String answer, String checkBackImage,
	  		String accountHolderNumber, String payeeName, String profileId, String cardNumber, String cardExpiry, String transferLocator,
	  		String transferId, String paymentId, String noofRecurrences, String payeName, String beneficiaryId, String payeeId, String validate, String overrides, String charges, String paidBy, String exchangeRate, String totalAmount,
	  		String serviceCharge, String convertedAmount, String transactionAmount, String beneficiaryName, String swiftCode, String status, String overrideList, String creditValueDate,
	  		String beneficiaryBankName, String beneficiaryAddressLine2,String beneficiaryAddressLine1, String beneficiaryPhone, String beneficiaryEmail, String beneficiaryState, String beneficiaryCity, String beneficiaryZipcode, String beneficiarycountry) {

		super();
		this.beneficiaryName=beneficiaryName;
        this.beneficiaryBankName = beneficiaryBankName;
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
        this.beneficiaryAddressLine2 = beneficiaryAddressLine2;
        this.beneficiaryPhone = beneficiaryPhone;
        this.beneficiaryEmail = beneficiaryEmail;
        this.beneficiaryState = beneficiaryState;
        this.beneficiaryZipcode = beneficiaryZipcode;
        this.beneficiaryCity = beneficiaryCity;
        this.beneficiarycountry = beneficiarycountry;
	}

	public IntraBankFundTransferBackendDTO convert(IntraBankFundTransferDTO dto) {
		
		super.convert(dto);
		this.beneficiaryName = dto.getBeneficiaryName();
		this.beneficiaryAddressLine1 = dto.getBeneficiaryAddressLine1();
		this.beneficiaryAddressLine2 = dto.getBeneficiaryAddressLine2();
		this.beneficiaryBankName = dto.getBeneficiaryBankName();
		this.beneficiaryPhone = dto.getBeneficiaryPhone();
		this.beneficiaryEmail = dto.getBeneficiaryEmail();
		this.beneficiaryState = dto.getBeneficiaryState();
		this.beneficiaryCity = dto.getBeneficiaryCity();
		this.beneficiaryZipcode = dto.getBeneficiaryZipcode();
		this.beneficiarycountry = dto.getBeneficiarycountry();
		return this;
	}
	
	public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
    }
    
     public String getBeneficiaryAddressLine1() {
        return beneficiaryAddressLine1;
    }

    public void setBeneficiaryAddressLine1(String beneficiaryAddressLine1) {
        this.beneficiaryAddressLine1 = beneficiaryAddressLine1;
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

    @Override
	public int hashCode() {
        super.hashCode();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
		result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
		result = prime * result + ((beneficiaryBankName == null) ? 0 : beneficiaryBankName.hashCode());
		result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
		result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
		result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
		result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
		result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
		result = prime * result + ((beneficiaryZipcode == null) ? 0 : beneficiaryZipcode.hashCode());
		result = prime * result + ((beneficiarycountry == null) ? 0 : beneficiarycountry.hashCode());
		return result;
	}

}