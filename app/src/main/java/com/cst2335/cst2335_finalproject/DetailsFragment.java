package com.cst2335.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.net.URL;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    //private static String imgURL;
    int position;
    String eventName;
    String eventURL;
    String eventDate;
    String imgURL;

    //Bundle args;
    TextView showName;
    TextView showURL;
    TextView showDate;
    ImageView showImg;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getArguments from Bundle of ChartRoom

        assert getArguments() != null;
        position = getArguments().getInt("position", 0);
        eventName = getArguments().getString("eventName", "");
        eventDate=getArguments().getString("eventDate","");
        eventURL = getArguments().getString("eventUrl", "");
        imgURL = getArguments().getString("imgURL", "");

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

        showImg = view.findViewById(R.id.imageView);
        showImg.setImageBitmap(getBitmap(imgURL));

        showDate = view.findViewById(R.id.textView2);
        showDate.setText("Event Start Date is: " + eventDate);
        showURL = view.findViewById(R.id.textView3);
        showURL.setText("Details Information at: " + imgURL);

         /*
        //click Hide button to remove the transaction
        btnHide = view.findViewById(R.id.hide);
        FragmentManager fm = getFragmentManager();
        btnHide.setOnClickListener( click -> fm.beginTransaction().remove(this).commit());
        */
        }

    public Bitmap getBitmap(String url) {
        try {
            //InputStream is = new java.net.URL(url).openStream();
            InputStream is = (InputStream) new URL(url).getContent();
            //is.close();
            return BitmapFactory.decodeStream(is);
        } catch (Exception e) {
            return null;
        }
    }

}