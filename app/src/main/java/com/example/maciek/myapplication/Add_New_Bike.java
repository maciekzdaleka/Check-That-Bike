package com.example.maciek.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;


import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class Add_New_Bike extends AppCompatActivity implements AdapterView.OnItemSelectedListener {


    String username, bike_type2;
    EditText bike_name, bike_make, bike_model, bike_number, bike_des;
    Button add_image, back, add_bike;
    ImageView imageView;
    Spinner spinner;
    File userbike;
    Uri currImageURI;
    private ProgressBar spin;

    private static final String API_KEY = "NBG84pCQp7EYgTe_4ML9Jg";

    static final HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
    static final JsonFactory JSON_FACTORY = new JacksonFactory();

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

        spinner = (Spinner) findViewById(R.id.spinner2);
        spinner.setOnItemSelectedListener(this);

        spin=(ProgressBar)findViewById(R.id.progressBar);
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

        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, bike_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);


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
                spin.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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


                    String inss = "insert into user_bikes values (NULL,?,?,?,?,?,?,?,?,NULL,NULL,NULL,?)";
                    st2 = c.prepareStatement(inss);
                    st2.setString(1, name);
                    st2.setString(2, user);
                    st2.setString(3, make);
                    st2.setBytes(4,image);
                    st2.setString(5, model);
                    st2.setString(6, no);
                    st2.setString(7, des);
                    st2.setString(8, "no");
                    st2.setString(9,bike_type2);
                    st2.execute();
                    st2.close();


                    runOnUiThread(new Runnable() {
                        public void run() {
                            spin.setVisibility(View.GONE);
                            bike_name.setText("");
                            bike_make.setText("");
                            bike_model.setText("");
                            bike_number.setText("");
                            bike_des.setText("");
                            imageView.setImageResource(android.R.drawable.ic_menu_report_image);
                            spinner.setSelection(0);
                            Toast.makeText(getBaseContext(), "Bike has been added to the database! ", Toast.LENGTH_LONG).show();
                            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
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
                userbike = new File(getPath(uri));
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                imageView.setImageBitmap(bitmap);
                spin.setVisibility(View.VISIBLE);
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                        WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        try  {

                            CSApi api = new CSApi(
                                    HTTP_TRANSPORT,
                                    JSON_FACTORY,
                                    API_KEY
                            );
                            CSPostConfig imageToPost = CSPostConfig.newBuilder()
                                    .withImage(userbike)
                                    .build();

                            CSPostResult portResult = null;

                            portResult = api.postImage(imageToPost);

                            Thread.sleep(30000);

                            CSGetResult scoredResult = null;

                            scoredResult = api.getImage(portResult);
                            String z = scoredResult.toString();

                            runOnUiThread(new Runnable() {
                                public void run() {
                                    try {
                                        JSONObject jsonObject = new JSONObject(z);
                                        spin.setVisibility(View.GONE);
                                        String bikedes = jsonObject.getString("name");
                                        Toast.makeText(getBaseContext(), bikedes, Toast.LENGTH_LONG).show();
                                        bike_des.setText(bikedes);
                                        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }
                            });


                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }).start();

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index =             cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
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
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String item = parent.getItemAtPosition(position).toString();
        if(item.equals("Select Bike Type"))
        {
            bike_type2 = "unknown";
        }
        else
        {
            bike_type2 = parent.getItemAtPosition(position).toString();
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        bike_type2 = "unknown";
    }


}
