package com.hubfly.ctq.util;

import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.Model.UserDetailsModel;

import java.util.ArrayList;

/**
 * Created by modern on 08-12-2016.
 */
public class Config {

//    public static String Baseurl = "https://stgHFFoundryQAAPI.azurewebsites.net/api/";//Dev
//    public static String Baseurl = "http://devhffoundryqaapi.azurewebsites.net/api/";//Live
    public static String Baseurl = "https://prdHFFoundryQAAPI.azurewebsites.net/api/";//Live
    public static String LoginUrl = "https://hubflysoft.sharepoint.com/sites/local/apps/";
    public static String ImageUrl = "https://hubflysoft.sharepoint.com";
//    public static String ImageUrl = "https://jsautocast.sharepoint.com";
//    public static String LoginUrl = "https://jsautocast.sharepoint.com";

    public static String ErrorLog = "https://devhubflylogger.azurewebsites.net/LogAPI/LogError";

    public static boolean IS_ONLINE = true;
    public static String GetUserDetails = "Master/GetUserDetails";

    public static String UpdateHeatActivityForCTQ = "QAC/UpdateHeatActivityForCTQ";
    public static String UpdateHeatActivityForQAP = "QAC/UpdateHeatActivityForQAP";

    public static String GetOpenQACToday = "QAC/GetOpenQACForToday";
    public static String CreateNewQAC = "QAC/CreateNewQAC";
    public static String GetCloseQAC = "QAC/GetClosedQACForToday";
    public static String GetPartsDeptWise = "QAC/GetQACPartsDeptWise";

    public static String UserProfile = "/_api/SP.UserProfiles.PeopleManager/GetMyProperties/PictureUrl";

    public static String UserName = "";
    public static String Department = "";
    public static String PictureUrl = "";

    public static String Rtfa = "";
    public static String FedAuth = "";

    public static UserDetailsModel mUserDetailsModel;

    public static ArrayList<PartModel> mAlParts = new ArrayList<>();
    public static ArrayList<CustomerModel> mAlCustomer = new ArrayList<>();

}



