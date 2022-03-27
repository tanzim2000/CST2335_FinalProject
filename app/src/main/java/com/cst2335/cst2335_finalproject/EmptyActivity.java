/*
 * @(#)DetailsFragment.java Mar 27, 2022
 * Professor: Frank Emanuel
 * CST82335-012 Project
 * Students: Xiaojie Zhao, Shanshu Hong, Jun Fan
 */
package com.cst2335.cst2335_finalproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

/**
 *
 */
public class EmptyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_empty);

        Bundle args = getIntent().getExtras();

        DetailsFragment newFragment = new DetailsFragment();
        newFragment.setArguments(args);

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(R.id.theFrameView, newFragment )
                .commit();
    }
}