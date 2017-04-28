package com.example.panyunyi.picwordview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {
    private PicWordView picWordView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        picWordView=(PicWordView)findViewById(R.id.picword);
        picWordView.addText("fdsafdsfafsdaffsdfsdafsdafsdf"+"<img" + R.drawable.main_picture + "/>fsdfasfsdfsdfsdfdsafsdafsdfds\n");
        picWordView.addText("你是不是个傻逼范德萨发生的发的fdsfsdfsdfsafsafsfsdf");
        picWordView.addText("你就是个傻逼fsdafdsfasfsdaff<img"+R.drawable.main_picture+"/>");
        picWordView.setPicMode("center");
    }
}
