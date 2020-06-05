package com.beru007.promoapp;

import retrofit2.Call;
import retrofit2.http.GET;

public interface Myinterface3 {
    String AkciiURl = "http://sh1024484.had.su/";

    @GET("akcii.php")
    Call<String> getString();
}
