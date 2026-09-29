package com.temenos.dbx.product.dto;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.util.DBPUtilitiesConstants;
import com.kony.dbputilities.util.ServiceCallHelper;
import com.kony.dbputilities.util.URLConstants;
import com.temenos.dbx.product.api.DBPDTOEXT;
import com.temenos.dbx.product.utils.DTOUtils;
public class CustomerPreferenceDTO implements DBPDTOEXT {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6388606684141168305L;
	private String id; 
	private String customer_id;
	private String isTypeBusiness;
	private String defaultAccountDeposit;
	private String defaultAccountTransfers;
	private String defaultModule_id;
	private String defaultAccountPayments;
	private String defaultAccountCardless;
	private String defaultAccountBillPay;
	private String defaultToAccountP2P;
	private String defaultFromAccountP2P; 
	private String defaultAccountWire;
	
	private String defaultAccountLoanPayment;
	private String defaultAccountCardPayment;
	private String defaultAccountCheckManagement;
	
	private String defaultAccountDashboard;
	private String defaultAccountQRPayment;
	private String defaultAccountCheckDeposit;
	private String defaultAccountCashWithdrawl;
	private String defaultAccounteSewa;
	
	public String getDefaultAccounteSewa() {
		return defaultAccounteSewa;
	}

	public void setDefaultAccounteSewa(String defaultAccounteSewa) {
		this.defaultAccounteSewa = defaultAccounteSewa;
	}


	private String areUserAlertsTurnedOn;
	private String areDepositTermsAccepted;
	private String areAccountStatementTermsAccepted;
	private String isBillPaySupported;
	private String isP2PSupported;
	private String isBillPayActivated;
	private String isP2PActivated;
	private String isWireTransferActivated; 
	private String isWireTransferEligible;
	private Boolean showBillPayFromAccPopup;
	private String sreferedOtpMethod;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private String softdeleteflag;
	private Boolean isChanged;
	private Boolean isNew;
	private String companyLegalUnit;
	private String DefaultFromAccountQR;


	
	public String getDefaultFromAccountQR() {
		return DefaultFromAccountQR;
	}


