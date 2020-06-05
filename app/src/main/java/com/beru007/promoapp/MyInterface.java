package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface MyInterface {

    String JSONURL = "http://sh1024484.had.su/";

    @GET("promocode.php")
    Call<String> getString();

}
