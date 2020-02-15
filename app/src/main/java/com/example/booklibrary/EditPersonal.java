package com.example.booklibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

public class EditPersonal extends AppCompatActivity {

    ImageView newAvatar;
    EditText newName;
    Button btn_save;
    Uri img_uri;
    String user_email;
    FirebaseFirestore db;
    private User user;
    ProgressDialog progressDialog;
    StorageReference mStorage;
    DocumentReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_personal);
        newAvatar = findViewById(R.id.iv_edit_avatar);
        newName = findViewById(R.id.et_edit_userName);
        btn_save = findViewById(R.id.btn_editPersonal_save);
        mStorage = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();
        progressDialog = new ProgressDialog(this);

        final Intent intent = getIntent();
        if (intent != null) {
            user_email = intent.getStringExtra("email");
        }

        clicked();

//        Toast.makeText(this, u, Toast.LENGTH_SHORT).show();

//        if (!(progressDialog.isShowing()) && !u.equals("none")){
//            user.setUserAvatar(u);
//            updateUser();
//            finish();
//        }
    }

    private void clicked(){
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Wait until uploading photo");
                progressDialog.show();
                final String name = newName.getText().toString();
                if (img_uri != null && !name.isEmpty()) {
                    db.collection("Users").whereEqualTo("email", user_email).addSnapshotListener(new EventListener<QuerySnapshot>() {
                        @Override
                        public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                            for (DocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots.getDocuments()) {
                                user = queryDocumentSnapshot.toObject(User.class);
                                user.setUserName(name);
                                final StorageReference filepath = mStorage.child(user.getEmail()).child("Avatar");
                                filepath.putFile(img_uri).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(EditPersonal.this, "Failed to upload image", Toast.LENGTH_SHORT).show();
                                    }
                                }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                    @Override
                                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                        Toast.makeText(EditPersonal.this, "sucees adding image", Toast.LENGTH_SHORT).show();
                                        progressDialog.dismiss();
                                        mStorage.child(user.getEmail()).child("Avatar").getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                                            @Override
                                            public void onComplete(@NonNull Task<Uri> task) {
                                                SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                                                SharedPreferences.Editor editor = sp.edit();
                                                editor.putString("uri", task.getResult()+"").apply();
                                            }
                                        });
                                    }
                                });
                            }
                            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getBaseContext());
                            String u = preferences.getString("uri", "none");
                            user.setUserAvatar(u);
                            updateUser();
                            progressDialog.dismiss();
                            finish();
                        }
                    });
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(EditPersonal.this, "please enter the image and name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    public void change_avatar(View view) {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(gallery, 200);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 200 && data != null) {

            Uri imagerUri = data.getData();
            newAvatar.setImageURI(imagerUri);
            img_uri = imagerUri;
        }
    }

    private void updateUser() {
        reference = db.collection("Users").document(user.getEmail());
        final DocumentReference finalReference = reference;
        reference.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    finalReference.set(user);
                } else {
                    task.getException().getMessage();
                }
            }
        });
    }
}
