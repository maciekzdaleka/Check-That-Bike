package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


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
                Toast.makeText(getBaseContext(), "Coming soon !", Toast.LENGTH_SHORT).show();

                //Intent k = new Intent(Menu.this, Garda_Stations.class);
               // k.putExtra("Name", username.toString());
               // startActivity(k);
               // finish();
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
}
