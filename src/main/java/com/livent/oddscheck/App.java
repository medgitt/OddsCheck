package com.livent.oddscheck;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import org.json.JSONArray;
import org.json.JSONObject;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

public class App 
{
	private static String getResponse() {
		String baseUrl = "https://eu-offering.kambicdn.org";
        RestAssured.baseURI = baseUrl;
        RequestSpecification request = RestAssured.given();
        Response response = request.get("/offering/v2018/ubse/event/live/open.json");
        String responseString = response.asString();
        return responseString;
	}
	
	private static int getEvent(int id, String responseString) {
        JSONObject responseObj = new JSONObject(responseString);
       if(responseObj.isEmpty()){ 
    	   return -1;
       }
        JSONArray livEvntArr = responseObj.optJSONArray("liveEvents"); 
      	 for (int i = 0; i < livEvntArr.length(); i++) {
        		JSONObject  livEvntObj = livEvntArr.optJSONObject(i); 
        		int eventId = livEvntObj.optJSONObject("event").optInt("id");  	
        		String eventName = livEvntObj.optJSONObject("event").optString("name"); 
        		if(eventId == id) {
        			System.out.println("Event : " + eventName);
        			return 1;
        		}       		
      	 }
      	return 0;
	}
	
	private static void parseResponse(int id, String responseString) {
        JSONObject responseObj = new JSONObject(responseString);
        JSONArray livEvntArr = responseObj.optJSONArray("liveEvents"); 
      	 for (int i = 0; i < livEvntArr.length(); i++) {
        		JSONObject  livEvntObj = livEvntArr.optJSONObject(i); 
        		int eventId = livEvntObj.optJSONObject("event").optInt("id");  	
        		if(eventId == id) {
        			String consoleOut = "";
        			JSONObject mainBetOfferObj = livEvntObj.optJSONObject("mainBetOffer");
        			JSONArray outcomeArr = mainBetOfferObj.optJSONArray("outcomes"); 
        			for (int j = 0; j < outcomeArr.length(); j++) {
        				String label = outcomeArr.optJSONObject(j).optString("label");
        				int oddsval = outcomeArr.optJSONObject(j).optInt("odds");
        				float dOdds = (float)oddsval/1000;
        				String odds = String.format("%.02f", dOdds);
        				if(j == 0) {          
            		        DateFormat formatter = new SimpleDateFormat("yyyy-mm-dd HH:mm:ss");
            		        String today = formatter.format(new Date());
        					consoleOut += "["+today+"]" + " | "; 
        				}
        				consoleOut +=  label+":  "+ odds + " | ";   				
        			}
        			System.out.println(consoleOut);
        			break;
        		}
        	 }
	}
	
    public static void main( String[] args )
    {   
    	String eventIdStr = args[0];
    	int eventId = 0;
    	try {
    		eventId = Integer.parseInt(eventIdStr);
    	}catch(NumberFormatException ex) {
    	}   
    	if(eventId == 0){
    		System.out.println("Please enter a valid EventId");
    		return;
    	}
    	String responseString = getResponse();
    	int eventRes = getEvent(eventId,responseString);
    	if(eventRes == 0 ||eventRes == -1 ) {
    		 String msg = eventRes == 0 ? "Please check the EventId" : "Please check the API json";
    		 System.out.println(msg);
    		 return;
    	}
    	parseResponse(eventId, responseString);
    	long delay = 10000;
    	Timer timer = new Timer();
    	LoopTask task = new LoopTask(eventId);
    	timer.scheduleAtFixedRate(task, delay, delay);    	    	
    }

    private static class LoopTask extends TimerTask {
    	int eventId;
    	LoopTask(int eventId){
    		this.eventId = eventId;
    	}
        public void run() {
        	String responseString = getResponse();
        	parseResponse(eventId, responseString);
        }
    }
}
