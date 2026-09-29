package com.temenos.infinity.api.wealth.vo;

/**
 * SectorVO is a POJO object and it holds sector data pertaining to the Strategy
 * 
 * @author Rajesh Kappera
 */
public class SectorVO extends WealthVO {
	private static final long serialVersionUID = -1488494185853617010L;

	private int sectorId;
	private String sectorName;
	private double recommendedValue;
	private double targetValue;
	
	/**
	 * @return the sectorId
	 */
	public int getSectorId() {
		return sectorId;
	}
	
	/**
	 * @param sectorId the sectorId to set
	 */
	public void setSectorId(int sectorId) {
		this.sectorId = sectorId;
	}
	
	/**
	 * @return the sectorName
	 */
	public String getSectorName() {
		return sectorName;
	}
	
	/**
	 * @param sectorName the sectorName to set
	 */
	public void setSectorName(String sectorName) {
		this.sectorName = sectorName;
	}
	
	/**
	 * @return the recommendedValue
	 */
	public double getRecommendedValue() {
		return recommendedValue;
	}
	
	/**
	 * @param recommendedValue the recommendedValue to set
	 */
	public void setRecommendedValue(double recommendedValue) {
		this.recommendedValue = recommendedValue;
	}
	
	/**
	 * @return the targetValue
	 */
	public double getTargetValue() {
		return targetValue;
	}
	
	/**
	 * @param targetValue the targetValue to set
	 */
	public void setTargetValue(double targetValue) {
		this.targetValue = targetValue;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SectorVO [sectorId=");
		builder.append(sectorId);
		builder.append(", sectorName=");
		builder.append(sectorName);
		builder.append(", recommendedValue=");
		builder.append(recommendedValue);
		builder.append(", targetValue=");
		builder.append(targetValue);
		builder.append("]");
		return builder.toString();
	}
}
