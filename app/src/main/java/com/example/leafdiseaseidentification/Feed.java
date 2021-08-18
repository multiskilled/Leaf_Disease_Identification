package com.example.leafdiseaseidentification;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Feed extends MainActivity{
    //final static String urlAddress="https://www.urbanorganicgardener.com/feed";
    final static String urlAddress="https://growagoodlife.com/feed/";
    //final static String urlAddress="http://10.0.2.2/galacticnews/index.php/feed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.feed);
//recycler view with crd view reference
        //https://developer.android.com/guide/topics/ui/layout/recyclerview

        //floating buuton
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);

        final RecyclerView rv= (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DataDownloader(Feed.this,urlAddress,rv).execute();
            }
        });


        //Navigations
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);
        //for Feed Selection
        bottomNavigationView.setSelectedItemId(R.id.feed);
        Menu menu=bottomNavigationView.getMenu();
        MenuItem menuItem=menu.getItem(0);
        menuItem.setChecked(true);
        //action for item selected listener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.feed:
                        startActivity(new Intent((getApplicationContext()), Feed.class));
                        finish();
                        return true;
                    case R.id.detect:
                        startActivity(new Intent(getApplicationContext()
                                ,MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.help:
                        startActivity(new Intent(getApplicationContext()
                                ,HelpQueries.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.settings:
                        startActivity(new Intent(getApplicationContext()
                                ,SettingsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }

                return false;
            }
        });



    }
}
