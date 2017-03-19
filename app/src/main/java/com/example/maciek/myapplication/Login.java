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

import static android.util.JsonToken.NULL;

public class Login extends AppCompatActivity {

    Button login, register;
    EditText username , password;
    static String p,u;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login = (Button) findViewById(R.id.login_button);
        register = (Button) findViewById(R.id.register_button);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                new Thread(new Runnable(){

                    public void run()
                    {
                        authenticate();
                    }

                }).start();


            }

            protected void authenticate() {

                try
                {
                    String user = username.getText().toString().toLowerCase();
                    String pass= password.getText().toString();

                    PreparedStatement st = null;

                    Class.forName("com.mysql.jdbc.Driver");
                    String url = "jdbc:mysql://178.62.50.210:3306/bikes";
                    Connection c = DriverManager.getConnection(url,"maciek","maciek93");
                    String sql = "select password , username from user where username=?";
                    st = c.prepareStatement(sql);
                    st.setString(1, user);
                    ResultSet rs = st.executeQuery();


                    while(rs.next())
                    {
                        p = rs.getString("password");
                        u = rs.getString("username").toLowerCase();
                    }

                    if(u != null && user.equals(u))
                    {
                        if(p  != null && pass.equals(p))
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), " User "+ user + " logged in ! ", Toast.LENGTH_SHORT).show();
                                }
                            });
                            Intent k = new Intent(Login.this, Menu.class);
                            k.putExtra("Name", username.getText().toString());
                            startActivity(k);
                            finish();

                        }
                        else
                        {
                            runOnUiThread(new Runnable() {
                                public void run() {

                                    Toast.makeText(getBaseContext(), "Password doesn't match ! " , Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                    else
                    {
                        runOnUiThread(new Runnable() {
                            public void run() {

                                Toast.makeText(getBaseContext(), " User: "+ user + " Doesn't Exist ! ", Toast.LENGTH_SHORT).show();
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

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent k = new Intent(Login.this, Register.class);
                startActivity(k);
                finish();
            }
        });
    }
}
