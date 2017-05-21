package com.example.aloyzas.medicineReminder;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Aloyzas on 2016-12-31.
 */

public class MedicineListFragment extends Fragment {

    private static final String SAVED_SUBTITLE_VISIBLE = "subtitle";

    private RecyclerView mMedicineRecyclerView;
    private MedicineAdapter mAdapter;
    private boolean mSubtitleVisible;
    private TextView mEmptyView;
    private ImageButton mNewMedicineButton;
    /*
    FragmentManager pranešama, kad MedicineListFragment turi gauti menu callback'us
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    //Uses fragment_medicine_list layout file
    //Finds RecyclerView in the layout file
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState){
        View view = inflater.inflate(R.layout.fragment_medicine_list,container,false);

        mMedicineRecyclerView = (RecyclerView)view.findViewById(R.id.medicine_recycler_view);
        mMedicineRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (savedInstanceState!=null){
            mSubtitleVisible=savedInstanceState.getBoolean(SAVED_SUBTITLE_VISIBLE);
        }
        /*
        Pradinis langas be įrašų, mygtukas naujam įrašui
        Paspaudus kreipiasi į privatų metodą įrašų sukūrimui
         */
        mEmptyView = (TextView) view.findViewById(R.id.empty_medicines_view);
        mNewMedicineButton = (ImageButton) view.findViewById(R.id.new_medicines_button);
        mNewMedicineButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewMedicine();
            }
        });
        updateUI();
        return view;
    }

    //Kai paspaudžiama ant Medicine, MedicineListActivity pereina ant resume
    //ir tada fragments, kuriuos turi activity irgi pastato ant resume.
    //Kai vėl iškviečiamas - pakeisti duomenys susinaikina.
    //Metodas perkraus lista, kad butu matomi pakeitimai.
   @Override
    public void onResume(){
        super.onResume();
        updateUI();
    }

    /*
    Išsaugomas subtitle matomumas, kai pasukamas ekranas
     */
    @Override
    public void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putBoolean(SAVED_SUBTITLE_VISIBLE,mSubtitleVisible);
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater){
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.fragment_medicine_list,menu);

        /*
        Pasirenka rodyti ar nerodyti subtitle
         */
        MenuItem subtitleItem = menu.findItem(R.id.menu_item_show_subtitle);
        if(mSubtitleVisible){
            subtitleItem.setTitle(R.string.hide_subtitle);
        } else {
            subtitleItem.setTitle(R.string.show_subtitle);
        }
    }
    /*
    MenuItem pasirinkimo realizacija
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.menu_item_new_medicine:
                Medicine medicine = new Medicine();
                MedicineLab.get(getActivity()).addMedicines(medicine);
                Intent intent = MedicinePagerActivity.newIntent(getActivity(), medicine.getId());
                startActivity(intent);
                return true;
            case R.id.menu_item_show_subtitle:
                mSubtitleVisible=!mSubtitleVisible;
                getActivity().invalidateOptionsMenu();
                updateSubtitle();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    /*
      Rodo įrašų skaičių
     */
    private void updateSubtitle(){
        MedicineLab medicineLab = MedicineLab.get(getActivity());
        int medicineCount = medicineLab.getMedicines().size();
        String subtitle = getString(R.string.subtitle_format, medicineCount);

        if (!mSubtitleVisible){
            subtitle=null;
        }

        AppCompatActivity activity = (AppCompatActivity) getActivity();
        activity.getSupportActionBar().setSubtitle(subtitle);
    }

    //Method connects Adapter to RecyclerView.
    private void updateUI() {
        MedicineLab medicineLab = MedicineLab.get(getActivity());
        List<Medicine> medicines = medicineLab.getMedicines();

        if (mAdapter == null) {
            mAdapter = new MedicineAdapter(medicines);
            mMedicineRecyclerView.setAdapter(mAdapter);
        } else {
                mAdapter.setMedicines(medicines);
                mAdapter.notifyDataSetChanged();
            }

        /*
        Kai nėra pradiniame lange įrašų
        */
            if (medicines.isEmpty()) {
                mMedicineRecyclerView.setVisibility(View.GONE);
                mEmptyView.setVisibility(View.VISIBLE);
                mNewMedicineButton.setVisibility(View.VISIBLE);
            } else {
                mMedicineRecyclerView.setVisibility(View.VISIBLE);
                mEmptyView.setVisibility(View.GONE);
                mNewMedicineButton.setVisibility(View.GONE);
            }

            updateSubtitle();
        }

    private void createNewMedicine() {
        Medicine medicine = new Medicine();
        MedicineLab.get(getActivity()).addMedicines(medicine);
        Intent intent = MedicinePagerActivity.newIntent(getActivity(), medicine.getId());
        startActivity(intent);
    }

    //Inner class of ViewHolder
    private class MedicineHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener {
        private TextView mTitleTextView;
        private TextView mDateTextView;
        private CheckBox mSolvedCheckBox;
        private Medicine mMedicine;

        public void bindMedicine(Medicine medicine) {
            mMedicine = medicine;
            mTitleTextView.setText(mMedicine.getTitle());
            mDateTextView.setText(mMedicine.getDate().toString());
            mSolvedCheckBox.setChecked(mMedicine.isTaken());
        }

        public MedicineHolder(View itemView){
            super(itemView);
            itemView.setOnClickListener(this);

            mTitleTextView = (TextView)itemView.findViewById(R.id.list_item_medicine_text_view);
            mDateTextView = (TextView)itemView.findViewById(R.id.list_item_medicine_date_text_view);
            mSolvedCheckBox = (CheckBox) itemView.findViewById(R.id.list_item_medicine_taken_check_box);
        }
        @Override
        public void onClick(View v) {
            //Pradedamas MedicinePagerActivity instance
            Intent intent = MedicinePagerActivity.newIntent(getActivity(), mMedicine.getId());
            startActivity(intent);
        }
    }
    //Inner class of adapter
    //The RecyclerView will communicate with this adapter
    //when ViewHolder needs to be created or connected with Medicine object
    private class MedicineAdapter extends RecyclerView.Adapter<MedicineHolder>{
        private List<Medicine> mMedicines;

        public MedicineAdapter(List<Medicine> medicines){
            mMedicines = medicines;
        }

        //Method is called by the RecyclerView when it needs a new View to display an item.
        //Creates the View and wrap it in a ViewHolder.
        //For View, you inflate a layout from Android standart library called
        //simple_list_item1. This contains single TextView, styled to look nice in a list.
        @Override
        public MedicineHolder onCreateViewHolder(ViewGroup parent, int viewType){
            LayoutInflater layoutInflater = LayoutInflater.from(getActivity());
            View view = layoutInflater.inflate(R.layout.list_item_medicine,parent,false);
            return new MedicineHolder(view);
        }

        //Method binds a ViewHolder's View to our model object. It receives the ViewHolder
        //and a position in our data set. To bind View, we use that position to find the right model data
        //Then View is updated to reflect that model data
        @Override
        public void onBindViewHolder(MedicineHolder holder, int position){
            Medicine medicine = mMedicines.get(position);
            holder.bindMedicine(medicine);
        }

        @Override
        public int getItemCount(){
            return mMedicines.size();
        }
        public void setMedicines(List<Medicine> medicines){
            mMedicines = medicines;
        }
    }
}
