package com.example.storageapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;

public class MainActivity extends AppCompatActivity {

    FirebaseStorage storage;
    StorageReference parentRef;
    StorageReference childRef;

    ImageView imageView;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imageView = findViewById(R.id.imageView);
        progressBar = findViewById(R.id.progressBar2);

        String [] permissions
                = {Manifest.permission.READ_EXTERNAL_STORAGE
                , Manifest.permission.WRITE_EXTERNAL_STORAGE};

        ActivityCompat.requestPermissions(this, permissions, 1);

        String path
                = "/storage/emulated/0/Pictures/IMG_20220326_064836.jpg";
        File file = new File(path);
        Uri uri = Uri.fromFile(file);

        storage = FirebaseStorage.getInstance();

        parentRef = storage.getReference();

        Long fileName = System.currentTimeMillis()/1000;
        String pathName = "uploads/images/"+fileName;
        childRef = parentRef.child(pathName);

        childRef.putFile(uri).addOnSuccessListener(taskSnapshot -> childRef.getDownloadUrl().addOnSuccessListener(uri1 -> {
                    progressBar.setVisibility(View.GONE);
                    Glide.with(MainActivity.this).load(uri1).into(imageView);
                })).addOnFailureListener(e -> Log.d("ttt", e.getMessage()));
    }
}