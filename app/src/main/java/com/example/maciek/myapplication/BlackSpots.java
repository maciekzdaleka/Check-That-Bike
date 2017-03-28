package com.example.maciek.myapplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class BlackSpots extends AppCompatActivity implements OnMapReadyCallback {


    String username;
    Button black;
    private GoogleMap mMap;
    static ArrayList<String>  bikesMake = new ArrayList<String>();
    static ArrayList<String>  bikesModel = new ArrayList<String>();
    static ArrayList<String>  bikesFrame = new ArrayList<String>();
    static ArrayList<String>  bikesDate = new ArrayList<String>();
    static ArrayList<String>  bikesType = new ArrayList<String>();
    static ArrayList<String>  bikesInfo = new ArrayList<String>();
    static ArrayList<LatLng>  bikesLoc = new ArrayList<LatLng>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_black_spots);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            username = bundle.getString("Name");
        }

        black = (Button) findViewById(R.id.button4);

        black.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent k = new Intent(BlackSpots.this, BlackSpots.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });

        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select theft_place, make, model, frame_no, theft_date, bike_type, theft_info from user_bikes where stolen=? ";
                    st = c.prepareStatement(sql);
                    st.setString(1, "yes");
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {

                        bikesLoc.add(getLocationFromAddress(rs.getString("theft_place")));
                        bikesType.add(rs.getString("bike_type"));
                        bikesMake.add(rs.getString("make"));
                        bikesModel.add(rs.getString("model"));
                        bikesFrame.add(rs.getString("frame_no"));
                        bikesDate.add(rs.getString("theft_date"));
                        bikesInfo.add(rs.getString("theft_info"));

                    }
                    st.close();
                    c.close();
                }
                catch (ClassNotFoundException e)
                {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }).start();

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {


        for(int i =0; i< bikesType.size();i++)
        {

            double lat = bikesLoc.get(i).latitude;
            double lng = bikesLoc.get(i).longitude;
           // Toast.makeText(getBaseContext(), "Bike has been added to the database! " + lat + " " +lng, Toast.LENGTH_LONG).show();
            googleMap.addMarker(new MarkerOptions().position(new LatLng(lat,lng))
                    .title("Theft Date: " + bikesDate.get(i))
                    .snippet("Bike: " + bikesMake.get(i) + ", " + bikesModel.get(i) + " Type: " + bikesType.get(i) + "\nFrame Number: " + bikesFrame.get(i) + "\nTheft Info: " + bikesInfo.get(i)));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));
            googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

                @Override
                public View getInfoWindow(Marker arg0) {
                    return null;
                }

                @Override
                public View getInfoContents(Marker marker) {

                    Context context = getApplicationContext(); //or getActivity(), YourActivity.this, etc.

                    LinearLayout info = new LinearLayout(context);
                    info.setOrientation(LinearLayout.VERTICAL);

                    TextView title = new TextView(context);
                    title.setTextColor(Color.BLACK);
                    title.setGravity(Gravity.CENTER);
                    title.setTypeface(null, Typeface.BOLD);
                    title.setText(marker.getTitle());

                    TextView snippet = new TextView(context);
                    snippet.setTextColor(Color.GRAY);
                    snippet.setText(marker.getSnippet());

                    info.addView(title);
                    info.addView(snippet);

                    return info;
                }
            });
        }


    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(BlackSpots.this, Menu.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public LatLng getLocationFromAddress(String strAddress) {

        Geocoder coder = new Geocoder(this, Locale.getDefault());
        List<Address> address;
        LatLng p1 = null;

        try {
            address = coder.getFromLocationName(strAddress, 5);
            if (address == null) {
                return null;
            }
            Address location = address.get(0);
            location.getLatitude();
            location.getLongitude();

            p1 = new LatLng(location.getLatitude(), location.getLongitude());

            return p1;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return p1;
    }



}
