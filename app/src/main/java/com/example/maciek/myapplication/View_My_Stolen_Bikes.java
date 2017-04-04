package com.example.maciek.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class View_My_Stolen_Bikes extends AppCompatActivity implements AdapterView.OnItemClickListener {

    Integer id2;
    String username,model,make,bike_type,description;
    byte [] bikeimagebyte;
    static ArrayList<Integer> bikesId = new ArrayList<Integer>();
    static ArrayList<String> bikesmodel = new ArrayList<String>();
    static ArrayList<String> bikesmake = new ArrayList<String>();
    static ArrayList<String> bikestype= new ArrayList<String>();
    static ArrayList<String> bikesdes = new ArrayList<String>();
    static ArrayList<byte []> bikesimage= new ArrayList<byte []>();
    ListView listView;
    ArrayList<Bike> list;
    BikeListAdapter adapter = null;
    private ProgressBar spin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view__my__stolen__bikes);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        bikesId.clear();
        bikesmodel.clear();
        bikesmake.clear();
        bikestype.clear();
        bikesimage.clear();
        if(bundle != null)
        {
            username = bundle.getString("Name");
        }
        listView = (ListView) findViewById(R.id.listview);
        list = new ArrayList<>();
        adapter = new BikeListAdapter(this,R.layout.bike_items,list);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(this);
        spin=(ProgressBar)findViewById(R.id.progressBar4);
        spin.setVisibility(View.VISIBLE);

        new Thread(new Runnable(){

            public void run()
            {
                try
                {

                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, bike_name, make, image, model, frame_no, bike_type ,description from user_bikes where username=? and stolen=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setString(2, "yes");
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        int id = rs.getInt("bike_id");
                        String name = rs.getString("bike_name");
                        String make = rs.getString("make");
                        Blob blob = rs.getBlob("image");
                        String model = rs.getString("model");
                        String frame = rs.getString("frame_no");
                        String des = rs.getString("bike_type");
                        int blobLength = (int) blob.length();
                        byte[] image = blob.getBytes(1, blobLength);
                        bikesimage.add(image);
                        bikesmodel.add(rs.getString("model"));
                        bikesmake.add(rs.getString("make"));
                        bikestype.add(rs.getString("bike_type"));
                        bikesId.add(rs.getInt("bike_id"));
                        bikesdes.add(rs.getString("description"));
                        blob.free();
                        list.add(new Bike(id, name, make, model, frame, des, image));
                    }
                    runOnUiThread(new Runnable() {
                        public void run() {
                            adapter.notifyDataSetChanged();
                            spin.setVisibility(View.GONE);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("What do you want to do? ");
        builder.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent k = new Intent(View_My_Stolen_Bikes.this, View_My_Stolen_Bikes.class);
                k.putExtra("Name", username.toString());
                new Thread(new Runnable(){

                    public void run()
                    {
                        delete_bike();
                    }

                }).start();
                startActivity(k);
                finish();
            }
            protected void delete_bike() {

                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "delete from user_bikes where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setInt(2, id2);
                    st.execute();

                    runOnUiThread(new Runnable() {
                        public void run() {
                            Toast.makeText(getBaseContext(), "Bike deleted !", Toast.LENGTH_LONG).show();
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
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.setNeutralButton("Search Advertisements", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent k = new Intent(View_My_Stolen_Bikes.this, Stolen_bike_add_checker.class);
                k.putExtra("Name", username);
                k.putExtra("bike_make", make);
                k.putExtra("bike_model", model);
                k.putExtra("bike_type", bike_type);
                k.putExtra("bike_des", description);
                startActivity(k);
                finish();
            }

        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
        @Override
        public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
            int itemId = (int) id;
            id2 =  bikesId.get(itemId);
            make = bikesmake.get(itemId);
            model = bikesmodel.get(itemId);
            bike_type = bikestype.get(itemId);
            bikeimagebyte = bikesimage.get(itemId);
            description = bikesdes.get(itemId);
            builder.show();
            return true;
        }
        });
    }


    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(View_My_Stolen_Bikes.this, Menu.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent k = new Intent(View_My_Stolen_Bikes.this, Stolen_Bike_Details.class);
        int itemId = (int) id;
        System.out.println(id2 + username);
        k.putExtra("Name", username.toString());
        k.putExtra("Bike_id",(bikesId.get(itemId)));
        startActivity(k);
        finish();
    }
}
