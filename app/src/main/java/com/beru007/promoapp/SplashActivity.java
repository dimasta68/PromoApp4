package com.beru007.promoapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.app.ProgressDialog;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.promoapp.R;
import com.google.firebase.analytics.FirebaseAnalytics;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;

public class SplashActivity extends AppCompatActivity {
    //Логическая переменная для статуса соединения
    boolean hash = false;
    Boolean isInternetPresent = false;
    ConnectionDetector cd;
    TextView stat;
    ProgressBar progressBar;
    ArrayList<HashMap<String, String>> productsListHASHAll;
    ArrayList<HashMap<String, String>> productsListHASHAkcii;
    ArrayList<HashMap<String, String>> productsListHASHApromo;
    private PackageManager packageManager = null;
    public List<ApplicationInfo> applist = null;
    public ProgressDialog progress = null;
    AlertDialog alertDialog;
    String AppString;
    private FirebaseAnalytics mFirebaseAnalytics;
    String statusEntenet = "0";///0-отключен хеша нет,1-отключен хеш есть,2-есть интернет
    String originalBeru = "No";

    /**
     * Called when the activity is first created.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_activity);
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        //init package mgr
        packageManager = getPackageManager();
        new LoadApplications().execute();

        ///init alert dialog
        alertDialog = new AlertDialog.Builder(this).create();
        //Настраиваем название Alert Dialog:
        alertDialog.setTitle("Нет соединения");

        //Настраиваем сообщение:
        alertDialog.setMessage("Интернет соединение отсутствует  ");

        //Настраиваем иконки, можете выбрать другие или добавить свои (мне лень):
        alertDialog.setIcon(R.mipmap.ic_launcher);

        alertDialog.setButton(Dialog.BUTTON_POSITIVE, "OK", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivityForResult(new Intent(android.provider.Settings.ACTION_SETTINGS), 0);
            }
        });
        Paper.init(getApplicationContext());
        productsListHASHAll = Paper.book().read("allFragment");
        productsListHASHAkcii = Paper.book().read("akcii");
        productsListHASHApromo = Paper.book().read("promocode");
        if (productsListHASHAll != null & productsListHASHAkcii != null & productsListHASHApromo != null) {
            Log.d("debug", "hash true" + isInternetPresent);
            hash = true;
        } else {
            Log.d("debug", "hash false");
            hash = false;
        }
        ;


        stat = (TextView) findViewById(R.id.status);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        cd = new ConnectionDetector(getApplicationContext());
        //Получаем статус Интернет
        isInternetPresent = cd.ConnectingToInternet();
        Log.d("debug", "isInternetPresent= " + isInternetPresent);
        //Проверяем Интернет статус:
        if (isInternetPresent) {
            /**
             * Duration of wait
             **/
            int SPLASH_DISPLAY_LENGTH = 1500;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    /**
                     * Загружаем главную Activity, когда будем готовы
                     */
                    stat.setText("Загрузка пожалуйста подождите");
                    Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
                    mainIntent.putExtra("internet", "2");
                    SplashActivity.this.startActivity(mainIntent);
                    SplashActivity.this.finish();
                }
            }, SPLASH_DISPLAY_LENGTH);

        }
        if (isInternetPresent == false && hash == false) {

            alertDialog.show();
            stat.setText("Что то пошло не так Проверьте подключение к интернету");
            progressBar.setVisibility(View.GONE);

        }
        if (isInternetPresent == false && hash == true) {
            stat.setText("Загрузка из хеша  подождите");
            Intent mainIntent = new Intent(SplashActivity.this, MainActivity.class);
            mainIntent.putExtra("internet", "1");
            SplashActivity.this.startActivity(mainIntent);
            SplashActivity.this.finish();
        }
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> list) {

        ArrayList<ApplicationInfo> appList = new ArrayList();

        for (ApplicationInfo info : list) {
            try {
                if (packageManager.getLaunchIntentForPackage(info.packageName) != null) {
                    appList.add(info);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return appList;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            applist = checkForLaunchIntent(packageManager.getInstalledApplications(PackageManager.GET_META_DATA));
            AppString = applist.toString();
            AppString.contains("ru.beru.android");
            Log.d("debug", "listApp= " + applist.toString());
            Log.d("debug", "listApp= " + AppString.contains("ru.beru.android"));
            if (AppString.contains("ru.beru.android")) {
                originalBeru = "yes";
                Bundle bundle = new Bundle();
                bundle.putString("original_app", originalBeru);
                mFirebaseAnalytics.logEvent("original_app", bundle);

            } else {
                originalBeru = "No";
                Bundle bundle = new Bundle();
                bundle.putString("original_app", originalBeru);
                mFirebaseAnalytics.logEvent("original_app", bundle);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //  progress.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onPreExecute() {
            // progress = ProgressDialog.show(getApplicationContext(), null, "Loading apps info...");
            Bundle params = new Bundle();
            params.putString("original_app", originalBeru);
            mFirebaseAnalytics.logEvent("original_app", params);
            Log.d("splash","original"+originalBeru);
            super.onPreExecute();
        }
    }
}


