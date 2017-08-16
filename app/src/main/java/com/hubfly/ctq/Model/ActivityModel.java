package com.hubfly.ctq.Model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by Admin on 12-07-2017.
 */

public class ActivityModel  implements Serializable {

    int ID;
    int JobIDHF;
    int PartIDHF;
    int ActivityIDHF;
    int CTQMinValueHF;
    ArrayList<ImageModel> mAlImage = new ArrayList<>();

    public ArrayList<ImageModel> getmAlImage() {
        return mAlImage;
    }

    public void setmAlImage(ArrayList<ImageModel> mAlImage) {
        this.mAlImage = mAlImage;
    }

    public int getCTQMinValueHF() {
        return CTQMinValueHF;
    }

    public void setCTQMinValueHF(int CTQMinValueHF) {
        this.CTQMinValueHF = CTQMinValueHF;
    }

    public int getCTQMaxValueHF() {
        return CTQMaxValueHF;
    }

    public void setCTQMaxValueHF(int CTQMaxValueHF) {
        this.CTQMaxValueHF = CTQMaxValueHF;
    }

    int CTQMaxValueHF;
    String HeatNoHF;
    String RemarksHF;
    String ActivityNameHF;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getJobIDHF() {
        return JobIDHF;
    }

    public void setJobIDHF(int jobIDHF) {
        JobIDHF = jobIDHF;
    }

    public int getPartIDHF() {
        return PartIDHF;
    }

    public void setPartIDHF(int partIDHF) {
        PartIDHF = partIDHF;
    }

    public int getActivityIDHF() {
        return ActivityIDHF;
    }

    public void setActivityIDHF(int activityIDHF) {
        ActivityIDHF = activityIDHF;
    }

    public String getHeatNoHF() {
        return HeatNoHF;
    }

    public void setHeatNoHF(String heatNoHF) {
        HeatNoHF = heatNoHF;
    }

    public String getRemarksHF() {
        return RemarksHF;
    }

    public void setRemarksHF(String remarksHF) {
        RemarksHF = remarksHF;
    }

    public String getActivityNameHF() {
        return ActivityNameHF;
    }

    public void setActivityNameHF(String activityNameHF) {
        ActivityNameHF = activityNameHF;
    }

    public String getCTQValueHF() {
        return CTQValueHF;
    }

    public void setCTQValueHF(String CTQValueHF) {
        this.CTQValueHF = CTQValueHF;
    }

    public Boolean getVerifiedHF() {
        return VerifiedHF;
    }

    public void setVerifiedHF(Boolean verifiedHF) {
        VerifiedHF = verifiedHF;
    }

    String CTQValueHF;
    Boolean VerifiedHF;

}
