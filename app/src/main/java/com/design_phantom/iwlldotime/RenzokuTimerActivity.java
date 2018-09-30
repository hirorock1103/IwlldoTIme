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

    //View
    private TextView setTimeTextSecond;
    private TextView setTimeTextMinute;
    private InputMethodManager inputMethodManager;
    private TextView penginComment;
    private EditText edit_title;
    private Button[] bts;
    private LinearLayout layout;
    private LinearLayout layout_select_category;

    //
    private Timer selectedTimer;
    private List<Timer> matrixTimerList;


    //class
    private TimerCategorySample manager;

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

        //list
        matrixTimerList = new ArrayList<>();

        //登録タイマー
        timerType = getTimerType(currentCategoryid , currentTimerId);

        //アニメ部分 a1アニメ
        findViewById(R.id.monster).startAnimation(AnimationUtils.loadAnimation(this, R.anim.a1));

        //ペンギンmsg
        penginComment = findViewById(R.id.pengin_comment);

        //edit title area
        edit_title = findViewById(R.id.edit_title);

        //layout
        layout = findViewById(R.id.layout);
        layout_select_category = findViewById(R.id.layout_select_category);

        //Category list
        final TimerCategorySample manager = new TimerCategorySample(RenzokuTimerActivity.this);
        List<Category> list = manager.getCategoryList();

        for(Category category : list){
            Button bt = new Button(RenzokuTimerActivity.this);
            bt.setText(category.getCategory_name());
            bt.setTag(category.getCategory_id());
            bt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //ボタン押下時に発動
                    int tag = Integer.parseInt(String.valueOf(view.getTag()));

                    //カテゴリに紐づくタイマーを取得する
                    List<JoinedMarix> joinedMatrix = manager.getJoinedMatrixListByCategoryId(tag);

                    if(joinedMatrix.size() > 0 ){

                        //実行するタイマー
                        selectedTimer = null;
                        matrixTimerList = new ArrayList<>();

                        //category_name
                        String categoryName = joinedMatrix.get(0).getCategory().getCategory_name();
                        description.setText("");
                        StringBuilder builder = new StringBuilder();
                        builder.append(categoryName+"\n");
                        //timer
                        for(JoinedMarix matrix : joinedMatrix){
                            //必要な情報　category_id, category_name, timer_name, timer_second
                            String btText = "";
                            btText += matrix.getTimer().getTimer_title()  +"/"+ matrix.getTimer().getTimer_second();
                            btText+="→";
                            builder.append(btText);

                            //実行するタイマーリストを作成する
                            Timer timer = matrix.getTimer();
                            matrixTimerList.add(timer);
                            //初回タイマーをセット
                            if(selectedTimer == null){
                                selectedTimer = timer;
                            }
                        }
                        description.setText(builder.toString());

                        //順番の最もわかいタイマーをセットするtimer
                        initTimer(selectedTimer);
                    }


                }
            });

            layout_select_category.addView(bt);

        }

        //カテゴリエリアにカテゴリ情報を表示
        description = findViewById(R.id.description);
        showCategory(timerType,currentCategoryid,currentTimerId);

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);

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

    //
    public void initTimer(Timer timer){

        setTimeTextSecond.setText(String.valueOf(timer.getTimer_second()));

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

                //millitimesから表示する秒数、分数を取得する
                setTimeTextSecond.setText(Common.getSecond(l));
                setTimeTextMinute.setText(Common.getMinutes(l));

            }

            @Override
            public void onFinish() {

                //実行したタイマーは実行リストから除去
                try{
                    Toast.makeText(RenzokuTimerActivity.this, "size before:" + matrixTimerList.size(), Toast.LENGTH_SHORT).show();
                    matrixTimerList.remove(0);
                    Toast.makeText(RenzokuTimerActivity.this, "size after:" + matrixTimerList.size(), Toast.LENGTH_SHORT).show();
                    //実行タイマーが残っていればタイマーを実行
                    if(matrixTimerList.size() > 0){

                        initTimer(matrixTimerList.get(0));
                        startTimer(matrixTimerList.get(0).getTimer_second());

                    }else{

                        //終了
                        setTimeTextSecond.setText("00");
                        setTimeTextSecond.setText("00");

                        //isStart
                        isStart = false;

                        //ボタンの文言変更
                        bts[3].setText("スタート");
                    }
                }catch(Exception e){
                    Toast.makeText(RenzokuTimerActivity.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }

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
