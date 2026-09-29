package com.kony.dbp.cardservices.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;


	@JsonInclude(value = Include.NON_NULL)
	@JsonIgnoreProperties(ignoreUnknown = true)
	public class CardStatementsDTO implements DBPDTO{

	
    private static final long serialVersionUID = 941272153557747819L;
    private String statementLink;
    private String month;
    private String Card_id;
    private String description;
    private String id;
    public CardStatementsDTO() {
		super();
		
	}
	public CardStatementsDTO(String statementLink, String month, String card_id, String description, String id) {
		super();
		this.statementLink = statementLink;
		this.month = month;
		this.Card_id = card_id;
		this.description = description;
		this.id = id;
	}
	public String getStatementLink() {
		return statementLink;
	}
	public void setStatementLink(String statementLink) {
		this.statementLink = statementLink;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getCard_id() {
		return Card_id;
	}
	public void setCard_id(String card_id) {
		Card_id = card_id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((Card_id == null) ? 0 : Card_id.hashCode());
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((month == null) ? 0 : month.hashCode());
		result = prime * result + ((statementLink == null) ? 0 : statementLink.hashCode());
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
		CardStatementsDTO other = (CardStatementsDTO) obj;
		if (Card_id == null) {
			if (other.Card_id != null)
				return false;
		} else if (!Card_id.equals(other.Card_id))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (month == null) {
			if (other.month != null)
				return false;
		} else if (!month.equals(other.month))
			return false;
		if (statementLink == null) {
			if (other.statementLink != null)
				return false;
		} else if (!statementLink.equals(other.statementLink))
			return false;
		return true;
	}
	
	
		

}
