package com.example.maciek.myapplication;

import android.app.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Environment;

import android.os.Bundle;


import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.google.android.gms.wearable.DataMap.TAG;


public class Reoprt extends Activity {

    String username;
    TextView name,email,number,address,city,county,make,model,frame,type,desc,location,date,info;
    ImageView image1;
    Button generate;
    byte [] pdfimage;
    int id;
    File myFilee;
    private Image bgImage;



    @Override
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



        AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle("What do you want to do? ");
        builder.setPositiveButton("View & Email Report", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                viewPdf();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });


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
                        pdfimage = blob.getBytes(1, blobLength);
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

        new Thread(new Runnable() {

            public void run() {
                try {
                    PreparedStatement st = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url, "maciek", "maciek93");
                    String sql = "select first_name, surname, email, telephone, street,town,county from user where username=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, username);
                    ResultSet rs = st.executeQuery();


                    while (rs.next()) {
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
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }

        }).start();

        generate.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {


                try {
                    createPdf();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (DocumentException e) {
                    e.printStackTrace();
                }

                Toast.makeText(Reoprt.this, "PDF File is Created. Location : " + myFilee, Toast.LENGTH_SHORT).show();

                builder.show();
            }
        });




    }

    private void viewPdf(){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(myFilee), "application/pdf");
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
        startActivity(intent);
    }


    private void emailNote()
    {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_SUBJECT,"PDF");
        email.putExtra(Intent.EXTRA_TEXT, "Report");
        Uri uri = Uri.parse(myFilee.getAbsolutePath());
        email.putExtra(Intent.EXTRA_STREAM, uri);
        email.setType("message/rfc822");
        startActivity(email);
    }

    private void createPdf() throws FileNotFoundException, DocumentException {

        File pdfFolder = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_DOCUMENTS), "pdfdemo");
        if (!pdfFolder.exists()) {
            pdfFolder.mkdir();
        }

        //Create time stamp
        Date datee = new Date() ;
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(datee);

        myFilee = new File(pdfFolder + timeStamp + ".pdf");
        String owner_name = name.getText().toString();
        String owner_email = email.getText().toString();
        String owner_number = number.getText().toString();
        String owner_address = address.getText().toString();
        String owner_town = city.getText().toString();
        String owner_county = county.getText().toString();
        String owner_bike_make = make.getText().toString();
        String owner_bike_model = model.getText().toString();
        String owner_bike_frame = frame.getText().toString();
        String owner_bike_type = type.getText().toString();
        String owner_bike_desc = desc.getText().toString();
        String owner_bike_location = location.getText().toString();
        String owner_bike_date = date.getText().toString();
        String owner_bike_info = info.getText().toString();
        Bitmap bmp = ((BitmapDrawable) image1.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);



        OutputStream output = new FileOutputStream(myFilee);
        Document document = new Document();
        PdfWriter.getInstance(document, output);
        document.open();
        Font normal = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.BOLD);
        Font bold = new Font(Font.FontFamily.TIMES_ROMAN, 12, Font.NORMAL);
        Font title = new Font(Font.FontFamily.TIMES_ROMAN, 30, Font.BOLD
                | Font.UNDERLINE, BaseColor.GRAY);

        Paragraph tittle = new Paragraph();
        tittle.setFont(title);
        tittle.add("Bike Theft Report");
        tittle.setAlignment(Element.ALIGN_CENTER);
        document.add(tittle);

        Paragraph name = new Paragraph();
        name.setFont(bold);
        name.add("\nName: " + owner_name);
        name.add("\nEmail: " + owner_email);
        name.add("\nNumber: " + owner_number);
        name.add("\nAddress: " + owner_address);
        name.add("\nCity: " + owner_town);
        name.add("\nCounty: " + owner_county);
        name.add("\n\n\n\n");
        document.add(name);

        Paragraph bike = new Paragraph();
        bike.setFont(bold);
        bike.add("\n" + owner_bike_make);
        bike.add("\n" + owner_bike_model);
        bike.add("\n" + owner_bike_frame);
        bike.add("\n" + owner_bike_type);
        bike.add("\n" + owner_bike_desc);
        bike.add("\n" + owner_bike_location);
        bike.add("\n" + owner_bike_date);
        bike.add("\n" + owner_bike_info);
        bike.add("\n\n\n\n");
        document.add(bike);
        try {
            Image image = Image.getInstance(stream.toByteArray());
            image.scaleAbsolute(500f, 300f);
            document.add(image);
        } catch (IOException e) {
            e.printStackTrace();
        }



        //Step 5: Close the document
        document.close();
       // Toast.makeText(Reoprt.this, "PDF File is Created. Location : " + myFilee, Toast.LENGTH_SHORT).show();

    }
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(Reoprt.this, Stolen_Bike_Details.class);
            k.putExtra("Name", username.toString());
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }



}
