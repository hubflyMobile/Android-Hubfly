package com.hubfly.ctq.util;

import android.app.Activity;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;


public class ApiClient {

    static InputStream mInputStream = null;
    StringEntity mStringEntity;


    public String makeHttpRequest(Activity mActivity, String url, String method, JSONObject mRequestJsonObject) {

        int postStatusCode = 0;
        StringBuilder mStringBuilder = null;
        if (Utility.isInternetConnected(mActivity)) {
            postStatusCode = 0;
            mStringBuilder = null;
            String line = null;

            long startTime = System.currentTimeMillis();

            try {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpResponse httpResponse = null;
                if (method == "POST") {
                    HttpPost httpPost = new HttpPost(url);
                    httpPost.setHeader("Content-type", "application/json;odata=verbose");
                    mStringEntity = new StringEntity(mRequestJsonObject.toString());
                    httpPost.setEntity(mStringEntity);
                    Utility.logging("input" + mRequestJsonObject);
                    Utility.logging("URL" + url);
                    long elapsedTime = System.currentTimeMillis() - startTime;
                    System.out.println("Total elapsed http request/response time in milliseconds: " + elapsedTime);
                    httpResponse = httpClient.execute(httpPost);
                } else {
                    HttpGet httpGet = new HttpGet(url);
                    httpGet.setHeader("Cookie", "rtFa=" + Config.Rtfa + "; FedAuth=" + Config.FedAuth);
                    httpGet.setHeader("Content-type", "application/json;odata=verbose");
                    httpGet.setHeader("Content-type", "application/json;odata=verbose");
                    httpResponse = httpClient.execute(httpGet);
                }

                HttpEntity httpEntity = httpResponse.getEntity();
                postStatusCode = httpResponse.getStatusLine().getStatusCode();
                mInputStream = httpEntity.getContent();
                BufferedReader reader = new BufferedReader(new InputStreamReader(mInputStream, "iso-8859-1"), 8);
                mStringBuilder = new StringBuilder();

                while ((line = reader.readLine()) != null) {
                    mStringBuilder.append(line + "\n");
                }
                Utility.logging("Sb String" + mStringBuilder.toString());
                mInputStream.close();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return mStringBuilder.toString() + postStatusCode;

    }


    public static String sendErrorReport(String Url, final JSONObject mJsonObject) {
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(Url);
            httpPost.setHeader("Content-type", "application/json;odata=verbose");
            StringEntity mStringEntity = new StringEntity(mJsonObject.toString());
            httpPost.setEntity(mStringEntity);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity httpEntity = httpResponse.getEntity();
            Integer postStatusCode = httpResponse.getStatusLine().getStatusCode();
            Utility.logging(postStatusCode + "ResponseCode"+mJsonObject);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return "";
    }

}



