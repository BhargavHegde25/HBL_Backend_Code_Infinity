package com.temenos.infinity.api.wealth.vo;

import java.util.ArrayList;

/**
 * SegmentVO is a POJO object and it holds segment specific data for Strategies
 * 
 * @author Rajesh Kappera
 */
public class AssetVO extends WealthVO {
	
	private static final long serialVersionUID = -5214571763399085131L;
	
	private int updateCount;
	private ArrayList<SegmentVO> segmentList;
	
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
	 * @return the segmentList
	 */
	public ArrayList<SegmentVO> getSegmentList() {
		return segmentList;
	}
	
	/**
	 * @param segmentList the segmentList to set
	 */
	public void setSegmentList(ArrayList<SegmentVO> segmentList) {
		this.segmentList = segmentList;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("AssetVO [updateCount=");
		builder.append(updateCount);
		builder.append(", segmentList=");
		builder.append(segmentList);
		builder.append("]");
		return builder.toString();
	}
}
