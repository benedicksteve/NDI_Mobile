package com.ndi_mobile.ndi_mobile.utils;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicHeader;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.SocketTimeoutException;

public class JSONParser {

    static InputStream is = null;
    static JSONObject jObj = null;
    static String json = "";

    // constructor
    public JSONParser() {

    }

    // function get json from url
    // by making HTTP POST or GET method
    public JSONObject makeHttpRequest(String url, String method, String token,
                                      JSONObject params) throws Exception {

        // Making HTTP request
        try {

            // check for request method
            if (method.equals("POST")) {
                // request method is POST
                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPost httpPost = new HttpPost(url);

                //httpPost.setEntity(new UrlEncodedFormEntity(params));
                StringEntity se = new StringEntity(params.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPost.setEntity(se);

                if(token != null){
                       httpPost.setHeader("Authorization", "Bearer "+token);
                }

                // new
                HttpParams httpParameters = httpPost.getParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpClient.execute(httpPost);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();

            }else if (method.equals("PUT")){
                // request method is PUT

                // defaultHttpClient
                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpPut httpPut = new HttpPut(url);

                // adding params
                StringEntity se = new StringEntity(params.toString());
                se.setContentType("application/json;charset=UTF-8");
                se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));
                httpPut.setEntity(se);

                // new
                HttpParams httpParameters = httpPut.getParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpClient.execute(httpPut);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }
            /* else if (method == "GET") {
                // request method is GET
                DefaultHttpClient httpClient = new DefaultHttpClient();
                String paramString = URLEncodedUtils.format(params, "utf-8");
                url += "?" + paramString;
                HttpGet httpGet = new HttpGet(url);
                // new
                HttpParams httpParameters = httpGet.getParams();
                // Set the timeout in milliseconds until a connection is
                // established.
                int timeoutConnection = 10000;
                HttpConnectionParams.setConnectionTimeout(httpParameters,
                        timeoutConnection);
                // Set the default socket timeout (SO_TIMEOUT)
                // in milliseconds which is the timeout for waiting for data.
                int timeoutSocket = 10000;
                HttpConnectionParams
                        .setSoTimeout(httpParameters, timeoutSocket);
                // new
                HttpResponse httpResponse = httpClient.execute(httpGet);
                HttpEntity httpEntity = httpResponse.getEntity();
                is = httpEntity.getContent();
            }*/

        } catch (UnsupportedEncodingException e) {
            throw new Exception("Unsupported encoding error.");
        } catch (ClientProtocolException e) {
            throw new Exception("Client protocol error.");
        } catch (SocketTimeoutException e) {
            throw new Exception("Sorry, socket timeout.");
        } catch (ConnectTimeoutException e) {
            throw new Exception("Sorry, connection timeout.");
        } catch (IOException e) {
            throw new Exception("I/O error(May be server down).");
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            json = sb.toString();
        } catch (Exception e) {
            throw new Exception(e.getMessage());
        }

        // try parse the string to a JSON object
        try {
            // on enlève les crochets TODO plus necéssaire apres modif serveur
            //json = json.substring(1,json.length()-1);
            json = json.replace("[", "");
            json = json.replace("]","");

            jObj = new JSONObject(json);
        } catch (JSONException e) {
            throw new Exception(e.getMessage());
        }

        // return JSON String
        return jObj;

    }
}