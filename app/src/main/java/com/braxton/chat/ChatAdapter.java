package com.braxton.chat;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by Braxton on 10/9/2017.
 */

public class ChatAdapter extends BaseAdapter{
    Context ctx;
    ArrayList<String> received;
    ArrayList<String> sent;
    LayoutInflater inflater;

    public ChatAdapter(Context context, ArrayList<String> received, ArrayList<String> sent) {
        this.ctx = context;
        this.received = received;
        this.sent = sent;

        inflater = (LayoutInflater)ctx.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return sent.size();
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
        view = inflater.inflate(R.layout.chat_layout,parent,false);
        TextView friend_name = (TextView)view.findViewById(R.id.received_text);
        TextView latest_text = (TextView)view.findViewById(R.id.sent_text);

        friend_name.setText(received.get(position));
        latest_text.setText(sent.get(position));
        return view;
    }

}
