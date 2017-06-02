package com.MWBE.Connects.NY.Activities;

import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.MWBE.Connects.NY.Fragments.AddOthersCapabilitiesFragment;
import com.MWBE.Connects.NY.Fragments.GeneralInformationSetup;
import com.MWBE.Connects.NY.R;

public class SetupActivity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);
        //deleteDatabase("CapalinoDataBase.sqlite");
        init();
    }

    private void init() {
        try {
            if (getIntent().getStringExtra("status") != null) {
                if (getIntent().getStringExtra("status").equalsIgnoreCase("addothers")) {
                    getSupportFragmentManager().beginTransaction().add(R.id.setup, new AddOthersCapabilitiesFragment()).commit();
                } else {
                    getSupportFragmentManager().beginTransaction().add(R.id.setup, new GeneralInformationSetup()).commit();
                }
            } else {
                getSupportFragmentManager().beginTransaction().add(R.id.setup, new GeneralInformationSetup()).commit();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

   /* @Override
    public void onBackPressed() {
        //this.getSupportFragmentManager().beginTransaction().replace(R.id.setup,new GeographicCoverageSetup()).commit();
        Fragment fragment = getSupportFragmentManager().findFragmentById("");
        if (fragment != null && fragment.isVisible()) {
            // add your code here
            this.getSupportFragmentManager().popBackStack();
        }
    }*/
   @Override
   public void onBackPressed() {
       if (getFragmentManager().getBackStackEntryCount() > 0) {
           getFragmentManager().popBackStack();
       } else {
           super.onBackPressed();
       }
   }
}
