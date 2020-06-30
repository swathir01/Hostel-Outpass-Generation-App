package com.example.hosteloutpassgeneration;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.app.ProgressDialog;
import com.example.hosteloutpassgeneration.GMailSender;

import androidx.fragment.app.Fragment;

public class RegisterFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    DataBaseHelper db;
    EditText name;
    EditText email;
    EditText phnno;
    EditText pno;
    Spinner dept;
    Button reg;
    String dtext;
    public RegisterFragment()
    {

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_register, container, false);
        if (android.os.Build.VERSION.SDK_INT > 9)
        {
            StrictMode.ThreadPolicy policy = new
                    StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        db = new DataBaseHelper(getContext());
        dept = root.findViewById(R.id.dept);
        final ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getContext(),R.array.Department,android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dept.setAdapter(adapter);
        dept.setOnItemSelectedListener(this);
        name = (EditText) root.findViewById(R.id.name);
        email = (EditText) root.findViewById(R.id.emailid);
        phnno = (EditText) root.findViewById(R.id.phnno);
        pno = (EditText) root.findViewById(R.id.parentno);
        reg = (Button) root.findViewById(R.id.register);
        reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(name.getText().toString().isEmpty() || email.getText().toString().isEmpty() || phnno.getText().toString().isEmpty() || pno.getText().toString().isEmpty())
                {
                    reg.setError("Please Enter All fields!!");
                }
                else{
                    String user = name.getText().toString();
                    String eid = email.getText().toString();
                    String phn = phnno.getText().toString();
                    String pnum = pno.getText().toString();
                    String d = dtext;
                    db.insertData(user,eid,phn,pnum,d);
                    String lastid = db.getlastid();
                    sendMessage(user,lastid,eid);
                    name.setText("");
                    email.setText("");
                    phnno.setText("");
                    pno.setText("");
                    dept.setSelection(adapter.getPosition("IT"));
                    Intent intent =  new Intent(getActivity().getApplication(),AdminActivity.class);
                    startActivity(intent);
                    Toast.makeText(getContext(),"Added",Toast.LENGTH_LONG).show();
                    //FragmentManager fm = getSupportFragmentManager();
                    //EmployeesFragment fragment = new EmployeesFragment();
                    //fm.beginTransaction().replace(R.id.addid,fragment).commit();
                }

            }
        });

        return root;
    }
    private void sendMessage(final String nam, final String id, final String ed) {
        final ProgressDialog dialog = new ProgressDialog(getContext());
        dialog.setTitle("Sending Email");
        dialog.setMessage("Please wait");
        dialog.show();
        Thread sender = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    GMailSender sender = new GMailSender("Your EmailId","Your Password");
                    sender.sendMail("Hostel Notification","Hi "+nam+"!\nYour Hosteller ID: "+ id+".","lalithswathi01@gmail.com",ed);
                    dialog.dismiss();
                } catch (Exception e) {
                    Log.e("mylog", "Error: " + e.getMessage());
                }
            }
        });
        sender.start();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        dtext = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
