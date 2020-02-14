package com.example.foodsmap;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.foodsmap.model.Menu;
import com.example.foodsmap.model.User;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.util.ArrayList;
import java.util.HashMap;

public class CommisionActivity extends AppCompatActivity {

    private TextView urun,fiyat;
    private MaterialEditText name,address,telefon;
    private Button onayla;
    String currentUser;
    String restoran_name="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_commision);

        Bundle intent =getIntent().getExtras();
       // String[] result=intent.getStringArray("result");

        final int positionn = intent.getInt("position");
        final String urun_value = intent.getString("urun");
        restoran_name = intent.getString("restoran_name");
        final String fiyat_value = intent.getString("fiyat");
        final String position=Integer.toString(positionn);

        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        telefon=findViewById(R.id.telefon);
        urun=(TextView) findViewById(R.id.urun);
        fiyat=findViewById(R.id.fiyat);
        onayla=findViewById(R.id.onayla);

        //MenuActivity ma =new MenuActivity();
        //ArrayList<Menu> result=ma.result();


       // String urun_value2=result[0];



        urun.setText(urun_value);
        fiyat.setText(fiyat_value);
        currentUser=FirebaseAuth.getInstance().getCurrentUser().getUid();

        final String[] name2 = {""};
        DatabaseReference userAddress= FirebaseDatabase.getInstance().getReference().child("Users");
        userAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot snapshot:dataSnapshot.getChildren()){
                    User user=snapshot.getValue(User.class);
                    if(user.getId()==currentUser) {
                        name2[0] =user.getName();
                        name.setText(name2[0]);
                    }
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        onayla.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference commisionAddress =   FirebaseDatabase.getInstance().getReference().child("Siparisler").child(restoran_name);
                String address_=address.getText().toString();
                String telefon_=telefon.getText().toString();
                String name_=name.getText().toString();
                HashMap<String,Object> hashMap = new HashMap<>();
                hashMap.put("food", urun_value);
                hashMap.put("pay", fiyat_value);
                hashMap.put("address",address_);
                hashMap.put("phone",telefon_);
                hashMap.put("sender",name_);
                hashMap.put("senderId",currentUser);
                hashMap.put("restaurant_name",restoran_name);
                commisionAddress.push().setValue(hashMap);

                my( urun_value,  fiyat_value);

                Toast.makeText(CommisionActivity.this, "Siparişiniz Tamamlandı", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void my(String urun_value, String fiyat_value){
        DatabaseReference commisionAddress =   FirebaseDatabase.getInstance().getReference().child("Siparislerim").child(currentUser);
        String address_=address.getText().toString();
        String telefon_=telefon.getText().toString();
        String name_=name.getText().toString();
        HashMap<String,Object> hashMap = new HashMap<>();
        hashMap.put("food", urun_value);
        hashMap.put("pay", fiyat_value);
        hashMap.put("address",address_);
        hashMap.put("phone",telefon_);
        hashMap.put("sender",name_);
        hashMap.put("senderId",currentUser);
        hashMap.put("restaurant_name",restoran_name);
        commisionAddress.push().setValue(hashMap);
    }
}
