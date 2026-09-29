package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;

public class ApplicationDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 9126612311911947700L;
	
	private String isAccountCentricCore;
	private String customerCreationMode;
	private String companyLegalUnit;
	

	public String getIsAccountCentricCore() {
		return isAccountCentricCore;
	}

	public void setIsAccountCentricCore(String isAccountCentricCore) {
		this.isAccountCentricCore = isAccountCentricCore;
	}
	
	public String getCompanyLegalUnit() {
        return companyLegalUnit;
    }

    public void setCompanyLegalUnit(String companyLegalUnit) {
        this.companyLegalUnit = companyLegalUnit;
    }

    /**
     * @return the customerCreationMode
     */
    public String getCustomerCreationMode() {
        return customerCreationMode;
    }

    /**
     * @param customerCreationMode the customerCreationMode to set
     */
    public void setCustomerCreationMode(String customerCreationMode) {
        this.customerCreationMode = customerCreationMode;
    }

}
