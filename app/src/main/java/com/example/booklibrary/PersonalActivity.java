package com.example.booklibrary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
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

public class PersonalActivity extends AppCompatActivity {

    TextView tv_userName;
    ImageView personal_avatar;
    FirebaseFirestore db;
    FirebaseAuth auth;
    Toolbar toolbar;
    User user;
    FloatingActionButton floatingActionButton;
    StorageReference mStorage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal);
        inflate();
        getData();

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_exit);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                finish();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), AddBook.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void getData() {
        db.collection("Users").whereEqualTo("email", auth.getCurrentUser().getEmail()).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                for (DocumentSnapshot queryDocumentSnapshot : queryDocumentSnapshots.getDocuments()) {
                    user = queryDocumentSnapshot.toObject(User.class);
                    tv_userName.setText(user.getUserName());
                    if (user.getUserAvatar() != null) {
                        Picasso.get().load(user.getUserAvatar()).into(personal_avatar);
                    }
//                    if (!user.getUserAvatar().isEmpty()){
                        Uri uri = Uri.parse(user.getUserAvatar());
                        Picasso.get().load(uri).into(personal_avatar);
//                    }
                }
            }
        });
    }

    private void inflate() {
        tv_userName = findViewById(R.id.tv_personal_userName);
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        toolbar = findViewById(R.id.personal_toolbar);
        personal_avatar = findViewById(R.id.iv_personal_img);
        floatingActionButton = findViewById(R.id.fab);
        mStorage = FirebaseStorage.getInstance().getReference();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.personal_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.edit_personal_page:
                // Todo: dialog
                Intent intent = new Intent(this, EditPersonal.class);
                intent.putExtra("email", user.getEmail());
                startActivity(intent);
//                finish();
                return true;
        }
        return false;
    }



}
