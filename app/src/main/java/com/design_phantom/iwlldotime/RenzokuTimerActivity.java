package com.design_phantom.iwlldotime;

import android.content.ContentValues;
import android.content.Intent;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
    private TextView penginComment;
    private Button[] bts;

    //
    private Timer selectedTimer;
    private List<Timer> matrixTimerList;

    //class
    private TimerCategorySample manager;

    //タイマー関連
    private int timerSecond = 0;
    private boolean isStart = false;
    CountDownTimer timer;

    //連続タイマー用
    private boolean interval = true;

    //現在実行中のタイマーID
    private int currentTimerId = 2;
    //現在実行中のカテゴリーID
    private int currentCategoryid = 2;

    //タイマータイプ : 0:通常 1:登録タイマー 2:連続タイマー
    private int timerType = 0;

    //あとで削除
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzoku_timer);

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);

        //list ※実行するタイマーをセット
        matrixTimerList = new ArrayList<>();

        //アニメ部分 a1アニメ
        findViewById(R.id.monster).startAnimation(AnimationUtils.loadAnimation(this, R.anim.a1));
        //ペンギンmsg
        penginComment = findViewById(R.id.pengin_comment);

        //カテゴリエリアにカテゴリ情報を表示
        description = findViewById(R.id.description);


        //遷移元からintentにセットされた値を取得する。
        Intent intent = getIntent();
        int categoryId = intent.getIntExtra("categoryId", 0);

        try{

            //Category list
            final TimerCategorySample manager = new TimerCategorySample(RenzokuTimerActivity.this);
            List<JoinedMarix> joinedMarixList = manager.getJoinedMatrixListByCategoryId(categoryId);

            for(JoinedMarix matrix : joinedMarixList){
                //実行するタイマーをセットする
                matrixTimerList.add(matrix.getTimer());
                Common.log(matrix.getTimer().getTimer_title());
            }

            if(joinedMarixList.size() > 0){
                initTimer(matrixTimerList.get(0));
                showCategory( categoryId, matrixTimerList.get(0).getTimer_id());
            }


        }catch(Exception e){





        }


        //戻るボタン
        Button btBack = findViewById(R.id.bt_move_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
                    int second = 0;
                    int minute = 0;
                    switch (view.getId()) {

                        case R.id.bt_clear:

                            if (timer != null) {
                                timer.cancel();
                            }

                            isStart = false;

                            //isStart = false;
                            setTimeTextSecond.setText("00");
                            setTimeTextMinute.setText("00");
                            bts[0].setText("スタート!");
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
                                bts[0].setText("再開");

                            } else {

                                //start Timer準備
                                int sec = Integer.parseInt(setTimeTextSecond.getText().toString());
                                int min = Integer.parseInt(setTimeTextMinute.getText().toString());
                                timerSecond = sec + (min * 60);
                                Log.i("INFO", "timerSecond : " + String.valueOf(timerSecond));

                                if (timerSecond == 0) {
                                    //カウントがセットされていない
                                    Toast.makeText(RenzokuTimerActivity.this, "タイマーがセットされていません！", Toast.LENGTH_SHORT).show();

                                } else {

                                    //start timer
                                    startTimer(timerSecond);

                                    //isStart
                                    isStart = true;
                                    //ボタンの文言変更
                                    bts[0].setText("一時停止");

                                }

                            }

                            break;
                    }

                }
            });
        }

    }

    //
    public void initTimer(Timer timer) {

        setTimeTextSecond.setText(String.valueOf(timer.getTimer_second()));

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

                Toast.makeText(RenzokuTimerActivity.this, "size before:" + matrixTimerList.size(), Toast.LENGTH_SHORT).show();
                if(matrixTimerList.size() > 0){

                    //Intervalをセットする
                    if(interval == true){

                        Timer timer = new Timer();
                        timer.setTimer_title("interval");
                        timer.setTimer_second(INTERVAL_TIME);
                        initTimer(timer);
                        startTimer(INTERVAL_TIME+1);
                        interval = false;

                    }else{

                        matrixTimerList.remove(0);

                        try {

                            //実行タイマーが残っていればタイマーを実行
                            if (matrixTimerList.size() > 0) {

                                initTimer(matrixTimerList.get(0));
                                startTimer(matrixTimerList.get(0).getTimer_second()+1);

                                if(matrixTimerList.size() > 1){
                                    interval = true;
                                }
                                

                            } else {

                                //終了
                                setTimeTextSecond.setText("00");
                                setTimeTextSecond.setText("00");
                                //isStart
                                isStart = false;
                                //ボタンの文言変更
                                bts[0].setText("スタート");
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
    public void showCategory(int currentCategoryid, int currentTimerId) {

        StringBuilder builder = new StringBuilder();

        List<Category> listCategory = TimerCategorySample.getCategorySample();
        List<Timer> listTimer = TimerCategorySample.getTimerSampleByCategoryId(0);

        builder.append(listCategory.get(0).getCategory_name() + "\n");

        for (int i = 0; i < listTimer.size(); i++) {
            builder.append(listTimer.get(i).getTimer_title() + "(" + listTimer.get(i).getTimer_minutes() + " : " + listTimer.get(i).getTimer_second() + ")" + "→");
        }

        description.setText(builder.toString());

    }


}
