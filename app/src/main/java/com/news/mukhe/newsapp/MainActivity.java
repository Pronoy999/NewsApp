package com.news.mukhe.newsapp;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.json.JSONException;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    static ListView listView;
    static Context context;
    String id,name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context=getApplicationContext();
        listView=(ListView) findViewById(R.id.list);
        NetworkInfo info=((ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if(info.isConnected()) {
            HttpConnector httpConnector = new HttpConnector();
            try {
                URL link = new URL(Constant.SOURCE_URL);
                URL url[] = {link};
                httpConnector.execute(url);
            }
            catch (MalformedURLException e){
                Message.logMessage("ERROR: ",e.toString());
            }
        }
        else
            Message.toastMessage(context,"Oops Your internet connection is wonky!","long");

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String text = listView.getItemAtPosition(position).toString();
                getKey(text);
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
    protected void getKey(String value){
        try{
            JsonParser jsonParser=new JsonParser(Constant.SOURCES);
            HashMap<String, String> sources=jsonParser.getSourceName();
            for (HashMap.Entry<String,String> e: sources.entrySet()){
                String key=e.getKey();
                String _value=e.getValue();
                if (_value.equals(value)) {
                    id = key;
                    name=_value;
                    break;
                }
            }
        }
        catch (JSONException e){
            Message.logMessage("ERROR: Main ",e.toString());
        }
        changeActivity();
    }
    private void changeActivity(){
        String linkParts[]=Constant.ARTICLES_URL.split(" ");
        String _part1=linkParts[0];
        _part1=_part1.trim();
        _part1+=id;
        String link=_part1+linkParts[1];
        Intent intent=new Intent(MainActivity.this,NewsActivity.class);
        Bundle bundle=new Bundle();
        bundle.putString(Constant.URL_TAG,link);
        bundle.putString(Constant.NAME_TAG,name);
        intent.putExtras(bundle);
        startActivity(intent);
    }
}
