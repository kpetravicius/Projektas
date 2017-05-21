package com.example.aloyzas.medicineReminder.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import com.example.aloyzas.medicineReminder.Medicine;
import com.example.aloyzas.medicineReminder.database.MedicineDbSchema.MedicineTable;

import java.util.Date;
import java.util.UUID;

/**
 * Cursor subclass. Helps to read data from Cursor
 * Created by Aloyzas on 2017-05-05.
 */

public class MedicineCursorWrapper extends CursorWrapper {
    public MedicineCursorWrapper (Cursor cursor){
        super(cursor);
    }

    public Medicine getMedicine() {
        String uuidString = getString(getColumnIndex(MedicineTable.Cols.UUID));
        String title = getString(getColumnIndex(MedicineTable.Cols.TITLE));
        long date = getLong(getColumnIndex(MedicineTable.Cols.DATE));
        int isSolved = getInt(getColumnIndex(MedicineTable.Cols.TAKEN));

        Medicine medicine = new Medicine(UUID.fromString(uuidString));
        medicine.setTitle(title);
        medicine.setDate(new Date(date));
        medicine.setTaken(isSolved !=0);

        return medicine;
    }
}
