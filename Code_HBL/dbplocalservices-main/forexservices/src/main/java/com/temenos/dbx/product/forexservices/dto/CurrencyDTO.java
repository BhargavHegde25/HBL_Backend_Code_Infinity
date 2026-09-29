package com.temenos.dbx.product.forexservices.dto;

import java.util.List;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.kony.dbputilities.util.ErrorCodeEnum;


@JsonInclude(value = Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CurrencyDTO implements DBPDTO{

	/**
	 * 
	 */
	private static final long serialVersionUID = 493499263664832460L;

	private String code;
	private String name;
	private String symbol;
	private String createdby;
	private String modifiedby;
	private String createdts;
	private String lastmodifiedts;
	private String synctimestamp;
	private List<MarketDTO> markets;	
	private List<ForeignCurrencyDTO> foreignCurrencies;
	private String localCurrencyId;
	private String foreignCurrencyId;
	private List<MarketDTO> currencyMarkets;
	
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
	
	private ErrorCodeEnum dbpErrorCode;
    @JsonAlias({"errorMessage", "errmsg"})
	private String dbpErrMsg;
	
    public CurrencyDTO() {
		super();
	}

	public CurrencyDTO(String code, String name, String symbol, String createdby, String modifiedby, String createdts,
			String lastmodifiedts, String synctimestamp, List<MarketDTO> currencyMarkets, String foreignCurrencyId, List<MarketDTO> markets, List<ForeignCurrencyDTO> foreignCurrencies, String localCurrencyId, ErrorCodeEnum dbpErrorCode, String dbpErrMsg) {
		super();
		this.code = code;
		this.name = name;
		this.symbol = symbol;
		this.createdby = createdby;
		this.modifiedby = modifiedby;
		this.createdts = createdts;
		this.lastmodifiedts = lastmodifiedts;
		this.synctimestamp = synctimestamp;
		this.markets = markets;
		this.dbpErrorCode = dbpErrorCode;
		this.dbpErrMsg = dbpErrMsg;		
		this.foreignCurrencies =  foreignCurrencies;
		this.localCurrencyId = localCurrencyId;
		this.currencyMarkets = currencyMarkets;
		this.foreignCurrencyId = foreignCurrencyId;
	}

	public String getLocalCurrencyId() {
		return localCurrencyId;
	}

	public void setLocalCurrencyId(String localCurrencyId) {
		this.localCurrencyId = localCurrencyId;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public String getCreatedby() {
		return createdby;
	}

	public void setCreatedby(String createdby) {
		this.createdby = createdby;
	}

	public String getModifiedby() {
		return modifiedby;
	}

	public void setModifiedby(String modifiedby) {
		this.modifiedby = modifiedby;
	}

	public String getCreatedts() {
		return createdts;
	}

	public void setCreatedts(String createdts) {
		this.createdts = createdts;
	}

	public String getLastmodifiedts() {
		return lastmodifiedts;
	}

	public void setLastmodifiedts(String lastmodifiedts) {
		this.lastmodifiedts = lastmodifiedts;
	}

	public String getSynctimestamp() {
		return synctimestamp;
	}

	public void setSynctimestamp(String synctimestamp) {
		this.synctimestamp = synctimestamp;
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

	public List<ForeignCurrencyDTO> getForeignCurrencies(){
		return foreignCurrencies;
	}

	public void setforeignCurrencies(List<ForeignCurrencyDTO> foreignCurrencies) {
		this.foreignCurrencies = foreignCurrencies;
	}	
	
	public List<MarketDTO> getMarkets() {
		return markets;
	}

	public void setMarkets(List<MarketDTO> markets) {
		this.markets = markets;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
		result = prime * result + ((createdby == null) ? 0 : createdby.hashCode());
		result = prime * result + ((createdts == null) ? 0 : createdts.hashCode());
		result = prime * result + ((dbpErrMsg == null) ? 0 : dbpErrMsg.hashCode());
		result = prime * result + ((dbpErrorCode == null) ? 0 : dbpErrorCode.hashCode());
		result = prime * result + ((lastmodifiedts == null) ? 0 : lastmodifiedts.hashCode());
		result = prime * result + ((markets == null) ? 0 : markets.hashCode());
		result = prime * result + ((modifiedby == null) ? 0 : modifiedby.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
		result = prime * result + ((synctimestamp == null) ? 0 : synctimestamp.hashCode());
		result = prime * result + ((localCurrencyId == null) ? 0 : localCurrencyId.hashCode());
		result = prime * result + ((foreignCurrencies == null) ? 0 : foreignCurrencies.hashCode());
		result = prime * result + ((foreignCurrencyId == null) ? 0 : foreignCurrencyId.hashCode());
		result = prime * result + ((currencyMarkets == null) ? 0 : currencyMarkets.hashCode());
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
		CurrencyDTO other = (CurrencyDTO) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (symbol == null) {
			if (other.symbol != null)
				return false;
		} else if (!symbol.equals(other.symbol))
			return false;
		return true;
	} 	
	    
}