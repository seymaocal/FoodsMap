package com.example.foodsmap;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsmap.Adapter.MenuAdapter;
import com.example.foodsmap.Adapter.RestaurantAdapter;
import com.example.foodsmap.model.Restaurant;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;

public class DenemeActivity extends AppCompatActivity implements RestaurantAdapter.OnMenuListener{

    BottomNavigationView bottomNavigationView;
    Fragment choosefragment = null;
    private RecyclerView recyclerView;
    private EditText search_restaurant;
    private ImageView productImage;
    ArrayList<String> ImgUrl;
    private RestaurantAdapter restaurantAdapter;
    private List<Restaurant> restaurantList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_deneme);


        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);

        Intent intent = getIntent();
        String longitude = intent.getStringExtra("long");
        String latitude = intent.getStringExtra("lat");
        String ilce = intent.getStringExtra("ilce");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        search_restaurant = findViewById(R.id.search_restaurant);
        search_restaurant.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });


        recyclerView = (RecyclerView) findViewById(R.id.recylerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        //linearLayoutManager.setReverseLayout(true);
        //linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        restaurantList = new ArrayList<>();
        restaurantAdapter = new RestaurantAdapter(this, restaurantList, this);
        recyclerView.setAdapter(restaurantAdapter);
        restaurantRead(ilce);

    }
    private void filter(String text){
    ArrayList<Restaurant> filteredList=new ArrayList<>();
    for(Restaurant item:restaurantList){
        if(item.getName().toLowerCase().contains(text.toLowerCase())){
            filteredList.add(item);
        }
    }
    restaurantAdapter.filterList(filteredList);
    }
/*if(longitude !=null && latitude!=null) {

    recyclerView = (RecyclerView) findViewById(R.id.recylerview);
    Restaurant restaurant = new Restaurant();
    RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, restaurant.getData(latitude, longitude));
    recyclerView.setAdapter(restaurantAdapter);
    restaurantAdapter.setItemListener(this);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(linearLayoutManager);
    Toast.makeText(this, "denemeactivity", Toast.LENGTH_SHORT).show();

}
else{
    recyclerView = (RecyclerView) findViewById(R.id.recylerview);
    Restaurant restaurant = new Restaurant();
    RestaurantAdapter restaurantAdapter = new RestaurantAdapter(this, restaurant.yukle());
    recyclerView.setAdapter(restaurantAdapter);
    restaurantAdapter.setItemListener(this);

    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
    recyclerView.setLayoutManager(linearLayoutManager);
    Toast.makeText(this, "vt", Toast.LENGTH_SHORT).show();

}*/
       // adapter = new ProductAdapter(ImgUrl, DenemeActivity.this);
      /*  Product pd = new Product();
        ProductAdapter adapter=new ProductAdapter(this, pd.getData(lat,lng));
        LinearLayoutManager  Manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(Manager);
        recyclerView.setAdapter(adapter);*/



    private void restaurantRead(String ilce){
        final String[] address = {""};
        final String[] name = {""};

        final String[] ilce2={""};
        ilce2[0]=ilce;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("restaurant");
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Restaurant menu = snapshot.getValue(Restaurant.class);
                    name[0] = menu.getName();
                    address[0] = menu.getAddress().toLowerCase();

                    boolean deger;
                    deger=address[0].contains(ilce2[0]);
                    if(deger==true){
                        Restaurant restaurantt = new Restaurant();
                        restaurantt.setName(menu.getName());
                        restaurantt.setAddress(menu.getAddress());
                        restaurantt.setImage_url(menu.getImage_url());
                        restaurantList.add(restaurantt);
                    }

                }
                restaurantAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    public void Restaurant(){
        String currentUser= FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("Konum").child(currentUser).child("ilce");

        database.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String ilce=dataSnapshot.getValue(String.class);
                Intent intent=new Intent(DenemeActivity.this, DenemeActivity.class);
                intent.putExtra("ilce", ilce);
                startActivity(intent);
            }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                    switch (menuItem.getItemId()){
                        case R.id.nav_home:
                            //recyclerView.setVisibility(recyclerView.VISIBLE);
                            choosefragment = null;
                            Restaurant();

                           // startActivity(new Intent(DenemeActivity.this, DenemeActivity.class));

                            break;
                        /*case R.id.nav_search:
                            recyclerView.setVisibility(recyclerView.GONE);
                            choosefragment = null;
                            startActivity(new Intent(DenemeActivity.this, SearchActivity.class));
                            //choosefragment = new SearchFragment();
                            //getSupportFragmentManager().beginTransaction().replace(R.id.deneme_fragment,choosefragment).commit();
                            break;*/
                        case R.id.nav_act:
                            choosefragment = null;
                            startActivity(new Intent(DenemeActivity.this, MapActivity.class));
                            break;
                        case R.id.nav_profile:
                            choosefragment = null;
                            startActivity(new Intent(DenemeActivity.this, ProfileActivity.class));
                            break;
                    }

                    if(choosefragment != null){//çerçevede birşey varsa çerçeveleri çağırır.

                        getSupportFragmentManager().beginTransaction().replace(R.id.deneme_fragment,choosefragment).commit();
                    }
                    return true;
                }
            };


   /* @Override
    public void onItemClicked(Restaurant deneme, int position) {

        Toast.makeText(DenemeActivity.this, "dene", Toast.LENGTH_SHORT).show();
        Intent ıntent=new Intent(DenemeActivity.this, Restaurant_DetailsActivity.class);
        ıntent.putExtra("position_", position);
        String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productName)).getText().toString();
        ıntent.putExtra("name", name);
        startActivity(ıntent);
    }*/

    @Override
    public void onMenuClick(int position) {
       // Toast.makeText(DenemeActivity.this, "dene", Toast.LENGTH_SHORT).show();
        Intent ıntent=new Intent(DenemeActivity.this, Restaurant_DetailsActivity.class);
        ıntent.putExtra("position_", position);
        String name = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productName)).getText().toString();
        ıntent.putExtra("name", name);
        startActivity(ıntent);
    }
       /* FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef=database.getReference().child("Restoranlar").child("name");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                Intent ıntent=new Intent(DenemeActivity.this, Restaurant_DetailsActivity.class);
                ıntent.putExtra("name", name);
                startActivity(ıntent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });*/


        //Product p = new Product();
       // String name = p.getName();
        //String name =   deneme.getName();
        //String name="seyma";




}
