package com.example.hosteloutpassgeneration;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class FormActivity extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    Fragment fragment;
    LayoutInflater inflater;
    DataBaseHelper db  = new DataBaseHelper(this);
    BadgeDrawable inboxbadge;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        loadFragment(new AddFormFragment());
        inflater = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        bottomNavigationView =(BottomNavigationView) findViewById(R.id.nav_view);
        bottomNavigationView.setBackgroundColor(getColor(R.color.colorPrimary));
        //setBadge();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.bottomNavigationFormMenuId:
                        fragment = new AddFormFragment();
                        int value = db.getnote();
                        if(value!=0)
                        {
                            inboxbadge= bottomNavigationView.getOrCreateBadge(R.id.bottomNavigationInboxMenuId);
                            addBadge(value);
                        }
                        break;
                    case R.id.bottomNavigationInboxMenuId:
                        fragment = new InboxFragment();
                        BadgeDrawable seen = bottomNavigationView.getBadge(R.id.bottomNavigationInboxMenuId);
                       if(seen!=null)
                       {
                           db.deletenote();
                           seen.clearNumber();
                           seen.setVisible(false);
                       }
                        break;
                }
                return loadFragment(fragment);
            }
        });


    }
    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }


     public void addBadge(int num)
     {
         inboxbadge.setNumber(num);
         inboxbadge.setVisible(true);
     }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_stud, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id){
            case R.id.item2:
                Intent i = new Intent(this,LoginActivity.class);
                startActivity(i);
                Toast.makeText(getApplicationContext(),"See you Later!!",Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


}

