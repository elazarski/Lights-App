package com.pa.eric.lightsapp;

import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by swlazarser on 3/24/15.
 */
public class RetrieveEvent extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... params) {
        try {
            URL u = new URL("http://" + params[0]);

            HttpURLConnection co = (HttpURLConnection)u.openConnection();
            co.setRequestMethod("GET");
            co.setDoOutput(false);
            co.setDoInput(true);
            BufferedReader reader = new BufferedReader(new InputStreamReader(co.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            reader.close();
            co.disconnect();



        } catch (MalformedURLException e) {
            // throw new ConnectException();
        } catch (ConnectException e) {
            //throw e;
        } catch (IOException e) {
            //throw new ConnectException();
        }

        return "a";
    }
}
