package com.example.maciek.myapplication;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Bike_Details extends AppCompatActivity {

    Button back, delete, stolen;
    TextView b_name, b_make, b_model, b_frame, b_des, b_type;
    ImageView b_image;
    String username, des_text;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bike__details);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if(bundle != null)
        {
            username = bundle.getString("Name");
            id = bundle.getInt("Bike_id");
        }

        back = (Button) findViewById(R.id.back);
        delete = (Button) findViewById(R.id.delete);
        stolen = (Button) findViewById(R.id.mark_stolen);
        b_name = (TextView) findViewById(R.id.bike_name);
        b_make = (TextView) findViewById(R.id.bike_make);
        b_model = (TextView) findViewById(R.id.bike_model);
        b_frame = (TextView) findViewById(R.id.bike_frame);
        b_des = (TextView) findViewById(R.id.description);
        b_type = (TextView) findViewById(R.id.stolen_bike_type);
        b_image = (ImageView) findViewById(R.id.imageView2);


        new Thread(new Runnable(){

            public void run()
            {
                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select bike_id, bike_name, make, image, model, frame_no, description , bike_type from user_bikes where username=? and bike_id=?";
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
                        String type = rs.getString("bike_type");
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
                                b_type.setText("Bike Type: "+type);
                                byte [] bikeImage = image;
                                Bitmap bitmap = BitmapFactory.decodeByteArray(bikeImage,0,bikeImage.length);
                                b_image.setImageBitmap(bitmap);
                                Toast.makeText(getBaseContext(), "username + id" + id + " "+ username, Toast.LENGTH_LONG).show();
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

        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent k = new Intent(Bike_Details.this, View_My_Bikes.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent k = new Intent(Bike_Details.this, View_My_Bikes.class);
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
                    st.setInt(2, id);
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

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Theft Description");
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT );
        builder.setView(input);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                des_text = input.getText().toString();
                Intent k = new Intent(Bike_Details.this, View_My_Bikes.class);
                k.putExtra("Name", username.toString());
                new Thread(new Runnable(){

                    public void run()
                    {
                        mark_stolen();
                    }

                }).start();
                startActivity(k);
                finish();
            }
            protected void mark_stolen() {

                try
                {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "update user_bikes set stolen=?, theft_place=? where username=? and bike_id=?";
                    st = c.prepareStatement(sql);
                    st.setString(1,"yes");
                    st.setString(2,des_text);
                    st.setString(3, username);
                    st.setInt(4, id);
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

        stolen.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {

                builder.show();

            }

        });



    }
}
