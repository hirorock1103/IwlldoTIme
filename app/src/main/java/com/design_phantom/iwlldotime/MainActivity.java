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


public class MainActivity extends AppCompatActivity {

    //View
    private TextView setTimeTextSecond;
    private TextView setTimeTextMinute;
    private InputMethodManager inputMethodManager;
    private TextView penginComment;
    private EditText edit_title;
    private Button[] bts;
    private LinearLayout layout;

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



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        layout = findViewById(R.id.layout);
        inputMethodManager = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);

        //登録タイマー
        timerType = getTimerType(currentCategoryid , currentTimerId);

        //アニメ部分 a1アニメ
        findViewById(R.id.monster).startAnimation(AnimationUtils.loadAnimation(this, R.anim.a1));

        //ペンギンmsg
        penginComment = findViewById(R.id.pengin_comment);

        //edit title area
        edit_title = findViewById(R.id.edit_title);

        //カテゴリエリアにカテゴリ情報を表示
        //showCategory(timerType,currentCategoryid,currentTimerId);

        //移動ボタン
        Button btMove = findViewById(R.id.bt_move);
        btMove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RenzokuTimerActivity.class);
                startActivity(intent);
            }
        });

        Button bt_move_to_matrix = findViewById(R.id.bt_move_to_make_matrix);
        bt_move_to_matrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MakeMtrxActivity.class);
                startActivity(intent);
            }
        });

        //表示時間
        setTimeTextSecond = findViewById(R.id.text_count_second);
        setTimeTextMinute = findViewById(R.id.text_count_minute);


        //ボタン
        bts = new Button[5];
        bts[0] = findViewById(R.id.bt_10second);
        bts[1] = findViewById(R.id.bt_1minute);
        bts[2] = findViewById(R.id.bt_10minute);
        //bts[3] = findViewById(R.id.bt_30minute);
        bts[3] = findViewById(R.id.bt_start);
        bts[4] = findViewById(R.id.bt_clear);

        for(int i = 0; i < bts.length; i++){

            bts[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int second = 0;
                    int minute = 0;
                    switch (view.getId()){

                        case R.id.bt_10second :
                            second += (Integer.parseInt(setTimeTextSecond.getText().toString()) + 10);
                            setTimeTextSecond.setText( String.format("%02d", second) );
                            break;
                        case R.id.bt_1minute :
                            minute += Integer.parseInt(setTimeTextMinute.getText().toString()) + 1;
                            setTimeTextMinute.setText( String.format("%02d", minute) );
                            break;
                        case R.id.bt_10minute :
                            minute += Integer.parseInt(setTimeTextMinute.getText().toString()) + 10;
                            setTimeTextMinute.setText( String.format("%02d", minute) );
                            break;

                        case R.id.bt_clear :

                            if(timer != null){
                                timer.cancel();
                            }

                            isStart = false;

                            //isStart = false;
                            setTimeTextSecond.setText("00");
                            setTimeTextMinute.setText("00");
                            bts[3].setText("スタート!");
                            timerSecond = 0;
                            isPushedFirstStart = false;
                            break;

                        case R.id.bt_start :

                            //タイマー起動かどうかで処理分ける
                            if(isStart == true){

                                //タイマー起動中
                                timer.cancel();
                                //isStart
                                isStart = false;
                                //ボタンの文言変更
                                bts[3].setText("再開");


                            }else{

                                //start Timer準備
                                int sec = Integer.parseInt(setTimeTextSecond.getText().toString());
                                int min = Integer.parseInt(setTimeTextMinute.getText().toString());
                                timerSecond = sec + (min * 60);

                                if(timerSecond == 0){
                                    //カウントがセットされていない
                                    Toast.makeText(MainActivity.this, "タイマーがセットされていません！", Toast.LENGTH_SHORT).show();

                                }else{

                                    //start timer
                                    startTimer(timerSecond);

                                    //登録するかどうかのジャッジ判定
                                    if(isPushedFirstStart == false){

                                        if(edit_title.getText().toString().equals("") == false ){

                                            penginComment.setText("『" +  edit_title.getText() + "』というタイトルでタイマーを登録するよ！");
                                            Toast.makeText(MainActivity.this,"timerStart(初回) & 登録対象："
                                                    + edit_title.getText().toString() ,Toast.LENGTH_SHORT).show();
                                            TimerCategorySample manager = new TimerCategorySample(MainActivity.this);

                                            Timer timer = new Timer();
                                            timer.setTimer_title(edit_title.getText().toString());
                                            timer.setTimer_second(timerSecond);
                                            manager.addTimer(timer);

                                        }else{

                                            Toast.makeText(MainActivity.this, "タイマーを登録するにはタイトルを入力してください。", Toast.LENGTH_SHORT).show();

                                        }

                                    }

                                    //isStart
                                    isStart = true;
                                    //ボタンの文言変更
                                    bts[3].setText("一時停止");

                                    //初回push判定
                                    isPushedFirstStart = true;

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

                //millitimesから表示する秒数、分数を取得する
                setTimeTextSecond.setText(Common.getSecond(l));
                setTimeTextMinute.setText(Common.getMinutes(l));
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

    }



}
