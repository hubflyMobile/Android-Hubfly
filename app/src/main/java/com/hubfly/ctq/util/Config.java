package com.hubfly.ctq.util;

import com.hubfly.ctq.Model.ActivityModel;
import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.Model.UserDetailsModel;

/**
 * Created by modern on 08-12-2016.
 */
public class Config {

    public static String Baseurl = "https://stgHFFoundaryQAAPI.azurewebsites.net/api/";
    public static String FormDigest = "/_api/contextinfo";
    public static Utility.onBroadcastReceived mBroadcastReceived;
    public static boolean IS_ONLINE = true;
    public static String GetUserDetails = "Master/GetUserDetails";
    public static String GetPartDetails = "Master/GetPartConfigMasterData";
    public static String GetClientDetails = "/Master/GetClientMasterData";
    public static String GetActivityDetails = "/QAC/GetActivityMaster";



    public static UserDetailsModel mGetUser;
    public static PartModel mGetPart;
    public static CustomerModel mCustomerModel;
    public static ActivityModel mActivityModel;






    //sample mail hari@hubfly006.onmicrosoft.com

}



