package com.design_phantom.iwlldotime;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TimerListActivity extends AppCompatActivity {

    private List<Category> categoryList;
    private TimerCategoryManager timerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_list);

        timerManager = new TimerCategoryManager(TimerListActivity.this);
        //categoryListを取得
        categoryList = timerManager.getCategoryList();

        //ListView表示
        ListView listView = findViewById(R.id.list_view1);
        ListAdapter myAdapter = new myAdapter(this, categoryList);
        listView.setAdapter(myAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TimerListActivity.this, RenzokuTimerActivity.class);
                //Toast.makeText(TimerListActivity.this, categoryList.get(i).getCategory_name(), Toast.LENGTH_SHORT).show();
                intent.putExtra("categoryId", categoryList.get(i).getCategory_id());
                startActivity(intent);
            }
        });

        //add button
        Button addBt = findViewById(R.id.add_timer);
        addBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(TimerListActivity.this, AddTimerActivity.class);
                startActivity(intent);
            }
        });




    }
}
