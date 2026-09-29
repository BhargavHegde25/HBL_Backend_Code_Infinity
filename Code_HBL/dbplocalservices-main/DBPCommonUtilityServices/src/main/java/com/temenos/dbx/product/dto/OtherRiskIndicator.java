package com.temenos.dbx.product.dto;

import com.dbp.core.api.DBPDTO;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

@JsonInclude(Include.NON_NULL)
public class OtherRiskIndicator implements DBPDTO {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7778721104259285501L;
	private String riskIndicatorType;
	private String doesRiskApply;
	private String explanationIfApplies;
	private String explanationIfDoesnotApply;
	
	public OtherRiskIndicator() {
	}
	
	/**
	 * @return the riskIndicatorType
	 */
	public String getRiskIndicatorType() {
		return riskIndicatorType;
	}
	/**
	 * @param riskIndicatorType the riskIndicatorType to set
	 */
	public void setRiskIndicatorType(String riskIndicatorType) {
		this.riskIndicatorType = riskIndicatorType;
	}
	/**
	 * @return the doesRiskApply
	 */
	public String getDoesRiskApply() {
		return doesRiskApply;
	}
	/**
	 * @param doesRiskApply the doesRiskApply to set
	 */
	public void setDoesRiskApply(String doesRiskApply) {
		this.doesRiskApply = doesRiskApply;
	}
	/**
	 * @return the explanationIfApplies
	 */
	public String getExplanationIfApplies() {
		return explanationIfApplies;
	}
	/**
	 * @param explanationIfApplies the explanationIfApplies to set
	 */
	public void setExplanationIfApplies(String explanationIfApplies) {
		this.explanationIfApplies = explanationIfApplies;
	}
	/**
	 * @return the explanationIfDoesnotApply
	 */
	public String getExplanationIfDoesnotApply() {
		return explanationIfDoesnotApply;
	}
	/**
	 * @param explanationIfDoesnotApply the explanationIfDoesnotApply to set
	 */
	public void setExplanationIfDoesnotApply(String explanationIfDoesnotApply) {
		this.explanationIfDoesnotApply = explanationIfDoesnotApply;
	}
	
}
