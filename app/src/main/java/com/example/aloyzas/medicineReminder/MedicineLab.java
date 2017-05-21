package com.example.aloyzas.medicineReminder;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.aloyzas.medicineReminder.database.MedicineBaseHelper;
import com.example.aloyzas.medicineReminder.database.MedicineDbSchema.MedicineTable;
import com.example.aloyzas.medicineReminder.database.MedicineCursorWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * Created by Aloyzas on 2016-12-30.
 * Singleton class. Allows app to have one owner of the medicine data
 * and provides a way to easily pass that data between controller classes.
 * Creates a class with private constructor
 * and a get() method witch if instance exists simply returns value
 */

public class MedicineLab {
    private static MedicineLab sMedicineLab;

    private Context mContext;
    private SQLiteDatabase mDataBase;

    public static MedicineLab get (Context context){
        if (sMedicineLab ==null){
            sMedicineLab = new MedicineLab(context);
        }
        return sMedicineLab;
    }

    private MedicineLab(Context context){
        //Opens dataBase
        mContext = context.getApplicationContext();
        mDataBase = new MedicineBaseHelper(mContext).getWritableDatabase();

    }
    public void addMedicines(Medicine c){
        ContentValues values = getContentValues(c);

        mDataBase.insert(MedicineTable.NAME, null, values);
    }
    //Returns the list
    public List<Medicine> getMedicines(){
        List<Medicine> medicines = new ArrayList<>();

        MedicineCursorWrapper cursor = queryMedicines(null,null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                medicines.add(cursor.getMedicine());
                cursor.moveToNext();
            }

        }finally {
            cursor.close();
        }
        return medicines;
    }

    //Returns Medicine with given ID
    public Medicine getMedicine(UUID id){
        MedicineCursorWrapper cursor = queryMedicines(
                MedicineTable.Cols.UUID + " = ?",
                new String[] {id.toString()}
        );
        try {
            if (cursor.getCount()==0){
                return null;
            }
            cursor.moveToFirst();
            return cursor.getMedicine();
        }finally {
            cursor.close();
        }
    }
    public void deleteMedicine(Medicine medicine) {

    }
    public void updateMedicine(Medicine medicine){
        String uuidString = medicine.getId().toString();
        ContentValues values = getContentValues(medicine);

        mDataBase.update(MedicineTable.NAME,values, MedicineTable.Cols.UUID + " = ?", new String[] { uuidString});
    }
    private static ContentValues getContentValues(Medicine medicine){
        ContentValues values = new ContentValues();
        values.put(MedicineTable.Cols.UUID, medicine.getId().toString());
        values.put(MedicineTable.Cols.TITLE, medicine.getTitle());
        values.put(MedicineTable.Cols.DATE, medicine.getDate().getTime());
        values.put(MedicineTable.Cols.TAKEN, medicine.isTaken() ? 1 : 0);

        return values;
    }

    private MedicineCursorWrapper queryMedicines(String whereClause, String[] whereArgs){
        Cursor cursor = mDataBase.query(
                MedicineTable.NAME,
                null, // selects all columns
                whereClause,
                whereArgs,
                null,  //groupBy
                null,  //having
                null   //orderBy
        );
        return new MedicineCursorWrapper(cursor);
    }
}
