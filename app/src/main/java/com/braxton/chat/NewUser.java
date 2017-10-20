package com.braxton.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.*;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewUser extends AppCompatActivity {


    DatabaseReference database;
    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    ProgressDialog pd;
    String Username, user_id, Phone, Address;
    String Online;
    EditText username, phone, address;
    TextView welcome;
    Button save;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        firebaseAuth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(this);
        welcome = (TextView) findViewById(R.id.welcome_text);
        username = (EditText) findViewById(R.id.newUsername);
        phone = (EditText) findViewById(R.id.newPhone);
        address = (EditText) findViewById(R.id.newAdd);
        save = (Button) findViewById(R.id.save_details);

        user = firebaseAuth.getCurrentUser();
        user_id = user.getUid();
        welcome.setText("Welcome "+user.getEmail());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Username = username.getText().toString();
                    Phone = phone.getText().toString();
                    Address = address.getText().toString();
                    saveData();
                    Intent login = new Intent(NewUser.this, Login.class);
                    startActivity(login);
                    finish();
            }
        });
    }

    private void saveData() {
        pd.setMessage("Saving...");
        pd.show();

        Map<String,String> userinfo = new HashMap<String,String>();
        userinfo.put("username",Username);
        userinfo.put("phone",Phone);
        userinfo.put("online","true");
        userinfo.put("address",Address);

        database.child("users").child(user_id).setValue(userinfo)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Data saved successfully.Now Sign In", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), "Could't save data.Try again later", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }
}
