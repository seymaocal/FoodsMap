package com.example.foodsmap.model;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;


public class Menu {
    private String urun;
    private String fiyat;
    private String image_url;
    public static String[] name = {""};
   // private String kategori_id;
    private String kategori_id;
    public Menu(String urun, String fiyat,String image_url,String kategori_id) {
        this.urun = urun;
        this.fiyat = fiyat;
        this.image_url = image_url;
        this.kategori_id = kategori_id;
    }

    public Menu() {
    }

    public String getKategori_id() {
        return kategori_id;
    }

    public void setKategori_id(String kategori_id) {
        this.kategori_id = kategori_id;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getUrun() {
        return urun;
    }

    public void setUrun(String urun) {
        this.urun = urun;
    }

    public String getFiyat() {
        return fiyat;
    }

    public void setFiyat(String fiyat) {
        this.fiyat = fiyat;
    }

}
