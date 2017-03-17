package com.gajendraprofile.callmask;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;


public class Notification extends ActionBarActivity {

    DatabaseHelper myDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myDb = new DatabaseHelper(this);
        setContentView(R.layout.list);
        populateListView();

        Button btn = (Button)findViewById(R.id.clearDb);
        Button backBtn = (Button)findViewById(R.id.BackBtn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myDb.deleteData();
                populateListView();
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void populateListView(){
        Cursor cursor = myDb.getAllData();
        String[] FieldNames = new String[]{myDb.COL_2, myDb.COL_3};
        int[] toViewIds = new int[]{R.id.item_layout_date, R.id.item_layout_message};
        SimpleCursorAdapter simpleCursorAdapter;
        simpleCursorAdapter = new SimpleCursorAdapter(getBaseContext(),R.layout.item_layout,cursor,FieldNames, toViewIds, 0);
        ListView listView = (ListView)findViewById(R.id.lv);
        listView.setAdapter(simpleCursorAdapter);

    }
}
