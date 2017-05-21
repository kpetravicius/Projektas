package com.example.aloyzas.medicineReminder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;
import java.util.UUID;

/**
 * Created by Aloyzas on 2016-12-29.
 */
public class MedicineFragment extends Fragment {

    private static final String ARG_MEDICINE_ID = "crime_id";
    private static final String DIALOG_DATE="DialogDate";
    private static final String DIALOG_TIME="DialogTime";

    private static final int REQUEST_DATE=0;
    private static final int REQUEST_TIME=1;

    private Medicine mMedicine;
    private EditText mTitleField;
    private Button mDateButton;
    private CheckBox mTakenCheckBox;

    //Pakeičia MedicineFragment vaizdą su Medicine duomenimis
    public static MedicineFragment newInstance(UUID medicineId) {
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEDICINE_ID, medicineId);

        MedicineFragment fragment = new MedicineFragment();
        fragment.setArguments(args);
        return fragment;
    }
    /*

     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_medicine,menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_item_del_medicine:
                // Check to see if the medicine has already been create and if so delete it
                // Do I need to do this or will mMedicine always exist(created in onCreate())??
                if (mMedicine != null) {
                    Toast.makeText(getActivity(), "Deleting this medicine", Toast.LENGTH_SHORT).show();
                  //  MedicineLab.get(getActivity()).deleteMedicine(mMedicine);
                    // Call finish() on MedicineFragment's hosting activity
                    getActivity().finish();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        UUID medicineId = (UUID) getArguments().getSerializable(ARG_MEDICINE_ID);
        mMedicine = MedicineLab.get(getActivity()).getMedicine(medicineId);
        setHasOptionsMenu(true);
    }

    @Override
    public void onPause(){
        super.onPause();

        MedicineLab.get(getActivity()).updateMedicine(mMedicine);
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_medicine,container,false);

        mTitleField = (EditText)v.findViewById(R.id.medicine_title);
        mTitleField.setText(mMedicine.getTitle());
        mTitleField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mMedicine.setTitle(s.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        mDateButton = (Button)v.findViewById(R.id.medicine_date);
        updateDate();

        /*
        DialogFragment rodymas
        Rodo, kai mygtukas yra paspaudžiamas, naudoja FragmentManager
         */
        mDateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                FragmentManager manager = getFragmentManager();
                DatePickerFragment dialog = DatePickerFragment.newInstance(mMedicine.getDate());
                dialog.setTargetFragment(MedicineFragment.this,REQUEST_DATE);
                dialog.show(manager, DIALOG_DATE);
            }
        });

        mTakenCheckBox = (CheckBox)v.findViewById(R.id.medicine_taken);
        mTakenCheckBox.setChecked(mMedicine.isTaken());
        mTakenCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                //Set the medicine's solved property
                mMedicine.setTaken(isChecked);
            }
        });

        return v;
    }

    /*
    Pasiemamas extra, Medicine objekte nustatome data, atnaujinamas tekstas mygtuke
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode!= Activity.RESULT_OK){
            return;
        }
        if (requestCode == REQUEST_DATE) {
            Date date = (Date) data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
            showTimeDialog(date);
        }
        if (requestCode == REQUEST_TIME){
            Date date = (Date) data.getSerializableExtra(TimePickerFragment.EXTRA_TIME);
            mMedicine.setDate(date);
            updateDate();
        }
    }
    private void showTimeDialog(Date date) {
        FragmentManager fragmentManager = getFragmentManager();
        TimePickerFragment dialog = TimePickerFragment.newInstance(date);

        dialog.setTargetFragment(MedicineFragment.this, REQUEST_TIME);
        dialog.show(fragmentManager, DIALOG_TIME);
    }
    private void updateDate() {
        mDateButton.setText(mMedicine.getDate().toString());
    }
}
