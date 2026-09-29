package com.temenos.infinity.api.loanspayoff.dto;

import java.io.Serializable;

public class Properties implements Serializable {
	
	private static final long serialVersionUID = 6597925078705187756L;
	
	private String propertyName;
	private double interestAmount;
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(interestAmount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((propertyName == null) ? 0 : propertyName.hashCode());
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
		Properties other = (Properties) obj;
		if (Double.doubleToLongBits(interestAmount) != Double.doubleToLongBits(other.interestAmount))
			return false;
		if (propertyName == null) {
			if (other.propertyName != null)
				return false;
		} else if (!propertyName.equals(other.propertyName))
			return false;
		return true;
	}
	public String getPropertyName() {
		return propertyName;
	}
	public void setPropertyName(String propertyName) {
		this.propertyName = propertyName;
	}
	public double getInterestAmount() {
		return interestAmount;
	}
	public void setInterestAmount(double interestAmount) {
		this.interestAmount = interestAmount;
	}

}
