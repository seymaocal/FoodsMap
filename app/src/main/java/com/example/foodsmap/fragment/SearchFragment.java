package com.example.foodsmap.fragment;

import android.os.Bundle;


import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.foodsmap.Adapter.UserAdapter;
import com.example.foodsmap.R;
import com.example.foodsmap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {


    private RecyclerView recyclerView;
    private UserAdapter userAdapter;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;


    EditText search_bar;

    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_search, container, false);

        recyclerView = view.findViewById(R.id.recyclerview_search);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));


        search_bar = view.findViewById(R.id.edit_search_bar);

        mUsers = new ArrayList<>();
        userAdapter = new UserAdapter(getContext(),mUsers,true);

        recyclerView.setAdapter(userAdapter);

        userRead();
        //arama çubuğuna birşeyler yazdıkça bize birşeyler verecek.!!!
        search_bar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {



            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //text değiştikçe ona göre arama yapsın.!!!
                userSearch(s.toString().toLowerCase());

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        return view;
    }

    //arama kodları
    private void userSearch(String s){
        //bu sorguya göre arama yapacak.!!!
        Query query = FirebaseDatabase.getInstance().getReference("Users").orderByChild("username").startAt(s).endAt(s+"\uf8ff");
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //dataSnapshot veritabanındaki veriler oluyor.!!!
                mUsers.clear();
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();//giriş yapmış olan kullancıyı aldık.!!!

                for(DataSnapshot snapshot : dataSnapshot.getChildren()){
                    if(search_bar.getText().toString().equals("")){
                        mUsers.clear();
                    }
                    else{//verileri alıp 'snapshot'a aktarıcak.!!!
                        User user = snapshot.getValue(User.class); //snapshot dan verileri user sınıfı şeklinde alacak.!!!
                        if(!user.getId().equals(firebaseUser.getUid())) {///kullanıcının kendisi değilse ekleme yapıyor listeye.!!!
                            mUsers.add(user);
                        }
                    }//kullanıcıyıda listeye aktardık.!!!
                }
                userAdapter.notifyDataSetChanged();//veriler her güncellendiğinde listede güncellensin.refresh gibi.!!!
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private void userRead(){//kullanıcıların yolunu useraddress ile tutuyoruz.!!!
        //DatabaseReference ile veritabanındaki yolu alıyoruz.
        DatabaseReference useradress = FirebaseDatabase.getInstance().getReference("Users");
        useradress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //sill..............
                firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
                //arama yapılacak yerden alınacak text boş ise bütün hepsini göstersin.!!!
                if(search_bar.getText().toString().equals("")) {
                    mUsers.clear();
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {//çocukları al.!!!
                        if (search_bar.getText().toString().equals("")) {
                            mUsers.clear();
                        } else {
                            User user = snapshot.getValue(User.class);
                            if (!user.getId().equals(firebaseUser.getUid())) {//siiillll................
                                mUsers.add(user);
                            }
                        }

                    }
                }
                userAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}