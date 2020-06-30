package com.example.hosteloutpassgeneration;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Generation.db";
    private static final int DATABASE_VERSION = 1;

    public DataBaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("DROP TABLE IF EXISTS students");
        db.execSQL("CREATE TABLE IF NOT EXISTS students (studid INTEGER PRIMARY KEY AUTOINCREMENT,name VARCHAR,emailid VARCHAR,phnno VARCHAR,parentno VARCHAR,dept VARCHAR,blackmark VARCHAR,forms VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS register (studentid INTEGER PRIMARY KEY,username VARCHAR,password VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS forms (sid INTEGER,outdate VARCHAR,outtime VARCHAR,reason VARCHAR,isrecom VARCHAR,rname VARCHAR,rno VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS notifications (sid INTEGER,mdate VARCHAR,message VARCHAR(100),isview VARCHAR)");
        db.execSQL("CREATE TABLE IF NOT EXISTS note (val VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }
    public boolean insertData(String user, String email, String phn, String pno, String d)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name",user);
        contentValues.put("emailid",email);
        contentValues.put("phnno",phn);
        contentValues.put("parentno",pno);
        contentValues.put("dept",d);
        contentValues.put("blackmark",0);
        contentValues.put("forms",2);
        long result = db.insert("students",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public String getlastid()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM students ORDER BY studid DESC LIMIT 1",null);
        String str = "";
        if(res.moveToFirst())
            str  =  res.getString(0);
        res.close();
        return str;
    }
    public boolean doesmatch(String id,String user)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM register WHERE studentid = "+id,null);
        res.moveToFirst();
        if(res.getCount()>0) {
            String use = res.getString(1);
            if (use.equals(user))
                return true;
            else
                return false;
        }
        return false;
    }
    public boolean insertnotify(String id, String cdate, String mess,String isv)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sid",id);
        contentValues.put("mdate",cdate);
        contentValues.put("message",mess);
        contentValues.put("isview",isv);
        long result = db.insert("notifications",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public boolean createForm(String id, String date, String time, String reason,String isrecom,String rname,String rno)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("sid",id);
        contentValues.put("outdate",date);
        contentValues.put("outtime",time);
        contentValues.put("reason",reason);
        contentValues.put("isrecom",isrecom);
        contentValues.put("rname",rname);
        contentValues.put("rno",rno);
        long result = db.insert("forms",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public int removeData(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("students","studid = ?",new String[]{id});
        int i =db.delete("register","studentid = ?",new String[]{id});
        return i;
    }
    public boolean updateData(String id, String email, String phn, String pno,String form,String bm)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("studid",id);
        contentValues.put("emailid",email);
        contentValues.put("phnno",phn);
        contentValues.put("parentno",pno);
        contentValues.put("blackmark",bm);
        contentValues.put("forms",form);
        db.update("students",contentValues,"studid = ?",new String[]{id});
        return true;
    }
    /*public boolean updaterecom(String id, String rename, String reno)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("rname",rename);
        contentValues.put("rno",reno);
        contentValues.put("isrecom","1");
        db.update("forms",contentValues,"sid = ?",new String[]{id});
        return true;
    }*/
    public boolean addUser(String sid, String user,String pwd){

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("studentid",sid);
        contentValues.put("username",user);
        contentValues.put("password",pwd);
        long result = db.insert("register",null,contentValues);
        if(result == -1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }
    public boolean checkUser(String username, String password){

        final String TABLE_NAME ="register";
        final String COL_1 ="username";
        final String COL_2 ="password";
        String[] columns = { COL_1 };
        SQLiteDatabase db = getReadableDatabase();
        String selection = COL_1 + "=?" + " and " + COL_2 + "=?";
        String[] selectionArgs = { username, password };
        Cursor cursor = db.query(TABLE_NAME,columns,selection,selectionArgs,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        if(count>0)
            return  true;
        else
            return  false;
    }

    public boolean checkId(String sid)
    {
        String TABLE = "students";
        String COL1 = "studid";
        String[] column = { COL1 };
        SQLiteDatabase db = getReadableDatabase();
        String select = COL1 + "=?";
        String[] selectionArg = { sid };
        Cursor cursor = db.query(TABLE,column,select,selectionArg,null,null,null);
        int count = cursor.getCount();
        cursor.close();
        db.close();
        if(count>0)
            return  true;
        else
            return  false;
    }
    public Cursor getAllData()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM students",null);
        return res;
    }
    public Cursor getiddata(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM students WHERE studid = "+id,null);
        return res;
    }
    public boolean updatefrm(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor res = getiddata(id);
        res.moveToFirst();
        int fms = Integer.parseInt(res.getString(7));
        if(fms!=0) {
            fms = fms - 1;
        }
        String f = String.valueOf(fms);
        contentValues.put("forms",f);
        db.update("students",contentValues,"studid = ?",new String[]{ id });
        return true;
    }
    public Cursor getidnotify(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM notifications WHERE sid= "+id,null);
        //result.moveToFirst();
        return result;
    }
    public String getid(String sname)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM register WHERE username= '"+sname+"'",null);
        result.moveToFirst();
        return result.getString(0);
    }
    public void notification(String val)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("val",val);
        db.insert("note",null,contentValues);
    }
    public int getnote()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM note",null);
        result.moveToFirst();
        int count = result.getCount();
        return count;
    }
    public void deletenote()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM note");
    }
    public boolean upauto()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor result = db.rawQuery("SELECT * FROM students",null);
        if(result.getCount()!=0)
        {
            result.moveToFirst();
            do {
                int frms = 2;
                updateform(frms,result.getString(0));
            }while (result.moveToNext());

        }
        return true;
    }
    public Cursor getforms()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM forms WHERE isrecom = 1",null);
        return res;
    }
    public Cursor getform(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM forms WHERE sid = "+id,null);
        return res;
    }
    public boolean removepform(String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor res = getform(id);
        res.moveToFirst();
        contentValues.put("isrecom","0");
        db.update("forms",contentValues,"sid = ?",new String[]{ id });
        return true;
    }
    public Cursor readforms()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM forms",null);
        return res;
    }
    public boolean checkuser(String snam)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM register WHERE username= '"+snam+"'",null);
        int count = res.getCount();
        if(count>0)
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    public boolean updateform(int frm,String id)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        String formm = String.valueOf(frm);
        contentValues.put("forms",formm);
        db.update("students",contentValues,"studid = ?",new String[]{id});
        return true;
    }
    /*
    public int removeAssign(String pid)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("assignPro","projectid = ?",new String[]{pid});
    }







    public List<String> getAllLabels(){
        List<String> list = new ArrayList<String>();

        // Select All Query
        String selectQuery = "SELECT  * FROM  projects" ;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);//selectQuery,selectedArguments

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                list.add(cursor.getString(0));//adding 1st column data
            } while (cursor.moveToNext());
        }
        // closing connection
        cursor.close();
        db.close();
        // returning lables
        return list;
    }*/
}


