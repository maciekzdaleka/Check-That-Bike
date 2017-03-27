package com.example.maciek.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.ArrayList;

public class view_advert extends AppCompatActivity {
    String username,advert_link,model,make,bike_type;
    private WebView ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_advert);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();

        if(bundle != null)
        {
            username = bundle.getString("Name");
            advert_link = bundle.getString("Bike_AD");
            model = bundle.getString("bike_model");
            make = bundle.getString("bike_make");
            bike_type = bundle.getString("bike_type");

        }
        ad = (WebView) findViewById(R.id.advert_view);
        ad.setWebViewClient(new MyBrowser());
        ad.getSettings().setLoadsImagesAutomatically(true);
        ad.getSettings().setJavaScriptEnabled(true);
        ad.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        ad.loadUrl(advert_link);
    }

    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent k = new Intent(view_advert.this, Stolen_bike_add_checker.class);
            k.putExtra("Name", username.toString());
            k.putExtra("Name", username);
            k.putExtra("bike_make", make);
            k.putExtra("bike_model", model);
            k.putExtra("bike_type", bike_type);
            startActivity(k);
            finish();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
