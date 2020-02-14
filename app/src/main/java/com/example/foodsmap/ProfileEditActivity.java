package com.example.foodsmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.foodsmap.fragment.ProfileFragment;
import com.example.foodsmap.model.User;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.HashMap;

public class ProfileEditActivity extends AppCompatActivity {

    private ImageView profile_photo, image_close;
    private TextView txt_save,txt_image_change;
    MaterialEditText mEdit_name,mEdit_username,mEdit_bio;

    FirebaseUser currentUser;
    private StorageTask uploadTask;//yükleme görevi
    private Uri mImageUri;
    StorageReference storageAddress;//depolama adresi

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);

        profile_photo=findViewById(R.id.profileImage_ProfileEditActivity);
        image_close=findViewById(R.id.ImageClose_ProfileEditActivity);
        txt_save=findViewById(R.id.txt_save_ProfileEditActivity);
        txt_image_change=findViewById(R.id.txt_change);
        mEdit_name=findViewById(R.id.materialTxt_name_ProfileEditActivity);
        mEdit_username=findViewById(R.id.materialTxt_username_ProfileEditActivity);
        mEdit_bio=findViewById(R.id.materialTxt_bio_ProfileEditActivity);

        currentUser= FirebaseAuth.getInstance().getCurrentUser();
        storageAddress=FirebaseStorage.getInstance().getReference("yuklemeler");

        DatabaseReference userAddress=FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        userAddress.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                User user=dataSnapshot.getValue(User.class);
                mEdit_name.setText(user.getName());
                mEdit_username.setText(user.getUsername());
                mEdit_bio.setText(user.getBio());
                Glide.with(getApplicationContext()).load(user.getphotoUrl()).into(profile_photo);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        txt_image_change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileEditActivity.this);
            }
        });

        profile_photo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CropImage.activity().setAspectRatio(1,1)
                        .setCropShape(CropImageView.CropShape.OVAL)
                        .start(ProfileEditActivity.this);
            }
        });

        txt_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                profileUpdate(mEdit_name.getText().toString(),mEdit_username.getText().toString(),mEdit_bio.getText().toString());
                startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

            }

        });

    }
    //kullanıcı bilgilerini güncelleme işlemi
    private void profileUpdate(final String name, final String username, final String bio) {
        final DatabaseReference userUpdate=FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
        userUpdate.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                HashMap<String,Object> hashMap=new HashMap<>();
                hashMap.put("name",name);
                hashMap.put("username",username);
                hashMap.put("bio",bio);
                userUpdate.updateChildren(hashMap);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    private String FileExtension(Uri uri){
        ContentResolver contentResolver=getContentResolver();
        MimeTypeMap  mimeTypeMap=MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }
    private void ImageDownload(){
        final ProgressDialog pd=new ProgressDialog(this);
        pd.setMessage("Yükleniyor..");
        pd.show();

        if(mImageUri!=null){
            final StorageReference fileAddress=storageAddress.child(System.currentTimeMillis()+"."+FileExtension(mImageUri));
            uploadTask=fileAddress.putFile(mImageUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw  task.getException();
                    }
                    return fileAddress.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri=task.getResult();
                        String myUri=downloadUri.toString();

                        DatabaseReference userAddress=FirebaseDatabase.getInstance().getReference("Users").child(currentUser.getUid());
                        HashMap<String,Object> hashMap=new HashMap<>();
                        hashMap.put("photoUrl",""+myUri);
                        userAddress.updateChildren(hashMap);
                        pd.dismiss();

                    }
                    else{
                        Toast.makeText(ProfileEditActivity.this, "Yükleme Başarısız!", Toast.LENGTH_SHORT).show();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(ProfileEditActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
        else{
            Toast.makeText(this, "Resim seçilemedi", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode==RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            mImageUri=result.getUri();
            ImageDownload();
        }
        else{
            Toast.makeText(this, "Bir şeyler yanlış gitti!", Toast.LENGTH_SHORT).show();
        }
    }
}
