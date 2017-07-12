package com.hubfly.ctq.Model;

/**
 * Created by Admin on 04-07-2017.
 */

public class OpenCtqModel {

    String Custname, Partname, jobCode, CreateBy, Creadetdate, CTQ, QAP;


    public OpenCtqModel(String Custname, String Partname, String jobCode, String CreateBy, String Creadetdate, String CTQ, String QAP) {
        this.Custname = Custname;
        this.Partname = Partname;
        this.jobCode = jobCode;
        this.CreateBy = CreateBy;
        this.Creadetdate = Creadetdate;
        this.CTQ = CTQ;
        this.QAP = QAP;
    }

    public String getCustname() {
        return Custname;
    }

    public void setCustname(String custname) {
        Custname = custname;
    }

    public String getPartname() {
        return Partname;
    }

    public void setPartname(String partname) {
        Partname = partname;
    }

    public String getJobCode() {
        return jobCode;
    }

    public void setJobCode(String jobCode) {
        this.jobCode = jobCode;
    }

    public String getCreateBy() {
        return CreateBy;
    }

    public void setCreateBy(String createBy) {
        CreateBy = createBy;
    }

    public String getDeptName() {
        return Creadetdate;
    }

    public void setDeptName(String deptName) {
        Creadetdate = deptName;
    }

    public String getCTQ() {
        return CTQ;
    }

    public void setCTQ(String CTQ) {
        this.CTQ = CTQ;
    }

    public String getQAP() {
        return QAP;
    }

    public void setQAP(String QAP) {
        this.QAP = QAP;
    }
}
