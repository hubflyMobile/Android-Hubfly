package com.hubfly.ctq.Model;

import java.util.ArrayList;

/**
 * Created by Admin on 03-07-2017.
 */

public class CtoModel {

    String taskName;
    String Comments;
    String CtqValue;
    String CTQValueHF;
    String ImagePath;
    ArrayList<ImageModel> mAlImage = new ArrayList<>();

    public ArrayList<ImageModel> getmAlImage() {
        return mAlImage;
    }

    public void setmAlImage(ArrayList<ImageModel> mAlImage) {
        this.mAlImage = mAlImage;
    }

    public String getImagePath() {
        return ImagePath;
    }

    public void setImagePath(String imagePath) {
        ImagePath = imagePath;
    }

    public String getImageName() {
        return ImageName;
    }

    public void setImageName(String imageName) {
        ImageName = imageName;
    }

    String ImageName;

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

    String RemarksHF;
    Boolean VerifiedHF;

    public Boolean getVerifiedHF() {
        return VerifiedHF;
    }

    public void setVerifiedHF(Boolean verifiedHF) {
        VerifiedHF = verifiedHF;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    String Status;

    public String getCtqValue() {
        return CtqValue;
    }

    public void setCtqValue(String ctqValue) {
        CtqValue = ctqValue;
    }

    public String getRemarks() {
        return Remarks;
    }

    public void setRemarks(String remarks) {
        Remarks = remarks;
    }

    String Remarks;

    public String getSelectedId() {
        return SelectedId;
    }

    public void setSelectedId(String selectedId) {
        SelectedId = selectedId;
    }

    String SelectedId;

    public String getOption() {
        return Option;
    }

    public void setOption(String option) {
        Option = option;
    }

    String Option;
    Boolean isChecked;
    Integer id;
    Integer index;



    public Integer getIndex() {
        return index;
    }

    public void setIndex(Integer index) {
        this.index = index;
    }

    public CtoModel() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getComments() {
        return Comments;
    }

    public void setComments(String comments) {
        Comments = comments;
    }

    public Boolean isChecked() {
        return isChecked;
    }

    public void setChecked(Boolean checked) {
        isChecked = checked;
    }
}
