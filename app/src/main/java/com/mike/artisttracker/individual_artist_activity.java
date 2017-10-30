package com.mike.artisttracker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;


public class individual_artist_activity extends AppCompatActivity {

    public TextView artist_name;
    public TextView artist_info;
    public TextView concert_info;
    public TextView album_info;

    public void init_layout() {

        artist_name = (TextView) findViewById(R.id.artist_name);
        artist_info = (TextView) findViewById(R.id.artist_info);
        concert_info = (TextView) findViewById(R.id.concert_info);
        album_info = (TextView) findViewById(R.id.album_info);

        // call setText Before setContentView ?
        artist_name.setText("");
        artist_info.setText("");
        concert_info.setText("");
        album_info.setText("");

    }

    // Gets artist object from bundle
    // Needs previous activity to pass bundle through intent
    // https://stackoverflow.com/questions/2906925/how-do-i-pass-an-object-from-one-activity-to-another-on-android
    public void get_artist() {
        //need artist object
    }

    public void parse_concert_data() {
        //need artist object
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.individual_artist);

        // call before setContentView?
        get_artist();
        parse_concert_data();
        init_layout();
    }
}