package com.eric.flowlayout;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private FlowLayout flowLayout;

    private FlowAdapter adapter;
    private ArrayList<String> stringList = new ArrayList<>();

    private int i = 0;
    private String[] strs = new String[]{
            "Android", "Flutter", "Kotlin", "Swift",
            "C", "C++", "C#", "GO", "Java", "JavaScript"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        flowLayout = findViewById(R.id.my_vg);

        final Random r = new Random();

        adapter = new FlowAdapter(this, stringList);
        flowLayout.setAdapter(adapter);
        flowLayout.setOnCheckListener(new FlowLayout.OnTagCheckListener() {
            @Override
            public void onItemCheck(int position) {
                Toast.makeText(MainActivity.this, "click is " + stringList.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        findViewById(R.id.click_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stringList.add(strs[r.nextInt(strs.length)]);
//                stringList.add(strs[(i ++) % strs.length]);
                adapter.notifyDataSet();
            }
        });
    }
}
