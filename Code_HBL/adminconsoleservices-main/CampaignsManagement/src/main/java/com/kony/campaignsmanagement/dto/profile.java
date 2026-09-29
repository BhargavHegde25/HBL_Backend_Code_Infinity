package com.kony.campaignsmanagement.dto;

public class profile
{
	private String profileId;
	public String getprofileId()
	{
		return this.profileId;
	}
	public void setprofileId(String value)
	{
		this.profileId = value;
	}

	private String profileName;
	public String getprofileName()
	{
		return this.profileName;
	}
	public void setprofileName(String value)
	{
		this.profileName = value;
	}

	private String profileDescription;
	public String getprofileDescription()
	{
		return this.profileDescription;
	}
	public void setprofileDescription(String value)
	{
		this.profileDescription = value;
	}

	private String profileStatus;
	public String getprofileStatus()
	{
		return this.profileStatus;
	}
	public void setprofileStatus(String value)
	{
		this.profileStatus = value;
	}

	private int numberOfUsers;
	public int getnumberOfUsers()
	{
		return this.numberOfUsers;
	}
	public void setnumberOfUsers(int value)
	{
		this.numberOfUsers = value;
	}

	private java.sql.Date profileCreationDate;
	public java.sql.Date getprofileCreationDate()
	{
		return this.profileCreationDate;
	}
	public void setprofileCreationDate(java.sql.Date value)
	{
		this.profileCreationDate = value;
	}

	private java.sql.Date profileDeactivatedDate;
	public java.sql.Date getprofileDeactivatedDate()
	{
		return this.profileDeactivatedDate;
	}
	public void setprofileDeactivatedDate(java.sql.Date value)
	{
		this.profileDeactivatedDate = value;
	}



}