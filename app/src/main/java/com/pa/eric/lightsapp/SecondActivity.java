package com.pa.eric.lightsapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;


public class SecondActivity extends ActionBarActivity implements GotIp,DownloadFragment.OnFragmentInteractionListener,PlaySong.OnFragmentInteractionListener,SelectSong.OnFragmentInteractionListener,SelectSetlist.OnFragmentInteractionListener {

    IpFragment ipFragment;
    DownloadFragment downloadFragment;
    SelectSong selectSong;
    int reason;
    FragmentManager fragmentManager = getSupportFragmentManager();
    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
    SelectSetlist selectSetlist;

    RetrieveEvent retrieveEvent;

    PlaySong playSong;

    ArrayList<String> songs;
    int songIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent sender = getIntent();
        reason = sender.getIntExtra("reason", 0);

        ipFragment = new IpFragment();

        fragmentTransaction.add(R.id.container, ipFragment, "ip_fragment");
        fragmentTransaction.commit();

        songs = new ArrayList<>();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_second, menu);
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

    // continues after getting the IP/Hostname of server based upon the reason this Activity was launched
    public void cont(String ipAddr) {
        // connect to specified IP Address
        if (reason == 0 || reason == 1) { // playing setlist or song

            // starts listening for events
            retrieveEvent = new RetrieveEvent(getApplicationContext());
            retrieveEvent.execute(ipAddr);

            if (reason == 0) { // setlist
                selectSetlist = new SelectSetlist();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, selectSetlist, "select_setlist");
                fragmentTransaction.remove(ipFragment);
                fragmentTransaction.commit();
            } else { // must select single file to load
                selectSong = new SelectSong();
                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, selectSong, "select_song");
                fragmentTransaction.remove(ipFragment);
                fragmentTransaction.commit();
            }
        } else { // learning setlist or song
            downloadFragment = DownloadFragment.newInstance(ipAddr, reason);
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, downloadFragment, "download_fragment");
            fragmentTransaction.remove(ipFragment);
            fragmentTransaction.commit();
        }
    }

    // not used
    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    // method to change view to PlaySong for the first song, works for setlists and songs
    @Override
    public void onFragmentInteraction(String file) {
        if (reason == 0) { //playing setlist

            String songDir = getApplicationInfo().dataDir + "/songs/";

            try {

                File f = new File(file);
                BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
                String line = br.readLine();

                while (line != null) {
                    System.out.println(line);
                    songs.add(line);
                    line = br.readLine();
                }
                br.close();


            } catch (Exception e) {
                Log.e("ERROR: ", e.toString());
            }

            playSong = PlaySong.newInstance(songs.get(0));
            if (playSong == null) System.out.println("playSong is null");
            songIndex++;
        } else { // playing song
            playSong = PlaySong.newInstance(file);
        }

        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, playSong, "play_song");

        if (reason == 0) fragmentTransaction.remove(selectSetlist);
        else fragmentTransaction.remove(selectSong);

        fragmentTransaction.commit();
    }

    // called when song is done
    @Override
    public void onFragmentInteraction() {
        if (reason == 0) {
            if (songIndex < songs.size()) { // load next song if there is another in the setlist
                String filePath = getApplicationInfo().dataDir + "/songs/";
                PlaySong newSong = PlaySong.newInstance(filePath + songs.get(songIndex));
                songIndex++;

                fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.add(R.id.container, newSong, "play_song");
                fragmentTransaction.remove(playSong);
                fragmentTransaction.commit();

                playSong = newSong;
            } else { // setlist is done
                retrieveEvent.cancel(true);
                finish();
            }
        } else {
            retrieveEvent.cancel(true);
            finish();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_second, container, false);
            return rootView;
        }
    }


}
