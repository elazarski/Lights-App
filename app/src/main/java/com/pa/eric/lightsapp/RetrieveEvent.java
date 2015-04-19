package com.pa.eric.lightsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

/**
 * Created by swlazarser on 3/24/15.
 */

// code from: https://github.com/codepath/android_guides/wiki/Sending-and-Receiving-Data-with-Sockets
public class RetrieveEvent extends AsyncTask<String, MidiEvent, TCPClient> {

    private final String TAG = "RetrieveEvent";
    private TCPClient tcpClient;
    private Context context;

    // context is used to update UI
    public RetrieveEvent(Context applicationContext) {
        context = applicationContext;
    }

    @Override
    protected TCPClient doInBackground(String... params) {
        Log.d(TAG, "In doInBackground");

        try {
            tcpClient = new TCPClient(params[0],
                    new TCPClient.MessageCallback() {
                        @Override
                        public void callbackMessageReciever(MidiEvent event) {
                            publishProgress(event);
                        }
                    });
        } catch (NullPointerException e) {
            Log.d(TAG, "Caught NullPointerException");
            e.printStackTrace();
        }

        tcpClient.run();
        return null;
    }

    // update UI
    @Override
    protected void onProgressUpdate(MidiEvent... values) {
        MidiEvent event = values[0];
        PlaySong.ParseEvent(event, context);
    }

    @Override
    protected void onPostExecute(TCPClient tcpClient) {
        super.onPostExecute(tcpClient);
        Log.d(TAG, "In onPostExecute");

        if (tcpClient != null && tcpClient.isRunning()) {
            tcpClient.stopClient();
        }
    }
}
