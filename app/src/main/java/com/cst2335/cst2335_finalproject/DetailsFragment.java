package com.cst2335.cst2335_finalproject;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;
import java.io.FileNotFoundException;


/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class DetailsFragment extends Fragment {

    View rootView;

    //private static String imgURL;
    int id;
    int isFavorite;
    String eventName;
    String eventURL;
    String eventDate;
    String imgURL;

    //Bundle args;
    TextView showName;
    TextView showId;
    TextView showURL;
    TextView showDate;
    ImageView showImg;
    CheckBox fvCb;

    SQLiteDatabase eventDB;
    MyOpener myOpenHelper;

    public DetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //getArguments from Bundle of ChartRoom

        assert getArguments() != null;
        id = (int) getArguments().getLong("dbId", 0);
        isFavorite =getArguments().getInt("favorite",0);
        eventName = getArguments().getString("eventName", "");
        eventDate=getArguments().getString("eventDate","");
        eventURL = getArguments().getString("eventUrl", "");
        imgURL = getArguments().getString("imgURL", "");

        //MyOpener opener=new MyOpener(this);

    }
    @Override
    public View onCreateView(LayoutInflater inflater,  ViewGroup parent,
                             Bundle savedInstanceState) {
        // Inflate the xml file for the fragment
        rootView=inflater.inflate(R.layout.fragment_details, parent, false);

        //open the database and create a query;
        myOpenHelper = new MyOpener(rootView.getContext());
        eventDB = myOpenHelper.getWritableDatabase();
            ContentValues favorite = new ContentValues();
            favorite.put(MyOpener.COL_Favorite,1);
            ContentValues nFavorite = new ContentValues();
            favorite.put(MyOpener.COL_Favorite,0);
        fvCb = rootView.findViewById(R.id.fvBt);
        fvCb.setOnCheckedChangeListener((checkView,isChecked) -> {

            if(isChecked) {
                eventDB.update(MyOpener.TABLE_NAME, favorite,"_id=?",
                        new String[]{Long.toString(id)});
              // eventDB.execSQL("UPDATE eventInfoDB SET isFavorite = "
              //        + 1 + "WHERE _id = " + "'" + id + "'");
            } else {
                eventDB.update(MyOpener.TABLE_NAME, nFavorite,"_id=?",
                        new String[]{Long.toString(id)});

               // eventDB.execSQL("UPDATE " + MyOpener.TABLE_NAME + " SET isFavorite = "
               //         + 0 + "WHERE _id = " + "'" + id + "'");
            }

            Snackbar.make(checkView, "checkStatus", Snackbar.LENGTH_LONG)
                    .setAction(getResources().getText(R.string.snb_undo),
                            click -> checkView.setChecked(!isChecked)).show();
        });

        return rootView;
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
        showURL.setText("Details Information at: " + eventURL);

        showId = view.findViewById(R.id.textView4);
        showId.setText("ID in database: " + id);
        }

    public Bitmap getBitmap(String url) {

        try {
            return BitmapFactory.decodeStream(getActivity().openFileInput(url));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

}