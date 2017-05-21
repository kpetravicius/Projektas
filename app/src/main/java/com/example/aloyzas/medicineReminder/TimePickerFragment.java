package com.example.aloyzas.medicineReminder;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TimePicker;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by Aloyzas on 2017-02-13.
 */

public class TimePickerFragment extends DialogFragment {
    private static final String ARG_TIME="time";

    public static final String EXTRA_TIME = "com.example.aloyzas.medicineReminder.time";

    private TimePicker mTimePicker;

    public static TimePickerFragment newInstance(Date time) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_TIME, time);

        TimePickerFragment fragment = new TimePickerFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @TargetApi(23)
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){

        //Datos objektas atsiųstas iš DatePickerFragment
        final Date date = (Date) getArguments().getSerializable(ARG_TIME);
        View v = LayoutInflater.from(getActivity())
                .inflate(R.layout.dialog_time, null);
        mTimePicker = (TimePicker) v.findViewById(R.id.dialog_time_time_picker);
        return new AlertDialog.Builder(getActivity())
                .setView(v)
                .setTitle("Time to take medicine:")
                .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener(){
                    @Override
                    public void onClick(DialogInterface dialog, int which){
                        Calendar calendar = new GregorianCalendar();
                        //Data iš DatePicker
                        calendar.setTime(date);
                        int year = calendar.get(Calendar.YEAR);
                        int month = calendar.get(Calendar.MONTH);
                        int day = calendar.get(Calendar.DAY_OF_MONTH);

                        //Vartotojas pasirenka valandas ir minutes
                        int hour = mTimePicker.getHour();
                        int minute = mTimePicker.getMinute();
                        Date time = new GregorianCalendar(year,month,day,hour,minute).getTime();
                        sendResult(Activity.RESULT_OK,time);
                    }
                })
                .create();
    }
    private void sendResult(int resultCode,Date time){
        if (getTargetFragment()==null){
            return;
        }

        Intent intent = new Intent();
        intent.putExtra(EXTRA_TIME,time);

        getTargetFragment().onActivityResult(getTargetRequestCode(),resultCode,intent);
    }
}
