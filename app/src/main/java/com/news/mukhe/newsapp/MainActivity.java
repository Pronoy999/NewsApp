package com.news.mukhe.newsapp;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static ListView listView;
    static Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        listView=(ListView) findViewById(R.id.list);
        NetworkInfo info=((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info.isConnected()) {
            HttpConnector httpConnector = new HttpConnector();
            httpConnector.execute();
        }
        else
            Message.toastMessage(context,"Oops Your internet connection is wonky!","long");
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text=listView.getItemAtPosition(position).toString();
            }
        });
    }
    public static void updateUI(String s){
        ArrayList<String> arrayList=new ArrayList<>();
        try {
            JsonParser jsonParser = new JsonParser(s);
            HashMap<String,String> source=jsonParser.getSourceName();
            for (String values:source.values()){
                arrayList.add(values);
            }
            ArrayAdapter<String> arrayAdapter=new ArrayAdapter<>(context,android.R.layout.simple_list_item_1,arrayList);
            listView.setAdapter(arrayAdapter);
        }
        catch (JSONException e){
            Message.logMessage("ERROR: ",e.toString());
        }
    }
}
