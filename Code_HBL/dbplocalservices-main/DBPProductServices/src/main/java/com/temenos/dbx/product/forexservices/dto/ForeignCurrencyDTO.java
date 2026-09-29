package com.temenos.dbx.product.forexservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;

public class ForeignCurrencyDTO implements DBPDTO {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6407219131042617203L;
	
	private List<MarketDTO> currencyMarkets;
	private String foreignCurrencyId;
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
    
	public ForeignCurrencyDTO() {
		super();
	}

	public ForeignCurrencyDTO(List<MarketDTO> currencyMarkets, String foreignCurrencyId, ErrorCodeEnum dbpErrorCode, String dbpErrMsg) {
		super();
		this.currencyMarkets = currencyMarkets;
		this.foreignCurrencyId = foreignCurrencyId;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;
	}

	public List<MarketDTO> getCurrencyMarkets() {
		return currencyMarkets;
	}

	public void setCurrencyMarkets(List<MarketDTO> currencyMarkets) {
		this.currencyMarkets = currencyMarkets;
	}
	
	public String getForeignCurrencyId() {
		return foreignCurrencyId;
	}

	public void setForeignCurrencyId(String foreignCurrencyId) {
		this.foreignCurrencyId = foreignCurrencyId;
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
		result = prime * result + ((currencyMarkets == null) ? 0 : currencyMarkets.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((foreignCurrencyId == null) ? 0 : foreignCurrencyId.hashCode());		
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
