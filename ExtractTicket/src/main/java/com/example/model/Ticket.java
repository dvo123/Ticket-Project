package com.example.model;

public class Ticket {
	private int id;
	private String title;
	private String createdUsersUsername;
	private String createdUser;
	private String createTime;
	private String finishedTime;
	private int locationId;
	private String status;
	private String approver;
	private String requestDescription;
	private String benefitReason;
	private String impactedApplications;

	// Getters and Setters
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getCreatedUsersUsername() {
		return createdUsersUsername;
	}

	public void setCreatedUsersUsername(String createdUsersUsername) {
		this.createdUsersUsername = createdUsersUsername;
	}

	public String getCreatedUser() {
		return createdUser;
	}

	public void setCreatedUser(String createdUser) {
		this.createdUser = createdUser;
	}

	public String getCreateTime() {
		return createTime;
	}

	public void setCreateTime(String createTime) {
		this.createTime = createTime;
	}

	public String getFinishedTime() {
		return finishedTime;
	}

	public void setFinishedTime(String finishedTime) {
		this.finishedTime = finishedTime;
	}

	public int getLocationId() {
		return locationId;
	}

	public void setLocationId(int locationId) {
		this.locationId = locationId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getRequestDescription() {
		return requestDescription;
	}

	public void setRequestDescription(String requestDescription) {
		this.requestDescription = requestDescription;
	}

	public String getBenefitReason() {
		return benefitReason;
	}

	public void setBenefitReason(String benefitReason) {
		this.benefitReason = benefitReason;
	}

	public String getImpactedApplications() {
		return impactedApplications;
	}

	public void setImpactedApplications(String impactedApplications) {
		this.impactedApplications = impactedApplications;
	}
}