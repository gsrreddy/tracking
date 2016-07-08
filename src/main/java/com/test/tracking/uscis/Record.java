package com.test.tracking.uscis;

import java.util.Date;

public class Record {

	private Date date;
	
	private String caseType;
	
	private String status;
	
	private String caseNumber;

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getCaseType() {
		return caseType;
	}

	public void setCaseType(String caseType) {
		this.caseType = caseType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getCaseNumber() {
		return caseNumber;
	}

	public void setCaseNumber(String caseNumber) {
		this.caseNumber = caseNumber;
	}

	@Override
	public String toString() {
		return "Record [" + (date != null ? "date=" + date + ", " : "")
				+ (caseType != null ? "caseType=" + caseType + ", " : "")
				+ (status != null ? "status=" + status + ", " : "")
				+ (caseNumber != null ? "caseNumber=" + caseNumber : "") + "]";
	}
}
