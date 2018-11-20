package Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.abubakr.gymfitnessadmin.HomeActivity;
import com.example.abubakr.gymfitnessadmin.R;

import Adapters.PagerAdapter;

@SuppressLint("ValidFragment")
public class FragmentStore extends Fragment {

    public HomeActivity homeActivity;
    private View iView;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;

    public FragmentStore(HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_store, container, false);
    }

}
