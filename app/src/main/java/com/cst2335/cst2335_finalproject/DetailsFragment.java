package com.cst2335.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    int position;
    String eventName;
    String eventURL;

    //Bundle args;
    TextView showName;
    TextView showURL;
    Button btnHide;
    DetailsFragment df;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getArguments from Bundle of ChartRoom

        position = getArguments().getInt("position", 0);
        eventName = getArguments().getString("eventName", "");
        eventURL = getArguments().getString("eventUrl", "");

    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,
                             Bundle savedInstanceState) {

        // Inflate the xml file for the fragment
        return inflater.inflate(R.layout.fragment_details, parent, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {

        //getArgument from CharRoom to Fragment


        showName = view.findViewById(R.id.textView);
        showName.setText("Event Name: " + eventName);
        showURL = view.findViewById(R.id.textView3);
        showURL.setText("Details Information at: " + eventURL);


        /*
        //click Hide button to remove the transaction
        btnHide = view.findViewById(R.id.hide);
        FragmentManager fm = getFragmentManager();
        btnHide.setOnClickListener( click -> fm.beginTransaction().remove(this).commit());
        */
        }

}