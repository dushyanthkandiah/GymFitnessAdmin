package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.abubakr.gymfitnessadmin.HomeActivity;
import com.example.abubakr.gymfitnessadmin.R;

import Adapters.PagerAdapter;
import OtherClasses.SessionData;

@SuppressLint("ValidFragment")
public class FragmentStore extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    public FragmentOrders fragmentOrders;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_order:
                    fragmentOrders = new FragmentOrders(FragmentStore.this);
                    SessionData.currentFragment = "orders";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentOrders, "Orders");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.txtSearch.setVisibility(View.GONE);
                    homeActivity.lblFragmentTitle.setText("Orders");
                    homeActivity.unCheck3Dots(false);
                    return true;

                case R.id.navigation_supplements:
                    return true;


            }
            return false;
        }
    };


    public FragmentStore(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_store, container, false);

        viewPager = iView.findViewById(R.id.viewPager);

        fragmentOrders = new FragmentOrders(this);

        /*Opening default fragment*/
        SessionData.currentFragment = "orders";
        pagerAdapter = new PagerAdapter(getFragmentManager());
        pagerAdapter.addFragments(fragmentOrders, "Orders");
        viewPager.setAdapter(pagerAdapter);
        homeActivity.txtSearch.setVisibility(View.GONE);


        BottomNavigationView navigation = iView.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return iView;
    }

    public void searchRecords(String inputStr) {
        fragmentOrders.inputStr = inputStr;

        if (SessionData.currentFragment.equals("orders")) {
            fragmentOrders.SearchData();
        } else if (SessionData.currentFragment.equals("supplements")) {
            fragmentOrders.SearchData();
        }

    }

    void callSearchInFragment(String inputStr) {

    }
}
