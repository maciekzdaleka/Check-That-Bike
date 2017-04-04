package com.example.maciek.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class Stolen_bike_add_checker extends AppCompatActivity {

    String username,model,make,bike_type,description;
    byte[] image;
    static ArrayList<String> bikes_links = new ArrayList<String>();
    ListView listView;
    ArrayList<Bike> list;
    static BikeListAdapter adapter = null;
    private ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolen_bike_add_checker);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            username = bundle.getString("Name");
            model = bundle.getString("bike_model");
            make = bundle.getString("bike_make");
            bike_type = bundle.getString("bike_type");
            description = bundle.getString("bike_des");
        }
        listView = (ListView) findViewById(R.id.listview4);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this,R.layout.bike_items,list);
        listView.setAdapter(adapter);
        spin=(ProgressBar)findViewById(R.id.progressBar3);
        spin.setVisibility(View.VISIBLE);

        list.clear();
        bikes_links.clear();
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
                    String sql = "SELECT *, jaro_winkler_similarity(`bike_des`, ?) AS score FROM scrapedbikes where title like ? ORDER BY `score`  DESC LIMIT 10;";
                    st = c.prepareStatement(sql);
                    st.setString(1,  "'" + description + "'");
                    st.setString(2, "%" + make  +  "%");

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
                            spin.setVisibility(View.GONE);
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

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int itemId = (int) id;
                Intent k = new Intent(Stolen_bike_add_checker.this, view_advert.class);
                k.putExtra("Name", username.toString());
                k.putExtra("bike_make", make);
                k.putExtra("bike_model", model);
                k.putExtra("bike_type", bike_type);
                k.putExtra("Bike_AD",(bikes_links.get(itemId)));
                k.putExtra("choice", 2);
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
            Intent k = new Intent(Stolen_bike_add_checker.this, View_My_Stolen_Bikes.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
