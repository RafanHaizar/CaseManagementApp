package com.inan.cmhs.official.casemanagementsystem;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity {
    CardView add,check,settings;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        add=findViewById(R.id.addcasemain);
        check=findViewById(R.id.checkcasemain);
        settings=findViewById(R.id.dropdowncasemain);
        add.setOnClickListener(v->add());
        check.setOnClickListener(v->check());
        settings.setOnClickListener(v->addDROPER());
    }

    private void check() {
        Intent intent = new Intent(this, Check_DB.class);
        startActivity(intent);
    }

    private void add() {
        Intent i = new Intent(this,Add_Case.class);
        startActivity(i);
    }
    private void addDROPER() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.adddrp, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        DBHelper dbHelper=new DBHelper(this);
        EditText i = view.findViewById(R.id.drpform);
        Button cancel=view.findViewById(R.id.canceldrp);
        Button addD=view.findViewById(R.id.adddrp);
        cancel.setOnClickListener(v->{
            alertDialog.dismiss();
        });
        addD.setOnClickListener(v->{
            if(i.getText().toString()==""|i.getText().toString()==null) {
                Toast.makeText(this,"Give a valid input",Toast.LENGTH_LONG).show();
                alertDialog.dismiss();
            }
            else {
                long j = dbHelper.addDRP(i.getText().toString());
                if (j == -1)
                    Toast.makeText(this, "Error while adding location", Toast.LENGTH_SHORT).show();
                else Toast.makeText(this, "Location added successfully", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
    }
}