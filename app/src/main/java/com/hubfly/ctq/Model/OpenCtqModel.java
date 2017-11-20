package com.hubfly.ctq.Model;

import java.util.ArrayList;

/**
 * Created by Admin on 04-07-2017.
 */

public class OpenCtqModel {

    String Custname;
    String Partname;
    String jobCode;
    String HeatNo;
    String QAPStatus;
    String CTQStatus;
    Integer PartID;
    String CTQ;
    String QAP;

    public String getGroupCodeHF() {
        return GroupCodeHF;
    }

    public void setGroupCodeHF(String groupCodeHF) {
        GroupCodeHF = groupCodeHF;
    }

    String GroupCodeHF;

    public int getProcessTypeHF() {
        return ProcessTypeHF;
    }

    public void setProcessTypeHF(int processTypeHF) {
        ProcessTypeHF = processTypeHF;
    }

    int ProcessTypeHF;
    public ArrayList<ActivityModel> getmAlCtq() {
        return mAlCtq;
    }

    public void setmAlCtq(ArrayList<ActivityModel> mAlCtq) {
        this.mAlCtq = mAlCtq;
    }

    ArrayList<ActivityModel> mAlCtq = new ArrayList<>();

    public ArrayList<ActivityModel> getmAlQap() {
        return mAlQap;
    }

    public void setmAlQap(ArrayList<ActivityModel> mAlQap) {
        this.mAlQap = mAlQap;
    }

    ArrayList<ActivityModel> mAlQap = new ArrayList<>();

    public String getQAPStatus() {
        return QAPStatus;
    }

    public void setQAPStatus(String QAPStatus) {
        this.QAPStatus = QAPStatus;
    }

    public String getCTQStatus() {
        return CTQStatus;
    }

    public void setCTQStatus(String CTQStatus) {
        this.CTQStatus = CTQStatus;
    }



    public Integer getPartID() {
        return PartID;
    }

    public void setPartID(Integer partID) {
        PartID = partID;
    }

    public String getHeatNo() {
        return HeatNo;
    }

    public void setHeatNo(String heatNo) {
        HeatNo = heatNo;
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
