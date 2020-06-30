package com.example.hosteloutpassgeneration;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import com.example.hosteloutpassgeneration.R;

public class HomeFragment extends Fragment {
    DataBaseHelper mydb;
    TableLayout tl;
    public HomeFragment() {
        // Required empty public constructor
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        mydb =new DataBaseHelper(getContext());
        final Cursor res = mydb.getAllData();
        final int count = res.getCount();
        if(count == 0)
        {
            Toast.makeText(getContext(),"no data found",Toast.LENGTH_LONG).show();
        }
        else {
            tl = (TableLayout) root.findViewById(R.id.students);
            TableRow row1 = new TableRow(getContext());
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row1.setBackgroundColor(Color.TRANSPARENT);
            row1.setPadding(5,0,5,5);
            row1.setLayoutParams(lp1);
            TextView studid = new TextView(getContext());
            studid.setText(" ID ");
            studid.setTypeface(studid.getTypeface(),Typeface.BOLD);
            studid.setBackgroundResource(R.drawable.cell);
            studid.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(studid);
            TextView stname = new TextView(getContext());
            stname.setText("  Name  ");
            stname.setTypeface(stname.getTypeface(),Typeface.BOLD);
            stname.setBackgroundResource(R.drawable.cell);
            stname.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(stname);
            TextView depart = new TextView(getContext());
            depart.setText(" DEPT  ");
            depart.setTypeface(depart.getTypeface(),Typeface.BOLD);
            depart.setBackgroundResource(R.drawable.cell);
            depart.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(depart);
            TextView dishonor= new TextView(getContext());
            dishonor.setText(" Dishonor ");
            dishonor.setTypeface(dishonor.getTypeface(),Typeface.BOLD);
            dishonor.setBackgroundResource(R.drawable.cell);
            dishonor.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(dishonor);
            TextView frms = new TextView(getContext());
            frms.setText(" Forms ");
            frms.setTypeface(frms.getTypeface(),Typeface.BOLD);
            frms.setBackgroundResource(R.drawable.cell);
            frms.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(frms);
            tl.addView(row1);
            res.moveToFirst();
            do {
                TableRow row = new TableRow(getContext());
                TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                row.setBackgroundColor(Color.TRANSPARENT);
                row.setPadding(5,0,5,5);
                row.setLayoutParams(lp);
                TextView sid = new TextView(getContext());
                sid.setText(res.getString(0));
                final String id = res.getString(0);
                sid.setBackgroundResource(R.drawable.cell);
                sid.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(sid);
                final TextView sname = new TextView(getContext());
                sname.setText(res.getString(1));
                //final String ename = res.getString(1);
                sname.setBackgroundResource(R.drawable.cell);
                sname.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(sname);
                TextView dept = new TextView(getContext());
                dept.setText(res.getString(5));
                dept.setBackgroundResource(R.drawable.cell);
                dept.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(dept);
                TextView bm = new TextView(getContext());
                bm.setText(res.getString(6));
                bm.setBackgroundResource(R.drawable.cell);
                bm.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(bm);
                TextView form = new TextView(getContext());
                form.setText(res.getString(7));
                form.setBackgroundResource(R.drawable.cell);
                form.setTextAppearance(android.R.style.TextAppearance_Medium);
                row.addView(form);
                ImageButton editbtn = new ImageButton(getContext());
                editbtn.setImageResource(R.drawable.edit);
                editbtn.setBackgroundResource(R.drawable.cell_shape);
                editbtn.setScaleType(ImageButton.ScaleType.FIT_XY);
                editbtn.setScaleY(2f);
                editbtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity().getApplication(), EditActivity.class);
                        intent.putExtra("stid",id);
                        startActivity(intent);
                    }
                });
                row.addView(editbtn);
                ImageButton minusBtn = new ImageButton(getContext());
                minusBtn.setImageResource(R.drawable.remove);
                minusBtn.setBackgroundResource(R.drawable.cell_shape);
                minusBtn.setScaleY(2f);
                minusBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onRemove(id);
                    }
                });
                row.addView(minusBtn);
                tl.addView(row);
            }while(res.moveToNext());
        }
        res.close();
        return root;
    }
    public void onRemove(String id)
    {
        //ntent intent = new Intent(getActivity(),EmployeesFragment.class);

        int msg = mydb.removeData(id);
        if(msg>0) {
            Toast.makeText(getContext(), "deleted", Toast.LENGTH_LONG).show();
            //startActivity(intent);
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }
    }
}
