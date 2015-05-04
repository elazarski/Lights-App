package com.pa.eric.lightsapp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

/**
 * Created by eric on 3/19/15.
 */
public class IpFragment extends Fragment {

    Button accept;
    EditText ipEditText;
    GotIp i;
    String ipAddr;

    public IpFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_second, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        accept = (Button)getActivity().findViewById(R.id.acceptButton);
        ipEditText = (EditText)getActivity().findViewById(R.id.ipEditText);

        accept.setOnClickListener(new AcceptListener());

        SharedPreferences sharedPreferences = getActivity().getPreferences(Context.MODE_PRIVATE);
        ipEditText.setText(sharedPreferences.getString("IP", null));
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            i = (GotIp)activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement GotIp");
        }
    }

    private class AcceptListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            ipAddr = ipEditText.getText().toString();
            i.cont(ipAddr);
        }
    }
}
