package com.example.foodsmap;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.example.foodsmap.RestApi.ManagerAll;
import com.example.foodsmap.model.Bilgiler;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RestActivity extends AppCompatActivity {

    List<Bilgiler> list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rest);
        istek();
    }

    public void istek(){
        list=new ArrayList<>();
        Call<List<Bilgiler>> bilgiList= ManagerAll.getInstance().getirBilgileri();
        bilgiList.enqueue(new Callback<List<Bilgiler>>() {
            @Override
            public void onResponse(Call<List<Bilgiler>> call, Response<List<Bilgiler>> response) {
                list=response.body();
                Log.i("xxxxxx",response.body().toString());
            }

            @Override
            public void onFailure(Call<List<Bilgiler>> call, Throwable t) {

            }
        });

    }
}
