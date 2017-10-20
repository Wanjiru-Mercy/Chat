package com.braxton.chat;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ChatRoom extends AppCompatActivity {

    LinearLayout layout_2;
    ScrollView scrollView;
    DatabaseReference db;
    DatabaseReference db2;
    DatabaseReference db3;
    ImageView send;
    TextView noTexts;
    EditText text_to_send;
    String sender,receiver,text_sent,datetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        receiver = getIntent().getExtras().getString("friend's_name");
        sender = Details.user;
        setTitle(receiver);
        noTexts = (TextView)findViewById(R.id.no_texts);
        layout_2 = (LinearLayout)findViewById(R.id.layout2);
        scrollView = (ScrollView)findViewById(R.id.scrollView);
        text_to_send = (EditText)findViewById(R.id.typing_text);
        send = (ImageView)findViewById(R.id.send_text);

        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("d-m HH:mm aa");
        datetime = dateformat.format(c.getTime());

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendText();
            }
        });

        db = FirebaseDatabase.getInstance().getReference(sender+"_"+receiver);
        db2 = FirebaseDatabase.getInstance().getReference(receiver+"_"+sender);

        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                if (!dataSnapshot.exists()) {
                    noTexts.setVisibility(View.VISIBLE);
                } else {
                    noTexts.setVisibility(View.GONE);
                    String message = dataSnapshot.child("message").getValue(String.class);
                    String userName = dataSnapshot.child("user").getValue(String.class);
                    String time_sent = dataSnapshot.child("time").getValue(String.class);

                    if (userName.equals(sender)) {
                        addMessageBox("You:\n" + message,time_sent,1);
                    } else {
                        pushNotification(userName, message);
                        addMessageBox(receiver + ":\n" + message,time_sent,2);
                    }
                }
            }



            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }


        });

    }

    private void sendText() {

        text_sent = text_to_send.getText().toString();


        if (!text_sent.equals("")){
            Map<String,String> map = new HashMap<String, String>();
            map.put("message",text_sent);
            map.put("user",sender);
            map.put("time",datetime);
            db.push().setValue(map);
            db2.push().setValue(map);
            text_to_send.setText("");
        }
    }


    public void addMessageBox(String message,String time, int type){
        TextView textView = new TextView(ChatRoom.this);
        textView.setText(message);
        textView.setTextSize(15);
        textView.setPadding(10,10,10,10);
        textView.setTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.white));

        TextView text_time = new TextView(ChatRoom.this);
        text_time.setText(time);
        text_time.setTextColor(ContextCompat.getColorStateList(getApplicationContext(),R.color.black));

        LinearLayout.LayoutParams lp2 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp2.weight = 0.5f;
        lp2.setMargins(10,5,5,5);

        LinearLayout.LayoutParams lp1 = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp1.weight = 0.2f;
        lp1.setMargins(5,5,5,5);
        lp1.gravity = Gravity.BOTTOM;


        if(type == 1) {
            lp2.gravity = Gravity.RIGHT;
            lp1.gravity = Gravity.RIGHT;
            textView.setBackgroundResource(R.drawable.bubble_right_green);
        }
        else{
            lp2.gravity = Gravity.LEFT;
            lp1.gravity = Gravity.LEFT;
            textView.setBackgroundResource(R.drawable.bubble_left_gray);
        }
        textView.setLayoutParams(lp2);
        text_time.setLayoutParams(lp1);
        layout_2.addView(textView);
        layout_2.addView(text_time);
        scrollView.fullScroll(View.FOCUS_DOWN);
    }

    public void pushNotification(String user,String message){
        int mid = 1;
        int REQUEST_ID = (int)System.currentTimeMillis();
        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this)
                        .setSmallIcon(R.drawable.logo)
                        .setContentTitle(user)
                        .setContentText(message).
                        setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
        mBuilder.setAutoCancel(true);
        Intent notificationIntent = new Intent(getApplicationContext(), ChatRoom.class);
        notificationIntent.putExtra("friend's_name",user);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent contentIntent = PendingIntent.getActivity(this,REQUEST_ID,notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        mBuilder.setContentIntent(contentIntent);
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(mid,mBuilder.build());

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.call_user){

        }
        return true;
    }



}
