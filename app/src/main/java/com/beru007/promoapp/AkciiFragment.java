package com.beru007.promoapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.HeaderViewListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;


import com.example.promoapp.R;
import com.squareup.picasso.Picasso;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;


import io.paperdb.Paper;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.scalars.ScalarsConverterFactory;


/**
 * A simple {@link Fragment} subclass.
 */
public class AkciiFragment extends Fragment {

    View view;
    ArrayList<HashMap<String, String>> productsList;
    ArrayList<HashMap<String, String>> productsListHASH;
    android.widget.ListAdapter adapter;
    String img_link_baner, bannerPos, link_baner;
    View headerView, footer;
    LinearLayout headerLayout;
    boolean headerchek = false;
    private RetroAdapter retroAdapter;
    private static final String TAG_PID = "pid";
    private static final String TAG_NAME = "title";
    private static final String TAG_END = "end_skidka";
    private static final String TAG_SKIDKA = "skidka";
    private static final String TAG_RATING = "rating";
    private static final String TAG_LINK = "links";
    private static final String TAG_PROMO = "decsript";
    ListView lv;
    // тут будет хранится список продуктов

    private Object ListAdapter;
    ImageView banner1, banner2;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    String internet;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_akcii, container, false);

        // Creating an extended library configuration.
        Intent intent = getActivity().getIntent();
        internet = intent.getStringExtra("internet");
        Animation mycombo = AnimationUtils.loadAnimation(getActivity(), R.anim.myalpha);
        productsList = new ArrayList<HashMap<String, String>>();
        lv = view.findViewById(R.id.list);///init click


        if (internet.equals("2")) {
            getBanner();
            getJSONResponse();
        }
        if (internet.equals("1")) {
            productsListHASH = Paper.book().read("akcii");

            android.widget.ListAdapter adapter = new SimpleAdapter(
                    getActivity(), productsListHASH,
                    R.layout.list_item, new String[]{TAG_PID,
                    TAG_NAME, TAG_END, TAG_SKIDKA, TAG_RATING, TAG_LINK, TAG_PROMO},
                    new int[]{R.id.pid, R.id.name, R.id.desriptionsidka, R.id.skid, R.id.rating, R.id.link, R.id.promocode});
            // обновляем listview
            lv.setAdapter(adapter);
        }
        mSwipeRefreshLayout = view.findViewById(R.id.swap);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (internet.equals("2")) {
                    getJSONResponse();
                } else {
                    mSwipeRefreshLayout.setRefreshing(false);
                    Toast.makeText(getContext(), "подключитесь к интернету", Toast.LENGTH_SHORT).show();
                }
            }
        });
        headerView = inflater.inflate(R.layout.header_view, null, true);
        banner1 = (ImageView) headerView.findViewById(R.id.banner1);
        headerLayout = (LinearLayout) headerView.findViewById(R.id.headerL);
        banner1.setClickable(true);
        banner1.setAnimation(mycombo);
        banner1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintetn = new Intent(getContext(), WActivity.class);
                webintetn.putExtra("link",link_baner);
                startActivity(webintetn);

            }
        });

        footer = inflater.inflate(R.layout.footer, null, true);
        banner2 = (ImageView) footer.findViewById(R.id.banner2);
        banner2.setClickable(true);
        banner2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent webintetn = new Intent(getContext(), WActivity.class);
                webintetn.putExtra("link",link_baner);
                startActivity(webintetn);

            }
        });
        // lv.addFooterView(footer);//Add view to list view as header view
        // на выбор одного продукта
        // запускается Edit Product Screen
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText().toString();
                String name = ((TextView) view.findViewById(R.id.name)).getText().toString();
                String links = ((TextView) view.findViewById(R.id.link)).getText().toString();
                String code = ((TextView) view.findViewById(R.id.promocode)).getText().toString();
                // Запускаем новый intent который покажет нам Activity
                Intent in = new Intent(getActivity(), EditProductActivity.class);
                // отправляем pid в следующий activity
                in.putExtra(TAG_PID, pid);
                in.putExtra("name", name);
                in.putExtra("internet", internet);
                in.putExtra("links", links);
                in.putExtra("code", code);
                in.putExtra("akcii", "2");
                // запуская новый Activity ожидаем ответ обратно
                startActivityForResult(in, 100);
            }
        });

        return view;

    }

    private void getJSONResponse() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MyInterface api = retrofit.create(MyInterface.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("debug", response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeListView(jsonresponse);

                    } else {
                        //  Log.d("debug", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeListView(String response) {

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            if (obj.optString("success").equals("1")) {

                ArrayList<ModelListView> modelListViewArrayList = new ArrayList<>();
                JSONArray dataArray = obj.getJSONArray("products");

                for (int i = 0; i < dataArray.length(); i++) {

                    ModelListView modelListView = new ModelListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);
                    Log.d("debug", "Akcii data " + dataobj);
                    // Paper.book().write("akcii", dataobj);

                    //  modelListView.se(dataobj.getString("imgURL"));
                    modelListView.setId(dataobj.getString("id"));
                    modelListView.setTitle(dataobj.getString("title"));
                    modelListView.setLinks(dataobj.getString("links"));
                    modelListView.setEnd_skidka(dataobj.getString("end_skidka"));
                    modelListView.setRating(dataobj.getString("rating"));
                    modelListView.setSkidka(dataobj.getString("skidka"));
                    modelListView.setDecsript(dataobj.getString("decsript"));


                    modelListViewArrayList.add(modelListView);
                    // Создаем новый HashMap
                    HashMap<String, String> map = new HashMap<String, String>();
                    // добавляем каждый елемент в HashMap ключ => значение
                    map.put(TAG_PID, dataobj.getString("id"));
                    map.put(TAG_NAME, dataobj.getString("title"));
                    map.put(TAG_END, dataobj.getString("end_skidka"));
                    map.put(TAG_SKIDKA, dataobj.getString("skidka"));
                    map.put(TAG_RATING, dataobj.getString("rating"));
                    map.put(TAG_LINK, dataobj.getString("links"));
                    map.put(TAG_PROMO, dataobj.getString("decsript"));
                    // добавляем HashList в ArrayList
                    productsList.add(map);
                    Paper.book().write("akcii", productsList);
                }

                retroAdapter = new RetroAdapter(getActivity(), modelListViewArrayList);
                lv.setAdapter(retroAdapter);
                mSwipeRefreshLayout.setRefreshing(false);

            } else {
                // Toast.makeText(getActivity(), obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    private void getBanner() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(MyInterface.JSONURL)
                .addConverterFactory(ScalarsConverterFactory.create())
                .build();

        MyInterface4 api = retrofit.create(MyInterface4.class);

        Call<String> call = api.getString();

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.i("Responsestring", response.body().toString());
                //Toast.makeText()
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        Log.d("splash", "banenr" + response.body().toString());

                        String jsonresponse = response.body().toString();
                        writeBaner(jsonresponse);

                    } else {
                        // Log.d("debug", "Returned empty response");//Toast.makeText(getContext(),"Nothing returned",Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {

            }
        });
    }

    private void writeBaner(String response) {

        try {
            //getting the whole json object from the response
            JSONObject obj = new JSONObject(response);
            if (obj.optString("success").equals("1")) {


                JSONArray dataArray = obj.getJSONArray("baner");


                for (int i = 0; i < dataArray.length(); i++) {

                    ModelListView modelListView = new ModelListView();
                    JSONObject dataobj = dataArray.getJSONObject(i);

                    // Paper.book().write("akcii", dataobj);
                    dataobj.getString("id");
                    img_link_baner = dataobj.getString("img_link");
                    link_baner = dataobj.getString("link_baner");
                    bannerPos = dataobj.getString("position");
                    String newLink = img_link_baner.substring(30);
                    img_link_baner = "http://" + newLink;
                    Log.d("splash", "img =" + img_link_baner);

                }


            } else {
                //  Toast.makeText(getActivity(), obj.optString("message") + "", Toast.LENGTH_SHORT).show();
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (img_link_baner != null && bannerPos.equals("1")) {
            Picasso.with(view.getContext().getApplicationContext())
                    .load(img_link_baner)
                    .placeholder(R.drawable.beru_main) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.beru_main) // показываем что-то, если не удалось скачать картинку
                    .into(banner1);
            lv.addHeaderView(headerView);//Add view to list view as header view
        }
        if (img_link_baner != null && bannerPos.equals("3")) {

            Log.d("splash", "disable banner");
        }
        if (img_link_baner != null && bannerPos.equals("2")) {

            Picasso.with(view.getContext().getApplicationContext())
                    .load(img_link_baner)
                    .placeholder(R.drawable.beru_main) //показываем что-то, пока не загрузится указанная картинка
                    .error(R.drawable.beru_main) // показываем что-то, если не удалось скачать картинку
                    .into(banner2);
            lv.addFooterView(footer);//Add view to list view as header view
            headerchek = true;
        }
    }
}
