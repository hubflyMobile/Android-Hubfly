package com.hubfly.ctq.Model;

/**
 * Created by Admin on 19-05-2017.
 */

public class UserDetailsModel {
    int UserId, ManagerId;
    Boolean IsSiteAdmin;

    public Boolean getAppAdmin() {
        return IsAppAdmin;
    }

    public void setAppAdmin(Boolean appAdmin) {
        IsAppAdmin = appAdmin;
    }

    Boolean IsAppAdmin;
    String Title;
    String Department;
    String Designation;
    String Location;
    String LoginName;
    String ManagerTitle;
    String ManagerLoginName;
    String HiredDate;

    public String getHiredDate() {
        return HiredDate;
    }

    public void setHiredDate(String hiredDate) {
        HiredDate = hiredDate;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getManagerId() {
        return ManagerId;
    }

    public void setManagerId(int managerId) {
        ManagerId = managerId;
    }

    public Boolean getSiteAdmin() {
        return IsSiteAdmin;
    }

    public void setSiteAdmin(Boolean siteAdmin) {
        IsSiteAdmin = siteAdmin;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDepartment() {
        return Department;
    }

    public void setDepartment(String department) {
        Department = department;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }

    public String getManagerTitle() {
        return ManagerTitle;
    }

    public void setManagerTitle(String managerTitle) {
        ManagerTitle = managerTitle;
    }

    public String getManagerLoginName() {
        return ManagerLoginName;
    }

    public void setManagerLoginName(String managerLoginName) {
        ManagerLoginName = managerLoginName;
    }


}
