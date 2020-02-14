package com.example.foodsmap.model;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ListView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;


public class Restaurant {

   /* private String name;
    private String reference;
    private String productDescription;
    private int imageID;
    private String id;
    private String icon_url;
    private String vicinity;
    private String longitude;
    private String latitude;*/

    private ListView mListView;
    private static final String API_KEY = "AIzaSyDbeqleRe7vsz3lHdGN0AgI9VYeXp3lLXQ";//AIzaSyDbeqleRe7vsz3lHdGN0AgI9VYeXp3lLXQ

    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";

    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String TYPE_DETAILS = "/details";
    private static final String TYPE_SEARCH = "/nearbysearch";
    private static final String OUT_JSON = "/json?";
    private static final String PHOTO = "/photo?";
    private static final String LOG_TAG = "ListRest";
    private static final String PHOTO_REFERENCE = "photo_reference";

    private String name;
    private String reference;
    private int imageID;
    private String id;
    private String icon_url;
    private String address;
    private String image_url;

    private String vicinity;
    private String phone;
    private String web_site;

    public Restaurant() {
    }

    public Restaurant(String name, String reference,String vicinity, int imageID, String id, String icon_url, String address, String image_url, String phone, String web_site) {
        this.name = name;
        this.reference = reference;
        this.imageID = imageID;
        this.id = id;
        this.icon_url = icon_url;
        this.address = address;
        this.image_url = image_url;
        this.phone = phone;
        this.web_site = web_site;
        this.vicinity = vicinity;
    }

    public String getVicinity() {
        return vicinity;
    }

    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getWeb_site() {
        return web_site;
    }

    public void setWeb_site(String web_site) {
        this.web_site = web_site;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getIcon_url() {
        return icon_url;
    }

    public void setIcon_url(String icon_url) {
        this.icon_url = icon_url;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReference() {
        return reference;
    }

    public void setReference(String reference) {
        this.reference = reference;
    }

    public int getImageID() {
        return imageID;
    }

    public void setImageID(int imageID) {
        this.imageID = imageID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}

