package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface4 {

    String banerNURL = "http://sh1024484.had.su/";

    @GET("baner.php")
    Call<String> getString();

}