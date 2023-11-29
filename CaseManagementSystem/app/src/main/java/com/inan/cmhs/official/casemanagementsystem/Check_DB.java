package com.inan.cmhs.official.casemanagementsystem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;

public class Check_DB extends AppCompatActivity implements RecyclerAdapter.ItemClickListener {
    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<RecyclerItems> recyclerItems = new ArrayList<>();
    String[] Location;
    Button click;
    DBHelper dbHelper=new DBHelper(this);
    Spinner spinner;
    String[] Locations;
    RecyclerAdapter recyclerAdapter=new RecyclerAdapter(getParent(),recyclerItems, this);
    @SuppressLint({"MissingInflatedId","NewApi"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_db);
        spinner=findViewById(R.id.spinnerfilter);
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
        spinner.setAdapter(adapter);
        click=findViewById(R.id.Check_DB);
        click.setOnClickListener(v->onClick());
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==0){
            edt(item.getGroupId());
        }else{
            dlt(item.getGroupId());
        }
        return super.onContextItemSelected(item);
    }

    private void dlt(int groupId) {
        long checker=dbHelper.delete(recyclerItems.get(groupId).getSerialNo());
        recyclerItems.remove(groupId);
        recyclerAdapter.notifyItemRemoved(groupId);
    }
    private void edt(int groupId) {
        Intent i = new Intent(this,Edit.class);
        i.putExtra("adapterposition",recyclerItems.get(groupId).getSerialNo());
        startActivity(i);
    }

    public void onClick() {
        recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerAdapter=new RecyclerAdapter(this,recyclerItems,this);
        recyclerView.setAdapter(recyclerAdapter);
        recyclerView.setLayoutManager(layoutManager);
        Load();
    }
    @SuppressLint("Range")
    public void Load() {
        Cursor cursor = dbHelper.cursor_case(spinner.getSelectedItem().toString());
        recyclerItems.clear();
        while (cursor.moveToNext()) {
            String CaseNo = cursor.getString(cursor.getColumnIndex(dbHelper.CaseNo));
            String name = cursor.getString(cursor.getColumnIndex(dbHelper.Name));
            long Serial = cursor.getLong(cursor.getColumnIndex(dbHelper.SerialNo));
            recyclerItems.add(new RecyclerItems(CaseNo,name,Serial));

        }
    }


    @Override
    public void onClick(int position) {
        Intent i =new Intent(this,Details.class);
        i.putExtra("adapterposition",position);
        startActivity(i);
    }
}