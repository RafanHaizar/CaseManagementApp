package com.inan.cmhs.official.casemanagementsystem;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

public class Details extends AppCompatActivity {
    TextView SerialNo,CaseNo,dropdown,time,Name,FName,MName,Address,Mobile;
    ImageView photo;
    DBHelper dbHelper=new DBHelper(this);
    @SuppressLint("Range")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Intent i = new Intent();
        i = getIntent();
        long v = i.getIntExtra("adapterposition",0);
        System.out.println(v);
        SerialNo = findViewById(R.id.SNO);
        Mobile = findViewById(R.id.Phne);
        dropdown = findViewById(R.id.Loc);
        time = findViewById(R.id.Date);
        CaseNo = findViewById(R.id.CNo);
        Name = findViewById(R.id.Nme);
        FName = findViewById(R.id.FNme);
        MName = findViewById(R.id.MNme);
        photo=findViewById(R.id.imageView2);
        Address = findViewById(R.id.Addrss);
        mitali(v);
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @SuppressLint("Range")
    private void mitali(long v) throws CursorIndexOutOfBoundsException {
        Cursor cursor = dbHelper.cursor_sno(v);
        if (cursor.moveToFirst()) {
            do {
                try {
                    System.out.println("CHECKPOINT");
                    byte[] bitmapdata = (cursor.getBlob(cursor.getColumnIndex(DBHelper.Image)));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    photo.setImageBitmap(bitmap);
                    photo.setImageURI(getImageUri(this, bitmap));
                }catch (Exception e){
                    e.printStackTrace();
                }
                SerialNo.setText("SerialNo: " + cursor.getString(cursor.getColumnIndex(DBHelper.SerialNo)));
                System.out.println("SerialNo: " + cursor.getString(cursor.getColumnIndex(DBHelper.SerialNo)));
                Mobile.setText("Mobile: " + cursor.getString(cursor.getColumnIndex(DBHelper.Phone)));
                System.out.println("Mobile: " + cursor.getString(cursor.getColumnIndex(DBHelper.Phone)));
                dropdown.setText("Area: " + cursor.getString(cursor.getColumnIndex(DBHelper.Location)));
                System.out.println("Area: " + cursor.getString(cursor.getColumnIndex(DBHelper.Location)));
                time.setText("Date: " + cursor.getString(cursor.getColumnIndex(DBHelper.Date)));
                System.out.println("Date: " + cursor.getString(cursor.getColumnIndex(DBHelper.Date)));
                CaseNo.setText("CaseNo: " + cursor.getString(cursor.getColumnIndex(DBHelper.CaseNo)));
                System.out.println("CaseNo: " + cursor.getString(cursor.getColumnIndex(DBHelper.CaseNo)));
                Name.setText("Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.Name)));
                System.out.println("Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.Name)));
                FName.setText("Father's Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.FName)));
                System.out.println("Father's Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.FName)));
                MName.setText("Mother's Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.MName)));
                System.out.println("Mother's Name: " + cursor.getString(cursor.getColumnIndex(DBHelper.MName)));
                Address.setText("Address: " + cursor.getString(cursor.getColumnIndex(DBHelper.Address)));
            } while (cursor.moveToNext());
            cursor.close();
        }

    }
}