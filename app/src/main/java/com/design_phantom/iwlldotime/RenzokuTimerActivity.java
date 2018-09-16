package com.design_phantom.iwlldotime;

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

import java.util.List;
import java.util.TimerTask;


public class RenzokuTimerActivity extends AppCompatActivity {

    //View
    private TextView setTimeTextSecond;
    private TextView setTimeTextMinute;
    private InputMethodManager inputMethodManager;
    private TextView penginComment;
    private EditText edit_title;
    private Button[] bts;
    private LinearLayout layout;
    private LinearLayout layout_category;
    private LinearLayout layout_timer;

    //class
    private TimerManager timerManager;

    //タイマー関連
    private int timerSecond = 0;
    private boolean isStart = false;
    private boolean isPushedFirstStart = false;
    CountDownTimer timer;

    //現在実行中のタイマーID
    private int currentTimerId = 1;
    //現在実行中のカテゴリーID
    private int currentCategoryid = 1;

    //タイマータイプ : 0:通常 1:登録タイマー 2:連続タイマー
    private int timerType = 0;

    //あとで削除
    private TextView description;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_renzoku_timer);

        layout = findViewById(R.id.layout);
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //timerManager
        timerManager = new TimerManager();

        //登録タイマー
        timerType = getTimerType(currentCategoryid , currentTimerId);

        //アニメ部分 a1アニメ
        findViewById(R.id.monster).startAnimation(AnimationUtils.loadAnimation(this, R.anim.a1));

        //ペンギンmsg
        penginComment = findViewById(R.id.pengin_comment);

        //edit title area
        edit_title = findViewById(R.id.edit_title);
        //開閉式表示エリア
        layout_category = findViewById(R.id.layout_select_category);
        layout_timer = findViewById(R.id.layout_select_timer);


        //カテゴリエリアにカテゴリ情報を表示
        description = findViewById(R.id.description);
        showCategory(timerType,currentCategoryid,currentTimerId);

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);


        //開閉式
        final Button btOpen1 = findViewById(R.id.bt_open_category);
        btOpen1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_category.getVisibility() != View.VISIBLE){
                    //edit_title.setVisibility(View.INVISIBLE);
                    layout_category.animate().alphaBy(0).alpha(1).translationYBy(-10).translationY(0).withStartAction(new TimerTask() {

                        @Override
                        public void run() {
                            // アニメーションが始まる前にViewをVISIBLEにする
                            layout_category.setVisibility(View.VISIBLE);
                            //取得する
                            List<TimerCategory> matrixList = TimerCategorySample.getMatrix();
                            StringBuilder builder = new StringBuilder();
                            for (TimerCategory matrix : matrixList){
                                builder.append(matrix.getCategory_id());
                                builder.append("-");
                                builder.append(matrix.getTimer_id()+"\n");
                            }
                            TextView text = new TextView(RenzokuTimerActivity.this);
                            text.setText(builder.toString());
                            layout_category.addView(text);
                        }
                    });

                }else{
                    layout_category.animate().alpha(0).translationY(-10).withEndAction(new TimerTask(){
                        @Override
                        public void run() {

                            // アニメーションが終わったらViewをGONEにする
                            layout_category.setVisibility(View.GONE);
                            layout_category.removeAllViews();


                        }
                    });
                }

            }
        });


        //開閉式
        final Button btOpen2 = findViewById(R.id.bt_open_timer);
        btOpen2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(layout_timer.getVisibility() != View.VISIBLE){
                    //edit_title.setVisibility(View.INVISIBLE);
                    layout_timer.animate().alphaBy(0).alpha(1).translationYBy(-10).translationY(0).withStartAction(new TimerTask() {

                        @Override
                        public void run() {

                            //add view to layout
                            List<Timer> timerList = timerManager.getTimers();




                            // アニメーションが始まる前にViewをVISIBLEにする
                            layout_timer.setVisibility(View.VISIBLE);

                        }
                    });

                }else{
                    layout_timer.animate().alpha(0).translationY(-10).withEndAction(new TimerTask(){
                        @Override
                        public void run() {

                            // アニメーションが終わったらViewをGONEにする
                            layout_timer.setVisibility(View.GONE);

                        }
                    });
                }

            }
        });


        //戻るボタン
        Button btBack = findViewById(R.id.bt_move_back);
        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RenzokuTimerActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });


        //ボタン
        bts = new Button[2];
        bts[0] = findViewById(R.id.bt_start);
        bts[1] = findViewById(R.id.bt_clear);

        for(int i = 0; i < bts.length; i++){

            bts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int second = 0;
                    int minute = 0;
                    switch (view.getId()){

                        case R.id.bt_clear :

                            if(timer != null){
                                timer.cancel();
                            }

                            isStart = false;

                            //isStart = false;
                            setTimeTextSecond.setText("00");
                            setTimeTextMinute.setText("00");
                            bts[0].setText("スタート!");
                            timerSecond = 0;
                            break;

                        case R.id.bt_start :

                            //タイマー起動かどうかで処理分ける
                            if(isStart == true){

                                //タイマー起動中
                                timer.cancel();
                                //isStart
                                isStart = false;
                                //ボタンの文言変更
                                bts[0].setText("再開");

                            }else{

                                //start Timer準備
                                int sec = Integer.parseInt(setTimeTextSecond.getText().toString());
                                int min = Integer.parseInt(setTimeTextMinute.getText().toString());
                                timerSecond = sec + (min * 60);
                                Log.i("INFO" , "timerSecond : " + String.valueOf(timerSecond));

                                if(timerSecond == 0){
                                    //カウントがセットされていない
                                    Toast.makeText(RenzokuTimerActivity.this, "タイマーがセットされていません！", Toast.LENGTH_SHORT).show();

                                }else{

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

    //タイマーの種類
    public int getTimerType(int currentCategoryid,int currentTimerId){
        int type = 0;

        if(currentCategoryid == 0 && currentTimerId == 0){
            //新規タイマー
            type = 0;
        }else if(currentCategoryid == 0 && currentTimerId != 0){
            //登録タイマー（単発）
            type = 1;
        }else if(currentCategoryid != 0 && currentTimerId != 0){
            //連続タイマー
            type = 2;
        }else{
            //Error
        }

        return type;
    }

    //Timer start!
    public void startTimer(int second){


        timer = new CountDownTimer(second * 1000,10) {
            @Override
            public void onTick(long l) {

                //setTimeTextSecond.setText( String.format("%02d", second) );
                long ans = l/1000;
                //1分以上ある場合は余りを秒数にセットする
                if( (ans / 60) >= 1 ){
                    long sec = (ans % 60);
                    setTimeTextSecond.setText(String.format("%02d", sec));
                    setTimeTextMinute.setText(String.format("%02d", (int)Math.floor(ans/60)));
                }else{
                    setTimeTextSecond.setText(String.format("%02d", ans));
                    setTimeTextMinute.setText("00");
                }


            }

            @Override
            public void onFinish() {

                //終了
                setTimeTextSecond.setText("00");
                setTimeTextSecond.setText("00");

                //isStart
                isStart = false;

                //ボタンの文言変更
                bts[3].setText("スタート");

            }


        }.start();


    }

    //
    public boolean onTouchEvent(MotionEvent event) {
        //キーボードを隠す
        inputMethodManager.hideSoftInputFromWindow(layout.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        //背景にフォーカスを移す
        layout.requestFocus();

        return false;
    }

    //カテゴリ表示
    public void showCategory(int timerType,int currentCategoryid, int currentTimerId){

        StringBuilder builder = new StringBuilder();

        if(timerType == 0){

        }else if(timerType == 1){

            Timer timer = TimerCategorySample.getTimerSampleByTimerId(currentTimerId);
            builder.append(timer.getTimer_title() + "("+timer.getTimer_minutes() + ":" + timer.getTimer_second() +")");

        }else if(timerType == 2){

            List<Category> listCategory = TimerCategorySample.getCategorySample();
            List<Timer> listTimer = TimerCategorySample.getTimerSampleByCategoryId(0);

            builder.append(listCategory.get(0).getCategory_name() + "\n");

            for (int i = 0; i < listTimer.size(); i++){
                builder.append(listTimer.get(i).getTimer_title() + "("+ listTimer.get(i).getTimer_minutes() + " : " + listTimer.get(i).getTimer_second() +")" + "→");
            }

        }


        description.setText(builder.toString());

    }



}
