package com.example.aloyzas.medicineReminder.database;

/**
 * Created by Aloyzas on 2017-05-05.
 */

public class MedicineDbSchema {
    public static final class MedicineTable {
        public static final String NAME = "medicines";

        public static final class Cols {
            public static final String UUID = "uuid";
            public static final String TITLE = "title";
            public static final String DATE = "date";
            public static final String TAKEN = "taken";
        }
    }
}
