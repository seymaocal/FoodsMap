package com.example.foodsmap.RestApi;

import com.example.foodsmap.model.Bilgiler;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RestApi {
    @GET("/Konum.json")
    Call<List<Bilgiler>> bilgigetir();
}
