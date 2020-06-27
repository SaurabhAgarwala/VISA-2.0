package com.visa.ATM;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.SignInButton;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Login extends AppCompatActivity {

    EditText Email;
    EditText Password;

    DatabaseReference cashProviders;
    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Email=findViewById(R.id.emailEditText);
        Password=findViewById(R.id.passwordEditText);
        final Button LoginButton=(Button)findViewById(R.id.loginbutton) ;
        final Button SignupButton = (Button) findViewById(R.id.signupbutton);

        cashProviders = FirebaseDatabase.getInstance().getReference().child("CashProviders");
        users = FirebaseDatabase.getInstance().getReference().child("Users");

        Intent intent = getIntent();
        final String option = Objects.requireNonNull(intent.getExtras()).getString("option");

        SignupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), SignUp.class);
                i.putExtra("option",option);
                startActivity(i);
            }});

        LoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                assert option != null;
                if (option.equals("cp")){
                    cashProviders.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot cashProvider : snapshot.getChildren())
                            {
                                CashProviders cashp = cashProvider.getValue(CashProviders.class);
                                assert cashp != null;
                                if (cashp.email.equals(Email.toString()))
                                {
                                    if (cashp.password.equals(Password.toString()))
                                    {
                                        Toast.makeText(getApplicationContext(), "Login Is Successful", Toast.LENGTH_SHORT).show();
                                        //login kardo start cash provider page
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
                else if (option.equals("us")){
                    users.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for (DataSnapshot user : snapshot.getChildren())
                            {
                                Users usr = user.getValue(Users.class);
                                if (usr.email.equals(Email.toString()))
                                {
                                    if (usr.password.equals(Password.toString()))
                                    {
                                        Toast.makeText(getApplicationContext(), "Login Is Successful", Toast.LENGTH_SHORT).show();
                                        //login kardo start users page
                                    }
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });










    }

    @Override
    protected void onStart() {
        super.onStart();



    }
}