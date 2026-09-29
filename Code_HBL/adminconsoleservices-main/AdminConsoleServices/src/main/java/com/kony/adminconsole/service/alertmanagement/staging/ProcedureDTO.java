package com.kony.adminconsole.service.alertmanagement.staging;

import java.util.ArrayList;
import java.util.List;

import com.konylabs.middleware.dataobject.Record;

public class ProcedureDTO {
	
	private StringBuilder insertSB = new StringBuilder();
	private StringBuilder updateSB = new StringBuilder();
	private StringBuilder deleteSB = new StringBuilder();
	
	private List<Record> insertReccList = new ArrayList<>(); 
	private List<Record> updateRecList = new ArrayList<>(); 
	private List<Record> deleteRecList = new ArrayList<>();
		
	public StringBuilder getInsertSB() {
		return insertSB;
	}
	public StringBuilder getUpdateSB() {
		return updateSB;
	}
	public StringBuilder getDeleteSB() {
		return deleteSB;
	}
	public List<Record> getInsertReccList() {
		return insertReccList;
	}
	public List<Record> getUpdateRecList() {
		return updateRecList;
	}
	public List<Record> getDeleteRecList() {
		return deleteRecList;
	}

}
