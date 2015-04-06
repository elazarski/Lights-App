package com.pa.eric.lightsapp;

import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.logging.Handler;


/**
 * Created by eric on 4/4/15.
 */

// code from: https://github.com/codepath/android_guides/wiki/Sending-and-Receiving-Data-with-Sockets
public class TCPClient {
    private static final String TAG = "TCPClient";
    private final Handler mHandler;
    private String ipNumber, incomingMessage;
    BufferedReader in;
    PrintWriter out;
    private MessageCallback listener = null;
    private boolean mRun = false;

    /**
     * TCPClient class constructor, which is created in AsyncTasks after the button click.
     * @param mHandler Handler passed as an argument for updating the UI with sent messages
     *
     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
     * @param listener Callback interface object
     */
    public TCPClient(Handler mHandler, String ipNumber, MessageCallback listener) {
        this.listener = listener;
        this.ipNumber = ipNumber;
        this.mHandler = mHandler;
    }

    // send message
    public void sendMessage(String message) {
        if (out != null && !out.checkError()) {
            out.println(message);
            out.flush();
            Log.d(TAG, "Sent message: " + message);
        }
    }

    // stop TCPCLient object
    public void stopClient() {
        Log.d(TAG, "Stopped client");
        mRun = false;
    }

    // running client. listenes for messages
    public void run() {
        mRun = true;

        try {
            // create InetAddress  from ipNumber
            InetAddress serverAddress = InetAddress.getByName(ipNumber);
            Log.d(TAG, "Connecting...");

            Socket socket = new Socket(serverAddress, 40002);
            try {
                // create PrintWriter to write to server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), true);

                // create buffered reader to read from server
                in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                Log.d(TAG, "In/Out created");

                while (mRun) {
                    char[] buffer = new char[128];
                    in.read(buffer);
                    Log.d(TAG, "Recieved message: " + buffer);
                }
            } catch (Exception e) {
                Log.d(TAG, "Error", e);
            } finally {
                out.flush();
                out.close();
                in.close();
                socket.close();
                Log.d(TAG, "Closed");
            }
        } catch (Exception e) {
            Log.d(TAG, "Error", e);
        }
    }

    public boolean isRunning() {
        return mRun;
    }

    public interface MessageCallback {
        public void callbackMessageReciever(String message);
    }

    public native void a();


}
