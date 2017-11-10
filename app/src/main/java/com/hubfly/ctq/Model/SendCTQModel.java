package com.hubfly.ctq.Model;

/**
 * Created by Admin on 19-07-2017.
 */

public class SendCTQModel {
    Integer ID;

    public Integer getQACJobIDHF() {
        return QACJobIDHF;
    }

    public void setQACJobIDHF(Integer QACJobIDHF) {
        this.QACJobIDHF = QACJobIDHF;
    }

    Integer QACJobIDHF;
    String CTQValueHF,RemarksHF;
    Boolean VerifiedHF;
    double CTQMinValueHF;

    public double getCTQMinValueHF() {
        return CTQMinValueHF;
    }

    public void setCTQMinValueHF(double CTQMinValueHF) {
        this.CTQMinValueHF = CTQMinValueHF;
    }

    public double getCTQMaxValueHF() {
        return CTQMaxValueHF;
    }

    public void setCTQMaxValueHF(double CTQMaxValueHF) {
        this.CTQMaxValueHF = CTQMaxValueHF;
    }

    double CTQMaxValueHF;

    public Integer getID() {
        return ID;
    }

    public void setID(Integer ID) {
        this.ID = ID;
    }

    public String getCTQValueHF() {
        return CTQValueHF;
    }

    public void setCTQValueHF(String CTQValueHF) {
        this.CTQValueHF = CTQValueHF;
    }

    public String getRemarksHF() {
        return RemarksHF;
    }

    public void setRemarksHF(String remarksHF) {
        RemarksHF = remarksHF;
    }

    public Boolean getVerifiedHF() {
        return VerifiedHF;
    }

    public void setVerifiedHF(Boolean verifiedHF) {
        VerifiedHF = verifiedHF;
    }
}
