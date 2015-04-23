package com.pa.eric.lightsapp;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;

/**
 * Created by eric on 4/4/15.
 */

// original code modified from: https://github.com/codepath/android_guides/wiki/Sending-and-Receiving-Data-with-Sockets
public class TCPClient {
    private static final String TAG = "TCPClient";
    private String ipNumber, incomingMessage;
    BufferedInputStream in;
    PrintWriter out;
    private MessageCallback listener = null;
    private boolean mRun = false;

    static {
        System.loadLibrary("ALSA_Java");
    }

    /**
     * TCPClient class constructor, which is created in AsyncTasks after the button click.
     *
     *
     * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
     * @param listener Callback interface object
     */
    public TCPClient(String ipNumber, MessageCallback listener) {
        this.listener = listener;
        this.ipNumber = ipNumber;
    }

    public native char[] ConvertBytesToArray(byte[] bytes);

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

    // running client. listens for messages
    public void run() {
        mRun = true;

        try {
            // create InetAddress  from ipNumber
            InetAddress serverAddress = InetAddress.getByName(ipNumber);
            Log.d(TAG, "Connecting...");

            Socket socket = new Socket(serverAddress, 40002);
            try {
                // create PrintWriter to write to server
                out = new PrintWriter(new BufferedWriter(new OutputStreamWriter(socket.getOutputStream())), false);

                // create buffered reader to read from server
                in = new BufferedInputStream(socket.getInputStream());

                Log.d(TAG, "In/Out created");

                while (mRun) {

                    byte[] buffer = new byte[32];
                    in.read(buffer);

                    Log.d(TAG, "Recieved message");
                    //byte[] readBytes = new String(buffer).getBytes();
                    char[] returned = ConvertBytesToArray(buffer);
                    if (returned == null) { throw new Exception(); }
                    else {
                        MidiEvent event = new MidiEvent(returned);
                        if (event.getType() == 6) {
                            listener.callbackMessageReciever(event);
                        }
                    }

                }
            } catch (Exception e) {
                Log.e("ERROR: ", e.toString());
            } finally {
                out.flush();
                out.close();
                in.close();
                socket.close();
                Log.d(TAG, "Closed");
            }
        } catch (Exception e) {
            Log.e("Error: ", e.toString());
        }
    }

    public boolean isRunning() {
        return mRun;
    }

    public interface MessageCallback {
        public void callbackMessageReciever(MidiEvent event);
    }

}
