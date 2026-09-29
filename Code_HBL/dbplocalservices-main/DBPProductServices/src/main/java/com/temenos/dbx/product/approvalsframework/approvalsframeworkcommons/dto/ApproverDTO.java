package com.temenos.dbx.product.approvalsframework.approvalsframeworkcommons.dto;

public class ApproverDTO {

    String customerId;
    String userName;
    String firstName;
    String lastName;
    String roleId;
    String roleName;

    public ApproverDTO() {
    }

    public ApproverDTO(String customerId, String userName, String firstName, String lastName, String roleId, String roleName) {
        this.customerId = customerId;
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.roleId = roleId;
        this.roleName = roleName;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }
}
