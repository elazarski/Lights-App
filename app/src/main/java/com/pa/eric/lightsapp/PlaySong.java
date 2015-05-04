package com.pa.eric.lightsapp;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.util.Xml;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link PlaySong.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link PlaySong#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PlaySong extends Fragment {

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String FILEPATH = "filePath";

    private String filePath;

    private static Song song;

    private static OnFragmentInteractionListener mListener;

    TextView songName;
    TextView bpm;
    TextView mood;
    private static TextView cueList;
    Button nextCue;

    public static Queue<MidiEvent> queue = new LinkedList<MidiEvent>();

    private static Cue finalCue;


    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param filePath Parameter 1.
     *
     * @return A new instance of fragment PlaySong.
     */

    public static PlaySong newInstance(String filePath) {
        PlaySong fragment = new PlaySong();
        Bundle args = new Bundle();
        args.putString(FILEPATH, filePath);
        fragment.setArguments(args);
        return fragment;
    }

    public PlaySong() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            filePath = getArguments().getString(FILEPATH);
        }

        // load song
        // We don't use namespaces
        String ns = null;
        String name = "";
        String mood = "";
        int bpm = 0;
        ArrayList<Cue> cues = new ArrayList<Cue>();

        try {
            BufferedInputStream in = new BufferedInputStream(new FileInputStream(filePath));

            // code found at: http://developer.android.com/training/basics/network-ops/xml.html
            XmlPullParser pullParser = Xml.newPullParser();
            pullParser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            pullParser.setInput(in, "ASCII");
            pullParser.nextTag();

            pullParser.require(XmlPullParser.START_TAG, ns, "song");
            while (pullParser.next() != XmlPullParser.END_DOCUMENT) {
                if (pullParser.getEventType() != XmlPullParser.START_TAG) continue;

                String nodeName = pullParser.getName();

                if (nodeName.equals("title")) name = pullParser.nextText();
                else if (nodeName.equals("bpm")) bpm = Integer.parseInt(pullParser.nextText());
                else if (nodeName.equals("mood")) mood = pullParser.nextText();
                else if (nodeName.equals("cue")) {
                    pullParser.next(); // cue
                    pullParser.next(); // get note
                    char num = (char)Integer.parseInt(pullParser.nextText());
                    pullParser.next(); // get String
                    pullParser.next();
                    String cue = pullParser.nextText();
                    cues.add(new Cue(num, cue));
                }
            }

            in.close();
            song = new Song(name, bpm, mood, cues);

        } catch (Exception e) {
            Log.e("ERROR: ", e.toString());
        }

        finalCue = song.getFinalCue();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_play_song, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        songName = (TextView)getActivity().findViewById(R.id.songNameTextView);
        bpm = (TextView)getActivity().findViewById(R.id.bpmTextView);
        mood = (TextView)getActivity().findViewById(R.id.moodTextView);
        cueList = (TextView)getActivity().findViewById(R.id.cueListTextView);
        nextCue = (Button)getActivity().findViewById(R.id.nextButton);

        songName.setText(song.getName());
        bpm.setText("BPM: " + Integer.toString(song.getBpm()));
        mood.setText(song.getMood());

        cueList.setText(song.getFirstCue().toString());

        cueList.setMovementMethod(new ScrollingMovementMethod());


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
        public void onFragmentInteraction();
    }

    public static void ParseEvent(Context context) {
        MidiEvent event = queue.poll();

        Cue cue = song.findCue(event.getNum());

        if (cue.getNum() == finalCue.getNum()) {
            mListener.onFragmentInteraction();
        } else {

            cueList.setText(cue.toString());

            // code found at: http://stackoverflow.com/questions/3506696/auto-scrolling-textview-in-android-to-bring-text-into-view
            int scrollAmount = cueList.getLayout().getLineTop(cueList.getLineCount()) - cueList.getHeight();

            if (scrollAmount > 0)
                cueList.scrollTo(0, scrollAmount);
            else
                cueList.scrollTo(0, 0);
        }
    }
}
