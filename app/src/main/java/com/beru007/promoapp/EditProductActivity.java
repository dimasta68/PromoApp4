package com.beru007.promoapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.promoapp.R;
import com.facebook.appevents.AppEventsLogger;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class EditProductActivity extends AppCompatActivity {
    String strTitle, strPomo, strlink;
    TextView title;
    EditText promocode;
    TextView link;
    Button btnCopy;
    ClipboardManager clipboard;
    String pid;
    ClipboardManager clipboardManager;
    ClipData clipData;
    ClipData clip;
    private ProgressDialog pDialog;
    private final int SPLASH_DISPLAY_LENGTH = 3000;
    JSONParser jParser = new JSONParser();
    LinearLayout linearLayout;
    AlertDialog.Builder alert;
    // url для получения одного продукта
    private static final String url_product_detials = "http://sh1024484.had.su/get_product_details.php";
    int timer = 1;
    private FirebaseAnalytics mFirebaseAnalytics;
    // JSON параметры
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCT = "product";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "title";
    private static final String TAG_PRICE = "links";
    private static final String TAG_DESCRIPTION = "decsript";
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
            new GetProductDetails().execute();
        }
        if (interntet.equals("1")) {
            title.setText(titleHash);
            promocode.setText(promocodeHash);
            strlink = linksHash;
            strPomo = promocodeHash;
        }

        clipboard = (ClipboardManager) this.getSystemService(Context.CLIPBOARD_SERVICE);
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

    /**
     * Фоновая асинхронная задача для получения полной информации о продукте
     **/
    @SuppressLint("StaticFieldLeak")
    class GetProductDetails extends AsyncTask<String, String, JSONObject> {

        /**
         * Перед началом показать в фоновом потоке прогресс диалог
         **/
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(EditProductActivity.this);
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Получение детальной информации о продукте в фоновом режиме
         **/
        @SuppressLint({"WrongViewCast", "WrongThread"})
        protected JSONObject doInBackground(String... args) {

            HashMap<String, String> params = new HashMap<>();
            params.put("pid", pid);
            // проверяем статус success тега
            int success;
            try {

                // получаем продукт по HTTP запросу
                JSONObject jsonObject = jParser.makeHttpRequest(url_product_detials, "GET", params);

                success = jsonObject.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // Успешно получинна детальная информация о продукте
                    JSONArray productObj = jsonObject.getJSONArray(TAG_PRODUCT);

                    // получаем первый обьект с JSON Array
                    JSONObject product = productObj.getJSONObject(0);

                    strTitle = product.getString(TAG_NAME);
                    strPomo = product.getString(TAG_DESCRIPTION);
                    strlink = product.getString(TAG_PRICE);

                } return jsonObject;
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        /**
         * После завершения фоновой задачи закрываем диалог прогресс
         **/
        protected void onPostExecute(JSONObject  jsonObject) {
            title.setText(strTitle);
            promocode.setText(strPomo);
            clip = ClipData.newPlainText("", promocode.getText().toString());
            clipboard.setPrimaryClip(clip);


            if (akcii.equals("3")) {
                linearLayout.setVisibility(View.GONE);
            } else if (akcii.equals("2")) {
            } else if (akcii.equals("1")) {
            }
            // закрываем диалог прогресс
            pDialog.dismiss();
        }
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

