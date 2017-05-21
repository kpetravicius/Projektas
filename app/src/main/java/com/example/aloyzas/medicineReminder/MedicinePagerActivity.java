package com.example.aloyzas.medicineReminder;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import java.util.List;
import java.util.UUID;

/**
 * Created by Aloyzas on 2017-01-29.
 */

public class MedicinePagerActivity extends AppCompatActivity {
    private static final String EXTRA_MEDICINE_ID =
            "com.example.aloyzas.medicineReminder.crime_id";

    private ViewPager mViewPager;
    private List<Medicine> mMedicines;

    //newIntent metodas su extra
    public static Intent newIntent(Context packageContext, UUID medicineId){
        Intent intent = new Intent(packageContext, MedicinePagerActivity.class);
        intent.putExtra(EXTRA_MEDICINE_ID, medicineId);
        return intent;
    }

    @Override
    protected void onCreate (Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine_pager);

        UUID medicineId = (UUID) getIntent().getSerializableExtra(EXTRA_MEDICINE_ID);

        //Adapteris, kad galėtumėm swipinti tarp puslapių
        //////////////////////////////////////////////////
        //Randamas ViewPager activy's view'e
        mViewPager = (ViewPager) findViewById(R.id.activity_medicine_pager_view_pager);

        //Iš MedicineLab'o pasiimam objektus
        mMedicines = MedicineLab.get(this).getMedicines();
        /*Sukuriamas adapteris, tam reikalingas ir fragmentManager'is
        FragmentStatePagerAdapter yra taupesnis su atmintimi, nes ištrina nereikalingus fragmentus
        FragmentPagerAdapter neištrina nenaudojamų fragmentų, geriausia naudoti
        kai reikalingi 2-3 fragmentai swipinant tarp tabbu.
         */
        FragmentManager fragmentManager = getSupportFragmentManager();
        mViewPager.setAdapter(new FragmentStatePagerAdapter(fragmentManager) {

            //Perneša Medicine instance pagal duotą poziciją ir pagal to Medicine ID sukuria ir grąžina
            //tinkamą MedicineFragment'ą
            @Override
            public Fragment getItem(int position) {
                Medicine medicine = mMedicines.get(position);
                return MedicineFragment.newInstance(medicine.getId());
            }

            //Grąžina array list'o narių skaičių
            @Override
            public int getCount() {
                return mMedicines.size();
            }
        });

        for (int i = 0; i< mMedicines.size(); i++){
            if (mMedicines.get(i).getId().equals(medicineId)){
                mViewPager.setCurrentItem(i);
                break;
            }
        }
    }
}
