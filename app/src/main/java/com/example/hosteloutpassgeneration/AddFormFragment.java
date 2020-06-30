package com.example.hosteloutpassgeneration;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddFormFragment extends Fragment {
    DataBaseHelper db;
    EditText studid;
    EditText outday;
    EditText outtym;
    EditText reas;
    DatePickerDialog picker;
   // FormActivity fm;
    Button sub;
    boolean  v;
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View root = inflater.inflate(R.layout.fragment_addform, null);
        db =new DataBaseHelper(getContext());
        final Calendar cldr = Calendar.getInstance();
        //pdf = new PDFAcitivity();
        //fm =  new FormActivity();
        final String nn = getActivity().getIntent().getStringExtra("nam");
        studid =(EditText) root.findViewById(R.id.studentid);
        outday =(EditText)  root.findViewById(R.id.outdate);
        outtym =(EditText) root.findViewById(R.id.outtime);
        reas =(EditText)  root.findViewById(R.id.reason);
        sub = (Button)root.findViewById(R.id.button_register);
        outday.setInputType(InputType.TYPE_NULL);
        outday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int day = cldr.get(Calendar.DAY_OF_MONTH);
                int month = cldr.get(Calendar.MONTH);
                int year = cldr.get(Calendar.YEAR);
                // date picker dialog
                picker = new DatePickerDialog(getContext(),new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) { outday.setText(dayOfMonth + "/" + (monthOfYear + 1) + "/" + year);
                            }
                        }, year, month, day);
                picker.show();
            }
        });
        sub.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("NewApi")
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                final String id = studid.getText().toString();
                boolean match = db.doesmatch(id,nn);
                if(reas.getText().toString().isEmpty())
                {
                    Toast.makeText(getContext(), "Enter All  Fields!", Toast.LENGTH_LONG).show();
                }
                else {
                    if (match) {
                        final Cursor res = db.getiddata(id);
                        res.moveToFirst();
                        int c = res.getCount();
                        if (c == 0) {
                            Toast.makeText(getContext(), "Invalid ID!", Toast.LENGTH_LONG).show();
                            studid.setText("");
                        }
                        String b = res.getString(6);
                        int bm = Integer.parseInt(b);
                        String f = res.getString(7);
                        int frms = Integer.parseInt(f);
                        final String reason = reas.getText().toString();
                        final String lastdate = "" + cldr.get(Calendar.DAY_OF_MONTH) + "/" + (cldr.get(Calendar.MONTH) + 1) + "/" + cldr.get(Calendar.YEAR);
                        final String lasttime = "" + cldr.get(Calendar.HOUR_OF_DAY) + ":" + cldr.get(Calendar.MINUTE);
                        if (bm == 0 && frms > 0) {
                            if (studid.getText().toString().isEmpty() || reas.getText().toString().isEmpty() || outday.getText().toString().isEmpty() || outtym.getText().toString().isEmpty()) {
                                sub.setError("Please Enter All fields!!");
                            } else {

                                db.createForm(id, lastdate, lasttime, reason, "0", "", "");
                                db.updatefrm(id);
                                createPDF(res, lastdate, lasttime, reason, "-", "-");
                                String date = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
                                db.insertnotify(id, date, "Your OutPass Form is Ready!", "1");
                                studid.setText("");
                                reas.setText("");
                                outtym.setText("");
                                outday.setText("");
                                Toast.makeText(getContext(), "Added", Toast.LENGTH_LONG).show();
                                db.notification("1");
                                res.close();
                            }
                        } else {
                            AlertDialog.Builder mBuilder = new AlertDialog.Builder(getContext());
                            View mview = getLayoutInflater().inflate(R.layout.activity_recom, null);
                            final EditText rname = (EditText) mview.findViewById(R.id.rename);
                            final EditText reno = (EditText) mview.findViewById(R.id.rephn);
                            Button sub = (Button) mview.findViewById(R.id.submit);
                            sub.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    if (rname.getText().toString().isEmpty() || reno.getText().toString().isEmpty()) {
                                        Toast.makeText(getContext(), "Must Enter All Fields", Toast.LENGTH_LONG).show();
                                    } else {
                                        boolean valid = db.createForm(id, lastdate, lasttime, reason, "1", rname.getText().toString(), reno.getText().toString());
                                        if (valid) {
                                            Toast.makeText(getContext(), "Your Request is Registered! Please wait for response.", Toast.LENGTH_LONG).show();
                                            rname.setText("");
                                            reno.setText("");
                                            studid.setText("");
                                            reas.setText("");
                                            outtym.setText("");
                                            outday.setText("");
                                        }
                                    }
                                }
                            });
                            mBuilder.setView(mview);
                            AlertDialog dialog = mBuilder.create();
                            dialog.show();
                        }
                    } else {
                        Toast.makeText(getContext(), "ID doesn't match!", Toast.LENGTH_LONG).show();
                        studid.setText("");
                    }
                }

            }
        });

        outtym.setInputType(InputType.TYPE_NULL);
        outtym.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int mHour = cldr.get(Calendar.HOUR_OF_DAY);
                int mMinute = cldr.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                outtym.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
        });
        return root;
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