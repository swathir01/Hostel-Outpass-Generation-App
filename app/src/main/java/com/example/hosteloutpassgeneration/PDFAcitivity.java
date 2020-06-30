
package com.example.hosteloutpassgeneration;

import android.content.ActivityNotFoundException;
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
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class PDFAcitivity extends AppCompatActivity {

/*    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void createPDF(Cursor res,String date,String tym,String reason,String rname,String rno)
    {
        // create a new document
        PdfDocument document = new PdfDocument();
        // crate a page description
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 1).create();
        // start a page
        PdfDocument.Page page = document.startPage(pageInfo);
        Canvas canvas = page.getCanvas();
        Paint paint = new Paint();
        paint.setColor(Color.RED);
       // canvas.drawCircle(50, 50, 30, paint);
        //paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        canvas.drawText("STUDENT OUT PASS FORM", 120, 50, paint);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create("Arial",Typeface.BOLD_ITALIC));
        canvas.drawText("NAME:",20,60,paint);
        canvas.drawText("EMAIL ID:",20,70,paint);
        canvas.drawText("DEPARTMENT:",20,80,paint);
        canvas.drawText("OUT DATE:",20,90,paint);
        canvas.drawText("OUT TIME:",20,100,paint);
        canvas.drawText("REASON:",20,110,paint);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD));
        paint.setColor(Color.MAGENTA);
        canvas.drawText("RECOMMENDATION",20,120,paint);
        paint.setColor(Color.BLACK);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD_ITALIC));
        canvas.drawText("FACULTY Name:",20,130,paint);
        canvas.drawText("Phone No:",20,140,paint);
        paint.setTypeface(Typeface.create("Arial",Typeface.NORMAL));
        canvas.drawText(res.getString(1),50,60,paint);
        canvas.drawText(res.getString(2),50,70,paint);
        canvas.drawText(res.getString(5),50,80,paint);
        canvas.drawText(date,50,90,paint);
        canvas.drawText(tym,50,100,paint);
        canvas.drawText(reason,50,110,paint);
        canvas.drawText(rname,50,130,paint);
        canvas.drawText(rno,50,140,paint);
        //canvas.drawt
        // finish the page
        document.finishPage(page);
// draw text on the graphics object of the page
        // Create Page 2
        pageInfo = new PdfDocument.PageInfo.Builder(300, 600, 2).create();
        page = document.startPage(pageInfo);
        canvas = page.getCanvas();
        paint = new Paint();
        paint.setColor(Color.RED);
        paint.setTypeface(Typeface.create("Arial", Typeface.BOLD_ITALIC));
        canvas.drawText("The Above Student is authorized to go out of college for some acceptable reason.",20,30,paint);
        canvas.drawText("AUTHORIZATION",150,450,paint);
        Bitmap image = BitmapFactory.decodeResource(getApplicationContext().getResources(), R.drawable.icon);
        //the matrix is used to size and position the bitmap on the page
        Matrix matrix = new Matrix();
        matrix.preTranslate(200, 500);
        matrix.preScale(2, 2);
        //add an image to the page
        canvas.drawBitmap(image, matrix, null);
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
            Toast.makeText(this, "Done", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Log.e("main", "error "+e.toString());
            Toast.makeText(this, "Something wrong: " + e.toString(),  Toast.LENGTH_LONG).show();
        }
        // close the document
        document.close();
    }*/



}
