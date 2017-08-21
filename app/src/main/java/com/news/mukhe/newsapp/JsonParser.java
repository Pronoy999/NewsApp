package com.news.mukhe.newsapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by mukhe on 20-Aug-17.
 */

public class JsonParser {
    private JSONObject jsonData;
    public JsonParser(String jsonData)throws JSONException{
        this.jsonData=new JSONObject(jsonData);
    }
    protected HashMap<String,String> getSourceName(){
        HashMap<String,String> sourcesName=new HashMap<>();
        try {
            JSONArray sources = jsonData.getJSONArray("sources");
            for(int i=0;i<sources.length();i++){
                JSONObject object=new JSONObject(sources.get(i).toString());
                sourcesName.put(object.getString("id"),object.getString("name"));
            }
        }
        catch (JSONException e){
            Message.logMessage("ERROR: ",e.toString());
        }
        return sourcesName;
    }
}
