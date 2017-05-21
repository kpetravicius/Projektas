package com.example.aloyzas.medicineReminder.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.aloyzas.medicineReminder.database.MedicineDbSchema.MedicineTable;

/**
 * Created by Aloyzas on 2017-05-05.
 */

public class MedicineBaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "medicineBase.db";

    public MedicineBaseHelper(Context context){
        super(context, DATABASE_NAME,null,VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create table " + MedicineTable.NAME + "("+
                " _id integer primary key autoincrement,"+
                MedicineTable.Cols.UUID + ", "+
                MedicineTable.Cols.TITLE + ", "+
                MedicineTable.Cols.DATE + ", " +
                MedicineTable.Cols.TAKEN + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){

    }
}
