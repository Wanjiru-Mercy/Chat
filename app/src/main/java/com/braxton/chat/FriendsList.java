package com.braxton.chat;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FriendsList extends AppCompatActivity {

    ProgressBar pr;
    ListView friend_list;
    String friend_name,username,email,phone,address,last_text,time_sent,friend_child;
    FloatingActionButton addChat;
    ArrayList<String> names;
    ArrayList<String> texts;
    ArrayList<String> online;
    ArrayList<String> time_of_text;
    ListAdapter adapter;
    DatabaseReference database;
    DatabaseReference database1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_list);

        pr = (ProgressBar)findViewById(R.id.loading_friend_list);
        friend_list = (ListView) findViewById(R.id.friends_list);
        friend_child = Details.user+"'s friends";
        database = FirebaseDatabase.getInstance().getReference(friend_child);

        fetchFriends();
        email = Details.email;
        username = Details.user;
        phone = Details.phone;
        address = Details.address;

        friend_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatroom = new Intent(FriendsList.this,ChatRoom.class);
                TextView o = (TextView)view.findViewById(R.id.friend_name);
                chatroom.putExtra("friend's_name",o.getText().toString());
                startActivity(chatroom);
            }
        });


        addChat = (FloatingActionButton)findViewById(R.id.add_chat);
        addChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent user_list = new Intent(FriendsList.this,UserList.class);
                startActivity(user_list);
            }
        });
    }

    private void fetchFriends() {
        pr.setVisibility(View.VISIBLE);
        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                names = new ArrayList<String>();
                texts = new ArrayList<String>();
                online = new ArrayList<String>();
                time_of_text = new ArrayList<String>();

                for(DataSnapshot sn : dataSnapshot.getChildren()) {
                    username = sn.child("username").getValue(String.class);
                    getLastTexts(username);

                    names.add(username);
                    texts.add(last_text);
                    online.add("true");
                    time_of_text.add(time_sent);
                }

                pr.setVisibility(View.GONE);

                adapter = new ListAdapter(getApplicationContext(),names,texts,online,time_of_text);
                friend_list.setAdapter(adapter);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }
    private  void getLastTexts(String friend_name){
        database1 = FirebaseDatabase.getInstance().getReference(Details.user+"_"+friend_name);
        database1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                last_text = dataSnapshot.child("message").getValue(String.class);
                time_sent = dataSnapshot.child("time").getValue(String.class);
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
    private  void getCurrentUser(){
        SharedPreferences preference = this.getSharedPreferences("ChatAppUser",Context.MODE_PRIVATE);

        if (preference !=null) {
            email = preference.getString("email","");
            username = preference.getString("username","");
            phone = preference.getString("phone","");
            address = preference.getString("address","");

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu,menu);
        return true ;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.menu_profile){

            startActivity(new Intent(FriendsList.this,Profile.class));
            return true;

        }else if (id == R.id.menu_about){
            return true;
        }else if (id == R.id.menu_help){
            return true;
        }else if (id == R.id.menu_log_out){

            startActivity(new Intent(FriendsList.this,Login.class));
            finish();
            return true;
        }
        return true;
    }
}
