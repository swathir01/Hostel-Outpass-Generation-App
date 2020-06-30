package com.example.hosteloutpassgeneration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {
    DataBaseHelper db;
    EditText mTextUsername;
    EditText mTextPassword;
    EditText mTextCnfPassword;
    EditText studentID;
    Button mButtonRegister;
    TextView mTextViewLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        db =  new DataBaseHelper(this);
        mTextUsername = (EditText)findViewById(R.id.edittext_username);
        studentID = (EditText) findViewById(R.id.studentid);
        mTextPassword = (EditText)findViewById(R.id.edittext_password);
        mTextCnfPassword = (EditText)findViewById(R.id.edittext_cnf_password);
        mButtonRegister = (Button)findViewById(R.id.button_register);
        mTextViewLogin = (TextView)findViewById(R.id.textview_login);
        mTextViewLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent LoginIntent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(LoginIntent);
            }
        });
        mButtonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = mTextUsername.getText().toString();
                String sid = studentID.getText().toString();
                String pwd = mTextPassword.getText().toString().trim();
                String cnf_pwd = mTextCnfPassword.getText().toString().trim();
                boolean exists= loginvalidate(sid);
                boolean userex = db.checkuser(user);
                if(exists==true) {
                    if(userex==false) {
                        if (pwd.equals(cnf_pwd)) {
                            boolean val = db.addUser(sid, user, pwd);
                            if (val) {
                                Toast.makeText(RegisterActivity.this, "You have registered", Toast.LENGTH_SHORT).show();
                                Intent moveToLogin = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(moveToLogin);
                            } else {
                                Toast.makeText(RegisterActivity.this, "Registeration Error", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(RegisterActivity.this, "Password is not matching", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegisterActivity.this, "Try unique Username!", Toast.LENGTH_SHORT).show();
                    }
                }
                else
                {
                    Toast.makeText(RegisterActivity.this,"Invalid Student ID!",Toast.LENGTH_SHORT).show();
                    studentID.setText("");
                    mTextUsername.setText("");
                    mTextPassword.setText("");
                    mTextCnfPassword.setText("");
                }
            }
        });

    }
    private boolean loginvalidate(String sid)
    {
        return db.checkId(sid);
    }
}
