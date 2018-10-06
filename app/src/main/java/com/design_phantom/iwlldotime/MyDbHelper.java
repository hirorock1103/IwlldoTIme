package com.design_phantom.iwlldotime;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amb01 on 2018/09/17.
 */

public class MyDbHelper extends SQLiteOpenHelper {
    private final static int version = 6;
    public final static String DBNAME = "PenginTimer.db";
    //Category
    public static final String TABLE_CATEGORY = "Category";
    public static final String CATEGORY_COLUMN_ID = "category_id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";
    public static final String CATEGORY_COLUMN_CREATEDATE = "category_createdate";

    //Timer
    public static final String TABLE_TIMER = "Timer";
    public static final String TIMER_COLUMN_ID = "timer_id";
    public static final String TIMER_COLUMN_MINUTES = "timer_minutes";
    public static final String TIMER_COLUMN_SECOND = "timer_second";
    public static final String TIMER_COLUMN_TITLE = "timer_title";
    public static final String TIMER_COLUMN_CREATEDATE = "category_createdate";

    //matrix
    public static final String TABLE_MATRIX = "TimerCategory";
    public static final String MATRIX_COLUMN_ID = "matrix_id";
    public static final String MATRIX_COLUMN_CATEGORY_ID = "category_id";
    public static final String MATRIX_COLUMN_TIMER_ID = "timer_id";
    public static final String MATRIX_COLUMN_ORDER = "show_order";
    public static final String MATRIX_COLUMN_CREATEDATE = "createdate";

    public MyDbHelper(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_NAME + " text, " +
                CATEGORY_COLUMN_CREATEDATE + " text " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_TIMER + "(" +
                TIMER_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                TIMER_COLUMN_MINUTES + " text, " +
                TIMER_COLUMN_SECOND + " text, " +
                TIMER_COLUMN_TITLE + " text, " +
                TIMER_COLUMN_CREATEDATE + " text " +
                ");";
        db.execSQL(query);

        query = "CREATE TABLE " + TABLE_MATRIX + "(" +
                MATRIX_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MATRIX_COLUMN_CATEGORY_ID + " INTEGER, " +
                MATRIX_COLUMN_TIMER_ID + " INTEGER, " +
                MATRIX_COLUMN_ORDER + " INTEGER, " +
                MATRIX_COLUMN_CREATEDATE + " text " +
                ");";
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        String query = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_TIMER;
        db.execSQL(query);

        query = "DROP TABLE IF EXISTS " + TABLE_MATRIX;
        db.execSQL(query);

        onCreate(db);
    }

