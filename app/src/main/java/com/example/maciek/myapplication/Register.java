package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Register extends AppCompatActivity {

    Button back,register;
    EditText username , password, password2, email, first_name,surname,phone,street,town,county;
    String u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        password2 = (EditText) findViewById(R.id.password2);
        email = (EditText) findViewById(R.id.email);
        first_name = (EditText) findViewById(R.id.first_name);
        surname = (EditText) findViewById(R.id.surname);
        phone = (EditText) findViewById(R.id.phone);
        street = (EditText) findViewById(R.id.street);
        town = (EditText) findViewById(R.id.town);
        county = (EditText) findViewById(R.id.county);

        back = (Button) findViewById(R.id.back);
        register = (Button) findViewById(R.id.register);

        back.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                Intent k = new Intent(Register.this, Login.class);
                startActivity(k);
                finish();
            }
        });

        register.setOnClickListener(new View.OnClickListener(){

            public void onClick (View v)
            {
                String user = username.getText().toString();
                String pass= password.getText().toString();
                String pass2 = password2.getText().toString();
                String emaill = email.getText().toString();
                String fname= first_name.getText().toString();
                String sur = surname.getText().toString();
                String tele = phone.getText().toString();
                String ulica = street.getText().toString();
                String miasto = town.getText().toString();
                String woje = county.getText().toString();

                //Intent k = new Intent(Register.this, Menu.class);
                //startActivity(k);
                //finish();

                new Thread(new Runnable(){

                    public void run()
                    {
                        insert();
                    }

                }).start();

            }
            protected void insert() {

                try
                {
                    String user = username.getText().toString().toLowerCase();
                    String pass= password.getText().toString();
                    String pass2 = password2.getText().toString();
                    String emaill = email.getText().toString();
                    String fname= first_name.getText().toString();
                    String sur = surname.getText().toString();
                    String tele = phone.getText().toString();
                    String ulica = street.getText().toString();
                    String miasto = town.getText().toString();
                    String woje = county.getText().toString();
                    PreparedStatement st = null;
                    PreparedStatement st2 = null;
                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select username from user where username=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, user);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        u = rs.getString("username");
                    }
                    if(u == null)
                    {
                        u = "hahah";
                    }
                    if(!pass.equals(pass2)&& pass != null)
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), "Passwords don't match! Try again!", Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else if(u.equals(user) && user != null)
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), "User already existt! " + u, Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    else
                    {
                        String inss = "insert into user values (?,?,?,?,?,?,?,?,?)";
                        st2 = c.prepareStatement(inss);
                        st2.setString(1,user);
                        st2.setString(2,pass);
                        st2.setString(3,fname);
                        st2.setString(4,sur);
                        st2.setString(5,emaill);
                        st2.setString(6,tele);
                        st2.setString(7,ulica);
                        st2.setString(8,miasto);
                        st2.setString(9,woje);
                        st2.execute();
                        st2.close();
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), "User Created! ", Toast.LENGTH_LONG).show();
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

        });
    }

}
