package com.visa.ATM;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.visa.ATM.CashProvider.HomeCashProvider;

import java.util.Objects;


public class SignUp extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        final int[] flag_cp = {0};
        final int[] flag_us = {0};
        final EditText Name;
        final EditText Email;
        final EditText Password;
        final EditText ConfirmPassword;

        Button signUpBotton;

        Name = findViewById(R.id.signUpNameEditText);
        Email = findViewById(R.id.signUpEmailEditText);
        Password = findViewById(R.id.signUpPasswordEditText);
        ConfirmPassword = findViewById(R.id.signUpConfirmPasswordEditText);

        final CashProviders newCashProvider = new CashProviders();
        final Users newUser = new Users();

        final DatabaseReference[] cashProviders = new DatabaseReference[1];
        final DatabaseReference[] users = new DatabaseReference[1];

        Intent intent = getIntent();
        final String option = Objects.requireNonNull(intent.getExtras()).getString("option");

        signUpBotton = findViewById(R.id.signUpButton);

        signUpBotton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (Password.toString().equals(ConfirmPassword.toString()))
                {
                    Toast.makeText(getApplicationContext(),"" + option, Toast.LENGTH_SHORT).show();
                    if(option.equals("cp"))
                    {
                        cashProviders[0] = FirebaseDatabase.getInstance().getReference().child("CashProviders");
                        cashProviders[0].addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot cashProvider : snapshot.getChildren())
                                {
                                    CashProviders cashp = cashProvider.getValue(CashProviders.class);
                                    assert cashp != null;
                                    if (cashp.email.equals(Email.toString()))
                                    {
                                        flag_cp[0] = 1;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if (flag_cp[0] == 0)
                        {
                            //do signup

                            newCashProvider.setMail(Email.toString());
                            newCashProvider.setName(Name.toString());
                            newCashProvider.setPassword(Password.toString());

                            String cashProviderID = cashProviders[0].push().getKey();
                            cashProviders[0].child(cashProviderID).setValue(newCashProvider);

                            Intent i = new Intent(getApplicationContext(), HomeCashProvider.class);
                            i.putExtra("cashProviderID",cashProviderID);
                            startActivity(i);

                        }
                    }
                    else if(option.equals("us"))
                    {
                        users[0] = FirebaseDatabase.getInstance().getReference().child("Users");
                        users[0].addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for (DataSnapshot user : snapshot.getChildren())
                                {
                                    Users usr = user.getValue(Users.class);
                                    assert usr != null;
                                    if (usr.email.equals(Email.toString()))
                                    {
                                        flag_us[0] = 1;
                                    }

                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });

                        if (flag_us[0] == 0)
                        {
                            //do signup

                            newUser.setMail(Email.toString());
                            newUser.setName(Name.toString());
                            newUser.setPassword(Password.toString());

                            String userID = users[0].push().getKey();
                            users[0].child(userID).setValue(newUser);

//                    Intent i = new Intent(getApplicationContext(), User_Home.class);
//                    i.putExtra("userID",userID);
//                    startActivity(i);
                        }
                    }

                }
                else{
                    Toast.makeText(getApplicationContext(), "Sign Unsuccessful", Toast.LENGTH_SHORT).show();
                }
            }});







    }
}