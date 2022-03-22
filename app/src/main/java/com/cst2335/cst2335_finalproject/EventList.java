package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class EventList extends AppCompatActivity {
    private static final String TAG = "EventList";

    private boolean isTablet;
    public ArrayList<Events> eventList = new ArrayList<>(  );
    private MyOpener myOpenHelper;
    private SQLiteDatabase eventDB;
    private MyListAdapter myAdapter;
    private DetailsFragment newFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_list);

        if(findViewById(R.id.theFrameView) == null){
            isTablet=false;
        } else {
            isTablet=true;
        }

        //open the database and create a query;
        myOpenHelper = new MyOpener(this);
        eventDB = myOpenHelper.getReadableDatabase();
        Cursor cursor = eventDB.rawQuery("select * from " +
                MyOpener.TABLE_NAME + ";", null);

        //Convert column names to indices:
        int eventDBId = cursor.getColumnIndex(MyOpener.COL_ID);
        int eventDBName = cursor.getColumnIndex(MyOpener.COL_EventName);
        int eventDBURL = cursor.getColumnIndex(MyOpener.COL_URL);

        //add elements to Arraylist of events
        while (cursor.moveToNext()){
            int id = cursor.getInt(eventDBId);
            String eventName = cursor.getString(eventDBName);
            String eventURL = cursor.getString(eventDBURL);

            eventList.add(new Events(id,eventName,eventURL));
        }

        ListView myList = findViewById(R.id.theListView);
        myList.setAdapter( myAdapter = new MyListAdapter()); //display the ListView to Adapter

        //click an item to open a dialog
        myList.setOnItemClickListener( (listView, view, pos, id) -> {

            //setArguments for the Fragment
            newFragment=new DetailsFragment();
            Bundle args = new Bundle();
            args.putInt("position", pos);
            args.putString("eventName",eventList.get(pos).getEventName());
            args.putString ("eventUrl", eventList.get(pos).getTicketMasterURL());
            newFragment.setArguments(args);

            //create a fragment transaction (if a tablet)
            if (isTablet)  {
                getSupportFragmentManager()
                        .beginTransaction()
                        .setReorderingAllowed(true)
                        .replace(R.id.theFrameView, newFragment)
                        .commit();
            }else{
                //go to Empty if phone and open Fragment if a phone
                Intent goToFrag = new Intent(this ,EmptyActivity.class);
                goToFrag.putExtras(args);
                startActivity(goToFrag);
            }

        });

        //long click one of the event to delete it from ArrayList and DB
        myList.setOnItemLongClickListener( (listView, view, pos, id) -> {
        Events evtChose = eventList.get(pos);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setTitle("Delete the event")

                //prompt message:
                .setMessage("Do you want to delete：" + eventList.get(pos).getEventName()+
                        "\n The selected row is: "+ pos +
                        "\n The database id is: "+id)

                //what the Yes button does:
                .setNegativeButton("No", (click1, arg) -> { })
                .setPositiveButton("Yes", (click2, arg) -> {
                    eventList.remove(pos);
                    myAdapter.notifyDataSetChanged();

                    eventDB.delete(MyOpener.TABLE_NAME,MyOpener.COL_ID+
                            "=?",new String[]{Long.toString(evtChose.getId())});

                //remove the fragment if delete
                FragmentManager fm = getSupportFragmentManager();
                fm.beginTransaction().remove(newFragment).commit();

                })

                .create().show();
        return true;
    });
 }

    // create ListAdapter to implement the lise view
    private class MyListAdapter extends BaseAdapter {
        public int getCount() { Log.i(TAG, "total number of even"+eventList.size());
        return eventList.size();
        }
        public Object getItem(int position) { return eventList.get(position).eventName; }
        public long getItemId(int id) { return (long) id;}
        public View getView(int position, View old, ViewGroup parent){
            LayoutInflater inflater = getLayoutInflater();
            View newView= inflater.inflate(R.layout.row_of_list, parent, false);
            TextView tView = newView.findViewById(R.id.eventLine);
            tView.setText(getItem(position).toString());
            return newView;
        }

    }
}