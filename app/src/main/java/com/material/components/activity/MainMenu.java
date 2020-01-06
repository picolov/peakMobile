package com.material.components.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.google.gson.JsonObject;
import com.material.components.AccountFragment;
import com.material.components.CatalogFragment;
import com.material.components.ExploreFragment;
import com.material.components.LoginFragment;
import com.material.components.ManageFragment;
import com.material.components.NotificationFragment;
import com.material.components.R;
import com.material.components.SupportFragment;
import com.material.components.api.SpektaAPI;
import com.material.components.api.SpektaInterface;

import java.io.InputStream;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainMenu extends AppCompatActivity implements SupportFragment.OnFragmentInteractionListener, ExploreFragment.OnFragmentInteractionListener, ManageFragment.OnFragmentInteractionListener, AccountFragment.OnFragmentInteractionListener, LoginFragment.OnFragmentInteractionListener, NotificationFragment.OnFragmentInteractionListener, CatalogFragment.OnFragmentInteractionListener {
    private View parent_view;
    private BottomNavigationView navigation;
    private boolean isLogin = false;

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
        Menu menu = navigation.getMenu();
        MenuItem navManage = menu.findItem(R.id.navigation_manage);
        MenuItem navAccount = menu.findItem(R.id.navigation_account);
        MenuItem navNotification = menu.findItem(R.id.navigation_notification);
        if (isLogin) {
            navManage.setIcon(R.drawable.ic_settings);
            navManage.setTitle("Kelola");
            navAccount.setIcon(R.drawable.ic_account);
            navAccount.setTitle("Akun");
            navNotification.setVisible(true);
        } else {
            navManage.setIcon(R.drawable.ic_hdd);
            navManage.setTitle("Katalog");
            navAccount.setIcon(R.drawable.ic_login);
            navAccount.setTitle("Login");
            navNotification.setVisible(false);
        }
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
                        if (isLogin) fragment = new ManageFragment();
                        else fragment = new CatalogFragment();
                        break;
                    case R.id.navigation_notification:
                        fragment = new NotificationFragment();
                        break;
                    case R.id.navigation_account:
                        if(isLogin) fragment = new AccountFragment();
                        else fragment = new LoginFragment();
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
        System.out.println("---URI--------------->  " + uri.getPath());
        isLogin = true;
        if (uri.getPath().equals("fromLogin")) {
            loadFragment(new AccountFragment());
            navigation = (BottomNavigationView) findViewById(R.id.navigation);
            Menu menu = navigation.getMenu();
            MenuItem navManage = menu.findItem(R.id.navigation_manage);
            MenuItem navAccount = menu.findItem(R.id.navigation_account);
            MenuItem navNotification = menu.findItem(R.id.navigation_notification);
            if (isLogin) {
                navManage.setIcon(R.drawable.ic_settings);
                navManage.setTitle("Kelola");
                navAccount.setIcon(R.drawable.ic_account);
                navAccount.setTitle("Akun");
                navNotification.setVisible(true);
            } else {
                navManage.setIcon(R.drawable.ic_hdd);
                navManage.setTitle("Katalog");
                navAccount.setIcon(R.drawable.ic_login);
                navAccount.setTitle("Login");
                navNotification.setVisible(false);
            }
        }
    }
}
