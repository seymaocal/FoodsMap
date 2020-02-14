package com.example.foodsmap;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;

import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.foodsmap.fragment.ProfileFragment;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    Uri photoUri;
    String myUri = "";
    StorageTask uploadTask;//yükleme görevi
    StorageReference photoUploadRefence;//Storage'e resim yükleme yolu

    ImageView image_close, image_added; //image_added=resim_eklendi;
    TextView txt_send;
    EditText edit_post_comment;//gönderi hakkında*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post);


        image_close = findViewById(R.id.close_post);
        image_added = findViewById(R.id.add_photo_post);
        txt_send = findViewById(R.id.txt_send);
        edit_post_comment = findViewById(R.id.edit_post_comment);

        photoUploadRefence = FirebaseStorage.getInstance().getReference("Gonderiler");

        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PostActivity.this, ProfileActivity.class ));
                finish();
            }
        });

        txt_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                photoUpload();
            }
        });

        CropImage.activity().setAspectRatio(1,1).start(PostActivity.this);

    }

    private String fileExtension(Uri uri){
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();

        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }


    private void photoUpload() {

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Gönderiyor..");
        progressDialog.show();
        //resim yükleme kodları
        if(photoUri!=null){
            final StorageReference fileExtension = photoUploadRefence.child(System.currentTimeMillis() + "."+fileExtension(photoUri));
            uploadTask = fileExtension.putFile(photoUri);
            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if(!task.isSuccessful()){
                        throw task.getException();
                    }
                    return fileExtension.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        Uri downloadUri = task.getResult();
                        myUri = downloadUri.toString();//url i stringe çevirdik.

                        DatabaseReference dataAddress = FirebaseDatabase.getInstance().getReference("Gonderiler");//verilerin databasede nereye gideceğinin yolunu aldık.

                        String postId = dataAddress.push().getKey();//veriyoluna getkey'İ ekleyecek.
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("postId", postId);
                        hashMap.put("postPhoto", myUri);
                        hashMap.put("postContent",edit_post_comment.getText().toString());
                        hashMap.put("sender", FirebaseAuth.getInstance().getCurrentUser().getUid());//kullanıcı id sini gönderecek

                        dataAddress.child(postId).setValue(hashMap);
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this, ProfileActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(PostActivity.this, "Gönderme Başarısız", Toast.LENGTH_SHORT).show();

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(PostActivity.this, ""+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }
        else{
            Toast.makeText(this, "Seçilen resim yok", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //galeriden resim seçme kodu.
        if(requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            photoUri = result.getUri();

            //seçilen resim imageviewe aktarılıyor.
            image_added.setImageURI(photoUri);
        }
        else{
            Toast.makeText(this, "Resim seçilemedi", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(PostActivity.this, ProfileActivity.class));
            finish();
        }
    }

}
