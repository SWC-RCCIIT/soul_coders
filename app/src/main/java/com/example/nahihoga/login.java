package com.example.nahihoga;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


public class login extends AppCompatActivity {
    private TextView t1;
    private EditText Name;
    private EditText Password;
    //  private TextView Info;
    private Button Login;
    private int counter = 5;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
        setContentView(R.layout.activity_login);

        Name = (EditText) findViewById(R.id.userLogin);
        Password = (EditText) findViewById(R.id.userPassword);
        //Info = (TextView)findViewById(R.id.tvInfo);
        Login = (Button) findViewById(R.id.loginButton);
        t1 = (TextView) findViewById(R.id.signupLink);

        // Info.setText("No of attempts remaining: 5");

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate(Name.getText().toString(), Password.getText().toString());
            }
        });



   //-- private void validate(String userName, String userPassword) {
        // if ((userName.equals("Admin")) && (userPassword.equals("1234"))) {
        //   Intent intent = new Intent(login.this, mapActivity.class);
        //   startActivity(intent);
        // } else {
        //     Toast.makeText(getApplicationContext(), "wrong username or password", Toast.LENGTH_SHORT).show();
        // }-->


        t1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v1) {
                Intent launchActivity1 = new Intent(login.this, registration.class);
                startActivity(launchActivity1);

            }
        });
    }
        private void validate(String userName, String userPassword) {
            if ((userName.equals("Admin")) && (userPassword.equals("1234"))) {
                Intent intent = new Intent(login.this, mapActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(getApplicationContext(), "wrong username or password", Toast.LENGTH_SHORT).show();
            }
    }

}
