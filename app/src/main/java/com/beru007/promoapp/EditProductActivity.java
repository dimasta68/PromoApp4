package com.beru007.promoapp;


import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;

import android.os.Handler;
import android.view.MenuItem;
import android.view.View;

import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.promoapp.R;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;


public class EditProductActivity extends AppCompatActivity {
    String strTitle, strPomo, strlink;
    TextView title;
    TextView promocode;
    TextView link;
    ClipboardManager clipboard;
    String pid;
    ClipData clip;
    private ProgressDialog pDialog;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    LinearLayout linearLayout;
    AlertDialog.Builder alert;
    private FirebaseAnalytics mFirebaseAnalytics;
    // JSON параметры

    private static final String TAG_PID = "pid";

    String akcii;
    String interntet, titleHash, promocodeHash, linksHash;
    DialogInterface dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_product);
        AppEventsLogger logger = AppEventsLogger.newLogger(EditProductActivity.this);
        logger.logEvent("sentFriendRequest");

        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        alert = new AlertDialog.Builder(this);
        alert.setIcon(R.drawable.logo);
        alert.setTitle(R.string.app_name);
        alert.setView(R.layout.dialog);
        alert.setMessage("Через 5 секунд Вы будите автоматически перенаправлены на Страницу с акцией");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        title = findViewById(R.id.title);
        link = findViewById(R.id.link);
        promocode = findViewById(R.id.promo);
        // показываем форму про детальную информацию о продукте
        Intent i = getIntent();
        // Get the application context
        // получаем id продукта (pid) с формы
        pid = i.getStringExtra(TAG_PID);
        akcii = i.getStringExtra("akcii");
        interntet = i.getStringExtra("internet");
        titleHash = i.getStringExtra("name");
        linksHash = i.getStringExtra("links");
        promocodeHash = i.getStringExtra("code");


        linearLayout = (LinearLayout) findViewById(R.id.layout);
        // Получение полной информации о продукте в фоновом потоке
        if (interntet.equals("2")) {
            title.setText(titleHash);
            promocode.setText(promocodeHash);
            strlink = linksHash;
            strPomo = promocodeHash;
        }
        if (interntet.equals("1")) {
            title.setText(titleHash);
            promocode.setText(promocodeHash);
            strlink = linksHash;
            strPomo = promocodeHash;
        }

        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
    }
    public  void editClick(View view){
        if (akcii.equals("3")) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "название : " + strTitle);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "промокод :" + strPomo + "ссылка: " + strlink);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            alert.setMessage("через 5 секунд вы будете перенаправлены на страницу магазина c акцией ");
            alert.create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(strlink));
                    startActivity(browserIntent);


                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            alert.setMessage("промокод   " + "'" + strPomo + "'" + "  скопирован в буфер обмена через 5 секунд вы будете перенаправлены на страницу магазина");
            alert.create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(strlink));
                    startActivity(browserIntent);

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }
    public void myclick(View view) {
        if (akcii.equals("3")) {
            Bundle bundle = new Bundle();
            bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "название : " + strTitle);
            bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "промокод :" + strPomo + "ссылка: " + strlink);
            mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            alert.setMessage("через 5 секунд вы будете перенаправлены на страницу магазина c акцией ");
            alert.create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(strlink));
                    startActivity(browserIntent);


                }
            }, SPLASH_DISPLAY_LENGTH);
        } else {
            alert.setMessage("промокод   " + "'" + strPomo + "'" + "  скопирован в буфер обмена через 5 секунд вы будете перенаправлены на страницу магазина");
            alert.create().show();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent browserIntent = new
                            Intent(Intent.ACTION_VIEW, Uri.parse(strlink));
                    startActivity(browserIntent);

                }
            }, SPLASH_DISPLAY_LENGTH);
        }
    }

    public void banerclick(View view) {
        Intent browserIntent = new
                Intent(Intent.ACTION_VIEW, Uri.parse("https://yamaneta.com/shops/beru?utm_source=appberu&utm_medium=cpc&utm_campaign=app1&utm_content=test2&utm_term=test3"));
        startActivity(browserIntent);
    }

    public void copy(View view) {
        clip = ClipData.newPlainText("", promocode.getText().toString());
        clipboard.setPrimaryClip(clip);
        alert.setMessage("промокод   " + "'" + strPomo + "'" + "  скопирован в буфер обмена через 5 секунд вы будете перенаправлены на страницу магазина");
        alert.create().show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent browserIntent = new
                        Intent(Intent.ACTION_VIEW, Uri.parse(strlink));
                startActivity(browserIntent);

            }
        }, SPLASH_DISPLAY_LENGTH);
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
    /**
     * This function assumes logger is an instance of AppEventsLogger and has been
     * created using AppEventsLogger.newLogger() call.
     */

}

