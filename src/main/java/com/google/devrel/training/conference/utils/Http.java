package com.google.devrel.training.conference.utils;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

import java.util.HashMap;
import java.util.Map;

public class Http {
	
	  public static String getDomainName(String url) throws URISyntaxException {
	        URI uri = new URI(url);
	        String domain = uri.getHost();
	        return domain.startsWith("www.") ? domain.substring(4) : domain;
	    }

	    public static String callApiGet(String urlAPI,HashMap<String, String> headers){
	        //HTTP
	        String reply = "";
	        URL url = null;
	        BufferedReader br = null;
	        String output = "";

	        try {
	            url = new URL(urlAPI);
	        } catch (MalformedURLException e) {
	            e.printStackTrace();
	        }

	        try {
	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Accept", "application/json");
	            addHeaders(conn, headers);
	            //System.out.println(conn.getResponseMessage());
	           // System.out.println(conn.getErrorStream());
	            br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
	            while ((output = br.readLine()) != null) {
	                reply=reply+output;
	            }
	            conn.disconnect();
	            br.close();
	            System.out.println(reply);
	        } catch (IOException e) {
	           // System.out.println("Message: "+ e.getMessage());
	            e.printStackTrace();
	        }

	        return reply;
	    }

	    public static String callApiPost(String requestURL,HashMap<String, String> params,HashMap<String, String> headers){

	        URL url = null;
	        String response = "";
	        try {
	            url = new URL(requestURL);

	            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
	            conn.setReadTimeout(15000);
	            conn.setConnectTimeout(15000);
	            conn.setRequestMethod("POST");
	            conn.setDoInput(true);
	            conn.setDoOutput(true);
	            addHeaders(conn,headers);


	            OutputStream os = conn.getOutputStream();
	            BufferedWriter writer = new BufferedWriter(
	                    new OutputStreamWriter(os, "UTF-8"));
	            writer.write(addParams(params));

	            writer.flush();
	            writer.close();
	            os.close();
	            int responseCode=conn.getResponseCode();

	            if (responseCode == HttpsURLConnection.HTTP_OK) {
	                String line;
	                BufferedReader br=new BufferedReader(new InputStreamReader(conn.getInputStream()));
	                while ((line=br.readLine()) != null) {
	                    response+=line;
	                }
	            }
	            else {
	                response="";
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return response;
	    }

	    private static String addParams(HashMap<String, String> params) throws UnsupportedEncodingException{
	        StringBuilder result = new StringBuilder();
	        boolean first = true;
	        for(Map.Entry<String, String> entry : params.entrySet()){
	            if (first)
	                first = false;
	            else
	                result.append("&");

	            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
	            result.append("=");
	            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
	        }

	        return result.toString();
	    }

	    private static void addHeaders(HttpURLConnection conn, HashMap<String, String> params) throws UnsupportedEncodingException{

	        for(Map.Entry<String, String> entry : params.entrySet()){
	            conn.setRequestProperty(entry.getKey(),entry.getValue());
	        }


	    }
}
