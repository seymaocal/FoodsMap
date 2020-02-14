package com.example.foodsmap;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsmap.Adapter.MenuAdapter;
import com.example.foodsmap.Adapter.PostAdapter;
import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.Post;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class MenuActivity extends AppCompatActivity implements MenuAdapter.OnMenuListener {
    private RecyclerView recyclerView;
    String restoran_name="";
    private MenuAdapter menuAdapter;
    private List<Menu> menuList;
    private TextView yiyecek,icecek,tatli;
    ArrayList<Menu>result=new ArrayList<>();
    private Button btn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        final Bundle intent =getIntent().getExtras();
        restoran_name = intent.getString("name");

        yiyecek=findViewById(R.id.yiyecek);
        icecek=findViewById(R.id.icecek);
        tatli=findViewById(R.id.tatli);
        //btn=findViewById(R.id.btn);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_menuActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        menuList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuList,this);
        recyclerView.setAdapter(menuAdapter);

        MenuRead();

        /*btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MenuActivity.this, CommisionActivity.class);
               // ArrayList<String>a=new ArrayList<>();
                String[] a=new String[4];
                for(int i=0;i<result.size();i++)
                    a[i]=result.get(i).toString();
                   // a.add(result.get(i).toString());

                intent.putExtra("result", a);
                startActivity(intent);
            }
        });*/
        yiyecek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                YiyecekRead();
            }
        });

        icecek.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            İcecekRead();
            }
        });
        tatli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TatliRead();
            }
        });
    }


    private void MenuRead() {

        final String[] address = {""};
        final String[] name = {""};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("menu").child(restoran_name);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);

                        name[0] = menu.getUrun();
                        address[0] = menu.getFiyat();

                        Menu restaurantt = new Menu();
                        restaurantt.setUrun(menu.getUrun());
                        restaurantt.setFiyat(menu.getFiyat());
                        restaurantt.setImage_url(menu.getImage_url());
                        menuList.add(restaurantt);

                }
                menuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    private void YiyecekRead() {

        final String[] address = {""};
        final String[] name = {""};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("menu").child(restoran_name);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    String k_id= menu.getKategori_id();
                    String d="0";
                    if(d.equals(k_id)) {
                        name[0] = menu.getUrun();
                        address[0] = menu.getFiyat();

                        Menu restaurantt = new Menu();
                        restaurantt.setUrun(menu.getUrun());
                        restaurantt.setFiyat(menu.getFiyat());
                        restaurantt.setImage_url(menu.getImage_url());
                        menuList.add(restaurantt);
                    }

                }
                menuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_menuActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        menuList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuList,this);
        recyclerView.setAdapter(menuAdapter);
    }
    private void TatliRead() {

        final String[] address = {""};
        final String[] name = {""};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("menu").child(restoran_name);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    String k_id= menu.getKategori_id();
                    String d="2";
                    if(d.equals(k_id)) {
                        name[0] = menu.getUrun();
                        address[0] = menu.getFiyat();

                        Menu restaurantt = new Menu();
                        restaurantt.setUrun(menu.getUrun());
                        restaurantt.setFiyat(menu.getFiyat());
                        restaurantt.setImage_url(menu.getImage_url());
                        menuList.add(restaurantt);
                    }

                }
                menuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_menuActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        menuList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuList,this);
        recyclerView.setAdapter(menuAdapter);
    }
    private void İcecekRead() {

        final String[] address = {""};
        final String[] name = {""};

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference a=database.getReference().child("menu").child(restoran_name);
        a.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //menuList.clear();
                for(DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Menu menu = snapshot.getValue(Menu.class);
                    String k_id= menu.getKategori_id();
                    String d="1";
                    if(d.equals(k_id)) {
                        name[0] = menu.getUrun();
                        address[0] = menu.getFiyat();

                        Menu restaurantt = new Menu();
                        restaurantt.setUrun(menu.getUrun());
                        restaurantt.setFiyat(menu.getFiyat());
                        restaurantt.setImage_url(menu.getImage_url());
                        menuList.add(restaurantt);
                    }

                }
                menuAdapter.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_menuActivity);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        menuList = new ArrayList<>();
        menuAdapter = new MenuAdapter(this, menuList,this);
        recyclerView.setAdapter(menuAdapter);
    }
    @Override
    public void onMenuClick(int position) {
        menuList.get(position);
        Toast.makeText(this, "bastıııı", Toast.LENGTH_SHORT).show();
        Intent ıntent=new Intent(MenuActivity.this, CommisionActivity.class);
        ıntent.putExtra("position", position);
        String urun = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productName)).getText().toString();
        String fiyat = ((TextView) recyclerView.findViewHolderForAdapterPosition(position).itemView.findViewById(R.id.productDescription)).getText().toString();

        Menu menu=new Menu();
        menu.setUrun(urun);
        menu.setFiyat(fiyat);
        result.add(menu);

        ıntent.putExtra("urun", urun);
        ıntent.putExtra("restoran_name", restoran_name);
        ıntent.putExtra("fiyat", fiyat);
        startActivity(ıntent);
    }




}
