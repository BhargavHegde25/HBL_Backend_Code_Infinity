package com.kony.dbputilities.usersecurityservices;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

import com.kony.dbputilities.exceptions.HttpCallException;
import com.kony.dbputilities.util.AdminUtil;
import com.kony.dbputilities.util.URLConstants;
import com.konylabs.middleware.controller.DataControllerRequest;

public class CreateUserAdminConsoleService {

	public void createNewUserAtAdminConsole(Map<String, String> userdetails, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		String spouseName = null;
		String maritalStatus = userdetails.get("maritalstatus");
		String employmentInfo = userdetails.get("employmentInfo");
		if (StringUtils.isNotBlank(userdetails.get("spousefirstname"))
				&& StringUtils.isNotBlank(userdetails.get("spouselastname"))) {
			spouseName = userdetails.get("spousefirstname") + " " + userdetails.get("spouselastname");
		}
		if ("Married".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_MARRIED";
		} else if ("Widowed".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_WIDOWED";
		} else if ("Divorced".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_DIVORCED";
		} else {
			maritalStatus = "SID_SINGLE";
		}

		if ("Unemployed".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_UNEMPLOYED";
		} else if ("Salaried".equalsIgnoreCase(employmentInfo) || "Employed".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_EMPLOYED";
		} else if ("Retired".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_RETIRED";
		} else {
			employmentInfo = "SID_STUDENT";
		}
		input.put("Username", userdetails.get("userName"));
		input.put("FirstName", userdetails.get("userFirstName"));
		input.put("LastName", userdetails.get("userLastName"));
		input.put("PrimaryEmail", userdetails.get("email"));
		input.put("PrimaryContactNumber", userdetails.get("phone"));
		input.put("Gender", userdetails.get("gender"));
		input.put("DateOfBirth", userdetails.get("dateOfBirth"));
		input.put("Ssn", userdetails.get("ssn"));
		input.put("MaritalStatus_id", maritalStatus);
		input.put("SpouseName", spouseName);
		input.put("EmployementStatus_id", employmentInfo);
		input.put("CountryCode", userdetails.get("countryCode"));
		AdminUtil.invokeAPI(input, URLConstants.ADMIN_CUSTOMER_CREATE, dcRequest);
	}

	public void createNewUser(Map<String, String> userdetails, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		String spouseName = null;
		String maritalStatus = userdetails.get("MaritalStatus");
		String employmentInfo = userdetails.get("EmploymentInfo");
		if (StringUtils.isNotBlank(userdetails.get("SpouseFirstName"))
				&& StringUtils.isNotBlank(userdetails.get("SpouseLastName"))) {
			spouseName = userdetails.get("SpouseFirstName") + " " + userdetails.get("SpouseLastName");
		}
		if ("Married".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_MARRIED";
		} else if ("Widowed".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_WIDOWED";
		} else if ("Divorced".equalsIgnoreCase(maritalStatus)) {
			maritalStatus = "SID_DIVORCED";
		} else {
			maritalStatus = "SID_SINGLE";
		}

		if ("Unemployed".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_UNEMPLOYED";
		} else if ("Salaried".equalsIgnoreCase(employmentInfo) || "Employed".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_EMPLOYED";
		} else if ("Retired".equalsIgnoreCase(employmentInfo)) {
			employmentInfo = "SID_RETIRED";
		} else {
			employmentInfo = "SID_STUDENT";
		}

		input.put("Username", userdetails.get("UserName"));
		input.put("FirstName", userdetails.get("FirstName"));
		input.put("LastName", userdetails.get("LastName"));
		input.put("DateOfBirth", userdetails.get("DateOfBirth"));

		input.put("PrimaryEmail", userdetails.get("email"));
		input.put("PrimaryContactNumber", userdetails.get("phone"));
		input.put("MaritalStatus_id", maritalStatus);
		input.put("SpouseName", spouseName);
		input.put("EmployementStatus_id", employmentInfo);
		input.remove("email");
		input.remove("phone");
		AdminUtil.invokeAPI(input, URLConstants.ADMIN_CUSTOMER_CREATE, dcRequest);
	}

	public void createNewUserAtAdminConsole(String username, DataControllerRequest dcRequest) throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		input.put("username", username);
		AdminUtil.invokeAPI(input, URLConstants.ADMIN_DEMO_CUSTOMER_CREATE, dcRequest);
	}

	public void createBBNewUserAtAdminConsole(String username, String group, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		input.put("group", group);
		input.put("username", username);

		AdminUtil.invokeAPI(input, URLConstants.ADMIN_DEMO_CUSTOMER_CREATE, dcRequest);
	}

	public void createNewUserAtAdminConsole(String username, String countryGroup, DataControllerRequest dcRequest)
			throws HttpCallException {
		Map<String, String> input = new HashMap<>();
		new HashMap<>();
		input.put("username", username);
		input.put("countryCode", countryGroup);

		AdminUtil.invokeAPI(input, URLConstants.ADMIN_DEMO_CUSTOMER_CREATE, dcRequest);
	}

	public String getAccessToken(DataControllerRequest dcRequest) throws HttpCallException {
		return AdminUtil.getAdminToken(dcRequest);
	}
}
