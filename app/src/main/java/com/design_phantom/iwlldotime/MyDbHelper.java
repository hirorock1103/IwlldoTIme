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

public class MyDbHelper extends SQLiteOpenHelper{
    private final static int version = 1;
    public final static String DBNAME = "PenginTimer.db";
    public static final String TABLE_CATEGORY = "Category";
    public static final String CATEGORY_COLUMN_ID = "category_id";
    public static final String CATEGORY_COLUMN_NAME = "category_name";
    public static final String CATEGORY_COLUMN_CREATEDATE = "category_createdate";

    public MyDbHelper(Context context) {
        super(context, DBNAME, null, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String query = "CREATE TABLE " + TABLE_CATEGORY + "(" +
                CATEGORY_COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                CATEGORY_COLUMN_NAME + " text, " +
                CATEGORY_COLUMN_CREATEDATE + " text " +
                ");" ;
        db.execSQL(query);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String query = "DROP TABLE IF EXISTS " + TABLE_CATEGORY;
        db.execSQL(query);
        onCreate(db);
    }

    /**
     * Category
     */
    //Add new Product
    public void addCategory(Category category){
        ContentValues values = new ContentValues();
        values.put(CATEGORY_COLUMN_NAME,category.getCategory_name());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_CATEGORY,null,values);
    }

    //delete
    public void deleteCategory(int categoryId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM " + TABLE_CATEGORY + " WHERE " + CATEGORY_COLUMN_ID +" = " + categoryId);
    }

    //select
    public List<Category> getCategoryList(){
        List<Category> list = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY + " ORDER BY " + CATEGORY_COLUMN_ID + " DESC ";
        Cursor c = db.rawQuery(query,null);
        while(!c.isAfterLast()){
            Category category = new Category();
            category.setCategory_id(c.getInt(c.getColumnIndex(CATEGORY_COLUMN_ID)));
            category.setCategory_name(c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)));
            category.setCreatedate(c.getString(c.getColumnIndex(CATEGORY_COLUMN_CREATEDATE)));
            list.add(category);
            c.moveToNext();
        }

        return list;
    }


    //Print
    public String getCategoryToString (){
        String dbString = "";
        SQLiteDatabase db = getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_CATEGORY + " ORDER BY " + CATEGORY_COLUMN_ID + " DESC ";
        //Cursor
        Cursor c = db.rawQuery(query,null);
        c.moveToFirst();

        while(!c.isAfterLast()){
            if( c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME)) != null ){
                dbString += c.getString(c.getColumnIndex(CATEGORY_COLUMN_ID));
                dbString += c.getString(c.getColumnIndex(CATEGORY_COLUMN_NAME));
                dbString += "\n";
            }
            c.moveToNext();
        }
        db.close();
        return dbString;
    }
}
