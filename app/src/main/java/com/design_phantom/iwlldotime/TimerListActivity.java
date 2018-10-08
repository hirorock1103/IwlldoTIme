package com.design_phantom.iwlldotime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class TimerListActivity extends AppCompatActivity {

    private List<Category> categoryList;
    private TimerCategoryManager timerManager;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timer_list);

        context = this;

        Button v = findViewById(R.id.tab_1);
        v.setBackgroundColor(Color.parseColor("#0280aa"));
        v.setTextColor(Color.WHITE);


        timerManager = new TimerCategoryManager(TimerListActivity.this);
        //categoryListを取得
        categoryList = timerManager.getCategoryList();


        Button[] tabs = new Button[3];
        tabs[0] = findViewById(R.id.tab_1);
        tabs[1] = findViewById(R.id.tab_2);
        tabs[2] = findViewById(R.id.tab_3);

        for(int i = 0; i < tabs.length; i++){
            tabs[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = null;
                    switch(view.getId()){
                        case R.id.tab_1:
                            intent = new Intent(context, TimerListActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.tab_2:
                            intent = new Intent(context, AddTimerActivity.class);
                            startActivity(intent);
                            break;

                        case R.id.tab_3:
                            intent = new Intent(context, MakeMtrxActivity.class);
                            startActivity(intent);
                            break;
                    }
                }
            });
        }


        //リストに何も入っていない場合
        if(categoryList.size() == 0){
            setNoContentsMessage();
            return;
        }

        //ListView表示
        ListView listView = findViewById(R.id.list_view1);
        ListAdapter myAdapter = new myAdapter(this, categoryList);
        listView.setAdapter(myAdapter);

        //リスナーセット
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(TimerListActivity.this, RenzokuTimerActivity.class);
                intent.putExtra("categoryId", categoryList.get(i).getCategory_id());
                startActivity(intent);
            }
        });

        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {

                final Category category = (Category)adapterView.getItemAtPosition(i);
                new AlertDialog.Builder(TimerListActivity.this)
                        .setTitle(getString(R.string.dialog0_title))
                        .setMessage(getString(R.string.dialog0_msg))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                TimerCategoryManager manager = new TimerCategoryManager(TimerListActivity.this);
                                String categoryName = category.getCategory_name();
                                manager.deleteCategory(category.getCategory_id());
                                Common.toast(TimerListActivity.this, categoryName + getString(R.string.dialog0_msg2));
                                //listView更新
                                categoryList = timerManager.getCategoryList();

                                //リストに何も入っていない場合
                                if(categoryList.size() == 0){
                                    setNoContentsMessage();
                                    return;
                                }

                                ListView listView = findViewById(R.id.list_view1);
                                ListAdapter myAdapter = new myAdapter(TimerListActivity.this, categoryList);
                                listView.setAdapter(myAdapter);
                            }
                        })
                        .setNegativeButton("Cancel", null)
                        .setCancelable(false)
                        .show();
                return true;
            }
        });

    }

    public void setNoContentsMessage(){

        LinearLayout layout = findViewById(R.id.wrap_listview);
        layout.removeAllViews();
        layout.setHorizontalGravity(Gravity.CENTER_HORIZONTAL);
        layout.setVerticalGravity(Gravity.CENTER_VERTICAL);
        layout.setGravity(Gravity.CENTER);
        TextView text = new TextView(this);
        text.setGravity(Gravity.CENTER);
        text.setText(R.string.no_contents_msg);
        layout.addView(text);

    }

}
