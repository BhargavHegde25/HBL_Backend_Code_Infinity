package com.temenos.dbx.product.forexservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.kony.dbputilities.util.ErrorCodeEnum;

public class MarketDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6407219131042617203L;
	
	private String market;
	private String buyRate;
	private String sellRate;
	private String midRevalRate;
	private String currencyMarket;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
    
	public MarketDTO() {
		super();
	}

	public MarketDTO(String market, String buyRate, String sellRate, String currencyMarket, String midRevalRate, ErrorCodeEnum dbpErrorCode, String dbpErrMsg) {
		super();
		this.market = market;
		this.buyRate = buyRate;
		this.sellRate = sellRate;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;
		this.currencyMarket = currencyMarket;
		this.midRevalRate = midRevalRate;
	}

	public String getMidRevalRate() {
		return midRevalRate;
	}

	public void setMidRevalRate(String midRevalRate) {
		this.midRevalRate = midRevalRate;
	}

	public String getCurrencyMarket() {
		return currencyMarket;
	}

	public void setCurrencyMarket(String currencyMarket) {
		this.currencyMarket = currencyMarket;
	}
	
	public String getMarket() {
		return market;
	}

	public void setMarket(String market) {
		this.market = market;
	}

	public String getBuyRate() {
		return buyRate;
	}

	public void setBuyRate(String buyRate) {
		this.buyRate = buyRate;
	}

	public String getSellRate() {
		return sellRate;
	}

	public void setSellRate(String sellRate) {
		this.sellRate = sellRate;
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
		result = prime * result + ((buyRate == null) ? 0 : buyRate.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((market == null) ? 0 : market.hashCode());
		result = prime * result + ((sellRate == null) ? 0 : sellRate.hashCode());
		result = prime * result + ((currencyMarket == null) ? 0 : currencyMarket.hashCode());
		result = prime * result + ((midRevalRate == null) ? 0 : midRevalRate.hashCode());
		
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
		return true;
	}
	
    
	

}
