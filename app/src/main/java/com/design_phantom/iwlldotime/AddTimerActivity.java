package com.design_phantom.iwlldotime;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


public class AddTimerActivity extends AppCompatActivity {

    //View
    private TextView setTimeTextSecond;
    private TextView setTimeTextMinute;
    private InputMethodManager inputMethodManager;
    private Button[] bts;
    private LinearLayout layout;
    private Context context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timer);

        context = this;

        Button v = findViewById(R.id.tab_2);
        v.setBackgroundColor(Color.parseColor("#0280aa"));
        v.setTextColor(Color.WHITE);

        layout = findViewById(R.id.layout);
        inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);

        //ボタン
        bts = new Button[5];
        bts[0] = findViewById(R.id.bt_10second);
        bts[1] = findViewById(R.id.bt_1minute);
        bts[2] = findViewById(R.id.bt_10minute);
        bts[3] = findViewById(R.id.bt_reg);
        bts[4] = findViewById(R.id.bt_clear);


        for (int i = 0; i < bts.length; i++) {

            bts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int second = 0;
                    int minute = 0;
                    switch (view.getId()) {

                        case R.id.bt_10second:
                            second += (Integer.parseInt(setTimeTextSecond.getText().toString()) + 10);
                            setTimeTextSecond.setText(String.format("%02d", second));
                            break;
                        case R.id.bt_1minute:
                            minute += Integer.parseInt(setTimeTextMinute.getText().toString()) + 1;
                            setTimeTextMinute.setText(String.format("%02d", minute));
                            break;
                        case R.id.bt_10minute:
                            minute += Integer.parseInt(setTimeTextMinute.getText().toString()) + 10;
                            setTimeTextMinute.setText(String.format("%02d", minute));
                            break;

                        case R.id.bt_clear:
                            setTimeTextSecond.setText("00");
                            setTimeTextMinute.setText("00");
                            break;

                        case R.id.bt_reg:

                            //start Timer準備
                            int sec = Integer.parseInt(setTimeTextSecond.getText().toString());
                            int min = Integer.parseInt(setTimeTextMinute.getText().toString());
                            final int timerSecond = sec + (min * 60);

                            if (timerSecond == 0) {
                                //カウントがセットされていない
                                Toast.makeText(AddTimerActivity.this, getString(R.string.error_text_1), Toast.LENGTH_SHORT).show();

                            } else {

                                final EditText edit_title = new EditText(AddTimerActivity.this);

                                new AlertDialog.Builder(AddTimerActivity.this)
                                        .setTitle(getString(R.string.dialog1_title))
                                        .setMessage(getString(R.string.dialog1_msg))
                                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {

                                            if (edit_title.getText().toString().equals("") == false) {

                                                TimerCategoryManager manager = new TimerCategoryManager(AddTimerActivity.this);

                                                Timer timer = new Timer();
                                                timer.setTimer_title(edit_title.getText().toString());
                                                timer.setTimer_second(timerSecond);
                                                manager.addTimer(timer);

                                                Toast.makeText(AddTimerActivity.this, getString(R.string.dialog1_msg2), Toast.LENGTH_SHORT).show();

                                            } else {

                                                Toast.makeText(AddTimerActivity.this, getString(R.string.dialog1_msg3), Toast.LENGTH_SHORT).show();

                                            }


                                            }
                                        })
                                        .setView(edit_title)
                                        .setCancelable(false)
                                        .setNegativeButton("Cancel", null)
                                        .show();

                            }

                            break;
                    }

                }
            });
        }


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
