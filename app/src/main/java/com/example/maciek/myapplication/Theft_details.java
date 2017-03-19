package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;

public class Theft_details extends AppCompatActivity {


    String username,bike_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theft_details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            username = bundle.getString("Name");
            bike_id = bundle.getString("id");
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Theft_details.this, View_My_Bikes.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
