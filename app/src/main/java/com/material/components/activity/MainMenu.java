package com.material.components.activity;

import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.material.components.AccountFragment;
import com.material.components.ExploreFragment;
import com.material.components.ManageFragment;
import com.material.components.NotificationFragment;
import com.material.components.R;
import com.material.components.SupportFragment;

public class MainMenu extends AppCompatActivity implements SupportFragment.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, ManageFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener {
    private View parent_view;
    private BottomNavigationView navigation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        parent_view = findViewById(android.R.id.content);
        initComponent();
    }

    private void initComponent() {
        loadFragment(new ExploreFragment());
        navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment fragment = new ExploreFragment();
                switch (item.getItemId()) {
                    case R.id.navigation_explore:
                        fragment = new ExploreFragment();
                        break;
                    case R.id.navigation_support:
                        fragment = new SupportFragment();
                        break;
                    case R.id.navigation_manage:
                        fragment = new ManageFragment();
                        break;
                    case R.id.navigation_notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.navigation_account:
                        fragment = new AccountFragment();
                        break;
                }
                return loadFragment(fragment);
            }
        });
    }

    private boolean loadFragment(Fragment fragment) {
        if (fragment != null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fl_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
