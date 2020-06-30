package com.example.hosteloutpassgeneration;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.File;

public class InboxFragment extends Fragment {
    DataBaseHelper db;
    TableLayout tl;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View root =inflater.inflate(R.layout.fragment_inbox, null);
        db = new DataBaseHelper(getContext());
        String sname = getActivity().getIntent().getStringExtra("name");
        String sid = db.getid(sname);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        final Cursor res = db.getidnotify(sid);
        res.moveToFirst();
        final int count = res.getCount();
        if(count == 0)
        {
            Toast.makeText(getContext(),"No Notifications!",Toast.LENGTH_LONG).show();
        }
        else {
            tl = (TableLayout) root.findViewById(R.id.inboxes);
            do {
                TableRow row = new TableRow(getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setBackgroundColor(Color.TRANSPARENT);
                row.setPadding(5,5,5,5);
                row.setLayoutParams(lp);
                TextView date = new TextView(getContext());
                date.setText(res.getString(1));
                //final String id = res.getString(0);
                //date.setBackgroundResource(R.drawable.cell);
                date.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(date);
                TextView gap = new TextView(getContext());
                gap.setText("=>");
                //final String id = res.getString(0);
                //date.setBackgroundResource(R.drawable.cell);
                gap.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(gap);
                TextView mess = new TextView(getContext());
                mess.setText(res.getString(2));
                //final String ename = res.getString(1);
                //mess.setBackgroundResource(R.drawable.cell);
                mess.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(mess);
                String value = res.getString(3);
                if(value.equals("1"))
                {
                    ImageButton viewbtn = new ImageButton(getContext());
                    viewbtn.setImageResource(R.drawable.ic_visibility_black_24dp);
                    viewbtn.setBackgroundResource(R.drawable.cell);
                    viewbtn.setScaleType(ImageButton.ScaleType.FIT_XY);
                   //viewbtn.setScaleY(2f);
                    viewbtn.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {

                              openfile();
                        }
                    });
                    row.addView(viewbtn);
                }
                tl.addView(row);
            }while(res.moveToNext());
        }
        res.close();
        return root;
    }
    public void openfile()
    {
        File file = new File(Environment.getExternalStorageDirectory().getAbsoluteFile()+"/mypdf/form.pdf");
        if (file.exists())
        {
            Intent intent=new Intent(Intent.ACTION_VIEW);
            Uri uri = Uri.fromFile(file);

            intent.setDataAndType(uri, "application/pdf");
            //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            try
            {
                getContext().startActivity(intent);
            }
            catch(ActivityNotFoundException e)
            {
                Toast.makeText(getContext(), "No Application available to view pdf", Toast.LENGTH_LONG).show();
            }
        }
        else
        {
            Toast.makeText(getContext(),"nothing found",Toast.LENGTH_LONG).show();
        }

    }

}