	public void setDefaultFromAccountQR(String defaultFromAccountQR) {
		DefaultFromAccountQR = defaultFromAccountQR;
	}


	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}


	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}


	/**
	 * @return the customer_id
	 */
	public String getCustomer_id() {
		return customer_id;
	}


	/**
	 * @param customer_id the customer_id to set
	 */
	public void setCustomer_id(String customer_id) {
		this.customer_id = customer_id;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }


	/**
	 * @return the defaultAccountDeposit
	 */
	public String getDefaultAccountDeposit() {
		return defaultAccountDeposit;
	}


	/**
	 * @param defaultAccountDeposit the defaultAccountDeposit to set
	 */
	public void setDefaultAccountDeposit(String defaultAccountDeposit) {
		this.defaultAccountDeposit = defaultAccountDeposit;
	}


	/**
	 * @return the defaultAccountTransfers
	 */
	public String getDefaultAccountTransfers() {
		return defaultAccountTransfers;
	}


	/**
	 * @param defaultAccountTransfers the defaultAccountTransfers to set
	 */
	public void setDefaultAccountTransfers(String defaultAccountTransfers) {
		this.defaultAccountTransfers = defaultAccountTransfers;
	}


	/**
	 * @return the defaultModule_id
	 */
	public String getDefaultModule_id() {
		return defaultModule_id;
	}


	/**
	 * @param defaultModule_id the defaultModule_id to set
	 */
	public void setDefaultModule_id(String defaultModule_id) {
		this.defaultModule_id = defaultModule_id;
	}


	/**
	 * @return the defaultAccountPayments
	 */
	public String getDefaultAccountPayments() {
		return defaultAccountPayments;
	}


	/**
	 * @param defaultAccountPayments the defaultAccountPayments to set
	 */
	public void setDefaultAccountPayments(String defaultAccountPayments) {
		this.defaultAccountPayments = defaultAccountPayments;
	}


	/**
	 * @return the defaultAccountCardless
	 */
	public String getDefaultAccountCardless() {
		return defaultAccountCardless;
	}


	/**
	 * @param defaultAccountCardless the defaultAccountCardless to set
	 */
	public void setDefaultAccountCardless(String defaultAccountCardless) {
		this.defaultAccountCardless = defaultAccountCardless;
	}


	/**
	 * @return the defaultAccountBillPay
	 */
	public String getDefaultAccountBillPay() {
		return defaultAccountBillPay;
	}


	/**
	 * @param defaultAccountBillPay the defaultAccountBillPay to set
	 */
	public void setDefaultAccountBillPay(String defaultAccountBillPay) {
		this.defaultAccountBillPay = defaultAccountBillPay;
	}


	/**
	 * @return the defaultToAccountP2P
	 */
	public String getDefaultToAccountP2P() {
		return defaultToAccountP2P;
	}


	/**
	 * @param defaultToAccountP2P the defaultToAccountP2P to set
	 */
	public void setDefaultToAccountP2P(String defaultToAccountP2P) {
		this.defaultToAccountP2P = defaultToAccountP2P;
	}


	/**
	 * @return the defaultFromAccountP2P
	 */
	public String getDefaultFromAccountP2P() {
		return defaultFromAccountP2P;
	}


	/**
	 * @param defaultFromAccountP2P the defaultFromAccountP2P to set
	 */
	public void setDefaultFromAccountP2P(String defaultFromAccountP2P) {
		this.defaultFromAccountP2P = defaultFromAccountP2P;
	}


	/**
	 * @return the defaultAccountWire
	 */
	public String getDefaultAccountWire() {
		return defaultAccountWire;
	}


	/**
	 * @param defaultAccountWire the defaultAccountWire to set
	 */
	public void setDefaultAccountWire(String defaultAccountWire) {
		this.defaultAccountWire = defaultAccountWire;
	}


	/**
	 * @return the areUserAlertsTurnedOn
	 */
	public String getAreUserAlertsTurnedOn() {
		return areUserAlertsTurnedOn;
	}


	/**
	 * @param areUserAlertsTurnedOn the areUserAlertsTurnedOn to set
	 */
	public void setAreUserAlertsTurnedOn(String areUserAlertsTurnedOn) {
		this.areUserAlertsTurnedOn = areUserAlertsTurnedOn;
	}


	/**
	 * @return the areDepositTermsAccepted
	 */
	public String getAreDepositTermsAccepted() {
		return areDepositTermsAccepted;
	}


	/**
	 * @param areDepositTermsAccepted the areDepositTermsAccepted to set
	 */
	public void setAreDepositTermsAccepted(String areDepositTermsAccepted) {
		this.areDepositTermsAccepted = areDepositTermsAccepted;
	}


	/**
	 * @return the areAccountStatementTermsAccepted
	 */
	public String getAreAccountStatementTermsAccepted() {
		return areAccountStatementTermsAccepted;
	}


	/**
	 * @param areAccountStatementTermsAccepted the areAccountStatementTermsAccepted to set
	 */
	public void setAreAccountStatementTermsAccepted(String areAccountStatementTermsAccepted) {
		this.areAccountStatementTermsAccepted = areAccountStatementTermsAccepted;
	}


	/**
	 * @return the isBillPaySupported
	 */
	public String getIsBillPaySupported() {
		return isBillPaySupported;
	}


	/**
	 * @param isBillPaySupported the isBillPaySupported to set
	 */
	public void setIsBillPaySupported(String isBillPaySupported) {
		this.isBillPaySupported = isBillPaySupported;
	}


	/**
	 * @return the isP2PSupported
	 */
	public String getIsP2PSupported() {
		return isP2PSupported;
	}


	/**
	 * @param isP2PSupported the isP2PSupported to set
	 */
	public void setIsP2PSupported(String isP2PSupported) {
		this.isP2PSupported = isP2PSupported;
	}


	/**
	 * @return the isBillPayActivated
	 */
	public String getIsBillPayActivated() {
		return isBillPayActivated;
	}


	/**
	 * @param isBillPayActivated the isBillPayActivated to set
	 */
	public void setIsBillPayActivated(String isBillPayActivated) {
		this.isBillPayActivated = isBillPayActivated;
	}


	/**
	 * @return the isP2PActivated
	 */
	public String getIsP2PActivated() {
		return isP2PActivated;
	}


	/**
	 * @param isP2PActivated the isP2PActivated to set
	 */
	public void setIsP2PActivated(String isP2PActivated) {
		this.isP2PActivated = isP2PActivated;
	}


	/**
	 * @return the isWireTransferActivated
	 */
	public String getIsWireTransferActivated() {
		return isWireTransferActivated;
	}


	/**
	 * @param isWireTransferActivated the isWireTransferActivated to set
	 */
	public void setIsWireTransferActivated(String isWireTransferActivated) {
		this.isWireTransferActivated = isWireTransferActivated;
	}


	/**
	 * @return the isWireTransferEligible
	 */
	public String getIsWireTransferEligible() {
		return isWireTransferEligible;
	}


	/**
	 * @param isWireTransferEligible the isWireTransferEligible to set
	 */
	public void setIsWireTransferEligible(String isWireTransferEligible) {
		this.isWireTransferEligible = isWireTransferEligible;
	}


	/**
	 * @return the showBillPayFromAccPopup
	 */
	public Boolean getShowBillPayFromAccPopup() {
		return showBillPayFromAccPopup;
	}


	/**
	 * @param showBillPayFromAccPopup the showBillPayFromAccPopup to set
	 */
	public void setShowBillPayFromAccPopup(Boolean showBillPayFromAccPopup) {
		this.showBillPayFromAccPopup = showBillPayFromAccPopup;
	}


	/**
	 * @return the sreferedOtpMethod
	 */
	public String getSreferedOtpMethod() {
		return sreferedOtpMethod;
	}


	/**
	 * @param sreferedOtpMethod the sreferedOtpMethod to set
	 */
	public void setSreferedOtpMethod(String sreferedOtpMethod) {
		this.sreferedOtpMethod = sreferedOtpMethod;
	}


	/**
	 * @return the createdts
	 */
	public String getCreatedts() {
		return createdts;
	}


	/**
	 * @param createdts the createdts to set
	 */
	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}


	/**
	 * @return the lastmodifiedts
	 */
	public String getLastmodifiedts() {
		return lastmodifiedts;
	}


	/**
	 * @param lastmodifiedts the lastmodifiedts to set
	 */
	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}


	/**
	 * @return the synctimestamp
	 */
	public String getSynctimestamp() {
		return synctimestamp;
	}


	/**
	 * @param synctimestamp the synctimestamp to set
	 */
	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
	}


	/**
	 * @return the softdeleteflag
	 */
	public String getSoftdeleteflag() {
		return softdeleteflag;
	}


	/**
	 * @param softdeleteflag the softdeleteflag to set
	 */
	public void setSoftdeleteflag(String softdeleteflag) {
		this.softdeleteflag = softdeleteflag;
	}


	/**
	 * @return the isChanged
	 */
	public Boolean getIsChanged() {
		return isChanged;
	}


	/**
	 * @param isChanged the isChanged to set
	 */
	public void setIsChanged(Boolean isChanged) {
		this.isChanged = isChanged;
	}


	/**
	 * @return the isNew
	 */
	public Boolean getIsNew() {
		return isNew;
	}


	/**
	 * @param isNew the isNew to set
	 */
	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}


	@Override
	public boolean persist(Map<String, Object> input, Map<String, Object> headers) {
		if(!isNew && isChanged){
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERPREFERENCE_UPDATE).has("errmsg");
		}

		if(isNew) {
			return !ServiceCallHelper.invokeServiceAndGetJson(input, headers, URLConstants.CUSTOMERPREFERENCE_CREATE).has("errmsg");
		}

		return true;

	}

	@Override
    public Object loadDTO(String id) {

        String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + id;
        List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

        DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERPREFERENCE_GET, true, true);

        if(exts != null && exts.size() >0) {
            return exts.get(0);
        }

        return null;
    }
    
	
	@Override
	public Object loadDTO() {

		if(StringUtils.isNotBlank(customer_id)) {
			String filter = "Customer_id"+ DBPUtilitiesConstants.EQUAL + customer_id;
			List<DBPDTOEXT> exts = new ArrayList<DBPDTOEXT>();

			DTOUtils.loadDTOListfromDB(exts, filter, URLConstants.CUSTOMERPREFERENCE_GET, true, true);

			if(exts != null && exts.size() >0) {
				return exts.get(0);
			}
		}

		return null;
	}


    /**
     * @return the isTypeBusiness
     */
    public String getIsTypeBusiness() {
        return isTypeBusiness;
    }


    /**
     * @param isTypeBusiness the isTypeBusiness to set
     */
    public void setIsTypeBusiness(String isTypeBusiness) {
        this.isTypeBusiness = isTypeBusiness;
    }


	public String getDefaultAccountLoanPayment() {
		return defaultAccountLoanPayment;
	}


	public void setDefaultAccountLoanPayment(String defaultAccountLoanPayment) {
		this.defaultAccountLoanPayment = defaultAccountLoanPayment;
	}


	public String getDefaultAccountCardPayment() {
		return defaultAccountCardPayment;
	}


	public void setDefaultAccountCardPayment(String defaultAccountCardPayment) {
		this.defaultAccountCardPayment = defaultAccountCardPayment;
	}


	public String getDefaultAccountCheckManagement() {
		return defaultAccountCheckManagement;
	}


	public void setDefaultAccountCheckManagement(String defaultAccountCheckManagement) {
		this.defaultAccountCheckManagement = defaultAccountCheckManagement;
	}


	@Override
	public String toString() {
		return "CustomerPreferenceDTO [id=" + id + ", customer_id=" + customer_id + ", isTypeBusiness=" + isTypeBusiness
				+ ", defaultAccountDeposit=" + defaultAccountDeposit + ", defaultAccountTransfers="
				+ defaultAccountTransfers + ", defaultModule_id=" + defaultModule_id + ", defaultAccountPayments="
				+ defaultAccountPayments + ", defaultAccountCardless=" + defaultAccountCardless
				+ ", defaultAccountBillPay=" + defaultAccountBillPay + ", defaultToAccountP2P=" + defaultToAccountP2P
				+ ", defaultFromAccountP2P=" + defaultFromAccountP2P + ", defaultAccountWire=" + defaultAccountWire
				+ ", defaultAccountLoanPayment=" + defaultAccountLoanPayment + ", defaultAccountCardPayment="
				+ defaultAccountCardPayment + ", defaultAccountCheckManagement=" + defaultAccountCheckManagement
				+ ", areUserAlertsTurnedOn=" + areUserAlertsTurnedOn + ", areDepositTermsAccepted="
				+ areDepositTermsAccepted + ", areAccountStatementTermsAccepted=" + areAccountStatementTermsAccepted
				+ ", isBillPaySupported=" + isBillPaySupported + ", isP2PSupported=" + isP2PSupported
				+ ", isBillPayActivated=" + isBillPayActivated + ", isP2PActivated=" + isP2PActivated
				+ ", isWireTransferActivated=" + isWireTransferActivated + ", isWireTransferEligible="
				+ isWireTransferEligible + ", showBillPayFromAccPopup=" + showBillPayFromAccPopup
				+ ", sreferedOtpMethod=" + sreferedOtpMethod + ", createdts=" + createdts + ", lastmodifiedts="
				+ lastmodifiedts + ", synctimestamp=" + synctimestamp + ", softdeleteflag=" + softdeleteflag
				+ ", isChanged=" + isChanged + ", isNew=" + isNew + ", companyLegalUnit=" + companyLegalUnit
				+ ", DefaultFromAccountQR=" + DefaultFromAccountQR + "]";
	}


	public String getDefaultAccountDashboard() {
		return defaultAccountDashboard;
	}


	public void setDefaultAccountDashboard(String defaultAccountDashboard) {
		this.defaultAccountDashboard = defaultAccountDashboard;
	}


	public String getDefaultAccountQRPayment() {
		return defaultAccountQRPayment;
	}


	public void setDefaultAccountQRPayment(String defaultAccountQRPayment) {
		this.defaultAccountQRPayment = defaultAccountQRPayment;
	}


	public String getDefaultAccountCheckDeposit() {
		return defaultAccountCheckDeposit;
	}


	public void setDefaultAccountCheckDeposit(String defaultAccountCheckDeposit) {
		this.defaultAccountCheckDeposit = defaultAccountCheckDeposit;
	}


	public String getDefaultAccountCashWithdrawl() {
		return defaultAccountCashWithdrawl;
	}


	public void setDefaultAccountCashWithdrawl(String defaultAccountCashWithdrawl) {
		this.defaultAccountCashWithdrawl = defaultAccountCashWithdrawl;
	}

	
}
