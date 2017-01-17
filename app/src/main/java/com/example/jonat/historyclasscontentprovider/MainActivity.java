package com.example.jonat.historyclasscontentprovider;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.facebook.stetho.Stetho;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<ProfileInfo> listProfile;
    ListView lvContacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        listProfile = new ContactRetriever(this).fetchAll();
        lvContacts = (ListView) findViewById(R.id.lvContacts);
        ProfileAdapter adapter = new ProfileAdapter(this, listProfile);
        lvContacts.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
}