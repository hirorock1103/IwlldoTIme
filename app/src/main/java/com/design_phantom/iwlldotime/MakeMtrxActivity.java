package com.design_phantom.iwlldotime;

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
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MakeMtrxActivity extends AppCompatActivity {

    private TimerCategorySample manager;
    private LinearLayout selecedTimerLayout;
    private LinearLayout timerLayout;
    private LinearLayout showTimerAreaLayout;
    private Button bt_ok;
    private Button[] items;
    private EditText categoryNameInput;

    //data
    private List<Category> categoryList;
    private List<TimerCategory> matrixList;
    private List<Timer> timerList;
    private List<Timer> selectedTimerList;

    //others
    InputMethodManager inputMethodManager;
    LinearLayout layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_mtrx);

        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        layout = findViewById(R.id.layout);

        manager = new TimerCategorySample(this);
        //getData
        categoryList = manager.getCategoryList();
        matrixList = manager.getMatrixList();

        timerList = manager.getTimerList();
        selectedTimerList = new ArrayList<>();

        //Button
        bt_ok = findViewById(R.id.matrix_bt_ok);
        bt_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText edit_category_title = findViewById(R.id.edit_category_title);
                Toast.makeText(MakeMtrxActivity.this, edit_category_title.getText(), Toast.LENGTH_SHORT).show();
                if (edit_category_title.getText().equals("") == true) {
                    Toast.makeText(MakeMtrxActivity.this, "cant register", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MakeMtrxActivity.this, "register", Toast.LENGTH_SHORT).show();


                    if (selectedTimerList.size() > 0) {

                        //カテゴリの登録
                        Category category = new Category();
                        category.setCategory_name(edit_category_title.getText().toString());

                        TimerCategorySample manager = new TimerCategorySample(MakeMtrxActivity.this);
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

                                Log.i("INFO", "insertId:" + id);

                                order++;

                            }

                            //登録された！


                        }


                    } else {

                        //cant register


                    }


                }
            }
        });

        //area
        timerLayout = findViewById(R.id.layout_show_timer_area);
        selecedTimerLayout = findViewById(R.id.layout_matix_selected_result);

        //show all timer
        showAllTimer(timerList);

        //select each timer

        //add Matrix


    }

    private void showAllTimer(List<Timer> list) {
        if (list.size() > 0) {

            for (final Timer timer : list) {

                Button bt = new Button(MakeMtrxActivity.this);
                bt.setText(timer.getTimer_title() + "(" + timer.getTimer_minutes() + ":" + timer.getTimer_second() + ")");
                bt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Button bt = new Button(MakeMtrxActivity.this);
                        bt.setText("☓ " + timer.getTimer_title() + "(" + timer.getTimer_minutes() + ":" + timer.getTimer_second() + ")");
                        bt.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {

                                int deleteId = -1;

                                for (int i = 0; i < selecedTimerLayout.getChildCount(); i++) {
                                    Button bt = (Button) selecedTimerLayout.getChildAt(i);

                                    if (bt == view) {
                                        Log.i("INFO", "僕が叩かれたよ@" + bt.getText().toString());
                                        Log.i("INFO", "僕の要素順は@" + i + "だよ！");
                                        deleteId = i;
                                    }
                                }

                                if (deleteId >= 0) {
                                    //remove View
                                    selecedTimerLayout.removeView(view);
                                    selectedTimerList.remove(deleteId);
                                }

                                checkSelectedTimer();
                            }
                        });
                        //show selected timer
                        selecedTimerLayout.addView(bt);
                        //selectedTimerListにも追加
                        selectedTimerList.add(timer);

                        checkSelectedTimer();
                    }
                });
                timerLayout.addView(bt);

            }
        }
    }


    //checkSelectedTimer
    public void checkSelectedTimer() {
        for (Timer timer : selectedTimerList) {
            Log.i("INFO:格納timer", timer.getTimer_title());
        }
    }


    //
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        layout.requestFocus();

        return false;
    }


}
