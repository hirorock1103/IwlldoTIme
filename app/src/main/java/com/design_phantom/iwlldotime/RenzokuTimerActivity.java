package com.design_phantom.iwlldotime;

import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimerTask;


public class RenzokuTimerActivity extends AppCompatActivity {

    private static int INTERVAL_TIME = 5;

    //View
    private TextView setTimeTextSecond;
    private TextView setTimeTextMinute;
    private Button[] bts;
    private LinearLayout timer_navgation_area;

    private int categoryId;
    private Timer selectedTimer;
    private List<Timer> matrixTimerList;
    private TimerCategoryManager manager;
    private int order = 0;

    //タイマー関連
    private int timerSecond = 0;
    private boolean isStart = false;
    CountDownTimer timer;

    //連続タイマー用
    private boolean interval = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzoku_timer);

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);

        //list ※実行するタイマーをセット
        matrixTimerList = new ArrayList<>();

        //カテゴリエリアにカテゴリ情報を表示
        timer_navgation_area = findViewById(R.id.timer_navgation_area);

        //遷移元からintentにセットされた値を取得する。
        Intent intent = getIntent();
        categoryId = intent.getIntExtra("categoryId", 0);

        try {

            //Category list
            manager = new TimerCategoryManager(RenzokuTimerActivity.this);
            List<JoinedMarix> joinedMarixList = manager.getJoinedMatrixListByCategoryId(categoryId);

            for (JoinedMarix matrix : joinedMarixList) {
                //実行するタイマーをセットする
                matrixTimerList.add(matrix.getTimer());
            }

            if (matrixTimerList.size() > 1) {
                //timerListのタイマーが２つ以上ならintervalを有効にする
                interval = true;
            }

            if (joinedMarixList.size() > 0) {
                //実行するタイマーを特定
                selectedTimer = matrixTimerList.get(0);
                //タイマー時間セット
                initTimer(selectedTimer);
                //文字列表示
                showCategory(joinedMarixList, selectedTimer, interval);
            }


        } catch (Exception e) {

            Common.log(e.getMessage());

        }

        //戻るボタン
        Button btBack = findViewById(R.id.bt_move_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (timer != null) {
                    timer.cancel();
                }

                Intent intent = new Intent(RenzokuTimerActivity.this, TimerListActivity.class);
                startActivity(intent);
            }
        });

        //ボタン
        bts = new Button[2];
        bts[0] = findViewById(R.id.bt_start);
        bts[1] = findViewById(R.id.bt_clear);

        for (int i = 0; i < bts.length; i++) {

            bts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    switch (view.getId()) {

                        case R.id.bt_clear:

                            if (timer != null) {
                                timer.cancel();
                            }

                            isStart = false;
                            interval = false;
                            order = 0;
                            try {

                                List<JoinedMarix> joinedMarixList = manager.getJoinedMatrixListByCategoryId(categoryId);

                                matrixTimerList = new ArrayList<>();

                                for (JoinedMarix matrix : joinedMarixList) {
                                    //実行するタイマーをセットする
                                    matrixTimerList.add(matrix.getTimer());
                                }


                                if (matrixTimerList.size() > 1) {
                                    //timerListのタイマーが２つ以上ならintervalを有効にする
                                    interval = true;
                                }

                                if (joinedMarixList.size() > 0) {
                                    //実行するタイマーを特定
                                    selectedTimer = matrixTimerList.get(0);
                                    //タイマー時間セット
                                    initTimer(selectedTimer);
                                    //文字列表示
                                    showCategory(joinedMarixList, selectedTimer, interval);
                                }


                            } catch (Exception e) {

                                Common.log(e.getMessage());

                            }

                            bts[0].setText(getString(R.string.bt_start_text));
                            timerSecond = 0;
                            break;

                        case R.id.bt_start:

                            //タイマー起動かどうかで処理分ける
                            if (isStart == true) {

                                //タイマー起動中
                                timer.cancel();
                                //isStart
                                isStart = false;
                                //ボタンの文言変更
                                bts[0].setText(getString(R.string.bt_restart_text));

                            } else {

                                //start Timer準備
                                int sec = Integer.parseInt(setTimeTextSecond.getText().toString());
                                int min = Integer.parseInt(setTimeTextMinute.getText().toString());
                                timerSecond = sec + (min * 60);

                                if (timerSecond == 0) {
                                    //カウントがセットされていない
                                    Toast.makeText(RenzokuTimerActivity.this, getString(R.string.error_text_1), Toast.LENGTH_SHORT).show();

                                } else {

                                    //start timer
                                    startTimer(timerSecond);

                                    //isStart
                                    isStart = true;
                                    //ボタンの文言変更
                                    bts[0].setText(getString(R.string.bt_stop_text));

                                    //showCategory
                                    List<JoinedMarix> matrixList = manager.getJoinedMatrixListByCategoryId(categoryId);
                                    showCategory(matrixList, matrixTimerList.get(0), interval);

                                }

                            }

                            break;
                    }

                }
            });
        }

    }

    //timer を初期化する
    public void initTimer(Timer timer) {

        int second = 0;
        int minute = 0;
        if (timer.getTimer_second() < 60) {
            second = timer.getTimer_second();
            Common.log(String.valueOf(second));
        } else {
            second = timer.getTimer_second() % 60;
            minute = timer.getTimer_second() / 60;
        }

        setTimeTextSecond.setText(String.format("%02d", second));
        setTimeTextMinute.setText(String.format("%02d", minute));

    }

    //Timer start!
    public void startTimer(int second) {


        timer = new CountDownTimer(second * 1000, 10) {
            @Override
            public void onTick(long l) {

                //millitimesから表示する秒数、分数を取得する
                setTimeTextSecond.setText(Common.getSecond(l));
                setTimeTextMinute.setText(Common.getMinutes(l));

            }

            @Override
            public void onFinish() {

                //実行したタイマーは実行リストから除去
                if (matrixTimerList.size() > 0) {

                    //Intervalをセットする
                    if (interval == true) {

                        Timer timer = new Timer();
                        timer.setTimer_title("interval");
                        timer.setTimer_second(INTERVAL_TIME);
                        initTimer(timer);
                        startTimer(INTERVAL_TIME + 1);

                        List<JoinedMarix> matrixList = manager.getJoinedMatrixListByCategoryId(categoryId);
                        showCategory(matrixList, matrixTimerList.get(0), interval);
                        interval = false;

                    } else {

                        matrixTimerList.remove(0);

                        try {

                            //実行タイマーが残っていればタイマーを実行
                            if (matrixTimerList.size() > 0) {

                                //実行タイマーの順番をインクリメント
                                order++;

                                initTimer(matrixTimerList.get(0));
                                startTimer(matrixTimerList.get(0).getTimer_second() + 1);

                                List<JoinedMarix> matrixList = manager.getJoinedMatrixListByCategoryId(categoryId);
                                showCategory(matrixList, matrixTimerList.get(0), false);

                                if (matrixTimerList.size() > 1) {
                                    interval = true;
                                }


                            } else {

                                //終了
                                setTimeTextSecond.setText("00");
                                setTimeTextSecond.setText("00");
                                //isStart
                                isStart = false;
                                //ボタンの文言変更
                                bts[0].setText(getString(R.string.bt_start_text));
                                //make sound

                            }


                        } catch (Exception e) {

                            Toast.makeText(RenzokuTimerActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();

                        }


                    }


                }


            }


        }.start();


    }


    //カテゴリ表示
    public void showCategory(List<JoinedMarix> list, Timer selectedTimer, boolean isInterval) {


        TimerTime timeManager = new TimerTime();

        //VIEW
        int i = 1;
        timer_navgation_area.removeAllViews();
        for (JoinedMarix matrix : list) {

            TextView textView = new TextView(RenzokuTimerActivity.this);
            timeManager.change(matrix.getTimer().getTimer_second());
            int second = timeManager.getSecond();
            int minute = timeManager.getMinute();

            textView.setText(matrix.getTimer().getTimer_title() + "\n(" + String.format("%02d", minute) + ":" + String.format("%02d", second) + ")");
            textView.setPadding(5, 3, 5, 3);
            textView.setTextSize(15);
            textView.setGravity(Gravity.CENTER);

            if (order == matrix.getMatrix().getShowOrder()) {
                textView.setTextColor(Color.RED);
            }


            ImageView img = new ImageView(RenzokuTimerActivity.this);
            img.setImageDrawable(getDrawable(android.R.drawable.ic_lock_idle_alarm));
            timer_navgation_area.addView(img);
            timer_navgation_area.addView(textView);

            if (i < list.size()) {
                TextView arrow = new TextView(RenzokuTimerActivity.this);
                arrow.setText("→");
                timer_navgation_area.addView(arrow);
            }


            i++;

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
