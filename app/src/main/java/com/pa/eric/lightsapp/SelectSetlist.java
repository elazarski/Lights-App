package com.pa.eric.lightsapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class SelectSetlist extends ListFragment {
    private OnFragmentInteractionListener mListener;

    List<String> fileList = new ArrayList<>();
    ArrayAdapter<String> adapter;
    ListView lv;

    public SelectSetlist() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_select_setlist, container, false);
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // populate fileList
        File f = new File(getActivity().getApplicationInfo().dataDir + "/setlists/");
        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {
            fileList.add(files[i].toString().substring(files[i].toString().lastIndexOf('/') + 1, files[i].toString().length() - 4)); // remove path and '.xml' so it looks nice
        }

        adapter = new ArrayAdapter<String>(getView().getContext(), R.layout.row, fileList);
        setListAdapter(adapter);

        lv = getListView();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String pathToFile = getActivity().getApplicationInfo().dataDir + "/setlists/" + (String)lv.getItemAtPosition(position) + ".txt";
                mListener.onFragmentInteraction(pathToFile);
            }
        });
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
        // TODO: Update argument type and name
        public void onFragmentInteraction(String file);
    }

}
