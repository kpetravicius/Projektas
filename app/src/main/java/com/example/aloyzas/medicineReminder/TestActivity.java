package com.example.aloyzas.medicineReminder;

/**
 * Created by Aloyzas on 2017-02-27.
 */


import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        TextView text = new TextView(this);
        text.setText("Test activity");
        setContentView(text);

    }
}

