package com.example.maciek.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;


public class Menu extends AppCompatActivity {

    String username;
    Button view_my_bikes, check_garda, view_stolen,adverts;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            username = bundle.getString("Name");
        }

        view_my_bikes = (Button) findViewById(R.id.viewbikes);
        check_garda = (Button) findViewById(R.id.check_garda);
        view_stolen = (Button) findViewById(R.id.viewstolenbikes);
        adverts = (Button) findViewById(R.id.checkadverts);

        view_my_bikes.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent k = new Intent(Menu.this, View_My_Bikes.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });

        check_garda.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Uri gmmIntentUri = Uri.parse("geo:0,0?q=garda station");
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
            }
        });

        view_stolen.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent k = new Intent(Menu.this, View_My_Stolen_Bikes.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });

        adverts.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent k = new Intent(Menu.this, Check_Adverts.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });



    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Do you want to log out ?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    Intent k = new Intent(Menu.this, Login.class);
                    startActivity(k);
                    finish();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            builder.show();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
