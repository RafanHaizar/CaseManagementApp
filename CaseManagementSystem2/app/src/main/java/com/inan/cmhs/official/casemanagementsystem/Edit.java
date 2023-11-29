package com.inan.cmhs.official.casemanagementsystem;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class Edit extends AppCompatActivity {
    Spinner dropdown;

    Button time;
    EditText CaseNo,Name,FName,MName,Address,Mobile;
    String giovanni;
    DBHelper dbHelper=new DBHelper(this);
    ArrayList<RecyclerItems> recyclerItems=new ArrayList<>();
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this,recyclerItems,null);
    //Variables
    ImageView photo;
    Button add,Cancel,image;
    Uri uri;

    String[] Locations;
    MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Update Date Of Crime").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();

    @SuppressLint({"MissingInflatedId","NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState){
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_edit);
        time = findViewById(R.id.Timet);
        CaseNo = findViewById(R.id.CaseNot);
        Name = findViewById(R.id.Namet);
        dropdown = findViewById(R.id.Locationt);
        FName = findViewById(R.id.FNamet);
        MName = findViewById(R.id.MNamet);
        Address = findViewById(R.id.Addresst);
        add = findViewById(R.id.Addt);
        Cancel = findViewById(R.id.Cancelt);
        Mobile = findViewById(R.id.Phonet);
        image=findViewById(R.id.selectimaget);
        photo=findViewById(R.id.imageViewt);
        image.setOnClickListener(v->{
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
        });
        Locations = new String[20];
        Cursor cursor1 = dbHelper.getDRP();
        for (int i = 0; i < Locations.length; i++) {
            Locations[i] = "N/A";
        }
        int columnIndex = 0;
        while (cursor1.moveToNext() && columnIndex < Locations.length) {
            String locs = cursor1.getString(0);
            if (locs != null) {
                Locations[columnIndex] = locs;
            } else {
                Locations[columnIndex] = "N/A";
            }
            columnIndex++;
        }
        cursor1.close();
        String[] Painkiller = Arrays.stream(Locations)
                .filter(location -> !location.equals("N/A"))
                .toArray(String[]::new);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, Painkiller);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            dropdown.setAdapter(adapter);
            dropdown.getSelectedItem().toString();
            Intent i = new Intent();
            i = getIntent();
            long position = i.getLongExtra("adapterposition", 0);
            mitali(position);
            time.setOnClickListener(v -> {
                materialDatePicker.show(getSupportFragmentManager(), "TAG_PICKER");
                materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                    @Override
                    public void onPositiveButtonClick(Object selection) {
                        giovanni = materialDatePicker.getHeaderText();
                        time.setText(giovanni);
                    }
                });
            });
            add.setOnClickListener(v -> add(position, giovanni, CaseNo, Name, FName, MName, Address, Mobile));
            Cancel.setOnClickListener(v -> {
                Intent is = new Intent(this, Check_DB.class);
                startActivity(is);
            });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        uri=data.getData();
        photo.setImageURI(uri);
        super.onActivityResult(requestCode, resultCode, data);
    }

    void add(long position, String date, EditText caseNo, EditText name, EditText fName, EditText mName, EditText address, EditText mobile) {
        try {
            String CaseNo = caseNo.getText().toString(), Name = name.getText().toString(), FName = fName.getText().toString(), MName = mName.getText().toString(), Address = address.getText().toString(), Mobile = mobile.getText().toString();
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            bitmap.recycle();
            if((!CaseNo.equals(""))&&(!Name.equals(""))) {
                DBHelper dbHelper=new DBHelper(this);
                    long adds = dbHelper.editCase(byteArray,position,CaseNo, date, dropdown.getSelectedItem().toString(), Name, FName, MName, Address, Mobile);
                    if(adds!=-1) {
                        Toast.makeText(this, "Edited Successfully", Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(this, "Could Not Edit Successfully", Toast.LENGTH_SHORT).show();
                    }
            }else{
                Toast.makeText(this, "You haven\'t entered the Date or Name or the CaseNo", Toast.LENGTH_LONG).show();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress((Bitmap.CompressFormat.PNG), 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @SuppressLint("Range")
    private void mitali(long v) throws CursorIndexOutOfBoundsException {
        Cursor cursor = dbHelper.cursor_sno(v);
        if (cursor.moveToFirst()) {
            do {
                try {
                    byte[] bitmapdata = (cursor.getBlob(cursor.getColumnIndex(DBHelper.Image)));
                    Bitmap bitmap = BitmapFactory.decodeByteArray(bitmapdata, 0, bitmapdata.length);
                    photo.setImageBitmap(bitmap);
                    photo.setImageURI(getImageUri(this, bitmap));
                    Mobile.setText(cursor.getString(cursor.getColumnIndex(DBHelper.Phone)));
                    time.setText(cursor.getString(cursor.getColumnIndex(DBHelper.Date)));
                    CaseNo.setText(cursor.getString(cursor.getColumnIndex(DBHelper.CaseNo)));
                    Name.setText(cursor.getString(cursor.getColumnIndex(DBHelper.Name)));
                    FName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.FName)));
                    MName.setText(cursor.getString(cursor.getColumnIndex(DBHelper.MName)));
                    Address.setText(cursor.getString(cursor.getColumnIndex(DBHelper.Address)));
                }catch (Exception e){
                    e.printStackTrace();
                }
                } while (cursor.moveToNext());
            cursor.close();
        }
    }
}