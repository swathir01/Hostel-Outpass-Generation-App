package com.example.hosteloutpassgeneration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class WelcomeActivity extends AppCompatActivity {
    Button wel;
    DataBaseHelper db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);
        db =  new DataBaseHelper(this);
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //Write whatever to want to do after delay specified (1 sec)
                updateauto();
                Log.d("Handler", "Running Handler");
            }
        }, 600000);
        wel = (Button) findViewById(R.id.welcome);
        wel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(WelcomeActivity.this,LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    public void updateauto()
    {
        db = new DataBaseHelper(this);
        db.upauto();
        Toast.makeText(this,"Form Automatic Updation!",Toast.LENGTH_LONG).show();
    }
}
