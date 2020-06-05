package com.beru007.promoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.example.promoapp.R;
import com.onesignal.OSNotificationOpenResult;
import com.onesignal.OneSignal;

public class WActivity extends AppCompatActivity {
     WebView webView;
     String linkUrl;
    public ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web);
        Intent i = getIntent();
        linkUrl=i.getStringExtra("link");
        Window window = this.getWindow();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
// clear FLAG_TRANSLUCENT_STATUS flag:
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);

// add FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS flag to the window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

// finally change the color
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.setStatusBarColor(ContextCompat.getColor(this,R.color.colorPrimary));
        }
        OneSignal.startInit(this).setNotificationOpenedHandler(new OneSignal.NotificationOpenedHandler() {
            @Override
            public void notificationOpened(OSNotificationOpenResult result) {

                String launchURL = result.notification.payload.launchURL;

                if (launchURL != null) {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("url", launchURL);
                    startActivity(intent);

                } else {
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }
            }
        }).init();


        webView = findViewById(R.id.Web);
        webView.getSettings().setJavaScriptEnabled(true);
        Intent intent=getIntent();
        if (intent.getStringExtra("url")!=null){
            webView.loadUrl(intent.getStringExtra("url"));

        }else{
            webView.loadUrl(linkUrl);

        }
        webView.getSettings().setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK);
/*        webView.setWebViewClient(new MyWebViewClient() {


            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                webView.setVisibility(view.INVISIBLE);
                init();
                showDialog1();

                //progressDialog = ProgressDialog.show(MainActivity.this, "", "Загрузка. Пожалуйста подождите...", true);

                Log.d("debug", "start");
            }

            public void onPageFinished(WebView view, String url) {
                progressDialog.dismiss();
                webView.setVisibility(view.VISIBLE);
                Log.d("debug", "finish");
            }
        });*/


    }

    @Override
    public void onBackPressed() {
        if (webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    private void init() {
        this.progressDialog=new ProgressDialog(this);
    }

    private void showDialog1() {
        progressDialog.setCancelable(false);
        progressDialog.show();
        progressDialog.setContentView(R.layout.dialog);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}