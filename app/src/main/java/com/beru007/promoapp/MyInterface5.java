package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface5 {
    String JSONURLBANERAll = "http://sh1024484.had.su/";

    @GET("baner3.php")
    Call<String> getString();
}
