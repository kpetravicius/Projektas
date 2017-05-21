package com.example.aloyzas.medicineReminder;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Aloyzas on 2016-12-29.
 */
public class Medicine {
    private UUID mId;
    private String mTitle;
    private Date mDate;
    private boolean mTaken;

    public Medicine(){
        //Generate unique identifier
        this(UUID.randomUUID());
    }
    public Medicine(UUID id) {
        mId=id;
        mDate=new Date();
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isTaken() {
        return mTaken;
    }

    public void setTaken(boolean taken) {
        mTaken = taken;
    }

    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }
}
