package com.example.music_zhangzhenghuan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        ImageView imageView = findViewById(R.id.music_image);
        String url ="http://p2.music.126.net/5NsE7E5efsMuA0UP9jtLBw==/109951169663480470.jpg";
        Glide.with(this).load(url).into(imageView);
        System.out.println("test");
    }
}