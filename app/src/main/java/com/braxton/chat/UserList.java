package com.braxton.chat;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UserList extends AppCompatActivity {

    ListView user_list;
    String username;
    SearchView search;
    ArrayAdapter adapter;
    ArrayList<String> names;
    DatabaseReference friends;
    ProgressBar pr;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        pr = (ProgressBar)findViewById(R.id.loading_user_list);
        user_list = (ListView)findViewById(R.id.user_list);
        search = (SearchView)findViewById(R.id.search_user);
        fetchUsers();

        user_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent chatroom = new Intent(UserList.this,ChatRoom.class);
                Object o = user_list.getItemAtPosition(position);
                addFriend(o.toString());
                chatroom.putExtra("friend's_name",o.toString());
                startActivity(chatroom);
            }
        });

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                user_list.setFilterText(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
    private  void addFriend(final String friend){
        friends = FirebaseDatabase.getInstance().getReference(Details.user+"'s friends");
        final Map<String,String> data  = new HashMap<>();
        data.put("username",friend);
        friends.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String username = ds.child("username").getValue(String.class);

                    if (!username.equals(friend)) {
                        friends.push().setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(getApplicationContext(), "Added as friend", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
            }

            @Override 
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private void fetchUsers() {

        pr.setVisibility(View.VISIBLE);
        final DatabaseReference databaseReference;
        databaseReference = FirebaseDatabase.getInstance().getReference("users");
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                names = new ArrayList<String>();
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    username = ds.child("username").getValue(String.class);
                    if(!username.equals(Details.user)){
                    names.add(username);
                    }
                    pr.setVisibility(View.GONE);
                    adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,names);
                    user_list.setAdapter(adapter);

                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Couldn't fetch users",Toast.LENGTH_SHORT).show();
            }
        });
    }
}