    /**
     * Category関連
     */
    //Add new Product
    public int addCategory(Category category) {
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME, category.getCategory_name());
        SQLiteDatabase db = getWritableDatabase();
        long insertId = db.insert(TABLE_CATEGORY, null, values);
        return (int) insertId;
    }

    //select
    public List<Category> getCategoryList() {
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY;
        //String query = "SELECT * FROM " + TABLE_CATEGORY + " ORDER BY " + CATEGORY_COLUMN_ID + " DESC ";
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {
            Category category = new Category();
            //Log.i("DBTEST",c.getString(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategory_id(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));
            list.add(category);
            c.moveToNext();
        }
        db.close();

        return list;
    }

    //delete
    public void deleteCategory(int categoryId) {
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE " + CATEGORY_COLUMN_ID + " = " + categoryId);
    }

    /**
     * Timer関連
     */

    //addTimer
    public void addTimer(Timer timer) {
        ContentValues values = new ContentValues();
        values.put(TIMER_COLUMN_TITLE, timer.getTimer_title());
        values.put(TIMER_COLUMN_MINUTES, timer.getTimer_minutes());
        values.put(TIMER_COLUMN_SECOND, timer.getTimer_second());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_TIMER, null, values);
    }


    //truncate
    public void deleteAllCategory() {
        String sql = "DELETE FROM " + TABLE_CATEGORY;
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(sql);
    }


    //select
    public List<Timer> getTimerList() {
        List<Timer> list = new ArrayList<>();

        SQLiteDatabase db = getWritableDatabase();
        String sql = "SELECT * FROM " + TABLE_TIMER + " ORDER BY " + TIMER_COLUMN_ID + " DESC";
        Log.i("INFO", sql);
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();

        while (!c.isAfterLast()) {

            Timer timer = new Timer();
            timer.setTimer_id(c.getInt(c.getColumnIndex(TIMER_COLUMN_ID)));
            timer.setTimer_title(c.getString(c.getColumnIndex(TIMER_COLUMN_TITLE)));
            timer.setTimer_second(c.getInt(c.getColumnIndex(TIMER_COLUMN_SECOND)));
            list.add(timer);

            c.moveToNext();
        }

        return list;
    }

    /**
     * Matrix関連
     */

    //select
    public List<TimerCategory> getMatrixList() {
        List<TimerCategory> list = new ArrayList<>();
        return list;
    }

    public List<TimerCategory> getMatrixListByCategoryId(int id) {
        List<TimerCategory> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_MATRIX +
                " WHERE " + MATRIX_COLUMN_CATEGORY_ID + " = " + id;

        sql = "SELECT * FROM " + TABLE_MATRIX + " INNER JOIN " + TABLE_CATEGORY +
                " ON " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID + " = " + TABLE_MATRIX + "." + MATRIX_COLUMN_CATEGORY_ID +
                " WHERE " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            TimerCategory matrix = new TimerCategory();
            matrix.setMatrix_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_ID)));
            matrix.setCategory_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_CATEGORY_ID)));
            matrix.setTimer_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_TIMER_ID)));
            matrix.setShowOrder(c.getInt(c.getColumnIndex(MATRIX_COLUMN_ORDER)));
            //Log.i("INFO", c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            list.add(matrix);
            c.moveToNext();
        }

        return list;
    }

    public List<JoinedMarix> getJoinedMatrixListByCategoryId(int id) {
        List<JoinedMarix> list = new ArrayList<>();
        String sql = "SELECT * FROM " + TABLE_MATRIX +
                " WHERE " + MATRIX_COLUMN_CATEGORY_ID + " = " + id;

        sql = "SELECT *" +
                " FROM " + TABLE_MATRIX + " INNER JOIN " + TABLE_CATEGORY +
                " ON " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID + " = " + TABLE_MATRIX + "." + MATRIX_COLUMN_CATEGORY_ID +
                " INNER JOIN " + TABLE_TIMER +
                " ON " + TABLE_TIMER + "." + TIMER_COLUMN_ID + " = " + TABLE_MATRIX + "." + MATRIX_COLUMN_TIMER_ID +
                " WHERE " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID + " = " + id;

        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        while (!c.isAfterLast()) {
            TimerCategory matrix = new TimerCategory();
            Timer timer = new Timer();
            Category category = new Category();
            JoinedMarix joinedMarix = new JoinedMarix();

            //Matrix11
            matrix.setCategory_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_CATEGORY_ID)));
            matrix.setTimer_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_TIMER_ID)));
            matrix.setShowOrder(c.getInt(c.getColumnIndex(MATRIX_COLUMN_ORDER)));

            //Category
            category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            //Timer
            timer.setTimer_title(c.getString(c.getColumnIndex(TIMER_COLUMN_TITLE)));
            timer.setTimer_second(c.getInt(c.getColumnIndex(TIMER_COLUMN_SECOND)));
            timer.setTimer_id(c.getInt(c.getColumnIndex(TIMER_COLUMN_ID)));

            joinedMarix.setMatrix(matrix);
            joinedMarix.setCategory(category);
            joinedMarix.setTimer(timer);

            list.add(joinedMarix);
            c.moveToNext();
        }

        return list;
    }

    public List<JoinedMarix> getJoinedMatrixList(){

        List<JoinedMarix> list = new ArrayList<>();
        String sql = "SELECT *" +
                " FROM " + TABLE_MATRIX + " INNER JOIN " + TABLE_CATEGORY +
                " ON " + TABLE_CATEGORY + "." + CATEGORY_COLUMN_ID + " = " + TABLE_MATRIX + "." + MATRIX_COLUMN_CATEGORY_ID +
                " INNER JOIN " + TABLE_TIMER +
                " ON " + TABLE_TIMER + "." + TIMER_COLUMN_ID + " = " + TABLE_MATRIX + "." + MATRIX_COLUMN_TIMER_ID +
                " ORDER BY "+ TABLE_MATRIX + "." + MATRIX_COLUMN_ID + " DESC ";
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.rawQuery(sql, null);
        c.moveToFirst();
        while(!c.isAfterLast()){
            TimerCategory matrix = new TimerCategory();
            Timer timer = new Timer();
            Category category = new Category();
            JoinedMarix joinedMarix = new JoinedMarix();

            //Matrix11
            matrix.setCategory_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_CATEGORY_ID)));
            matrix.setTimer_id(c.getInt(c.getColumnIndex(MATRIX_COLUMN_TIMER_ID)));
            matrix.setShowOrder(c.getInt(c.getColumnIndex(MATRIX_COLUMN_ORDER)));

            //Category
            category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            //Timer
            timer.setTimer_title(c.getString(c.getColumnIndex(TIMER_COLUMN_TITLE)));
            timer.setTimer_second(c.getInt(c.getColumnIndex(TIMER_COLUMN_SECOND)));

            joinedMarix.setMatrix(matrix);
            joinedMarix.setCategory(category);
            joinedMarix.setTimer(timer);

            list.add(joinedMarix);
            c.moveToNext();
        }

        return list;


    }


    public int addMatrix(TimerCategory matrix) {

        ContentValues values = new ContentValues();
        SQLiteDatabase db = getWritableDatabase();
        values.put(MATRIX_COLUMN_CATEGORY_ID, matrix.getCategory_id());
        values.put(MATRIX_COLUMN_TIMER_ID, matrix.getTimer_id());
        values.put(MATRIX_COLUMN_ORDER, matrix.getShowOrder());
        long insertId = db.insert(TABLE_MATRIX, null, values);
        return (int) insertId;
    }


}
