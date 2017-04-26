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
        picWordView.setText("fdsafdsfafsdaf<img" + R.drawable.main_activity_receive + "/>fdsafsdafsdfds");
    }
}
