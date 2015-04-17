package com.pa.eric.lightsapp;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

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
        Toast t = Toast.makeText(context, event.toString(), Toast.LENGTH_SHORT);
        t.show();
    }

    @Override
    protected void onPostExecute(TCPClient tcpClient) {
        super.onPostExecute(tcpClient);
        Log.d(TAG, "In onPostExecute");

        if (tcpClient != null && tcpClient.isRunning()) {
            tcpClient.stopClient();
        }

       // mHandler.sendEmptyMessageDelayed(SecondActivity.SENT, 4000);
    }

    /*
    @Override
    protected Void doInBackground(String... params) {
        /*try {
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

        return null;
    }*/

        /*
        try {
            DatagramSocket datagramSocket = new DatagramSocket();
            byte[] recievedData = new byte[1024];
            while (true) {
                DatagramPacket datagramPacket = new DatagramPacket(recievedData, recievedData.length);
                Log.d("UDP", "S: Recieving...");
                datagramSocket.receive(datagramPacket);
                String recieved = new String(datagramPacket.getData());
                System.out.println(recieved);
                Log.d("Recieved String: ", recieved);
                InetAddress inetAddress = datagramPacket.getAddress();
                int port = datagramPacket.getPort();
                Log.d("IPAddress: ", inetAddress.toString());
                Log.d("Port: ", Integer.toString(port));
            }
        } catch (Exception e) {
            Log.e("Error", e.toString());
        }



        return null;
    }


    private class TCPClient {
        private final String TAG = "TCPClient";
        private final Handler mhandler;
        private String ipNumber, incomingMessage, command;
        BufferedReader in;
        PrintWriter out;
        private MessageCallback listener = null;
        boolean mrun = false;

        /**
         * TCPClient class constructor, which is created in AsyncTasks after the button click.
         * @param mHandler Handler passed as an argument for updating the UI with sent messages
         * @param command  Command passed as an argument, e.g. "shutdown -r" for restarting computer
         * @param ipNumber String retrieved from IpGetter class that is looking for ip number.
         * @param listener Callback interface object
         *
        public TCPClient(Handler mhandler, String command, String ipNumber, MessageCallback listener) {
            this.listener = listener;
            this.ipNumber = ipNumber;
            this.command = command;
            this.mhandler = mhandler;
        }

        /**
         * Public method for sending the message via OutputStream object.
         * @param message Message passed as an argument and sent via OutputStream object.
         *
        public void SendMessage(String message) {
            if (out != null && !out.checkError()) {
                out.println(message);
                out.flush();
                mhandler.sendEmptyMessageDelayed(SecondActivity.SENDING, 1000);
            }
        }
    }

    public interface MessageCallback {
        public void callbackMessageReciever(String message);
    }*/
}
