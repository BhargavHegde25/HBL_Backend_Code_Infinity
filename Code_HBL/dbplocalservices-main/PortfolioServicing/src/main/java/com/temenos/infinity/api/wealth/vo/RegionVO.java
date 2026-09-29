package com.temenos.infinity.api.wealth.vo;

import java.util.ArrayList;

/**
 * RegionVO is a POJO object and it holds region specific data for Strategies
 * 
 * @author Rajesh Kappera
 */
public class RegionVO extends WealthVO {
	private static final long serialVersionUID = 4119447589202789947L;

	private int regionId;
	private String regionName;
	private double recommendedValue;
	private double targetValue;
	
	private ArrayList<SectorVO> sectorList;

	/**
	 * @return the regionId
	 */
	public int getRegionId() {
		return regionId;
	}

	/**
	 * @param regionId the regionId to set
	 */
	public void setRegionId(int regionId) {
		this.regionId = regionId;
	}

	/**
	 * @return the regionName
	 */
	public String getRegionName() {
		return regionName;
	}

	/**
	 * @param regionName the regionName to set
	 */
	public void setRegionName(String regionName) {
		this.regionName = regionName;
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
	 * @return the sectorList
	 */
	public ArrayList<SectorVO> getSectorList() {
		return sectorList;
	}

	/**
	 * @param sectorList the sectorList to set
	 */
	public void setSectorList(ArrayList<SectorVO> sectorList) {
		this.sectorList = sectorList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("RegionVO [regionId=");
		builder.append(regionId);
		builder.append(", regionName=");
		builder.append(regionName);
		builder.append(", recommendedValue=");
		builder.append(recommendedValue);
		builder.append(", targetValue=");
		builder.append(targetValue);
		builder.append(", sectorList=");
		builder.append(sectorList);
		builder.append("]");
		return builder.toString();
	}
}
