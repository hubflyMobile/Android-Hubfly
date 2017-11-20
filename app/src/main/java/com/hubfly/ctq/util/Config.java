package com.hubfly.ctq.util;

import com.hubfly.ctq.Model.CustomerModel;
import com.hubfly.ctq.Model.PartModel;
import com.hubfly.ctq.Model.UserDetailsModel;

import java.util.ArrayList;

/**
 * Created by modern on 08-12-2016.
 */
public class Config {

    public static String Baseurl = "http://devhffoundryqaapi-saapl.azurewebsites.net/api/";//Live
//    public static String Baseurl = "https://prdHFFoundryQAAPI.azurewebsites.net/api/";//Live
//    public static String LoginUrl = "https://hubflysoft.sharepoint.com/sites/local/apps/";
//    public static String ImageUrl = "https://hubflysoft.sharepoint.com";
 public static String LoginUrl = "https://hubflysoft.sharepoint.com/sites/Saapl/Apps/";
    public static String ImageUrl = "https://hubflysoft.sharepoint.com";
//    public static String Baseurl = "https://prdhffoundryqaapi-saapl.azurewebsites.net/api/";
    public static String CORE_SHOP = "core shop";
    public static String CORE_SHOP_CS1 = "core shop1";
    public static String CORE_SHOP_CS2 = "core shop2";
    public static String CreateNewQAC = "QAC/CreateNewQAC";
    public static String DISA_MOLDING = "disa moulding";
    public static String Department = "";
    public static String Email = "";
    public static String ErrorLog = "https://devhubflylogger.azurewebsites.net/LogAPI/LogError";
    public static String FedAuth = "";
    public static String GetCloseQAC = "QAC/GetClosedQACForToday";
    public static String GetOpenQACToday = "QAC/GetOpenQACForToday";
    public static String GetPartsDeptWise = "QAC/GetQACPartsDeptWise";
    public static String GetUserDetails = "Master/GetUserDetails";
    //    public static String ImageUrl = "https://saapl.sharepoint.com";
//    public static String LoginUrl = "https://saapl.sharepoint.com";
    public static String MELTING = "melting";
    public static String PATTERN_SHOP = "pattern shop";
    public static String Rtfa = "";
    public static String UpdateHeatActivityForCTQ = "QAC/UpdateHeatActivityForCTQ";
    public static String UpdateHeatActivityForQAP = "QAC/UpdateHeatActivityForQAP";
    public static String UserName = "";
    public static String UserProfilePhoto = (ImageUrl + "/sites/dev/_layouts/15/userphoto.aspx?size=L&accountname=");
    public static ArrayList<CustomerModel> mAlCustomer = new ArrayList();
    public static ArrayList<PartModel> mAlParts = new ArrayList();
    public static UserDetailsModel mUserDetailsModel;


}



