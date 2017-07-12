package com.hubfly.ctq.Model;

/**
 * Created by Admin on 03-07-2017.
 */

public class CtoModel {

    String taskName;
    String Comments;

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


    public CtoModel(String taskName,int index,String option) {
        this.taskName = taskName;
        this.index = index;
        this.Option = option;
    }

    public CtoModel(String taskName, Boolean isChecked,int index,String option){
        this.taskName = taskName;
        this.isChecked = isChecked;
        this.index = index;
        this.Option = option;
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
