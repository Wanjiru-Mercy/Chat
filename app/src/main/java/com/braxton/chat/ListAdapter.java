package com.braxton.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Braxton on 10/9/2017.
 */

public class ListAdapter extends BaseAdapter {
    Context ctx;
    ArrayList<String> names;
    ArrayList<String> texts;
    ArrayList<String> online;
    ArrayList<String> time_of_text;
    LayoutInflater inflater;

    public ListAdapter(Context context, ArrayList<String> names, ArrayList<String> texts,ArrayList<String> online,ArrayList<String> time_of_text) {
        this.ctx = context;
        this.names = names;
        this.texts = texts;
        this.online = online;
        this.time_of_text = time_of_text;

        inflater = (LayoutInflater) ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return names.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        view = inflater.inflate(R.layout.custom_row, parent, false);
        TextView friend_name = (TextView) view.findViewById(R.id.friend_name);
        TextView latest_text = (TextView) view.findViewById(R.id.last_text);
        TextView time_of_latest_text = (TextView) view.findViewById(R.id.time_of_last_text);
        ImageView active = (ImageView) view.findViewById(R.id.active);

        friend_name.setText(names.get(position));
        latest_text.setText(texts.get(position));
        if (online.get(position).equals("false")){
            active.setImageResource(R.drawable.inactive_dot);
        }else if(online.get(position).equals("true")){
            active.setImageResource(R.drawable.active_dot);
        }
        time_of_latest_text.setText(time_of_text.get(position));
        return view;
    }
}