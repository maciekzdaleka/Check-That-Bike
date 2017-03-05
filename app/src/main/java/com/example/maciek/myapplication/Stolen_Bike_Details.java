package com.example.maciek.myapplication;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Stolen_Bike_Details extends AppCompatActivity {


    TextView b_name, b_make, b_model, b_frame, b_des,b_t_desc;
    ImageView b_image;
    String username;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stolen__bike__details);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            username = bundle.getString("Name");
            id = bundle.getInt("Bike_id");
        }
        b_name = (TextView) findViewById(R.id.bike_name);
        b_make = (TextView) findViewById(R.id.bike_make);
        b_model = (TextView) findViewById(R.id.bike_model);
        b_frame = (TextView) findViewById(R.id.bike_frame);
        b_des = (TextView) findViewById(R.id.description);
        b_image = (ImageView) findViewById(R.id.imageView2);
        b_t_desc = (TextView) findViewById(R.id.t_description);

        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, bike_name, make, image, model, frame_no, description, theft_place from user_bikes where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    st.setInt(2, id);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        int idddd = rs.getInt("bike_id");
                        String name = rs.getString("bike_name");
                        String make = rs.getString("make");
                        Blob blob = rs.getBlob("image");
                        String model = rs.getString("model");
                        String frame = rs.getString("frame_no");
                        String des = rs.getString("description");
                        String t = rs.getString("theft_place");
                        int blobLength = (int) blob.length();
                        byte[] image = blob.getBytes(1, blobLength);
                        blob.free();

                        runOnUiThread(new Runnable() {
                            public void run() {
                                b_name.setText(name);
                                b_make.setText("Make: " + make);
                                b_model.setText("Model: " + model);
                                b_frame.setText("Frame Number: " + frame);
                                b_des.setText("Description: " + des);
                                b_t_desc.setText("Theft Location: " + t);
                                byte [] bikeImage = image;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
                                b_image.setImageBitmap(bitmap);
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

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Stolen_Bike_Details.this, View_My_Stolen_Bikes.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
