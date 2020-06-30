package com.example.hosteloutpassgeneration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity {

    private EditText Name;
    private EditText Password;
    private Button Login;
    private TextView Info;
    private int counter = 5;
    private TextView signup;
    private TextView adminlink;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        db = new DataBaseHelper(this);
        Name = (EditText)findViewById(R.id.username);
        Password = (EditText)findViewById(R.id.logpassword);
        Login = (Button)findViewById(R.id.btnlogin);
        //Info = (TextView)findViewById(R.id.tvinfo);
        //Info.setText("No of remaining attempts: 5");
        adminlink = (TextView)findViewById(R.id.adminlink);
        signup = (TextView)findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);

                Toast.makeText(LoginActivity.this,"Enter the valid id sent!",Toast.LENGTH_LONG).show();
            }
        });
        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    String user = Name.getText().toString();
                    String pwd = Password.getText().toString().trim();
                    Boolean res = db.checkUser(user, pwd);
                    if(res == true)
                    {
                        Intent HomePage = new Intent(LoginActivity.this,FormActivity.class);
                        HomePage.putExtra("name",user);
                        HomePage.putExtra("nam",user);
                        startActivity(HomePage);
                        Name.setText("");
                        Password.setText("");
                    }
                    else
                    {
                        Toast.makeText(LoginActivity.this,"Login Error",Toast.LENGTH_SHORT).show();
                        Name.setText("");
                        Password.setText("");
                    }
                }


        });
        adminlink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate(Name.getText().toString(),Password.getText().toString());

            }
        });
    }
    private void validate(String user,String pass)
    {
        if((user.equals("swathi"))&&(pass.equals("sss")))
        {
            Intent intent = new Intent(LoginActivity.this, AdminActivity.class);
            startActivity(intent);
            Name.setText("");
            Password.setText("");
            Toast.makeText(LoginActivity.this,"Hai Manager",Toast.LENGTH_LONG).show();
        }else{
            counter--;
            Toast.makeText(LoginActivity.this,"You are not a Manager",Toast.LENGTH_LONG).show();
            //Info.setText("No of remaining attempts:"+String.valueOf(counter));
            if(counter==0)
            {
                Login.setEnabled(false);
            }
        }
    }
}
