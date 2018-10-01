package com.design_phantom.iwlldotime;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by amb01 on 2018/09/15.
 */

public class TimerCategorySample extends MyDbHelper {

    public TimerCategorySample(Context context) {
        super(context);
    }

    public int addCategory(Category category) {
        return super.addCategory(category);
    }

    public void addTimer(Timer timer) {
        super.addTimer(timer);
    }


    //Sample を返す
    public static List<Category> getCategorySample() {
        List<Category> list = new ArrayList<>();

        String[] categoryArr = {"筋トレ", "電車の中", "始業時", "ランニング"};

        for (int i = 0; i < categoryArr.length; i++) {
            Category category = new Category();
            category.setCategory_id(i + 1);
            category.setCategory_name(categoryArr[i]);
            category.setCreatedate("2018/01/22");
            Random random = new Random();
            category.setUse_count(random.nextInt(100));
            list.add(category);
        }

        return list;

    }

    //Category
    public List<Category> getCategoryList() {

        List<Category> list = super.getCategoryList();

        return list;
    }

    //matrix
    public static List<TimerCategory> getMatrix() {
        List<TimerCategory> list = new ArrayList<>();

        int[] idList = {1, 2, 3, 4};

        for (int i = 0; i < idList.length; i++) {
            TimerCategory matrix = new TimerCategory();
            matrix.setMatrix_id(idList[i]);
            matrix.setTimer_id(idList[i]);
            matrix.setCategory_id(1);
            matrix.setCreatedate("2018/09/12");
            list.add(matrix);
        }

        return list;
    }

    public List<TimerCategory> getMatrixListByCategoryId(int id) {

        return super.getMatrixListByCategoryId(id);
    }

    public List<JoinedMarix> getJoinedMatrixList(){
        return super.getJoinedMatrixList();
    }

    public List<JoinedMarix> getJoinedMatrixListByCategoryId(int id) {
        return super.getJoinedMatrixListByCategoryId(id);
    }

    public int addMatrix(TimerCategory matrix) {
        return super.addMatrix(matrix);
    }

    //sample
    public static List<Timer> getTimers() {

        List<Timer> list = new ArrayList<>();
        String[] titles = {"腕立て", "腹筋", "テキストフッター", "握力", "勉強", "英語"};
        Integer[] minutes = {12, 22, 14, 33, 22, 5};
        Integer[] secondes = {0, 0, 10, 0, 0, 0};
        for (int i = 0; i < titles.length; i++) {
            Timer timer = new Timer();
            timer.setTimer_id(i + 1);
            timer.setTimer_title(titles[i]);
            timer.setTimer_minutes(minutes[i]);
            timer.setTimer_second(secondes[i]);
            list.add(timer);
        }

        return list;
    }

    //sample
    public static Timer getTimerSampleByTimerId(int timerId) {

        Timer timer = new Timer();
        List<String[]> sampleListTitle = new ArrayList<>();
        List<Integer[]> sampleMinute = new ArrayList<>();
        List<Integer[]> sampleSecond = new ArrayList<>();

        String[] titles = {"test", "腕立て", "腹筋", "テキストフッター", "握力"};
        Integer[] minutes = {5, 4, 12, 1, 2};
        Integer[] seconds = {5, 12, 0, 0, 0};

        timer.setTimer_id(timerId);
        timer.setTimer_title(titles[timerId]);
        timer.setTimer_minutes(minutes[timerId]);
        timer.setTimer_second(seconds[timerId]);

        return timer;

    }

    //sample を返す
    public static List<Timer> getTimerSampleByCategoryId(int categoryId) {
        List<Timer> list = new ArrayList<>();
        List<String[]> sampleListTitle = new ArrayList<>();
        List<Integer[]> sampleMinute = new ArrayList<>();
        List<Integer[]> sampleSecond = new ArrayList<>();

        String[] timerTitleKintore = {"腕立て", "腹筋", "テキストフッター", "握力"};
        Integer[] timerKintoreMinute = {4, 12, 1, 2};
        Integer[] timerKintoreSecond = {12, 0, 0, 0};


        String[] timerTitleInTrain = {"test", "読書", "英語"};
        Integer[] timerInTrainMinute = {0, 24, 32};
        Integer[] timerInTrainSecond = {0, 12, 0};

        String[] timerTitleBeforeWork = {"test", "メールチェック", "デスクトップ整理"};
        Integer[] timerBeforeWorkMinute = {0, 4, 12};
        Integer[] timerBeforeWorkSecond = {0, 12, 0};

        String[] timerTitleRunning = {"test", "ランニング"};
        Integer[] timerRunningMinute = {0, 4};
        Integer[] timerRunningSecond = {0, 12};

        sampleListTitle.add(timerTitleKintore);
        sampleListTitle.add(timerTitleInTrain);
        sampleListTitle.add(timerTitleBeforeWork);
        sampleListTitle.add(timerTitleRunning);

        sampleMinute.add(timerKintoreMinute);
        sampleMinute.add(timerInTrainMinute);
        sampleMinute.add(timerBeforeWorkMinute);
        sampleMinute.add(timerRunningMinute);

        sampleSecond.add(timerKintoreSecond);
        sampleSecond.add(timerInTrainSecond);
        sampleSecond.add(timerBeforeWorkSecond);
        sampleSecond.add(timerRunningSecond);


        for (int i = 0; i < sampleListTitle.size(); i++) {

            if (i != categoryId) {
                continue;
            }

            for (int j = 0; j < sampleListTitle.get(categoryId).length; j++) {

                // title , minute, secondがわかる
                Timer timer = new Timer();
                timer.setTimer_id(j);
                timer.setTimer_title(sampleListTitle.get(i)[j]);
                timer.setTimer_minutes(sampleMinute.get(i)[j]);
                timer.setTimer_second(sampleSecond.get(i)[j]);

                list.add(timer);

            }

        }

        return list;

    }


}
