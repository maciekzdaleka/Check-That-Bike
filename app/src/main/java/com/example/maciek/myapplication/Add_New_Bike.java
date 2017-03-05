package com.example.maciek.myapplication;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Add_New_Bike extends AppCompatActivity {


    String username;
    EditText bike_name, bike_make, bike_model, bike_number, bike_des;
    Button add_image, back, add_bike;
    ImageView imageView;


    final int REQUEST_CODE_GALLERY = 999;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__new__bike);
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            username = bundle.getString("Name");
        }

        back = (Button) findViewById(R.id.back);
        add_image = (Button) findViewById(R.id.addimage);
        add_bike = (Button) findViewById(R.id.addbike);

        bike_name = (EditText) findViewById(R.id.bike_name);
        bike_make = (EditText) findViewById(R.id.make);
        bike_model = (EditText) findViewById(R.id.model);
        bike_number = (EditText) findViewById(R.id.frame_no);
        bike_des = (EditText) findViewById(R.id.description);

        imageView = (ImageView) findViewById(R.id.bikeimage);


        back.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent k = new Intent(Add_New_Bike.this, View_My_Bikes.class);
                k.putExtra("Name", username.toString());
                startActivity(k);
                finish();
            }
        });

        add_image.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                ActivityCompat.requestPermissions(
                        Add_New_Bike.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_GALLERY
                );
            }
        });

        add_bike.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                new Thread(new Runnable() {

                    public void run() {
                        insert();
                    }

                }).start();
            }

            protected void insert() {

                try {
                    String user = username.toString().toLowerCase();
                    String name = bike_name.getText().toString();
                    String make = bike_make.getText().toString();
                    String model = bike_model.getText().toString();
                    String no = bike_number.getText().toString();
                    String des = bike_des.getText().toString();
                    byte[] image = imageViewToByte(imageView);
                    PreparedStatement st2 = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url, "maciek", "maciek93");


                    String inss = "insert into user_bikes values (NULL,?,?,?,?,?,?,?,?,?,NULL)";
                    st2 = c.prepareStatement(inss);
                    st2.setString(1, name);
                    st2.setString(2, user);
                    st2.setString(3, make);
                    st2.setBytes(4,image);
                    st2.setString(5, model);
                    st2.setString(6, no);
                    st2.setString(7, des);
                    st2.setString(8, "no");
                    st2.setString(9, " ");
                    st2.execute();
                    st2.close();


                    runOnUiThread(new Runnable() {
                        public void run() {
                            bike_name.setText("");
                            bike_make.setText("");
                            bike_model.setText("");
                            bike_number.setText("");
                            bike_des.setText("");
                            imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                            Toast.makeText(getBaseContext(), "Bike has been added to the database! ", Toast.LENGTH_LONG).show();
                        }
                    });


                    st2.close();
                    c.close();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });

    }

    private byte[] imageViewToByte(ImageView image) {
        Bitmap bitmap = ((BitmapDrawable) image.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == REQUEST_CODE_GALLERY) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, REQUEST_CODE_GALLERY);
            } else {
                Toast.makeText(getApplicationContext(), "You don't have permission to access file location!", Toast.LENGTH_SHORT).show();
            }
            return;
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == REQUEST_CODE_GALLERY && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();

            try {
                InputStream inputStream = getContentResolver().openInputStream(uri);

                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Add_New_Bike.this, View_My_Bikes.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }


}
