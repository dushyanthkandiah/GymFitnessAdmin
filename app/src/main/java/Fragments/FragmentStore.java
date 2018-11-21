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
import OtherClasses.ShowDialog;

@SuppressLint("ValidFragment")
public class FragmentStore extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public FragmentOrders fragmentOrders;
    public FragmentSupplements fragmentSupplements;

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
                    homeActivity.lblFragmentTitle.setVisibility(View.VISIBLE);
                    homeActivity.imgAddBtn.setVisibility(View.GONE);

                    return true;

                case R.id.navigation_supplements:
                    fragmentSupplements = new FragmentSupplements(FragmentStore.this);
                    SessionData.currentFragment = "supplements";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentSupplements, "Supplements");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.txtSearch.setVisibility(View.VISIBLE);
                    homeActivity.lblFragmentTitle.setVisibility(View.GONE);
                    homeActivity.imgAddBtn.setVisibility(View.VISIBLE);

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
        fragmentSupplements = new FragmentSupplements(this);

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
        fragmentSupplements.inputStr = inputStr;

        if (SessionData.currentFragment.equals("orders")) {
            fragmentOrders.SearchData();
        } else if (SessionData.currentFragment.equals("supplements")) {
            fragmentSupplements.SearchData();
        }

    }

    void callSearchInFragment(String inputStr) {

    }
}
