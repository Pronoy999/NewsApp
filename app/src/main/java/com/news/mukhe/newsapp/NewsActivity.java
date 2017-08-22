package com.news.mukhe.newsapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class NewsActivity extends AppCompatActivity {
    String link;
    ListView listView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);
        Intent intent=getIntent();
        Bundle bundle=intent.getExtras();
        link=bundle.getString(Constant.URL_TAG);
        listView=(ListView) findViewById(R.id.articles);
        HttpHandler httpHandler=new HttpHandler();
        httpHandler.execute();
    }
    public class HttpHandler extends AsyncTask<URL,Void,String>{
        @Override
        protected String doInBackground(URL... params) {
            HttpURLConnection httpURLConnection;
            InputStream inputStream;
            StringBuilder data=new StringBuilder();
            try{
                URL url=new URL(link);
                httpURLConnection=(HttpURLConnection)url.openConnection();
                httpURLConnection.setRequestMethod("GET");
                httpURLConnection.setConnectTimeout(10000);
                inputStream=httpURLConnection.getInputStream();
                data=readFromStream(inputStream);
            }
            catch (Exception e){
                Message.logMessage("ERROR: ",e.toString());
            }
            return data.toString();
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

        @Override
        protected void onPostExecute(String s) {
            Constant.NEWS=s;
            updateUI();
        }
    }
    public void updateUI(){
        ArrayList<Articles> articlesArrayList=new ArrayList<>();
        try {
            JsonParser jsonParser = new JsonParser(Constant.NEWS);
            JSONArray jsonArray=jsonParser.getArticles();
            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject=new JSONObject(jsonArray.get(i).toString());
                String _title=jsonObject.getString(Constant.TITLE);
                String _desc=jsonObject.getString(Constant.DESCRIPTION);
                String _url=jsonObject.getString(Constant.URL);
                Articles articles=new Articles(_title,_desc,_url);
                articlesArrayList.add(articles);
            }
            ArticlesAdapter articlesAdapter=new ArticlesAdapter(this,articlesArrayList);
            listView.setAdapter(articlesAdapter);
        }
        catch (JSONException e){
            Message.logMessage("ERROR: ",e.toString());
        }
    }
}
