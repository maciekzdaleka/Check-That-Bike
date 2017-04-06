package com.example.maciek.myapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
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
    static BikeListAdapter adapter = null;
    private ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_adverts);



        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            username = bundle.getString("Name");
            bike_type2 = bundle.getString("bike_type");

        }
        search_f = (EditText) findViewById(R.id.search_field);
        search_button = (Button) findViewById(R.id.search);
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        listView = (ListView) findViewById(R.id.listview3);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this,R.layout.bike_items,list);
        listView.setAdapter(adapter);
        spin=(ProgressBar)findViewById(R.id.progressBar6);
        spin.setVisibility(View.GONE);

        List<String> bike_type = new ArrayList<String>();
        bike_type.add("Select Bike Type");
        bike_type.add("Mountain Bike");
        bike_type.add("BMX");
        bike_type.add("Electric & Folding");
        bike_type.add("Fixies & Singlespeed");
        bike_type.add("Hybrid");
        bike_type.add("Kids");
        bike_type.add("Ladies");

        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(search_f.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bike_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


        search_button.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                spin.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                String search_text = search_f.getText().toString();
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
                            if(bike_type2.equals("normal"))
                            {
                                String sql = "select bike_id, title, image_link, advert_link, bike_type from scrapedbikes where title LIKE ? ;";
                                st = c.prepareStatement(sql);
                                st.setString(1, "%" + search_text  + "%");
                            }
                            else
                            {
                                String sql = "select bike_id, title, image_link, advert_link, bike_type from scrapedbikes where title LIKE ? and bike_type like ? or title LIKE ? and bike_type like ?;";
                                st = c.prepareStatement(sql);
                                st.setString(1, "%" + search_text  + "%");
                                st.setString(2, "%" + bike_type2  + "%");
                                st.setString(3, "%" + search_text  + "%");
                                st.setString(4, "%" + unknown + "%");
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
                                    spin.setVisibility(View.GONE);
                                    getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                k.putExtra("bike_type",(bike_type2));
                k.putExtra("choice", 1 );
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

    protected void setupParent(View view) {
        //Set up touch listener for non-text box views to hide keyboard.
        if(!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard();
                    return false;
                }
            });
        }
        //If a layout container, iterate over children
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupParent(innerView);
            }
        }
    }

    private void hideSoftKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(this.getCurrentFocus().getWindowToken(), 0);
    }
}
