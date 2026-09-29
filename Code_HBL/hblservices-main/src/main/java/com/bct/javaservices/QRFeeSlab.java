package com.bct.javaservices;

import java.util.HashMap;
import java.util.Map;

public class QRFeeSlab {
	private double minAmount;
	private double maxAmount;
	private double fee;
	private String aggregatorType;
	private String aggregatorName;

	public double getMinAmount() {
		return minAmount;
	}

	public void setMinAmount(double minAmount) {
		this.minAmount = minAmount;
	}

	public double getMaxAmount() {
		return maxAmount;
	}

	public void setMaxAmount(double maxAmount) {
		this.maxAmount = maxAmount;
	}

	public double getFee() {
		return fee;
	}

	public void setFee(double fee) {
		this.fee = fee;
	}

	public String getAggregatorType() {
		return aggregatorType;
	}

	public void setAggregatorType(String aggregatorType) {
		this.aggregatorType = aggregatorType;
	}

	public String getAggregatorName() {
		return aggregatorName;
	}

	public void setAggregatorName(String aggregatorName) {
		this.aggregatorName = aggregatorName;
	}

	// Convert FeeSlab to HashMap
	public Map<String, Object> toMap() {
		Map<String, Object> map = new HashMap<>();
		map.put("minAmount", minAmount);
		map.put("maxAmount", maxAmount);
		map.put("fee", fee);
		map.put("aggregatorType", aggregatorType);
		map.put("aggregatorName", aggregatorName);
		return map;
	}

	@Override
	public String toString() {
		return "FeeSlab{minAmount=" + minAmount + ", maxAmount=" + maxAmount + ", fee=" + fee + ", aggregatorType='"
				+ aggregatorType + "'" + ", aggregatorName='" + aggregatorName + "'}";
	}

}
