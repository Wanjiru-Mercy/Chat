package com.braxton.chat;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class Profile extends AppCompatActivity {

    TextView username,email,phone,address;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        username = (TextView)findViewById(R.id.profile_username);
        email = (TextView)findViewById(R.id.profile_email);
        phone = (TextView)findViewById(R.id.profile_phone);
        address = (TextView)findViewById(R.id.profile_location);

        username.setText("Username\n"+Details.user);
        email.setText("Email\n"+Details.email);
        phone.setText("Phone Number\n"+Details.phone);
        address.setText("Address\n"+Details.address);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.edit_profile);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });


    }

}
