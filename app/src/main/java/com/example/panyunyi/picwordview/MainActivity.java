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
        picWordView.addText("fdsafdsfafsdaf"+"<img" + R.drawable.main_picture + "/>fdsafsdafsdfds\n");
        picWordView.addText("你是不是个傻逼");
        picWordView.addText("你就是个傻逼<img"+R.drawable.main_picture+"/>");
    }
}
