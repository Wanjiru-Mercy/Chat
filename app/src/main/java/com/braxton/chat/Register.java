package com.braxton.chat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {

    ProgressDialog pd;
    ImageView ok;
    EditText email,password,confirm;
    String Email,Password,Confirmation;
    LinearLayout register;
    FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        pd = new ProgressDialog(this);

        firebaseAuth = FirebaseAuth.getInstance();

        ok = (ImageView)findViewById(R.id.ok);
        email = (EditText)findViewById(R.id.regEmail);
        password = (EditText)findViewById(R.id.regPassword);
        confirm = (EditText)findViewById(R.id.confirm_pass);
        register = (LinearLayout) findViewById(R.id.sign_up);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Email = email.getText().toString();
                Password = password.getText().toString();
                Confirmation = confirm.getText().toString();

                if((Email.isEmpty())){
                    Toast.makeText(getApplicationContext(),"Enter Email",Toast.LENGTH_LONG).show();
                }else if(password.getText().toString().length() < 6){
                    Toast.makeText(getApplicationContext(),"Password must be at least 6 characters",Toast.LENGTH_LONG).show();
                }else if(!Confirmation.equals(Password)){
                    Toast.makeText(getApplicationContext(),"Password don't match",Toast.LENGTH_LONG).show();
                }else{
                    ok.setVisibility(View.VISIBLE);
                    registerUser();
                }
            }
        });
    }

    private void registerUser() {
        pd.setMessage("Registering you...");
        pd.setCancelable(false);
        pd.show();

        firebaseAuth.createUserWithEmailAndPassword(Email,Password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            pd.dismiss();
                            finish();
                            Toast.makeText(getApplicationContext(),"Succesful Registration",Toast.LENGTH_SHORT).show();
                            Intent new_user = new Intent(Register.this,NewUser.class);
                            startActivity(new_user);
                        }else{
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(),"Couldn't register you.Please try later",Toast.LENGTH_SHORT).show();
                        }
                    }
                });


    }
}
