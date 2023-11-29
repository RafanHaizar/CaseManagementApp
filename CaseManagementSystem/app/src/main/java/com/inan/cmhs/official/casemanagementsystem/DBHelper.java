package com.inan.cmhs.official.casemanagementsystem;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TableName = "Cases";
    public static final String TableNameDRP = "Drp";
    public static final String SerialNo = "sno";
    public static final String CaseNo = "cno";
    public static final String Date = "dt";
    public static final String Location = "lk";
    public static final String Image = "img";
    public static final String Name = "nk";
    public static final String DRP = "drp";
    public static final String FName = "fnk";
    public static final String MName = "mnk";
    public static final String Address = "adk";
    public static final String Phone = "phk";
    public static final String Create = "CREATE TABLE " + TableName + " (" + SerialNo + " INTEGER PRIMARY KEY AUTOINCREMENT," + CaseNo + " VARCHAR,"+ Image + " BLOB," + Date + " VARCHAR," + Location + " TEXT," + Name + " TEXT," + FName + " TEXT," + MName + " TEXT," + Address + " VARCHAR," + Phone + " VARCHAR);";
    public static final String Get = "SELECT * FROM " + TableName;
    public static final String CreateDRP = "CREATE TABLE " + TableNameDRP + " (" + DRP + " TEXT );";
    public static final String GetDRP = "SELECT * FROM " + TableNameDRP;
    public static final String DeleteDRP = "DROP TABLE IF EXISTS " + TableNameDRP;
    public static final String Get_SNo = "SELECT * FROM " + TableName + " WHERE "+SerialNo+" =?";
    public static final String Delete = "DROP TABLE IF EXISTS " + TableName;

    public DBHelper(@Nullable Context context) {
        super(context, "CaseFiles.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(Create);
        db.execSQL(CreateDRP);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        try {
            db.execSQL(Delete);
            db.execSQL(DeleteDRP);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    public long addCase(byte[] bytes,String caseNo, String date, String location, String name, String fName, String mName, String address, String mobile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Image, bytes);
        contentValues.put(CaseNo, caseNo);
        contentValues.put(Date, date);
        contentValues.put(Location, location);
        contentValues.put(Name, name);
        contentValues.put(FName, fName);
        contentValues.put(MName, mName);
        contentValues.put(Address, address);
        contentValues.put(Phone, mobile);
            return sqLiteDatabase.insert(TableName, null, contentValues);

    }
    public long addDRP(String data){
        SQLiteDatabase sqLiteDatabase=this.getWritableDatabase();
        ContentValues contentValues=new ContentValues();
        contentValues.put(DRP,data);
            return sqLiteDatabase.insert(TableNameDRP,null,contentValues);
    }
    Cursor getDRP(){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery(GetDRP ,null);
    }
    Cursor cursor_case(String Loc){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.query(TableName,null,Location+"=?",new String[]{Loc},null,null,SerialNo);
    }

    Cursor cursor_sno(long sno){
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        return sqLiteDatabase.rawQuery(Get_SNo ,new String[]{String.valueOf(sno)});
    }
    public long editCase(byte[] bytes,long position,String caseNo, String date, String location, String name, String fName, String mName, String address, String mobile) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Image, bytes);
        contentValues.put(CaseNo, caseNo);
        contentValues.put(Date, date);
        contentValues.put(Location, location);
        contentValues.put(Name, name);
        contentValues.put(FName, fName);
        contentValues.put(MName, mName);
        contentValues.put(Address, address);
        contentValues.put(Phone, mobile);
        try {
            return sqLiteDatabase.update(TableName, contentValues,SerialNo+"=?",new String[]{String.valueOf(position)});
        } catch (Exception e) {
            System.out.println(e.getCause()+" : "+e.getMessage()+" : "+e.getLocalizedMessage());
            return -1;
        }
    }

    long delete(long Loc){
            SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
            return sqLiteDatabase.delete(TableName,SerialNo+"=?",new String[]{String.valueOf(Loc)});
    }



    public Boolean verifyData(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + DBHelper.TableName + " WHERE " + CaseNo + " =?";
        Cursor res = db.rawQuery(query, new String[]{id});
        if (res.moveToFirst()){
            res.close();
            return true;
        }
        res.close();
        return false;
    }
}
