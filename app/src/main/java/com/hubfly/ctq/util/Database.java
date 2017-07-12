package com.hubfly.ctq.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.hubfly.ctq.Model.CtoModel;

import java.util.ArrayList;

/**
 * Created by Admin on 04-04-2017.
 */

public class Database extends SQLiteOpenHelper {

    static final int DATABASE_VERSION = 1;
    static final String DATABASE_NAME = "js_auto_cast";
    String TABLE_CTO = "js_cto";
    String KEY_TASK_NAME = "taskname";
    String KEY_ID = "id";
    String KEY_STATUS = "status";


    public Database(Context mContext) {
        super(mContext, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_CTO + "("
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + KEY_TASK_NAME + " TEXT,"
                + KEY_STATUS + " BOOLEAN" + ")";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CTO);
        onCreate(db);
    }

    public void addCTO(CtoModel mCtoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, mCtoModel.getTaskName());
        if (mCtoModel.isChecked()) {
            values.put(KEY_STATUS, "1");
        } else {
            values.put(KEY_STATUS, "0");
        }
        db.insert(TABLE_CTO, null, values);
        db.close();
    }


    public ArrayList<CtoModel> getAllCTO() {
        ArrayList<CtoModel> mAlCto = new ArrayList<CtoModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_CTO;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);


        if (cursor.moveToFirst()) {
            do {
                CtoModel contact = new CtoModel();
                contact.setId(cursor.getInt(0));
                contact.setTaskName(cursor.getString(1));
                boolean value = Integer.parseInt(cursor.getString(2)) > 0;
                contact.setChecked(value);
                // Adding contact to list
                mAlCto.add(contact);
            } while (cursor.moveToNext());
        }

        return mAlCto;
    }

    public void delete() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from " + TABLE_CTO);
    }

    public void deleteCTO(CtoModel mCtoModel) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CTO, KEY_ID + " = ?",
                new String[]{String.valueOf(mCtoModel.getId())});
        db.close();
    }

    public int updateCTO(CtoModel mCtoModel) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_TASK_NAME, mCtoModel.getTaskName());
        if (mCtoModel.isChecked()) {
            values.put(KEY_STATUS, "1");
        } else {
            values.put(KEY_STATUS, "0");
        }
        return db.update(TABLE_CTO, values, KEY_ID + " = ?",
                new String[]{String.valueOf(mCtoModel.getId())});
    }

}
