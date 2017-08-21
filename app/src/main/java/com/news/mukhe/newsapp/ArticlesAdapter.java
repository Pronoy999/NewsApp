package com.news.mukhe.newsapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * Created by mukhe on 21-Aug-17.
 */

public class ArticlesAdapter extends ArrayAdapter<Articles> {
    public ArticlesAdapter(Activity context, ArrayList<Articles> articles){
        super(context,0,articles);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listitem=convertView;
        if(listitem!=null){
            listitem= LayoutInflater.from(getContext()).inflate(R.layout.custom_layout,parent,false);
        }
        TextView title=(TextView) listitem.findViewById(R.id.title);
        TextView desc=(TextView) listitem.findViewById(R.id.desc);
        final TextView readMore=(TextView) listitem.findViewById(R.id.readMore);
        Articles articles=getItem(position);
        title.setText(articles.getTitle());
        desc.setText(articles.getDescription());
        String temp="Read More:"+articles.getUrl();
        readMore.setText(temp);
        readMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    String text=readMore.getText().toString();
                    String link=text.split(":")[1];
                    Intent intent=new Intent(Intent.ACTION_VIEW, Uri.parse(link));
                    getContext().startActivity(intent);
                }
                catch (ActivityNotFoundException e){
                    Message.toastMessage(getContext(),"No Application is installed to handle this event.","");
                }
            }
        });
        return listitem;
    }
}
