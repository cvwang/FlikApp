package com.example.flik;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.database.CursorJoiner.Result;
import android.os.StrictMode;
import android.util.Log;

public class DataFetcher {
	
	public String foodName = "";
	public int surveyNumber;
	public int questionNumber;
	public String username;
	public String userComment;
	public int update;

	public DataFetcher()
	{
		StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
		StrictMode.setThreadPolicy(policy);
	}
	
	public int getNumberItems()
	{
		JSONObject json = readServer("getItems");
		int result = 0;
		try {
			JSONArray a = json.getJSONArray("foods");
			result = a.length();
		    }
		 catch (JSONException e) {
		    e.printStackTrace();
		}		
		return result;
	}
	
	public ArrayList<String> getItemNames()
	{
		JSONObject json = readServer("getItems");
		ArrayList<String> list = new ArrayList<String>();
		try {   
			JSONArray jsonArray = json.getJSONArray("foods");
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ 
			    list.add(jsonArray.get(i).toString());
			   } 
			} 
			return list;
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getQuestions()
	{
		JSONObject json = readServer("getQuestions");
		ArrayList<String> list = new ArrayList<String>();
		try {   
			JSONArray jsonArray = json.getJSONArray("questions");
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ 
			    list.add(jsonArray.get(i).toString());
			   } 
			} 
			return list;
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public boolean submitComment(int x, String name, String comment)
	{
		username = fixSpaces(name);
		userComment = fixSpaces(comment);
		
		boolean re = false;
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://charles.gearheadlabs.com/flik_android.php?a=submitComment" + "&surveyNumber=" + surveyNumber + "&questionNumber=" + (x+1) + "&username=" + username + "&userComment=" + userComment);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      re = true;
	    }
	    catch(Exception e){}
	    return re;
	}
	
	public ArrayList<String> getComments(int x)
	{
		questionNumber = x;
		JSONObject json = readServer("getComments");
		ArrayList<String> list = new ArrayList<String>();
		try {   
			JSONArray jsonArray = json.getJSONArray("comments");
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ 
			    list.add(jsonArray.get(i).toString());
			   } 
			} 
			return list;
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public ArrayList<String> getCommenterNames(int x)
	{
		questionNumber = x;
		JSONObject json = readServer("getCommenterNames");
		ArrayList<String> list = new ArrayList<String>();
		try {   
			JSONArray jsonArray = json.getJSONArray("commenters");
			if (jsonArray != null) { 
			   for (int i=0;i<jsonArray.length();i++){ 
			    list.add(jsonArray.get(i).toString());
			   } 
			} 
			return list;
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return list;
	}
	
	public int getTotalRating(String x)
	{
		foodName = fixSpaces(x);
		JSONObject json = readServer("getTotalRating");
		int result = 0;
		try {
			result = json.getInt("ratings");
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	public int getNumberRating(String x)
	{
		foodName = fixSpaces(x);
		JSONObject json = readServer("getNumberRating");
		int result = 0;
		try {
			result = json.getInt("ratings");
		}
		catch (JSONException e) {
		    e.printStackTrace();
		}
		return result;
	}
	
	public boolean updateRating(String x, int y, int z)
	{
		foodName = fixSpaces(x);
		boolean re = false;
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://charles.gearheadlabs.com/flik_android.php?a=updateTotalRating" + "&foodName=" + foodName + "&totalRating=" + y + "&numberRating=" + z);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      re = true;
	    }
	    catch(Exception e){}
	    return re;
	}
	
	public String fixSpaces(String x)
	{
		String newString = x.replace(" ","%20");
		return newString;
	}

	public JSONObject readServer(String action)
	{
		JSONObject jObj = null;
	    StringBuilder builder = new StringBuilder();
	    HttpClient client = new DefaultHttpClient();
	    HttpGet httpGet = new HttpGet("http://charles.gearheadlabs.com/flik_android.php?a=" + action + "&foodName=" + foodName + "&surveyNumber=" + surveyNumber + "&questionNumber=" + questionNumber);
	    try {
	      HttpResponse response = client.execute(httpGet);
	      StatusLine statusLine = response.getStatusLine();
	      int statusCode = statusLine.getStatusCode();
	      if (statusCode == 200) {
	        HttpEntity entity = response.getEntity();
	        InputStream content = entity.getContent();
	        BufferedReader reader = new BufferedReader(new InputStreamReader(content));
	        String line;
	        while ((line = reader.readLine()) != null) {
	          builder.append(line);
	        }
	      }
	      else {
	    	  Log.e("Failed to download file", "");
	      }
	    } catch (ClientProtocolException e) {
	      e.printStackTrace();
	    } catch (IOException e) {
	      e.printStackTrace();
	    }
	    try {
        jObj = new JSONObject(builder.toString());
	    } catch (JSONException e) {
        Log.e("JSON Parser", "Error parsing data " + e.toString());
	    }
	
	    // return JSON String
	    return jObj;
	}
}
