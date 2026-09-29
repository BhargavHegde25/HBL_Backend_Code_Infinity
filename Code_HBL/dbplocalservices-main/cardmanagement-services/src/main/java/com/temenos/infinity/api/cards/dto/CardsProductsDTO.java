package com.temenos.infinity.api.cards.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)

public class CardsProductsDTO implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3202719271905966333L;

	private int productId;
	private String productName;
	private String accountType;
	private String featureOverview;
	private String featureDescription;
	private String representativeLabel1;
	private String representativeLabel2;
	private String representativeLabel3;
	private String representativeValue1;
	private String representativeValue2;
	private String representativeValue3;
	private String withdrawlLimit;
	private String withdrawalMinLimit;
	private String withdrawalMaxLimit;
	private String withdrawalStepLimit;
	private String purchaseLimit;
	private String purchaseMinLimit;
	private String purchaseMaxLimit;
	private String purchaseStepLimit;
	public CardsProductsDTO() {
		super();
		// TODO Auto-generated constructor stub
	}
	public CardsProductsDTO(int productId, String productName, String accountType, String featureOverview,
			String featureDescription, String representativeLabel1, String representativeLabel2,
			String representativeLabel3, String representativeValue1, String representativeValue2,
			String representativeValue3, String withdrawlLimit, String withdrawalMinLimit, String withdrawalMaxLimit,
			String withdrawalStepLimit, String purchaseLimit, String purchaseMinLimit, String purchaseMaxLimit,
			String purchaseStepLimit) {
		super();
		this.productId = productId;
		this.productName = productName;
		this.accountType = accountType;
		this.featureOverview = featureOverview;
		this.featureDescription = featureDescription;
		this.representativeLabel1 = representativeLabel1;
		this.representativeLabel2 = representativeLabel2;
		this.representativeLabel3 = representativeLabel3;
		this.representativeValue1 = representativeValue1;
		this.representativeValue2 = representativeValue2;
		this.representativeValue3 = representativeValue3;
		this.withdrawlLimit = withdrawlLimit;
		this.withdrawalMinLimit = withdrawalMinLimit;
		this.withdrawalMaxLimit = withdrawalMaxLimit;
		this.withdrawalStepLimit = withdrawalStepLimit;
		this.purchaseLimit = purchaseLimit;
		this.purchaseMinLimit = purchaseMinLimit;
		this.purchaseMaxLimit = purchaseMaxLimit;
		this.purchaseStepLimit = purchaseStepLimit;
	}
	public int getProductId() {
		return productId;
	}
	public void setProductId(int productId) {
		this.productId = productId;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public String getAccountType() {
		return accountType;
	}
	public void setAccountType(String accountType) {
		this.accountType = accountType;
	}
	public String getFeatureOverview() {
		return featureOverview;
	}
	public void setFeatureOverview(String featureOverview) {
		this.featureOverview = featureOverview;
	}
	public String getFeatureDescription() {
		return featureDescription;
	}
	public void setFeatureDescription(String featureDescription) {
		this.featureDescription = featureDescription;
	}
	public String getRepresentativeLabel1() {
		return representativeLabel1;
	}
	public void setRepresentativeLabel1(String representativeLabel1) {
		this.representativeLabel1 = representativeLabel1;
	}
	public String getRepresentativeLabel2() {
		return representativeLabel2;
	}
	public void setRepresentativeLabel2(String representativeLabel2) {
		this.representativeLabel2 = representativeLabel2;
	}
	public String getRepresentativeLabel3() {
		return representativeLabel3;
	}
	public void setRepresentativeLabel3(String representativeLabel3) {
		this.representativeLabel3 = representativeLabel3;
	}
	public String getRepresentativeValue1() {
		return representativeValue1;
	}
	public void setRepresentativeValue1(String representativeValue1) {
		this.representativeValue1 = representativeValue1;
	}
	public String getRepresentativeValue2() {
		return representativeValue2;
	}
	public void setRepresentativeValue2(String representativeValue2) {
		this.representativeValue2 = representativeValue2;
	}
	public String getRepresentativeValue3() {
		return representativeValue3;
	}
	public void setRepresentativeValue3(String representativeValue3) {
		this.representativeValue3 = representativeValue3;
	}
	public String getWithdrawlLimit() {
		return withdrawlLimit;
	}
	public void setWithdrawlLimit(String withdrawlLimit) {
		this.withdrawlLimit = withdrawlLimit;
	}
	public String getWithdrawalMinLimit() {
		return withdrawalMinLimit;
	}
	public void setWithdrawalMinLimit(String withdrawalMinLimit) {
		this.withdrawalMinLimit = withdrawalMinLimit;
	}
	public String getWithdrawalMaxLimit() {
		return withdrawalMaxLimit;
	}
	public void setWithdrawalMaxLimit(String withdrawalMaxLimit) {
		this.withdrawalMaxLimit = withdrawalMaxLimit;
	}
	public String getWithdrawalStepLimit() {
		return withdrawalStepLimit;
	}
	public void setWithdrawalStepLimit(String withdrawalStepLimit) {
		this.withdrawalStepLimit = withdrawalStepLimit;
	}
	public String getPurchaseLimit() {
		return purchaseLimit;
	}
	public void setPurchaseLimit(String purchaseLimit) {
		this.purchaseLimit = purchaseLimit;
	}
	public String getPurchaseMinLimit() {
		return purchaseMinLimit;
	}
	public void setPurchaseMinLimit(String purchaseMinLimit) {
		this.purchaseMinLimit = purchaseMinLimit;
	}
	public String getPurchaseMaxLimit() {
		return purchaseMaxLimit;
	}
	public void setPurchaseMaxLimit(String purchaseMaxLimit) {
		this.purchaseMaxLimit = purchaseMaxLimit;
	}
	public String getPurchaseStepLimit() {
		return purchaseStepLimit;
	}
	public void setPurchaseStepLimit(String purchaseStepLimit) {
		this.purchaseStepLimit = purchaseStepLimit;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((accountType == null) ? 0 : accountType.hashCode());
		result = prime * result + ((featureDescription == null) ? 0 : featureDescription.hashCode());
		result = prime * result + ((featureOverview == null) ? 0 : featureOverview.hashCode());
		result = prime * result + productId;
		result = prime * result + ((productName == null) ? 0 : productName.hashCode());
		result = prime * result + ((purchaseLimit == null) ? 0 : purchaseLimit.hashCode());
		result = prime * result + ((purchaseMaxLimit == null) ? 0 : purchaseMaxLimit.hashCode());
		result = prime * result + ((purchaseMinLimit == null) ? 0 : purchaseMinLimit.hashCode());
		result = prime * result + ((purchaseStepLimit == null) ? 0 : purchaseStepLimit.hashCode());
		result = prime * result + ((representativeLabel1 == null) ? 0 : representativeLabel1.hashCode());
		result = prime * result + ((representativeLabel2 == null) ? 0 : representativeLabel2.hashCode());
		result = prime * result + ((representativeLabel3 == null) ? 0 : representativeLabel3.hashCode());
		result = prime * result + ((representativeValue1 == null) ? 0 : representativeValue1.hashCode());
		result = prime * result + ((representativeValue2 == null) ? 0 : representativeValue2.hashCode());
		result = prime * result + ((representativeValue3 == null) ? 0 : representativeValue3.hashCode());
		result = prime * result + ((withdrawalMaxLimit == null) ? 0 : withdrawalMaxLimit.hashCode());
		result = prime * result + ((withdrawalMinLimit == null) ? 0 : withdrawalMinLimit.hashCode());
		result = prime * result + ((withdrawalStepLimit == null) ? 0 : withdrawalStepLimit.hashCode());
		result = prime * result + ((withdrawlLimit == null) ? 0 : withdrawlLimit.hashCode());
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
		CardsProductsDTO other = (CardsProductsDTO) obj;
		if (accountType == null) {
			if (other.accountType != null)
				return false;
		} else if (!accountType.equals(other.accountType))
			return false;
		if (featureDescription == null) {
			if (other.featureDescription != null)
				return false;
		} else if (!featureDescription.equals(other.featureDescription))
			return false;
		if (featureOverview == null) {
			if (other.featureOverview != null)
				return false;
		} else if (!featureOverview.equals(other.featureOverview))
			return false;
		if (productId != other.productId)
			return false;
		if (productName == null) {
			if (other.productName != null)
				return false;
		} else if (!productName.equals(other.productName))
			return false;
		if (purchaseLimit == null) {
			if (other.purchaseLimit != null)
				return false;
		} else if (!purchaseLimit.equals(other.purchaseLimit))
			return false;
		if (purchaseMaxLimit == null) {
			if (other.purchaseMaxLimit != null)
				return false;
		} else if (!purchaseMaxLimit.equals(other.purchaseMaxLimit))
			return false;
		if (purchaseMinLimit == null) {
			if (other.purchaseMinLimit != null)
				return false;
		} else if (!purchaseMinLimit.equals(other.purchaseMinLimit))
			return false;
		if (purchaseStepLimit == null) {
			if (other.purchaseStepLimit != null)
				return false;
		} else if (!purchaseStepLimit.equals(other.purchaseStepLimit))
			return false;
		if (representativeLabel1 == null) {
			if (other.representativeLabel1 != null)
				return false;
		} else if (!representativeLabel1.equals(other.representativeLabel1))
			return false;
		if (representativeLabel2 == null) {
			if (other.representativeLabel2 != null)
				return false;
		} else if (!representativeLabel2.equals(other.representativeLabel2))
			return false;
		if (representativeLabel3 == null) {
			if (other.representativeLabel3 != null)
				return false;
		} else if (!representativeLabel3.equals(other.representativeLabel3))
			return false;
		if (representativeValue1 == null) {
			if (other.representativeValue1 != null)
				return false;
		} else if (!representativeValue1.equals(other.representativeValue1))
			return false;
		if (representativeValue2 == null) {
			if (other.representativeValue2 != null)
				return false;
		} else if (!representativeValue2.equals(other.representativeValue2))
			return false;
		if (representativeValue3 == null) {
			if (other.representativeValue3 != null)
				return false;
		} else if (!representativeValue3.equals(other.representativeValue3))
			return false;
		if (withdrawalMaxLimit == null) {
			if (other.withdrawalMaxLimit != null)
				return false;
		} else if (!withdrawalMaxLimit.equals(other.withdrawalMaxLimit))
			return false;
		if (withdrawalMinLimit == null) {
			if (other.withdrawalMinLimit != null)
				return false;
		} else if (!withdrawalMinLimit.equals(other.withdrawalMinLimit))
			return false;
		if (withdrawalStepLimit == null) {
			if (other.withdrawalStepLimit != null)
				return false;
		} else if (!withdrawalStepLimit.equals(other.withdrawalStepLimit))
			return false;
		if (withdrawlLimit == null) {
			if (other.withdrawlLimit != null)
				return false;
		} else if (!withdrawlLimit.equals(other.withdrawlLimit))
			return false;
		return true;
	}
	
	

}
