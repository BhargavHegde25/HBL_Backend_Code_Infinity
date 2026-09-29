package com.kony.adminconsole.dto.campaign;

import java.util.List;

public class Segment {
	
	private String segmentId;
	
	private List<DataContext> dcList;
	
    private long numberOfUsers;

    public Segment(String segmentId) {
		this.segmentId = segmentId;
	}
	public String getSegmentId() {
		return segmentId;
	}
	public void setSegmentId(String segmentId) {
		this.segmentId = segmentId;
	}
	public List<DataContext> getDcList() {
		return dcList;
	}
	public void setDcList(List<DataContext> dcList) {
		this.dcList = dcList;
	}
    public long getNumberOfUsers() {
		return numberOfUsers;
	}
	public void setNumberOfUsers(long numberOfUsers) {
		this.numberOfUsers = numberOfUsers;
	}

}
