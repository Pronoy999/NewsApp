package com.news.mukhe.newsapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by mukhe on 21-Aug-17.
 */

public class HttpConnector extends AsyncTask<URL,Void,String> {
    @Override
    protected String doInBackground(URL... params) {
        StringBuilder data=new StringBuilder();
        HttpURLConnection httpURLConnection=null;
        InputStream inputStream;
        try{
            URL url=params[0];
            httpURLConnection=(HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setConnectTimeout(10000);
            inputStream=httpURLConnection.getInputStream();
            data=readFromStream(inputStream);
        }
        catch (Exception e){
            Message.logMessage("ERROR: ",e.toString());
        }
        finally {
            if(httpURLConnection!=null){
                httpURLConnection.disconnect();
            }
        }
        return data.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        Constant.SOURCES=s;
        MainActivity.updateUI(s);
    }
    private StringBuilder readFromStream(InputStream inputStream){
        StringBuilder builder=new StringBuilder();
        try{
            if(inputStream!=null){
                BufferedReader bufferedReader=new BufferedReader(new InputStreamReader(inputStream,"UTF-8"));
                String line=bufferedReader.readLine();
                while(line!=null){
                    builder.append(line);
                    line=bufferedReader.readLine();
                }
            }
            if (builder.toString().equals(""))
                Message.logMessage("JSON DATA: ","Data Downloaded Successfully!");
        }
        catch (IOException e){
            Message.logMessage("ERROR: ",e.toString());
        }
        return builder;
    }
}
