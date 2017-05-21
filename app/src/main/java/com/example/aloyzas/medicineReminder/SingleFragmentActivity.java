package com.example.aloyzas.medicineReminder;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by Aloyzas on 2016-12-31.
 */

public abstract class SingleFragmentActivity extends AppCompatActivity{

    protected abstract Fragment createFragment();

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);

        FragmentManager fm = getSupportFragmentManager();
        Fragment fragment = fm.findFragmentById(R.id.fragment_container);

        if (fragment==null){
            fragment = createFragment();
            //Create new fragment transaction,
            //include one add operation in it, and then commit it
            fm.beginTransaction().add(R.id.fragment_container,fragment).commit();
        }
    }
}
