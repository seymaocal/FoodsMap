package com.example.foodsmap.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.foodsmap.HomeActivity;
import com.example.foodsmap.R;
import com.example.foodsmap.fragment.ProfileFragment;
import com.example.foodsmap.model.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserAdapter extends  RecyclerView.Adapter<UserAdapter.ViewHolder> {

    private Context mContext;
    private List<User> mUsers;
    private FirebaseUser firebaseUser;
    private boolean isFragment;

    public UserAdapter(Context mContext, List<User> mUsers,boolean isFragment) {
        this.mContext = mContext;
        this.mUsers = mUsers;
        this.isFragment = isFragment;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        //userobject.xml sayfasını buraya bağladık.!!!
        View view = LayoutInflater.from(mContext).inflate(R.layout.user_object, viewGroup,false);


        return new UserAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, int i) {

        firebaseUser = FirebaseAuth.getInstance().getCurrentUser(); //mevcut kullanıcıyı al.
        final User user = mUsers.get(i);//modeldeki user sınıfından örnek aldık!'
        viewHolder.btn_follow.setVisibility(View.VISIBLE);//buton takipet in görünür olmasını sağlar.gone yapmıştık tsarımda onu visible yapıyoruz.!!
        viewHolder.username.setText(user.getUsername()); //kullanıcıadını modelden getter setterını aldık.
        viewHolder.name.setText(user.getName());//adını aldık.
        Glide.with(mContext).load(user.getphotoUrl()).into(viewHolder.profile_photo);//profil resimini ekledik. glide kütüphanesiyle ekledik.

        following(user.getId(), viewHolder.btn_follow);

        if(user.getId().equals(firebaseUser.getUid())){//eğer kullanıcı kendini görürse takipet butonu çıkmasın
            viewHolder.btn_follow.setVisibility(View.GONE);
        }
        //takip et butonuna tıklandığında yapılacaklar..!!!
        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               if (isFragment) {
                    SharedPreferences.Editor editor = mContext.getSharedPreferences("PREFS", Context.MODE_PRIVATE).edit();
                    editor.putString("profileid", user.getId());
                    editor.apply();

                    ((FragmentActivity) mContext).getSupportFragmentManager().beginTransaction().replace(R.id.home_fragment, new ProfileFragment()).commit();
                }
                else{
                    Intent intent=new Intent(mContext, HomeActivity.class);
                    intent.putExtra("sender",user.getId());
                    mContext.startActivity(intent);
                }
            }
        });


        viewHolder.btn_follow.setOnClickListener(new View.OnClickListener() {//takit ete basıldığında
            @Override
            public void onClick(View v) {
                if(viewHolder.btn_follow.getText().toString().equals("Takip Et")){//butonun texti takip et ise bu kullanıcıyı firebasede takip edecek şekilde ayarlasın.!!!
                    //takip edileni veritabaınında takip ettiklerim kısmına ekleyecek.
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(user.getId()).setValue(true);

                    //karşı tarafın takipçilerinede takip edeni ekler.takipçi olarak.
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).setValue(true);


                }
                else{//takipten çıkma
                    FirebaseDatabase.getInstance().getReference().child("Takip").child(firebaseUser.getUid())
                            .child("TakipEdilenler").child(user.getId()).removeValue();


                    FirebaseDatabase.getInstance().getReference().child("Takip").child(user.getId())
                            .child("Takipciler").child(firebaseUser.getUid()).removeValue();
                }
            }
        });


    }

    @Override
    public int getItemCount() {

        return mUsers.size();//kullanıcılar listesindeki öge kadar döndürecek.
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView username;
        public TextView name;
        public CircleImageView profile_photo;
        public Button btn_follow;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            username = itemView.findViewById(R.id.txt_username_object);
            name = itemView.findViewById(R.id.txt_name_object);
            profile_photo = itemView.findViewById(R.id.profile_photo_object);
            btn_follow = itemView.findViewById(R.id.btn_follow_object);
        }
    }
    private void following(final String userId, final Button button){//takip ediliyor..

        //yol alacaz. firebasde takip işlerini nerede yapılacağı
        //takip diye bir yere kaydetcek. Onun içindede bir çoçuk olacak. Herkesin takip ettikleri ve onu takip edenler kendi idine göre görülecek. ve Kendi idsinin altında takip ettiklerinin yolu verildi.
        DatabaseReference follow_address = FirebaseDatabase.getInstance().getReference().child("Takip")
                .child(firebaseUser.getUid()).child("TakipEdilenler");

        follow_address.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.child(userId).exists()){
                    button.setText("Takip Ediliyor");
                }
                else{
                    button.setText("Takip Et");
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}