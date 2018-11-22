package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abubakr.gymfitnessadmin.HomeActivity;
import com.example.abubakr.gymfitnessadmin.R;

import Adapters.PagerAdapter;
import OtherClasses.SessionData;

@SuppressLint("ValidFragment")
public class FragmentCustomerSupport extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public FragmentMessageCustomer fragmentMessageCustomer;
    public FragmentMessages fragmentMessages;

    public FragmentCustomerSupport(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_customer_support, container, false);


        viewPager = iView.findViewById(R.id.viewPager);

        fragmentMessageCustomer = new FragmentMessageCustomer(this);

        /*Opening default fragment*/
        SwitchFragments("customer", 0, "", null);

        return iView;
    }

    public void SwitchFragments(String whichFrag, int cusId, String customerName, byte[] picture) {

        switch (whichFrag) {

            case "customer":
                SessionData.currentFragment = "messageCustomer";
                fragmentMessageCustomer = new FragmentMessageCustomer(this);
                pagerAdapter = new PagerAdapter(getFragmentManager());
                pagerAdapter.addFragments(fragmentMessageCustomer, "Message Customer");
                viewPager.setAdapter(pagerAdapter);
                homeActivity.lblFragmentTitle.setVisibility(View.GONE);
                homeActivity.txtSearch.setVisibility(View.VISIBLE);
                break;

            case "messages":

                SessionData.currentFragment = "messages";
                fragmentMessages = new FragmentMessages(this, cusId, picture);
                pagerAdapter = new PagerAdapter(getFragmentManager());
                pagerAdapter.addFragments(fragmentMessages, "Messages");
                viewPager.setAdapter(pagerAdapter);
                homeActivity.lblFragmentTitle.setVisibility(View.VISIBLE);
                homeActivity.lblFragmentTitle.setText(customerName);
                homeActivity.txtSearch.setVisibility(View.GONE);
                break;

        }
    }

    public void searchRecords(String inputStr) {
        fragmentMessageCustomer.inputStr = inputStr;

        if (SessionData.currentFragment.equals("messageCustomer")) {
            fragmentMessageCustomer.SearchData();
        } else if (SessionData.currentFragment.equals("supplements")) {
//            fragmentSupplements.SearchData();
        }

    }

}
