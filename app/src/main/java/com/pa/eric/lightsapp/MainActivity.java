package com.pa.eric.lightsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.io.File;


public class MainActivity extends ActionBarActivity {

    Button playSetlist;
    Button playSong;
    Button learnSetlist;
    Button learnSong;
    Button deleteSetlist;
    Button deleteSong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        playSetlist = (Button)findViewById(R.id.playSetlistButton);
        playSong = (Button)findViewById(R.id.playSongButton);
        learnSetlist = (Button)findViewById(R.id.learnSetlistButton);
        learnSong = (Button)findViewById(R.id.learnSongButton);
        deleteSetlist = (Button)findViewById(R.id.deleteSetlistButton);
        deleteSong = (Button)findViewById(R.id.deleteSongButton);

        playSetlist.setOnClickListener(new PlaySetlistListener());
        playSong.setOnClickListener(new PlaySongListener());
        learnSetlist.setOnClickListener(new LearnSetlistListener());
        learnSong.setOnClickListener(new LearnSongListener());
        deleteSetlist.setOnClickListener(new DeleteSetlistListener());
        deleteSong.setOnClickListener(new DeleteSongListener());

        // create directories if they do note exist
        String dir = getApplicationInfo().dataDir;
        File f = new File(dir + "/songs");
        f.mkdirs();
        f = new File(dir + "/setlists");
        f.mkdirs();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public class PlaySetlistListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // read data from setlist and play songs
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("reason", 0);
            startActivity(intent);
        }
    }

    public class PlaySongListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // get song from user and play it
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("reason", 1);
            startActivity(intent);
        }
    }

    public class LearnSetlistListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // connect to host computer to get setlist data
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("reason", 2);
            startActivity(intent);
        }
    }

    public class LearnSongListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // connect to host computer to get song data
            Intent intent = new Intent(MainActivity.this, SecondActivity.class);
            intent.putExtra("reason", 3);
            startActivity(intent);
        }
    }

    public class DeleteSetlistListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // delete setlist data
        }
    }

    public class DeleteSongListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            // delete song data
        }
    }
}
