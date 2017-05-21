package com.example.aloyzas.medicineReminder;

        import android.support.v4.app.Fragment;

/**
 * Created by Aloyzas on 2016-12-31.
 */

public class MedicineListActivity extends SingleFragmentActivity {

    @Override
    protected Fragment createFragment(){
        return new MedicineListFragment();
    }
}
