package com.example.abubakr.gymfitnessadmin;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import Dialogs.DialogExercise;
import Dialogs.DialogNutrition;
import Dialogs.DialogTrainer;
import Fragments.FragmentCustomers;
import Fragments.FragmentHome;
import Fragments.FragmentStore;
import GettersAndSetters.ClassSchedule;
import OtherClasses.SessionData;
import ServerLink.ServerSchedule;
import pl.droidsonroids.gif.GifImageView;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public FragmentManager fragmentManager = getSupportFragmentManager();
    public NavigationView navigationView;
    public FragmentHome fragmentHome;
    public EditText txtSearch;
    public ImageView imgAddBtn;
    Toolbar toolbar;
    FragmentCustomers fragmentCustomers;
    FragmentStore fragmentStore;
    private GifImageView progressBar;
    public TextView lblFragmentTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        progressBar = findViewById(R.id.progressBar);
        txtSearch = findViewById(R.id.txtSearch);
        lblFragmentTitle = findViewById(R.id.lblFragmentTitle);
        imgAddBtn = findViewById(R.id.imgAddBtn);


        navigationView.setNavigationItemSelectedListener(this);
        navigationView.getMenu().getItem(0).setChecked(true);
        fragmentHome = new FragmentHome(fragmentManager, this);

        if (savedInstanceState == null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            SessionData.currentMainFrag = "store";
            fragmentStore = new FragmentStore(fragmentManager, this);
            unCheck3Dots(false);
            lblFragmentTitle.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragmentStore, "fragment");
            ft.commit();
            imgAddBtn.setVisibility(View.GONE);
        }

        txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (SessionData.currentMainFrag.equals("home")) {
                    fragmentHome.searchRecords(txtSearch.getText().toString().trim().replace("'", "''"));
                } else if (SessionData.currentMainFrag.equals("customer")) {
                    fragmentCustomers.inputStr = txtSearch.getText().toString().trim().replace("'", "''");
                    fragmentCustomers.SearchData();
                } else if (SessionData.currentMainFrag.equals("store")) {
                    fragmentStore.searchRecords(txtSearch.getText().toString().trim().replace("'", "''"));
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    public void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        ServerSchedule serverSchedule = new ServerSchedule();
        serverSchedule.getAllRecords("");
        ArrayList<ClassSchedule> list = serverSchedule.getList();

        menu.add(0, 0, 0, "All").setShortcut('3', 'c');

        for (int i = 0; i < list.size(); i++) {
            menu.add(0, list.get(i).getSchdId(), 0, list.get(i).getType()).setShortcut('3', 'c');
        }

        return true;
    }

    public void unCheck3Dots(boolean check) {
        for (int i = 0; i < toolbar.getMenu().size(); i++) {
            toolbar.getMenu().getItem(i).setVisible(check);
        }

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        fragmentHome.searchRecords(txtSearch.getText().toString().trim().replace("'", "''"), id);

        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();

        int id = item.getItemId();

        if (id == R.id.nav_home) {
            SessionData.currentMainFrag = "home";
            fragmentHome = new FragmentHome(fragmentManager, this);
            unCheck3Dots(true);
            lblFragmentTitle.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragmentHome, "fragment");
            ft.commit();
            imgAddBtn.setVisibility(View.VISIBLE);

        } else if (id == R.id.nav_customers) {
            SessionData.currentMainFrag = "customer";
            fragmentCustomers = new FragmentCustomers(fragmentManager, this);
            unCheck3Dots(false);
            lblFragmentTitle.setVisibility(View.GONE);
            txtSearch.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragmentCustomers, "fragment");
            ft.commit();
            imgAddBtn.setVisibility(View.GONE);

        } else if (id == R.id.nav_store) {
            SessionData.currentMainFrag = "store";
            fragmentStore = new FragmentStore(fragmentManager, this);
            unCheck3Dots(false);
            lblFragmentTitle.setVisibility(View.VISIBLE);
            txtSearch.setVisibility(View.VISIBLE);
            ft.replace(R.id.content_frame, fragmentStore, "fragment");
            ft.commit();
            imgAddBtn.setVisibility(View.GONE);

        }else if (id == R.id.nav_logout) {
            SessionData.clearAll();
            startActivity(new Intent(this, LoginActivity.class));
            overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
            finish();

        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void imgAddBtn(View view) {

        if (SessionData.currentFragment.equals("trainer")) {
            DialogTrainer dialogTrainer = DialogTrainer.newInstance(fragmentHome.fragmentTrainers, null, "add");
            dialogTrainer.show(fragmentHome.fragmentTrainers.getFragmentManager(), "dialog");
        } else if (SessionData.currentFragment.equals("nutrition")) {
            DialogNutrition dialogNutrition = DialogNutrition.newInstance(fragmentHome.fragmentNutritions, null, "add");
            dialogNutrition.show(fragmentHome.fragmentNutritions.getFragmentManager(), "dialog");
        } else if (SessionData.currentFragment.equals("exercise")) {
            DialogExercise dialogExercise = DialogExercise.newInstance(fragmentHome.fragmentExercises, null, "add");
            dialogExercise.show(fragmentHome.fragmentExercises.getFragmentManager(), "dialog");
        }

    }
}
