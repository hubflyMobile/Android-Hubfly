package com.hubfly.ctq.Model;

/**
 * Created by Admin on 19-07-2017.
 */

public class SendCTQModel {
    Integer ID;
    String CTQValueHF,RemarksHF;
    Boolean VerifiedHF;

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
