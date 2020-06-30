package com.example.hosteloutpassgeneration;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
public class SearchActivity extends AppCompatActivity {
    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private ExampleAdapter mAdapter;
    DataBaseHelper db;
    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        db = new DataBaseHelper(this);
        createExampleList();
        buildRecyclerView();
        EditText editText = findViewById(R.id.edittext);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }
            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
    }
    private void filter(String text) {
        ArrayList<ExampleItem> filteredList = new ArrayList<>();
        for (ExampleItem item : mExampleList) {
            if (item.getText1().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        mAdapter.filterList(filteredList);
    }
    private void createExampleList() {
        mExampleList = new ArrayList<>();
        Cursor result = db.readforms();
        result.moveToFirst();
        mExampleList.add(new ExampleItem("ID"," OutDate  "," Time"," RNo ","RName "));
        if(result.getCount()!=0) {
            do {
                mExampleList.add(new ExampleItem(result.getString(0)," "+result.getString(1), result.getString(2)+"   ", result.getString(6),result.getString(5)));
            } while (result.moveToNext());
            result.close();
        }
    }
    private void buildRecyclerView() {
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }
}
