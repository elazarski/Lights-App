package com.pa.eric.lightsapp;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;


public class DeleteActivity extends ListActivity {

    private ArrayAdapter<String> adapter;
    private ArrayList<String> files;
    ListView lv;
    int reason;
    String path;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);

        path = getApplicationInfo().dataDir;

        Intent intent = getIntent();
        reason = intent.getIntExtra("reason", 0);

        lv = getListView();

        if (reason == 4) path = path + "/setlists/";
        else path = path + "/songs/";

        File f[] = new File(path).listFiles();
        for (int i = 0; i < f.length; i++) {
            files.add(f[i].toString());
        }

        adapter = new ArrayAdapter<String>(this, R.layout.row, files);
        setListAdapter(adapter);
    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        File f = new File(path + lv.getItemAtPosition(position));
        f.delete();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_delete, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
