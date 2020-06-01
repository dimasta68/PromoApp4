package com.beru007.promoapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.promoapp.R;
import com.squareup.picasso.Picasso;

import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import io.paperdb.Paper;


public class PrpmocodeFragment extends Fragment {
    private ProgressDialog pDialog;
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> productsListHASH;

    // url получения списка всех продуктов
    private static String url_all_products = "http://sh1024484.had.su/akcii.php";
    private static String url_all_baners = "http://sh1024484.had.su/baner.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_PRODUCTS = "products";
    private static final String TAG_BANER = "baner";
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "title";
    private static final String TAG_END = "end_skidka";
    private static final String TAG_SKIDKA = "skidka";
    private static final String TAG_RATING = "rating";
    private static final String TAG_IMG = "img_link";
    private static final String TAG_LINK_BANER = "link_baner";
    private static final String TAG_POS = "position";
    private static final String TAG_LINK = "links";
    private static final String TAG_PROMO = "decsript";

    ListView lv;
    // тут будет хранится список продуктов
    JSONArray products = null;
    JSONArray baner = null;
    private Object ListAdapter;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    String internet;
    ImageView banner1, banner2;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_prpmocode, container, false);
        Animation mycombo = AnimationUtils.loadAnimation(getActivity(), R.anim.myalpha);
        Intent intent=getActivity().getIntent();
        internet=intent.getStringExtra("internet");

        mSwipeRefreshLayout = view.findViewById(R.id.swap);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if(internet.equals("2")){new LoadAllProductsPromo().execute();}
                else{mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "подключитесь к интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        View headerView = inflater.inflate(R.layout.header_view, null, true);
        banner1 = (ImageView) headerView.findViewById(R.id.banner1);
        Picasso.with(view.getContext().getApplicationContext())
                .load("http://sh1024484.had.su/web/images/beru_banner_app.png")
                .placeholder(R.drawable.common_google_signin_btn_icon_dark) //показываем что-то, пока не загрузится указанная картинка
                .error(R.drawable.ic_launcher_background) // показываем что-то, если не удалось скачать картинку
                .into(banner1);
        banner1.setClickable(true);
        banner1.setAnimation(mycombo);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintetn= new Intent(getContext(),WActivity.class);
                startActivity(webintetn);

            }
        });
        productsList = new ArrayList<HashMap<String, String>>();
        lv = view.findViewById(R.id.list);
        lv.addHeaderView(headerView);//Add view to list view as header view
        if(internet.equals("2")) {
            Log.d("debug","loAD PRODUCTS INTERNET");
            new LoadAllProductsPromo().execute();
        }if(internet.equals("1")){
            productsListHASH=Paper.book().read("promocode");
            android.widget.ListAdapter adapter = new SimpleAdapter(
                    getActivity(), productsListHASH,
                    R.layout.list_item, new String[]{TAG_PID,
                    TAG_NAME, TAG_END, TAG_SKIDKA, TAG_RATING,TAG_LINK,TAG_PROMO},
                    new int[]{R.id.pid, R.id.name, R.id.desriptionsidka, R.id.skid, R.id.rating,R.id.link,R.id.promocode});
            // обновляем listview
            lv.setAdapter(adapter);
        }

        // на выбор одного продукта
        // запускается Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();
                String name=((TextView)view.findViewById(R.id.name)).getText().toString();
                String links=((TextView)view.findViewById(R.id.link)).getText().toString();
                String code=((TextView)view.findViewById(R.id.promocode)).getText().toString();
                Log.d("debug", pid);
                // Запускаем новый intent который покажет нам Activity
                Intent in = new Intent(getActivity(), EditProductActivity.class);
                // отправляем pid в следующий activity
                in.putExtra(TAG_PID, pid);
                in.putExtra("name", name);
                in.putExtra("internet", internet);
                in.putExtra("links", links);
                in.putExtra("code", code);
                in.putExtra("akcii", "3");
                // запуская новый Activity ожидаем ответ обратно
                startActivityForResult(in, 100);
            }
        });
        return view;
    }
    @SuppressLint("StaticFieldLeak")
    class LoadAllProductsPromo extends AsyncTask<String, String, JSONObject> {

        /**
         * Перед началом фонового потока Show Progress Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Загрузка продуктов. Подождите...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * Получаем все продукт из url
         */
        @RequiresApi(api = Build.VERSION_CODES.KITKAT)
        protected JSONObject doInBackground(String... args) {
            // Будет хранить параметры
            HashMap<String, String> params = new HashMap<>();
            // получаем JSON строк с URL
            JSONObject json = jParser.makeHttpRequest(url_all_products, "GET", params);
            try {
                // Получаем SUCCESS тег для проверки статуса ответа сервера
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // продукт найден
                    // Получаем масив из Продуктов

                    products = json.getJSONArray(TAG_PRODUCTS);

                    // перебор всех продуктов
                    for (int i = 0; i < products.length(); i++) {
                        JSONObject c = products.getJSONObject(i);

                        // Сохраняем каждый json елемент в переменную
                        String id = c.getString("id");
                        String name = c.getString(TAG_NAME);
                        String end = c.getString(TAG_END);
                        String skidka = c.getString(TAG_SKIDKA);
                        String rating =c.getString(TAG_RATING);
                        String links = c.getString(TAG_LINK);
                        String descript = c.getString(TAG_PROMO);
                        //    Log.d("debug", name);

                        // Создаем новый HashMap
                        HashMap<String, String> map = new HashMap<String, String>();

                        // добавляем каждый елемент в HashMap ключ => значение
                        map.put(TAG_PID, id);
                        map.put(TAG_NAME, name);
                        map.put(TAG_END, end);
                        map.put(TAG_SKIDKA, skidka);
                        map.put(TAG_RATING, rating);
                        map.put(TAG_LINK, links);
                        map.put(TAG_PROMO, descript);

                        // добавляем HashList в ArrayList
                        productsList.add(map);
                        Paper.book().write("promocode", productsList);
                    }
                } else {
                    // продукт не найден
                    // Запускаем Add New Product Activity
                    Intent i = new Intent(getContext(),
                            MainActivity.class);
                    // Закрытие всех предыдущие activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            JSONObject json2 = jParser.makeHttpRequest(url_all_baners, "GET", (HashMap<String, String>) params);
            try {
                // Получаем SUCCESS тег для проверки статуса ответа сервера
                int success = json2.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // продукт найден
                    // Получаем масив из Продуктов
                    baner = json2.getJSONArray(TAG_BANER);

                    // перебор всех продуктов
                    for (int i = 0; i < baner.length(); i++) {
                        JSONObject c = baner.getJSONObject(i);

                        // Сохраняем каждый json елемент в переменную
                        String link = c.getString(TAG_IMG);
                        String link_baner = c.getString(TAG_LINK_BANER);
                        String pos = c.getString(TAG_POS);
                        String newLink= link.substring(30);

                         Log.d("debug", newLink);
                    }
                }return json;
              /*  else {
                    // продукт не найден
                    // Запускаем Add New Product Activity
                    Intent i = new Intent(getContext(),
                            MainActivity.class);
                    // Закрытие всех предыдущие activities
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * После завершения фоновой задачи закрываем прогрес диалог
         **/
        protected void onPostExecute(JSONObject json) {
            // закрываем прогресс диалог после получение все продуктов
            pDialog.dismiss();
            // обновляем UI форму в фоновом потоке
            mSwipeRefreshLayout.setRefreshing(false);
            /**
             * Обновляем распарсенные JSON данные в ListView
             * */
            android.widget.ListAdapter adapter = new SimpleAdapter(
                    getActivity(), productsList,
                    R.layout.list_item, new String[]{TAG_PID,
                    TAG_NAME, TAG_END, TAG_SKIDKA, TAG_RATING,TAG_LINK,TAG_PROMO},
                    new int[]{R.id.pid, R.id.name, R.id.desriptionsidka, R.id.skid, R.id.rating,R.id.link,R.id.promocode});
            // обновляем listview
            lv.setAdapter(adapter);
        }

    }
}
