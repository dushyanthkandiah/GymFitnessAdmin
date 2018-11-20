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
public class FragmentHome extends Fragment {

    public HomeActivity homeActivity;
    public FragmentTrainers fragmentTrainers;
    public FragmentNutritions fragmentNutritions;
    public FragmentExercises fragmentExercises;
    public FragmentSchedule fragmentSchedule;
    private View iView;
    private FragmentManager fragmentManager;
    private PagerAdapter pagerAdapter;
    private ViewPager viewPager;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @SuppressLint("ResourceType")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_trainer:
                    SessionData.currentFragment = "trainer";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentTrainers, "Trainers");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.imgAddBtn.setVisibility(View.VISIBLE);
                    homeActivity.unCheck3Dots(true);
                    return true;
                case R.id.navigation_nutrition:
                    SessionData.currentFragment = "nutrition";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentNutritions, "Nutrition");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.imgAddBtn.setVisibility(View.VISIBLE);
                    homeActivity.unCheck3Dots(true);
                    return true;

                case R.id.navigation_exercise:
                    SessionData.currentFragment = "exercise";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentExercises, "Exercises");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.imgAddBtn.setVisibility(View.VISIBLE);
                    homeActivity.unCheck3Dots(true);
                    return true;

                case R.id.navigation_Schedule:
                    SessionData.currentFragment = "schedule";
                    pagerAdapter = new PagerAdapter(getFragmentManager());
                    pagerAdapter.addFragments(fragmentSchedule, "Schedule");
                    viewPager.setAdapter(pagerAdapter);
                    homeActivity.imgAddBtn.setVisibility(View.GONE);
                    homeActivity.unCheck3Dots(false);
                    return true;

            }
            return false;
        }
    };


    public FragmentHome(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.fragmentManager = fragmentManager;
        this.homeActivity = homeActivity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_home, container, false);

        viewPager = iView.findViewById(R.id.viewPager);

        fragmentTrainers = new FragmentTrainers(this);
        fragmentNutritions = new FragmentNutritions(this);
        fragmentExercises = new FragmentExercises(this);
        fragmentSchedule = new FragmentSchedule(this);

        /*Opening default fragment*/
        SessionData.currentFragment = "trainer";
        pagerAdapter = new PagerAdapter(getFragmentManager());
        pagerAdapter.addFragments(fragmentTrainers, "Trainers");
        viewPager.setAdapter(pagerAdapter);

        BottomNavigationView navigation = iView.findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        return iView;
    }

    public void searchRecords(String inputStr, int schdId) {
//        System.out.println(schdId + "*************");

        if (SessionData.currentFragment.equals("trainer"))
            fragmentTrainers.serverTrainers.getClassTrainers().setSchdId(schdId);
        else if (SessionData.currentFragment.equals("nutrition")) {
            fragmentNutritions.serverNutrition.getClassNutritions().setSchdId(schdId);
        } else if (SessionData.currentFragment.equals("exercise")) {
            fragmentExercises.serverExercise.getClassExercise().setSchdId(schdId);
        } else if (SessionData.currentFragment.equals("schedule")) {

        }

        callSearchInFragment(inputStr);

    }

    public void searchRecords(String inputStr) {
        callSearchInFragment(inputStr);

    }

    void callSearchInFragment(String inputStr) {
        fragmentTrainers.inputStr = inputStr;
        fragmentNutritions.inputStr = inputStr;
        fragmentExercises.inputStr = inputStr;
        fragmentSchedule.inputStr = inputStr;

        if (SessionData.currentFragment.equals("trainer")) {
            fragmentTrainers.SearchData();
        } else if (SessionData.currentFragment.equals("nutrition")) {
            fragmentNutritions.SearchData();
        } else if (SessionData.currentFragment.equals("exercise")) {
            fragmentExercises.SearchData();
        } else if (SessionData.currentFragment.equals("schedule")) {
            fragmentSchedule.SearchData();
        }
    }


}
