package com.inan.cmhs.official.casemanagementsystem;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.io.ByteArrayOutputStream;
import java.security.AccessController;
import java.util.ArrayList;
import java.util.Arrays;

public class Add_Case extends AppCompatActivity {
    //Items
    Spinner dropdown;
    Button time,image;
    ImageView photo;
    EditText CaseNo,Name,FName,MName,Address,Mobile;
    String giovanni;
    ArrayList<RecyclerItems> recyclerItems=new ArrayList<>();
    ImageView o;
    RecyclerAdapter recyclerAdapter = new RecyclerAdapter(this,recyclerItems,null);
    //Variables

    Button add,Cancel;
    Uri uri;
    String[] Locations;

    MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.datePicker().setTitleText("Select Date Of Crime").setSelection(MaterialDatePicker.todayInUtcMilliseconds()).build();
    //On Create
    @SuppressLint({"MissingInflatedId", "Range","NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_case);
        dropdown=findViewById(R.id.Location);
        DBHelper dbHelper = new DBHelper(this);
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
        dropdown.getSelectedItem();
        time=findViewById(R.id.Time);
        CaseNo=findViewById(R.id.CaseNo);
        Name=findViewById(R.id.Name);
        FName=findViewById(R.id.FName);
        MName=findViewById(R.id.MName);
        Address=findViewById(R.id.Address);
        add=findViewById(R.id.Add);
        Cancel=findViewById(R.id.Cancel);
        Mobile=findViewById(R.id.Phone);
        image=findViewById(R.id.selectimage);
        photo=findViewById(R.id.imageView);
        photo.setVisibility(View.INVISIBLE);
        image.setOnClickListener(v->{
            ImagePicker.with(this)
                    .crop()
                    .compress(1024)
                    .maxResultSize(1080, 1080)
                    .start();
            photo.setVisibility(View.VISIBLE);
        });
        add.setOnClickListener(v->add(giovanni,CaseNo,Name,FName,MName,Address,Mobile));
        time.setOnClickListener(v->{
            materialDatePicker.show(getSupportFragmentManager(),"TAG_PICKER");
            materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
                @Override
                public void onPositiveButtonClick(Object selection) {
                    giovanni=materialDatePicker.getHeaderText();
                    time.setText(giovanni);
                }
            });
        });

        Cancel.setOnClickListener(v->{
            Intent i = new Intent(this,MainActivity.class);
            startActivity(i);
        });
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        uri=data.getData();
        photo.setImageURI(uri);
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void add(String date, EditText caseNo, EditText name, EditText fName, EditText mName, EditText address, EditText mobile) {
            try {
                String CaseNo = caseNo.getText().toString(), Name = name.getText().toString(), FName = fName.getText().toString(), MName = mName.getText().toString(), Address = address.getText().toString(), Mobile = mobile.getText().toString();
                if(FName.equals("")){
                    FName="N/A";
                }
                if(MName.equals("")){
                    MName="N/A";
                }
                if(Address.equals("")){
                    Address="N/A";
                }
                if(Mobile.equals("")){
                    Mobile="N/A";
                }
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), uri);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress((Bitmap.CompressFormat.PNG), 100, stream);
                byte[] byteArray = stream.toByteArray();
                bitmap.recycle();
                if(!date.equals("DD-MM-YYYY")&&(!CaseNo.equals(""))&&(!Name.equals(""))) {
                    DBHelper dbHelper=new DBHelper(this);
                    if (dbHelper.verifyData(CaseNo)) {
                        Toast.makeText(this, "The CaseNo. Already Exists", Toast.LENGTH_SHORT).show();
                    }else {
                        long adds = dbHelper.addCase(byteArray,CaseNo, date, dropdown.getSelectedItem().toString(), Name, FName, MName, Address, Mobile);
                        if(adds!=-1) {
                            Toast.makeText(this, "Added Successfully", Toast.LENGTH_SHORT).show();
                        }else{
                            Toast.makeText(this, "Could Not Add Successfully", Toast.LENGTH_SHORT).show();
                        }
                    }
                }else {
                    Toast.makeText(this, "You haven\'t entered all of the fields!!", Toast.LENGTH_LONG).show();
                }

            } catch (Exception e) {
                Toast.makeText(this, "You haven\'t entered all of the fields!!", Toast.LENGTH_LONG).show();
            }


    }
}