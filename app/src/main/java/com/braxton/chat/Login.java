package com.braxton.chat;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    String Email,Password,u_id;
    EditText email,password;
    FirebaseAuth firebaseAuth;
    DatabaseReference reference;
    ProgressDialog pd;
    Button login;
    TextView sign_up;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        firebaseAuth = FirebaseAuth.getInstance();

        pd = new ProgressDialog(this);
        email = (EditText) findViewById(R.id.logEmail);
        password = (EditText) findViewById(R.id.logPassword);
        sign_up = (TextView)findViewById(R.id.sign_up_link);
        login = (Button)findViewById(R.id.login);
        sign_up.setPaintFlags(sign_up.getPaintFlags()|Paint.UNDERLINE_TEXT_FLAG);
        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent register = new Intent(Login.this,Register.class);
                startActivity(register);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Email = email.getText().toString();
                Password = password.getText().toString();
                if (Email.isEmpty()||Password.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Fill all details",Toast.LENGTH_SHORT);
                }else{
                    loginUser();
                }
            }
        });
    }

    private void loginUser() {
        pd.setMessage("Logging you in...");
        pd.setCancelable(false);
        pd.show();
        firebaseAuth.signInWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if (task.isSuccessful()){
                           pd.dismiss();
                           Toast.makeText(getApplicationContext(),"Succesful Login",Toast.LENGTH_SHORT).show();
                           Intent home = new Intent(Login.this, FriendsList.class);
                           startActivity(home);
                           reference = FirebaseDatabase.getInstance().getReference();
                           u_id = firebaseAuth.getCurrentUser().getUid();
                           reference.child("users").child(u_id).addValueEventListener(new ValueEventListener() {
                               @Override
                               public void onDataChange(DataSnapshot dataSnapshot) {
                                   String user = dataSnapshot.child("username").getValue(String.class);
                                   String phone = dataSnapshot.child("phone").getValue(String.class);
                                   String address = dataSnapshot.child("address").getValue(String.class);
                                   String email = firebaseAuth.getCurrentUser().getEmail();


                                   Details.email = email;
                                   Details.user = user;
                                   Details.phone = phone;
                                   Details.address = address;
                                   Details.user_id = u_id;

                                   finish();
                               }

                               @Override
                               public void onCancelled(DatabaseError databaseError) {

                               }
                           });
                       }else{
                           pd.dismiss();
                           Toast.makeText(getApplicationContext(),"Couldn't log you in.Please try later",Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }
    private void saveCurrentUser(String email,String username,String phone,String address){
        SharedPreferences preference;
        preference = this.getSharedPreferences("ChatAppUser", Context.MODE_PRIVATE);

        SharedPreferences.Editor prefsEditor = preference.edit();

        if (preference == null){
            prefsEditor.putString("email",email);
            prefsEditor.putString("username",username);
            prefsEditor.putString("phone",phone);
            prefsEditor.putString("address",address);
            prefsEditor.commit();
        }
    }


}
