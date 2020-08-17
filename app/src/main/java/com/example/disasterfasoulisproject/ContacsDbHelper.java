package com.example.disasterfasoulisproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ContacsDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "ContacsDB.db";
    public static final int DATABASE_VERSION = 1;
    public static final String TEXT_TYPE = " TEXT";
    public static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_MOVIE_TABLE =
            "CREATE TABLE " + ContacsDbContract.ContacsEntry.TABLE_NAME + " (" +
                    ContacsDbContract.ContacsEntry._ID + " INTEGER PRIMARY KEY," +
                    ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_TITLE + " TEXT," +
                    ContacsDbContract.ContacsEntry.COLUMN_NAME_CONTACT_PHONE + TEXT_TYPE + ")";


    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + ContacsDbContract.ContacsEntry.TABLE_NAME;

    public ContacsDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        //SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(SQL_DELETE_ENTRIES);
        onCreate(sqLiteDatabase);
    }
}
