package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface6 {
    String JSONURLBANERPROMO = "http://sh1024484.had.su/";

    @GET("baner2.php")
    Call<String> getString();
}
