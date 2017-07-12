package com.hubfly.ctq.util;

import android.app.Activity;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.List;


/**
 * Created by vis-1597 on 10/7/2016.
 */

public class ApiClient {

    JSONObject mJsonObject = null;
    SessionManager mSessionManager;
    static InputStream is = null;
    StringEntity se;


    public String makeHttpRequest(Activity mActivity, String url, String method,
                                  List<NameValuePair> params, String option, JSONObject mRequestJsonObject) {
        int postStatusCode = 0;

        try {
            if (method == "POST") {
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                httpPost.setHeader("Content-type", "application/json;odata=verbose");

                if (option.equals("3") || option.equals("4")) {
                    httpPost.setHeader("IF-MATCH", "*");
                    if (option.equals("3")) {
                        httpPost.setHeader("X-HTTP-Method", "DELETE");
                    } else {
                        httpPost.setHeader("X-HTTP-Method", "MERGE");
                    }
                }

                if (mRequestJsonObject != null) {
                    Utility.logging("Params" + mRequestJsonObject);
                    se = new StringEntity(mRequestJsonObject.toString());
                    httpPost.setEntity(se);
                }

                Utility.logging("URL" + url);
                Utility.logging("Params" + mRequestJsonObject);

                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                postStatusCode = httpResponse.getStatusLine().getStatusCode();
                Utility.logging("" + postStatusCode);
                is = httpEntity.getContent();
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        StringBuilder sb = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            Utility.logging("Sb String" + sb.toString());
            is.close();
        } catch (Exception e) {
            Log.e("Buffer Error", "Error converting result " + e.toString());
        }

        return sb.toString() + postStatusCode;
    }
}

