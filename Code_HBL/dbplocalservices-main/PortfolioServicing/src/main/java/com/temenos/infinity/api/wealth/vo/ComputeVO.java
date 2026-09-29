package com.temenos.infinity.api.wealth.vo;

/**
 * ComputeVO is a POJO object to hold input data during the user customizes the strategy
 * 
 * @author Rajesh Kappera
 */
public class ComputeVO extends WealthVO {
	private static final long serialVersionUID = 4120055915841171080L;
	
	private int id;
	private int updateCount;
	private String name;
	private double recommendedValue;
	private double targetValue;
	private String portfolioId;
	
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return the updateCount
	 */
	public int getUpdateCount() {
		return updateCount;
	}

	/**
	 * @param updateCount the updateCount to set
	 */
	public void setUpdateCount(int updateCount) {
		this.updateCount = updateCount;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name the name to set
	 */
	public void setName(String name) {
		this.name = name;
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

	/**
	 * @return the portfolioId
	 */
	public String getPortfolioId() {
		return portfolioId;
	}

	/**
	 * @param portfolioId the portfolioId to set
	 */
	public void setPortfolioId(String portfolioId) {
		this.portfolioId = portfolioId;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ComputeVO [id=");
		builder.append(id);
		builder.append(", updateCount=");
		builder.append(updateCount);
		builder.append(", name=");
		builder.append(name);
		builder.append(", recommendedValue=");
		builder.append(recommendedValue);
		builder.append(", targetValue=");
		builder.append(targetValue);
		builder.append(", portfolioId=");
		builder.append(portfolioId);
		builder.append("]");
		return builder.toString();
	}
}
