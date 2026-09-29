package com.temenos.dbx.product.transactionservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(value = Include.NON_NULL)
public class IntraBankFundTransferDTO extends TransferDTO implements DBPDTO{

	/**
	 *
	 */
	private static final long serialVersionUID = -6809020923323456022L;
    @JsonAlias("Id")
	private String beneficiaryName;	
	@JsonAlias({"errorMessage", "errmsg"})
    private String beneficiaryBankName;
    private String beneficiaryAddressLine2;
    private String beneficiaryPhone;
    private String beneficiaryEmail;
    private String beneficiaryState;
    private String beneficiaryAddressLine1;
    private String beneficiaryCity;
    private String beneficiaryZipcode;
    private String beneficiarycountry;

	public IntraBankFundTransferDTO() {
		super();
	}

	public IntraBankFundTransferDTO(String transactionId, String referenceId, String message, String confirmationNumber,
			String status, String requestId, String featureActionId, String transactionType, String companyId, String roleId,
			String transactionCurrency, String fromAccountCurrency, String toAccountCurrency, String frequencyTypeId,
			String frequencyType, String fromAccountNumber, String toAccountNumber, double amount, String notes,
			String transactionsNotes, String transactionts, String frequencyEndDate, String numberOfRecurrences,
			String scheduledDate, String createdby, String modifiedby, String createdts, String lastmodifiedts,
			String synctimestamp, boolean softdeleteflag,	 String processingDate, String personId, String fromNickName,
			String fromAccountType, String day1, String day2, String toAccountType, String payPersonName,
			String securityQuestion, String SecurityAnswer, String checkImageBack, String payeeName, String profileId,
			String cardNumber, String cardExpiry, String isScheduled, String frequencyStartDate, String deliverBy, String payeName, String validate, 
			String overrides, String dbpErrCode, String dbpErrMsg, String charges, String paidBy, String exchangeRate, String totalAmount,
			String serviceCharge, String convertedAmount, String transactionAmount, String beneficiaryName, String swiftCode, String overrideList, String creditValueDate,
			String errorDetails, String messageDetails,String beneficiaryBankName, String beneficiaryAddressLine2,String beneficiaryAddressLine1, String beneficiaryPhone, String beneficiaryEmail, String beneficiaryState, String beneficiaryCity, String beneficiaryZipcode, String beneficiarycountry) {
		super();
		this.beneficiaryName = beneficiaryName;
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
	
	@SuppressWarnings("unlikely-arg-type")
	public IntraBankFundTransferDTO updateValues(IntraBankFundTransferDTO dto) {
		super.updateValues(dto);
			dto.beneficiaryName = (dto.beneficiaryName != null && !"".equals(dto.beneficiaryName)) ? dto.beneficiaryName : this.beneficiaryName;
	        dto.beneficiaryBankName = this.getBeneficiaryBankName();
            dto.beneficiaryAddressLine2 = this.getBeneficiaryAddressLine2();
            dto.beneficiaryPhone = this.getBeneficiaryPhone();
            dto.beneficiaryEmail = this.getBeneficiaryEmail();
            dto.beneficiaryCity = this.getBeneficiaryCity();
            dto.beneficiaryState = this.getBeneficiaryState();
            dto.beneficiaryAddressLine1 = this.getBeneficiaryAddressLine1();
            dto.beneficiarycountry = this.getBeneficiarycountry();
            dto.beneficiaryZipcode = this.getBeneficiaryZipcode();

		return dto;
	}

	public String getBeneficiaryName() {
        return beneficiaryName;
    }

    public void setBeneficiaryName(String beneficiaryName) {
        this.beneficiaryName = beneficiaryName;
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

    @Override
	public int hashCode() {
    	super.hashCode();
		final int prime = 31;
		int result = 1;
		result = prime * result + ((SecurityAnswer == null) ? 0 : SecurityAnswer.hashCode());		
		result = prime * result + ((beneficiaryName == null) ? 0 : beneficiaryName.hashCode());
        result = prime * result + ((beneficiaryBankName == null) ? 0 : beneficiaryBankName.hashCode());
        result = prime * result + ((beneficiaryAddressLine2 == null) ? 0 : beneficiaryAddressLine2.hashCode());
        result = prime * result + ((beneficiaryPhone == null) ? 0 : beneficiaryPhone.hashCode());
        result = prime * result + ((beneficiaryState == null) ? 0 : beneficiaryState.hashCode());
        result = prime * result + ((beneficiaryEmail == null) ? 0 : beneficiaryEmail.hashCode());
        result = prime * result + ((beneficiaryZipcode == null) ? 0 : beneficiaryZipcode.hashCode());
        result = prime * result + ((beneficiaryCity == null) ? 0 : beneficiaryCity.hashCode());
        result = prime * result + ((beneficiaryAddressLine1 == null) ? 0 : beneficiaryAddressLine1.hashCode());
        result = prime * result + ((beneficiarycountry == null) ? 0 : beneficiarycountry.hashCode());
		return result;
	}


}