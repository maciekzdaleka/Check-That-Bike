package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
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

public class Check_Adverts extends AppCompatActivity  {

    String username;
    EditText search_f;
    Button search_button;

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

        listView = (ListView) findViewById(R.id.listview3);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this,R.layout.bike_items,list);
        listView.setAdapter(adapter);
        //listView.setOnItemClickListener(this);


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
                            PreparedStatement st = null;
                            Class.forName("com.mysql.jdbc.Driver");
                            String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                            Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                            String sql = "select bike_id, title, image_link, advert_link, bike_type from scrapedbikes where title LIKE ?;";
                            st = c.prepareStatement(sql);
                            st.setString(1, "%" + search_text  + "%");
                            ResultSet rs = st.executeQuery();


                            while(rs.next())
                            {
                                int id = rs.getInt("bike_id");
                                String title = rs.getString("title");
                                URL image_link = rs.getURL("image_link");
                                String advert_link = rs.getString("advert_link");
                                String biketype = rs.getString("bike_type");
                                String imagee = " ";
                                String des = " ";
                                byte[] image = downloadUrl(image_link);
                                list.add(new Bike(id, title, imagee, advert_link, biketype, des, image));
                            }
                            runOnUiThread(new Runnable() {
                                public void run() {
                                    adapter.notifyDataSetChanged();
                                    Toast.makeText(getBaseContext(), "Searching for: "  + search_text , Toast.LENGTH_LONG).show();
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
                //Intent k = new Intent(Check_Adverts.this, view_advert.class);
                // k.putExtra("Name", username.toString());
                //startActivity(k);
                // finish();
                int itemId = (int) id;
                String selectedFromList = (listView.getItemAtPosition(position).toString());
                Toast.makeText(getBaseContext(),  itemId + " " + selectedFromList, Toast.LENGTH_LONG).show();
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
}
