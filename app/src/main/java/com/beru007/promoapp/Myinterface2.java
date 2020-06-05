package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Myinterface2 {
    String AllUrl = "http://sh1024484.had.su/";

    @GET("get_all_products.php")
    Call<String> getString();
}

