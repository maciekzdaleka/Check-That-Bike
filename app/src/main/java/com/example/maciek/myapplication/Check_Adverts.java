package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Check_Adverts extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    String username, bike_type2;
    EditText search_f;
    Button search_button;
    Spinner spinner;
    static ArrayList<String> bikes_links = new ArrayList<String>();

    ListView listView;
    ArrayList<Bike> list;
    BikeListAdapter adapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_adverts);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            username = bundle.getString("Name");
        }
        search_f = (EditText) findViewById(R.id.search_field);
        search_button = (Button) findViewById(R.id.search);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listview3);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this,R.layout.bike_items,list);
        listView.setAdapter(adapter);

        List<String> bike_type = new ArrayList<String>();
        bike_type.add("Select Bike Type");
        bike_type.add("Mountain Bike");
        bike_type.add("BMX");
        bike_type.add("Electric & Folding");
        bike_type.add("Fixies & Singlespeed");
        bike_type.add("Hybrid");
        bike_type.add("Kids");
        bike_type.add("Ladies");

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bike_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        search_button.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                String search_text = search_f.getText().toString();
                list.clear();
                new Thread(new Runnable(){

                    public void run()
                    {
                        try
                        {
                            String unknown = "unknown";
                            PreparedStatement st = null;
                            Class.forName("com.mysql.jdbc.Driver");
                            String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                            Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                            if(bike_type2.equals("normal"))
                            {
                                String sql = "select bike_id, title, image_link, advert_link, bike_type from scrapedbikes where title LIKE ? ;";
                                st = c.prepareStatement(sql);
                                st.setString(1, "%" + search_text  + "%");
                            }
                            else
                            {
                                String sql = "select bike_id, title, image_link, advert_link, bike_type from scrapedbikes where title LIKE ? and bike_type like ? or bike_type ?;";
                                st = c.prepareStatement(sql);
                                st.setString(1, "%" + search_text  + "%");
                                st.setString(2, "%" + bike_type2  + "%");
                                st.setString(3, "%" + unknown  + "%");
                            }
                            ResultSet rs = st.executeQuery();


                            while(rs.next())
                            {
                                int id = rs.getInt("bike_id");
                                String title = rs.getString("title");
                                URL image_link = rs.getURL("image_link");
                                bikes_links.add(rs.getString("advert_link"));
                                //String advert_link = rs.getString("advert_link");
                                String advert_link = (" ");
                                String biketype = rs.getString("bike_type");
                                String imagee = " ";
                                String des = " ";
                                byte[] image = downloadUrl(image_link);
                                list.add(new Bike(id, title, imagee, advert_link, biketype, des, image));
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                }
                            });
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
        });
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = (int) id;
                Intent k = new Intent(Check_Adverts.this, view_advert.class);
                k.putExtra("Name", username.toString());
                k.putExtra("Bike_AD",(bikes_links.get(itemId)));
                startActivity(k);
                finish();
            }

        });


    }
    private byte[] downloadUrl(URL toDownload) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        try {
            byte[] chunk = new byte[4096];
            int bytesRead;
            InputStream stream = toDownload.openStream();

            while ((bytesRead = stream.read(chunk)) > 0) {
                outputStream.write(chunk, 0, bytesRead);
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

        return outputStream.toByteArray();
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Check_Adverts.this, Menu.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Select Bike Type"))
        {
            bike_type2 = "normal";
        }
        else
        {
            bike_type2 = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        bike_type2 = "normal";
    }
}
