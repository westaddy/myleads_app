package com.myleads.android.activities;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.myleads.android.R;
import com.myleads.android.custom.PrefManager;

import com.myleads.android.dummy.DummyContent;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.BottomBarTab;
import com.roughike.bottombar.OnTabSelectListener;


public class DashboardActivity extends AppCompatActivity implements PostFragment.OnListFragmentInteractionListener {

    private final String PUSHER_APP_KEY = "c1b4cc8e03e022c0e56c";
    private final String PUSHER_APP_CLUSTER = "eu";

    private String API_URL;
    private int fragment_container;
    private Fragment fragment;
    private PrefManager prefManager;
    private String TAG = DashboardActivity.class.getSimpleName();
    private BottomBar bottomBar;
    private BottomBarTab tab_create, tab_report;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragment_container = R.id.fragment_container;
        setContentView(R.layout.activity_dashboard);
        API_URL = getString(R.string.api_url);


        prefManager = new PrefManager(this);


        bottomBar = (BottomBar) findViewById(R.id.bottomBar);
        tab_create = bottomBar.getTabWithId(R.id.tab_create);
        tab_report = bottomBar.getTabWithId(R.id.tab_report);

        changeFragment(savedInstanceState);//This method change fragment on Tab selected

    }

    public void changeFragment(final Bundle savedInstanceState) {

        String menuFragment = getIntent().getStringExtra("menuFragment");

        // If menuFragment is defined, then this activity was launched with a fragment selection
        if (menuFragment != null) {
            // Here we can decide what do to -- perhaps load other parameters from the intent extras such as IDs, etc
            if (menuFragment.equals("createFragment")) {
                bottomBar.setDefaultTab(R.id.tab_report);
            }
             else if (menuFragment.equals("reportFragment")) {
                bottomBar.setDefaultTab(R.id.tab_create);
            }
        }

        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_create:
                        fragment = new PostFragment();
                        break;
                    case R.id.tab_report:
                        fragment = new ReportFragment();
                        break;
                }
                if (fragment != null) {
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.fragment_container, fragment);
                    ft.commit();
                }
            }
        });
    }


    public void displayMessage(String error) {
        Toast.makeText(this, error, Toast.LENGTH_LONG).show();
    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }


    @Override
    public void onListFragmentInteraction(DummyContent.DummyItem item) {

    }
}
