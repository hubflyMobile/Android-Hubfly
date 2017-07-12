package com.hubfly.ctq.Model;

/**
 * Created by Admin on 12-07-2017.
 */

public class PartModel {
    public int ID ;
    public String PartNoHF,PartNameHF,CustomerIDHF ,SpecTypeHF;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getPartNoHF() {
        return PartNoHF;
    }

    public void setPartNoHF(String partNoHF) {
        PartNoHF = partNoHF;
    }

    public String getPartNameHF() {
        return PartNameHF;
    }

    public void setPartNameHF(String partNameHF) {
        PartNameHF = partNameHF;
    }

    public String getCustomerIDHF() {
        return CustomerIDHF;
    }

    public void setCustomerIDHF(String customerIDHF) {
        CustomerIDHF = customerIDHF;
    }

    public String getSpecTypeHF() {
        return SpecTypeHF;
    }

    public void setSpecTypeHF(String specTypeHF) {
        SpecTypeHF = specTypeHF;
    }
}
