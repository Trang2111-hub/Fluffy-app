package com.fluffy.app.util;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.content.ContentValues;

public class PasswordStorage extends SQLiteOpenHelper {

    private static final String DB_NAME = "pass.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE = "password_store";
    private static final String COLUMN_PASS = "password";

    public PasswordStorage(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE + "(" + COLUMN_PASS + " TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldV, int newV) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }

    public void savePassword(String pass) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE, null, null);  // Xoá pass cũ (nếu có)
        ContentValues values = new ContentValues();
        values.put(COLUMN_PASS, pass);
        db.insert(TABLE, null, values);
        db.close();
    }

    public String getPassword() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT " + COLUMN_PASS + " FROM " + TABLE + " LIMIT 1", null);
        String result = null;
        if (cursor.moveToFirst()) {
            result = cursor.getString(0);
        }
        cursor.close();
        db.close();
        return result;
    }
}

