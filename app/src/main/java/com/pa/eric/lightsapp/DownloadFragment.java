package com.pa.eric.lightsapp;

import android.app.Activity;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

// code found at: http://androidtutorials-codes.blogspot.com/2013/05/android-app-development-201-2nd.html

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DownloadFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DownloadFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DownloadFragment extends ListFragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String PATH = "path";
    private static final String REASON = "reason";


    private String path;
    private int reason;

    private OnFragmentInteractionListener mListener;

    List<String> songList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    ListView lv;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param urlPath Parameter 1.
     * @param userReason Parameter 2.
     * @return A new instance of fragment DownloadFragment.
     */

    public static DownloadFragment newInstance(String urlPath, int userReason) {
        DownloadFragment fragment = new DownloadFragment();
        Bundle args = new Bundle();
        args.putString(PATH, urlPath);
        args.putInt(REASON, userReason);
        fragment.setArguments(args);
        return fragment;
    }

    public DownloadFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            path = getArguments().getString(PATH);
            reason = getArguments().getInt(REASON);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_download, container, false);

        // Inflate the layout for this fragment
        return v;
    }

    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        new DownloadFileList().execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        public void onFragmentInteraction(Uri uri);
    }

    // method to load data from DownloadFileList into songList
    public void loadContents(ArrayList<String> strings) {
        for (int i = 0; i < strings.size(); i++) {

            String line = strings.get(i);
            // extract required part of string and add to songList
            int beginIndex = line.indexOf("f=") + 3;
            int endIndex = line.indexOf(".xml");

            songList.add(line.substring(beginIndex, endIndex));
        }

        adapter = new ArrayAdapter<String>(getView().getContext(), R.layout.row, songList);
        setListAdapter(adapter);

        // code found at: http://stackoverflow.com/questions/4508979/android-listview-get-selected-item
        lv = (ListView)this.getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String filePath = path + (String)(lv.getItemAtPosition(position)) + ".xml";
                String selectedFromList = (String)(lv.getItemAtPosition(position));
                new DownloadFile().execute(filePath, selectedFromList);
            }
        });

    }

    private class DownloadFileList extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... params) {

            try {
                // declare URL object to be defined below based upon the reason this fragment was launched
                URL url;

                if (reason == 2) { // setlist
                    path = "http://" + path + "/setlists/";
                    url = new URL(path);
                } else { // song
                    path = "http://" + path + "/songs/";
                    url = new URL(path);
                }

                // connect to url and download data where needed
                HttpURLConnection connection = (HttpURLConnection)url.openConnection();
                connection.setRequestMethod("GET");
                connection.setDoOutput(false);
                connection.setDoInput(true);
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                String line;
                ArrayList<String> requiredLines = new ArrayList<String>();
                line = bufferedReader.readLine();
                while (line != null) {

                    // check if current line is needed
                    if (line.contains("[TXT]")) requiredLines.add(line);

                    line = bufferedReader.readLine();
                }

                bufferedReader.close();
                connection.disconnect();

                return requiredLines;
            } catch (Exception e) {
                Log.e("ERROR: ", e.toString());
                return null;
            }
        }

        @Override
        protected void onPostExecute(ArrayList<String> strings) {
            loadContents(strings);
        }
    }

    // downloads a file from the path it receives
    private class DownloadFile extends AsyncTask<String, Void, File> {
        @Override
        protected File doInBackground(String... params) {

            // get filename
            String fileName = params[1] + ".xml";
            String filePath = getActivity().getApplicationContext().getApplicationInfo().dataDir;
            if (params[0].contains("set")) filePath = filePath + "/setlists/";
            else filePath = filePath + "/songs/";

            // code found at: http://stackoverflow.com/questions/15758856/android-how-to-download-file-from-webserver

            try {
                URL url = new URL(params[0]);
                URLConnection connection = url.openConnection();
                connection.connect();

                // download file
                InputStream input = new BufferedInputStream(url.openStream());

                OutputStream output = new FileOutputStream(filePath + fileName, false);

                byte data[] = new byte[1024];

                while (input.read(data) != -1) {
                    output.write(data);
                }

                output.flush();
                output.close();
                input.close();

                File f = new File(filePath + fileName);
                return f;

            } catch (Exception e) {
                Log.e("ERROR: ", e.toString());
            }
            return null;
        }
    }
}
