package com.design_phantom.iwlldotime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MakeMtrxActivity extends AppCompatActivity {

    private TimerCategoryManager manager;
    private LinearLayout selecedTimerLayout;
    private LinearLayout timerLayout;
    private Button bt_ok;
    private Context context;
    private TextView matrix_msg;
    private TextView timer_msg;

    //data
    private List<Timer> timerList;
    private List<Timer> selectedTimerList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_mtrx);
        context = this;

        Button v = findViewById(R.id.tab_3);
        v.setBackgroundColor(Color.parseColor("#0280aa"));
        v.setTextColor(Color.WHITE);

        matrix_msg = findViewById(R.id.matrix_msg);
        timer_msg = findViewById(R.id.timer_msg);

        manager = new TimerCategoryManager(this);
        //getData
        timerList = manager.getTimerList();
        selectedTimerList = new ArrayList<>();

        //Button
        bt_ok = findViewById(R.id.matrix_bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //何もセットされていない場合
                if(selectedTimerList.size() == 0){
                    Toast.makeText(MakeMtrxActivity.this, getString(R.string.error_text_2), Toast.LENGTH_SHORT).show();
                    return;
                }

                final EditText edit_category_title = new EditText(MakeMtrxActivity.this);
                new AlertDialog.Builder(MakeMtrxActivity.this)
                        .setTitle(getString(R.string.dialog2_title))
                        .setMessage(getString(R.string.dialog2_msg))
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                if (edit_category_title.getText().equals("") == true) {
                                    //Toast.makeText(MakeMtrxActivity.this, "cant register", Toast.LENGTH_SHORT).show();
                                } else {

                                    if (selectedTimerList.size() > 0) {
                                        //カテゴリの登録
                                        Category category = new Category();
                                        category.setCategory_name(edit_category_title.getText().toString());
                                        TimerCategoryManager manager = new TimerCategoryManager(MakeMtrxActivity.this);
                                        int insertId = manager.addCategory(category);
                                        if (insertId > 0) {
                                            int order = 0;
                                            for (Timer timer : selectedTimerList) {
                                                //timerが選択されていれば、マトリクスを登録する
                                                TimerCategory matrix = new TimerCategory();
                                                matrix.setCategory_id(insertId);
                                                matrix.setTimer_id(timer.getTimer_id());
                                                matrix.setShowOrder(order);
                                                int id = manager.addMatrix(matrix);
                                                order++;
                                            }
                                            //登録された！
                                        }
                                    } else {
                                        //cant register
                                    }
                                }

                            }
                        })
                        .setView(edit_category_title)
                        .setCancelable(false)
                        .setNegativeButton("Cancel", null)
                        .show();

            }
        });

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


        //area
        timerLayout = findViewById(R.id.layout_show_timer_area);
        selecedTimerLayout = findViewById(R.id.layout_matix_selected_result);

        //show all timer
        showAllTimer(timerList);

        //選択されたITEMがない場合
        if(selectedTimerList.size() == 0){
            matrix_msg.setText(getString(R.string.error_text_3));
            bt_ok.setEnabled(false);
        }
    }

    private void showAllTimer(List<Timer> list) {

        timerLayout.removeAllViews();

        if (list.size() > 0) {

            for (final Timer timer : list) {
                TimerTime timerTime = new TimerTime();
                timerTime.change(timer.getTimer_second());
                final int min =timerTime.getMinute();
                final int sec =timerTime.getSecond();

                Button bt = new Button(MakeMtrxActivity.this);

                bt.setTag(timer.getTimer_id());

                bt.setText(  timer.getTimer_title() + "(" + String.format("%02d", min) + ":" + String.format("%02d", sec) + ")");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button bt = new Button(MakeMtrxActivity.this);
                        bt.setText("☓ " + timer.getTimer_title() + "(" + String.format("%02d", min) + ":" + String.format("%02d", sec) + ")");
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int deleteId = -1;

                                for (int i = 0; i < selecedTimerLayout.getChildCount(); i++) {
                                    Button bt = (Button) selecedTimerLayout.getChildAt(i);

                                    if (bt == view) {
                                        deleteId = i;
                                    }
                                }

                                if (deleteId >= 0) {
                                    //remove View
                                    selecedTimerLayout.removeView(view);
                                    selectedTimerList.remove(deleteId);

                                    //選択されたITEMがない場合
                                    if(selectedTimerList.size() == 0){
                                        matrix_msg.setText(getString(R.string.error_text_3));
                                        bt_ok.setEnabled(false);
                                    }else{
                                        matrix_msg.setText( getString(R.string.confirm_msg1) + selectedTimerList.size() + getString(R.string.tani) );
                                        bt_ok.setEnabled(true);
                                    }
                                }

                                //checkSelectedTimer();
                            }
                        });
                        //show selected timer
                        selecedTimerLayout.addView(bt);
                        //selectedTimerListにも追加
                        selectedTimerList.add(timer);

                        if(selectedTimerList.size() > 0){
                            matrix_msg.setText( getString(R.string.confirm_msg1) + selectedTimerList.size() + getString(R.string.tani) );
                            bt_ok.setEnabled(true);
                        }
                        //checkSelectedTimer();
                    }
                });

                bt.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {

                        final int timerId = Integer.parseInt(view.getTag().toString());

                        new AlertDialog.Builder(MakeMtrxActivity.this)
                                .setTitle(getString(R.string.dialog3_title))
                                .setMessage(getString(R.string.dialog3_msg))
                                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        manager.deleteTimer(timerId);
                                        Toast.makeText(MakeMtrxActivity.this, getString(R.string.dialog3_msg1),Toast.LENGTH_SHORT).show();
                                        timerList = manager.getTimerList();

                                        for (Timer timer : timerList){
                                            Common.log("\n" + timer.getTimer_title() + "\n");
                                        }

                                        showAllTimer(timerList);

                                    }
                                })
                                .setNegativeButton("Cancel", null)
                                .setCancelable(false)
                                .show();
                        return true;

                    }
                });

                timerLayout.addView(bt);

            }
        }
    }


    //checkSelectedTimer ※確認debug用
    public void checkSelectedTimer() {
        for (Timer timer : selectedTimerList) {
            Log.i("INFO:格納timer", timer.getTimer_title());
        }
    }

    private class TimerTime {

        private int minute;
        private int second;

        TimerTime() {

        }

        public void change(int second) {
            if (second < 60) {
                this.second = second;
                this.minute = 0;
            } else {

                this.second = second % 60;
                this.minute = second / 60;
            }
        }

        public int getMinute() {
            return minute;
        }
        public int getSecond() {
            return second;
        }
    }

}
