package com.example.hosteloutpassgeneration;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static com.example.hosteloutpassgeneration.R.drawable.reject;

public class FormFragment extends Fragment {
    DataBaseHelper db;
    TableLayout tl;
    FormActivity fm;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_form, container, false);
        db = new DataBaseHelper(getContext());
        fm = new FormActivity();
        //ad = new AddFormFragment();
        Cursor res = db.getforms();
        final int count = res.getCount();
        if (count == 0) {
            Toast.makeText(getContext(), "no data found", Toast.LENGTH_LONG).show();
        } else {
            tl = (TableLayout) root.findViewById(R.id.forms);
            TableRow row1 = new TableRow(getContext());
            TableRow.LayoutParams lp1 = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
            row1.setBackgroundColor(Color.TRANSPARENT);
            row1.setPadding(5, 0, 5, 5);
            row1.setLayoutParams(lp1);
            TextView studid = new TextView(getContext());
            studid.setText(" ID ");
            studid.setTypeface(studid.getTypeface(),Typeface.BOLD);
            studid.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(studid);
            TextView rname = new TextView(getContext());
            rname.setText(" RName ");
            rname.setTypeface(rname.getTypeface(),Typeface.BOLD);
            rname.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(rname);
            TextView reas = new TextView(getContext());
            reas.setText(" Reason ");
            reas.setTypeface(studid.getTypeface(),Typeface.BOLD);
            reas.setTextAppearance(android.R.style.TextAppearance_Medium);
            row1.addView(reas);
            tl.addView(row1);
            if(res.moveToFirst())
            {
                do {
                    final String odate = res.getString(1);
                    final String otime = res.getString(2);
                    final String rson = res.getString(3);
                    final String rsname = res.getString(5);
                    final String rsno = res.getString(6);
                    TableRow row = new TableRow(getContext());
                    TableRow.LayoutParams lp = new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT);
                    row.setBackgroundColor(Color.TRANSPARENT);
                    row.setPadding(5, 0, 5, 5);
                    row.setLayoutParams(lp);
                    TextView sid = new TextView(getContext());
                    sid.setText(res.getString(0));
                    final String id = res.getString(0);
                    sid.setTextAppearance(android.R.style.TextAppearance_Medium);
                    row.addView(sid);
                    TextView rn = new TextView(getContext());
                    rn.setText(res.getString(5));
                    rn.setTextAppearance(android.R.style.TextAppearance_Medium);
                    row.addView(rn);
                    TextView rs = new TextView(getContext());
                    rs.setText(" "+res.getString(3));
                    rs.setTextAppearance(android.R.style.TextAppearance_Medium);
                    row.addView(rs);
                    Button reject = new Button(getContext());
                    reject.setBackground(getResources().getDrawable(R.drawable.reject));
                    reject.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            removeForm(id);
                        }
                    });
                    row.addView(reject);
                    Button accept = new Button(getContext());
                    accept.setBackground(getResources().getDrawable(R.drawable.accept));
                    accept.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            addform(id,odate,otime,rson,rsname,rsno);
                        }
                    });
                    row.addView(accept);
                    tl.addView(row);
                } while (res.moveToNext());
            }
            res.close();
        }
        return root;
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void addform(String id,String lastdate,String lasttime,String reason,String rn,String rno)
    {
        db.updatefrm(id);
        Cursor result = db.getiddata(id);
        result.moveToFirst();
        createPDF(result,lastdate,lasttime,reason,rn,rno);
        boolean valid=db.removepform(id);
        String date = new SimpleDateFormat("dd--MM-yyyy", Locale.getDefault()).format(new Date());
        db.insertnotify(id,date,"Your OutPass is Approved!","1");
        db.notification("1");
        if(valid)
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    public void removeForm(String id)
    {
        boolean valid = db.removepform(id);
        if(valid) {
            //fm.addBadgeView();
            String date = new SimpleDateFormat("dd--MM-yyyy", Locale.getDefault()).format(new Date());
            db.insertnotify(id,date,"Your Form is Rejected!","0");
            Toast.makeText(getContext(), "Rejected", Toast.LENGTH_LONG).show();
            //startActivity(intent);
            db.notification("1");
            getFragmentManager().beginTransaction().detach(this).attach(this).commit();

        }

    }
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPDF(Cursor res,String date,String tym,String reason,String rname,String rno)
    {
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(450, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
        // canvas.drawCircle(50, 50, 30, paint);
        //paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(20);
        canvas.drawText("STUDENT OUT PASS FORM", canvas.getWidth()/2, 40, paint);
        paint.setColor(Color.BLACK);
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTypeface(Typeface.create("Arial",Typeface.BOLD_ITALIC));
        canvas.drawText("NAME",20,100,paint);
        canvas.drawText("EMAIL ID",20,125,paint);
        canvas.drawText("DEPARTMENT",20,150,paint);
        canvas.drawText("OUT DATE",20,175,paint);
        canvas.drawText("OUT TIME",20,200,paint);
        canvas.drawText("REASON",20,225,paint);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setColor(Color.MAGENTA);
        canvas.drawText("RECOMMENDATION",20,290,paint);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD_ITALIC));
        canvas.drawText("FACULTY Name",20,320,paint);
        canvas.drawText("Phone No",20,345,paint);
        paint.setTypeface(Typeface.create("Arial",Typeface.NORMAL));
        canvas.drawText(res.getString(1),canvas.getWidth()/2,100,paint);
        canvas.drawText(res.getString(2),canvas.getWidth()/2,125,paint);
        canvas.drawText(res.getString(5),canvas.getWidth()/2,150,paint);
        canvas.drawText(date,canvas.getWidth()/2,175,paint);
        canvas.drawText(tym,canvas.getWidth()/2,200,paint);
        canvas.drawText(reason,canvas.getWidth()/2,225,paint);
        canvas.drawText(rname,canvas.getWidth()/2,320,paint);
        canvas.drawText(rno,canvas.getWidth()/2,345,paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);

        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(400, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD_ITALIC));
        canvas.drawText("The Above Student is authorized to go out of college for some",20,30,paint);
        canvas.drawText("acceptable reason.",20,50,paint);
        canvas.drawText("AUTHORIZED BY-",280,380,paint);
        Bitmap image = BitmapFactory.decodeResource(getContext().getResources(), R.drawable.icon);
        //the matrix is used to size and position the bitmap on the page
        Bitmap resized = Bitmap.createScaledBitmap(image, 50, 50, true);
        Matrix matrix = new Matrix();
        matrix.preTranslate(290, 400);
        matrix.preScale(2,2);
        //add an image to the page
        canvas.drawBitmap(resized, matrix, null);
        //canvas.drawBitmap(image1, matrix, null);
        document.finishPage(page);
        // write the document content
        String directory_path = Environment.getExternalStorageDirectory().getPath() + "/mypdf/";
        File file = new File(directory_path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String targetPdf = directory_path+"form.pdf";
        File filePath = new File(targetPdf);
        try {
            document.writeTo(new FileOutputStream(filePath));
            Toast.makeText(getContext(), "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(getContext(), "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }

}