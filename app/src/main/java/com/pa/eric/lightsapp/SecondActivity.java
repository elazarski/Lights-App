package com.pa.eric.lightsapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


public class SecondActivity extends ActionBarActivity implements GotIp,SongFragment.OnFragmentInteractionListener {

    IpFragment ipFragment;
    int reason;
  //  static public final int SENT = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        Intent sender = getIntent();
        reason = sender.getIntExtra("reason", 0);

        ipFragment = new IpFragment();
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container, ipFragment, "fragment_second");
        fragmentTransaction.commit();
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
            RetrieveEvent retrieveEvent = new RetrieveEvent(getApplicationContext());
            retrieveEvent.execute(ipAddr);
        } else { // learning setlist or song
            SongFragment songFragment = SongFragment.newInstance(new Integer(reason).toString(), ipAddr);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.add(R.id.container, songFragment, "fragment_song_list");
            fragmentTransaction.commit();
        }
    }

    @Override
    public void onFragmentInteraction(String id) {

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
