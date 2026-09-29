package com.temenos.infinity.api.wealth.vo;

import java.util.ArrayList;

/**
 * SegmentVO is a POJO object and it holds segment specific data for Strategies
 * 
 * @author Rajesh Kappera
 */
public class SegmentVO extends WealthVO {
	private static final long serialVersionUID = -9168814661756882875L;
	
	private int segmentId;
	private boolean isCustomized;
	private String segmentName;
	private double recommendedValue;
	private double targetValue;
	
	private ArrayList<RegionVO> regionList;

	/**
	 * @return the segmentId
	 */
	public int getSegmentId() {
		return segmentId;
	}

	/**
	 * @param segmentId the segmentId to set
	 */
	public void setSegmentId(int segmentId) {
		this.segmentId = segmentId;
	}

	/**
	 * @return the isCustomized
	 */
	public boolean isCustomized() {
		return isCustomized;
	}

	/**
	 * @param isCustomized the isCustomized to set
	 */
	public void setCustomized(boolean isCustomized) {
		this.isCustomized = isCustomized;
	}

	/**
	 * @return the segmentName
	 */
	public String getSegmentName() {
		return segmentName;
	}

	/**
	 * @param segmentName the segmentName to set
	 */
	public void setSegmentName(String segmentName) {
		this.segmentName = segmentName;
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
	 * @return the regionList
	 */
	public ArrayList<RegionVO> getRegionList() {
		return regionList;
	}

	/**
	 * @param regionList the regionList to set
	 */
	public void setRegionList(ArrayList<RegionVO> regionList) {
		this.regionList = regionList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("SegmentVO [segmentId=");
		builder.append(segmentId);
		builder.append(", isCustomized=");
		builder.append(isCustomized);
		builder.append(", segmentName=");
		builder.append(segmentName);
		builder.append(", recommendedValue=");
		builder.append(recommendedValue);
		builder.append(", targetValue=");
		builder.append(targetValue);
		builder.append(", regionList=");
		builder.append(regionList);
		builder.append("]");
		return builder.toString();
	}
}
