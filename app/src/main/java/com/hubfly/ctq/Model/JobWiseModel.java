package com.hubfly.ctq.Model;

/**
 * Created by Admin on 17-07-2017.
 */

public class JobWiseModel {
    int CustomerIDHF;

    public int getCustomerIDHF() {
        return CustomerIDHF;
    }

    public void setCustomerIDHF(int customerIDHF) {
        CustomerIDHF = customerIDHF;
    }

    public int getPartIDHF() {
        return PartIDHF;
    }

    public void setPartIDHF(int partIDHF) {
        PartIDHF = partIDHF;
    }

    public String getHeatNumberHF() {
        return HeatNumberHF;
    }

    public void setHeatNumberHF(String heatNumberHF) {
        HeatNumberHF = heatNumberHF;
    }

    int PartIDHF;
    String HeatNumberHF;

    public String getFurnaceNameHF() {
        return FurnaceNameHF;
    }

    public void setFurnaceNameHF(String furnaceNameHF) {
        FurnaceNameHF = furnaceNameHF;
    }

    String FurnaceNameHF;
}
