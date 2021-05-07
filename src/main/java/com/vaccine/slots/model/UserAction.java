package com.vaccine.slots.model;

import org.springframework.stereotype.Component;

@Component
public class UserAction {

	@Override
	public String toString() {
		return "UserAction [districtId=" + districtId + ", userEmail=" + userEmail + ", requiredAge=" + requiredAge
				+ ", requriredPincode=" + requriredPincode + "]";
	}

	private String districtId;
	private String userEmail;
	private int requiredAge;
	private int requriredPincode;

	public int getRequiredAge() {
		return requiredAge;
	}

	public void setRequiredAge(int requiredAge) {
		this.requiredAge = requiredAge;
	}

	public int getRequriredPincode() {
		return requriredPincode;
	}

	public void setRequriredPincode(int requriredPincode) {
		this.requriredPincode = requriredPincode;
	}

	public UserAction() {

	}

	public String getDistrictId() {
		return districtId;
	}

	public void setDistrictId(String districtId) {
		this.districtId = districtId;
	}

	public String getUserEmail() {
		return userEmail;
	}

	public UserAction(String districtId, String userEmail, int requiredAge) {
		this.districtId = districtId;
		this.userEmail = userEmail;
		this.requiredAge = requiredAge;
	}

	public void setUserEmail(String userEmail) {
		this.userEmail = userEmail;
	}

	public int requiredAge() {
		return requiredAge;
	}
}
