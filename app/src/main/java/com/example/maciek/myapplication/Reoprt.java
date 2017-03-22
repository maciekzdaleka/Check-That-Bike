package com.example.maciek.myapplication;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.example.maciek.myapplication.R.id.time;

public class Reoprt extends Fragment {

    TextView name,email,number,address,city,county,make,model,frame,type,desc,location,date,info;
    ImageView image1;
    Button generate;
    String username;
    int id;
    View mRootView;

    public Reoprt() {
        // Required empty public constructor
    }

    public static Reoprt newInstance(){
        Reoprt fragment = new Reoprt();
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mRootView = inflater.inflate(R.layout.activity_reoprt, container, false);
        name = (TextView) mRootView.findViewById(R.id.name);
        email = (TextView) mRootView.findViewById(R.id.email);
        number = (TextView) mRootView.findViewById(R.id.phone);
        address = (TextView) mRootView.findViewById(R.id.address);
        city = (TextView) mRootView.findViewById(R.id.city);
        county = (TextView) mRootView.findViewById(R.id.county);
        make = (TextView) mRootView.findViewById(R.id.make);
        model = (TextView) mRootView.findViewById(R.id.model);
        frame = (TextView) mRootView.findViewById(R.id.frame);
        type = (TextView) mRootView.findViewById(R.id.type);
        desc = (TextView) mRootView.findViewById(R.id.description);
        location = (TextView) mRootView.findViewById(R.id.location);
        date = (TextView) mRootView.findViewById(R.id.date);
        info = (TextView) mRootView.findViewById(R.id.info);
        image1 = (ImageView) mRootView.findViewById(R.id.imageView4);
        generate = (Button) mRootView.findViewById(R.id.button5);
        return mRootView;

        class AsyncTaskRunner extends AsyncTask<String, String, String> {

            private String resp;
            ProgressDialog progressDialog;

            @Override
            protected String doInBackground(String... params) {
                publishProgress("Working"); // Calls onProgressUpdate()
                try {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, make, image, model, frame_no, description, theft_place, bike_type , theft_date ,theft_info from user_bikes where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setInt(2, id);
                    ResultSet rs = st.executeQuery();

                    while(rs.next())
                    {
                        int idddd = rs.getInt("bike_id");
                        String makee = rs.getString("make");
                        Blob blob = rs.getBlob("image");
                        String modell = rs.getString("model");
                        String framee = rs.getString("frame_no");
                        String des = rs.getString("description");
                        String t = rs.getString("theft_place");
                        String bt = rs.getString("bike_type");
                        String td = rs.getString("theft_date");
                        String ta = rs.getString("theft_info");
                        int blobLength = (int) blob.length();
                        byte[] image = blob.getBytes(1, blobLength);
                        blob.free();

                    }
                    st.close();
                    c.close();



                } catch (Exception e) {
                    e.printStackTrace();
                    resp = e.getMessage();
                }
                return resp;
            }


            @Override
            protected void onPostExecute(String result) {
                // execution of result of Long time consuming operation
                make.setText("Make: " + makee);
                model.setText("Model: " + modell);
                frame.setText("Frame Number: " + framee);
                desc.setText("Description: " + des);
                location.setText("Theft Location: " + t);
                date.setText("Theft Date: " + td);
                info.setText("Theft Additional Info: " + ta);
                type.setText("Bike Type: "+bt);
                byte [] bikeImage = image;
                Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
                image1.setImageBitmap(bitmap);
                progressDialog.dismiss();

            }


            @Override
            protected void onPreExecute() {

            }


            @Override
            protected void onProgressUpdate(String... text) {


            }
        }

        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, make, image, model, frame_no, description, theft_place, bike_type , theft_date ,theft_info from user_bikes where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setInt(2, id);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        int idddd = rs.getInt("bike_id");
                        String makee = rs.getString("make");
                        Blob blob = rs.getBlob("image");
                        String modell = rs.getString("model");
                        String framee = rs.getString("frame_no");
                        String des = rs.getString("description");
                        String t = rs.getString("theft_place");
                        String bt = rs.getString("bike_type");
                        String td = rs.getString("theft_date");
                        String ta = rs.getString("theft_info");
                        int blobLength = (int) blob.length();
                        byte[] image = blob.getBytes(1, blobLength);
                        blob.free();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                make.setText("Make: " + makee);
                                model.setText("Model: " + modell);
                                frame.setText("Frame Number: " + framee);
                                desc.setText("Description: " + des);
                                location.setText("Theft Location: " + t);
                                date.setText("Theft Date: " + td);
                                info.setText("Theft Additional Info: " + ta);
                                type.setText("Bike Type: "+bt);
                                byte [] bikeImage = image;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
                                image1.setImageBitmap(bitmap);
                            }
                        });


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

    /*@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reoprt);


        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            username = bundle.getString("Name");
            id = bundle.getInt("Bike_id");
        }

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        number = (TextView) findViewById(R.id.phone);
        address = (TextView) findViewById(R.id.address);
        city = (TextView) findViewById(R.id.city);
        county = (TextView) findViewById(R.id.county);
        make = (TextView) findViewById(R.id.make);
        model = (TextView) findViewById(R.id.model);
        frame = (TextView) findViewById(R.id.frame);
        type = (TextView) findViewById(R.id.type);
        desc = (TextView) findViewById(R.id.description);
        location = (TextView) findViewById(R.id.location);
        date = (TextView) findViewById(R.id.date);
        info = (TextView) findViewById(R.id.info);
        image1 = (ImageView) findViewById(R.id.imageView4);
        generate = (Button) findViewById(R.id.button5);

        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, make, image, model, frame_no, description, theft_place, bike_type , theft_date ,theft_info from user_bikes where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setInt(2, id);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        int idddd = rs.getInt("bike_id");
                        String makee = rs.getString("make");
                        Blob blob = rs.getBlob("image");
                        String modell = rs.getString("model");
                        String framee = rs.getString("frame_no");
                        String des = rs.getString("description");
                        String t = rs.getString("theft_place");
                        String bt = rs.getString("bike_type");
                        String td = rs.getString("theft_date");
                        String ta = rs.getString("theft_info");
                        int blobLength = (int) blob.length();
                        byte[] image = blob.getBytes(1, blobLength);
                        blob.free();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                make.setText("Make: " + makee);
                                model.setText("Model: " + modell);
                                frame.setText("Frame Number: " + framee);
                                desc.setText("Description: " + des);
                                location.setText("Theft Location: " + t);
                                date.setText("Theft Date: " + td);
                                info.setText("Theft Additional Info: " + ta);
                                type.setText("Bike Type: "+bt);
                                byte [] bikeImage = image;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
                                image1.setImageBitmap(bitmap);
                            }
                        });


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

        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select first_name, surname, email, telephone, street,town,county from user where username=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        String first = rs.getString("first_name");
                        String surname = rs.getString("surname");
                        String emaill = rs.getString("email");
                        String telephonee = rs.getString("telephone");
                        String street = rs.getString("street");
                        String town = rs.getString("town");
                        String count = rs.getString("county");


                        runOnUiThread(new Runnable() {
                            public void run() {
                                name.setText(first + " " + surname);
                                email.setText(emaill);
                                number.setText(telephonee);
                                address.setText(street);
                                city.setText(town);
                                county.setText(count);
                            }
                        });


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

        }).start();*/



    }
}
