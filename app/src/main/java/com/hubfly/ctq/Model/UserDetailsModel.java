package com.hubfly.ctq.Model;

/**
 * Created by Admin on 19-05-2017.
 */

public class UserDetailsModel {
    int UserId;


    String Title;
    String Department;
    String Designation;
    String LoginName;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
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

    public String getLoginName() {
        return LoginName;
    }

    public void setLoginName(String loginName) {
        LoginName = loginName;
    }
}
