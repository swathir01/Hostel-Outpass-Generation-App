package com.example.hosteloutpassgeneration;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {
    DataBaseHelper mydb;
    EditText mid;
    TextView studentid,studmail,studname,studphn,studpno,studdept,studfrm,studbm;
    EditText sphnno;
    EditText sppno;
    EditText frms;
    EditText dhrs;
    Button edit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mydb =new DataBaseHelper(this);
        mid = (EditText)findViewById(R.id.eid);
        sphnno =(EditText)findViewById(R.id.sno);
        sppno = (EditText)findViewById(R.id.spn);
        frms = (EditText)findViewById(R.id.fms);
        dhrs = (EditText)findViewById(R.id.dis);
        edit  = (Button)findViewById(R.id.editbtn);
        studentid = (TextView)findViewById(R.id.sid);
        studmail = (TextView)findViewById(R.id.semail);
        studname = (TextView)findViewById(R.id.sname);
        studphn = (TextView)findViewById(R.id.sphn);
        studpno = (TextView)findViewById(R.id.spno);
        studdept = (TextView)findViewById(R.id.sdept);
        studfrm =  (TextView)findViewById(R.id.sform);
        studbm = (TextView)findViewById(R.id.sbm);
        final String id = getIntent().getStringExtra("stid");
        final Cursor res = mydb.getiddata(id);
        final int count = res.getCount();
        res.moveToFirst();
        if(count == 0)
        {
            Toast.makeText(this,"no data found",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(EditActivity.this,AdminActivity.class);
            startActivity(intent);
        }
        else
        {
           studentid.setText(res.getString(0));
           studmail.setText(res.getString(2));
           studname.setText(res.getString(1));
           studphn.setText(res.getString(3));
           studpno.setText(res.getString(4));
           studdept.setText(res.getString(5));
           studbm.setText(res.getString(6));
           studfrm.setText(res.getString(7));
           mid.setText(res.getString(2));
           sphnno.setText(res.getString(3));
           sppno.setText(res.getString(4));
           frms.setText(res.getString(7));
           dhrs.setText(res.getString(6));
           edit.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   String mail = mid.getText().toString();
                   String phone = sphnno.getText().toString();
                   String parent = sppno.getText().toString();
                   String form = frms.getText().toString();
                   String dishonor = dhrs.getText().toString();
                   String sid = studentid.getText().toString();
                   if(frms.getText().toString().isEmpty()||dhrs.getText().toString().isEmpty())
                   {
                           dhrs.setError("Edit Required fields!!");
                   }
                   else{
                       boolean isupdate = mydb.updateData(sid,mail,phone,parent,form,dishonor);
                       if(isupdate==true) {
                           frms.setText("");
                           dhrs.setText("");
                           mid.setText("");
                           sphnno.setText("");
                           sppno.setText("");
                           Intent intent = new Intent(EditActivity.this, AdminActivity.class);
                           startActivity(intent);
                       }
                   }
               }
           });

        }

    }
}
