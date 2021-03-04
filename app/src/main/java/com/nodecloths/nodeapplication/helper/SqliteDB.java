package com.nodecloths.nodeapplication.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.nodecloths.nodeapplication.model.ApplyRecordLIst;

import java.util.ArrayList;
import java.util.List;

public class SqliteDB extends SQLiteOpenHelper {


    private final static String DATABASE_NAME = "BitList.db";
    private final static int VERSION = 1;
    private final static String TABLE_NAME = "APPLY";
    private final static String ID = "id";
    private final static String POST_ID = "post_id";
    private final static String STATUS = "status";

    private Context context;

    SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    private final static String CREATE_TABLE = "CREATE TABLE "
            + TABLE_NAME + "( " + ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + POST_ID + " VARCHAR(25),"
            + STATUS + " VARCHAR(25));";


    private final static String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;

    public SqliteDB(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }

    public long insertApplyList(String post_id, String status) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(POST_ID, post_id);
        contentValues.put(STATUS, status);
        long rowId = sqLiteDatabase.insert(TABLE_NAME, null, contentValues);
        return rowId;
    }



    public List<ApplyRecordLIst> applyList() {
        List<ApplyRecordLIst> data = new ArrayList<>();
        Cursor cursor = sqLiteDatabase.rawQuery("select * from APPLY", null);
        while (cursor.moveToNext()) {
            data.add(new ApplyRecordLIst(cursor.getString(0), cursor.getString(1), cursor.getString(2)));
        }
        return data;
    }


    public boolean productDelete(String id) {
        return sqLiteDatabase.delete(TABLE_NAME, ID + "=?", new String[]{id}) > 0;
    }

}