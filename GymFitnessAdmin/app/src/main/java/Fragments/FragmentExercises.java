package Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Adapters.ExerciseViewAdapter;
import GettersAndSetters.ClassExercise;
import OtherClasses.ShowDialog;
import ServerLink.ServerExercise;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentExercises extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public ServerExercise serverExercise;
    public String inputStr = "";
    private View iView;
    private FragmentHome fragmentHome;
    private RecyclerView rcyViewExercises;
    private ExerciseViewAdapter exerciseViewAdapter;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassExercise> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;


    public FragmentExercises(FragmentHome fragmentHome) {
        this.fragmentHome = fragmentHome;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iView = inflater.inflate(R.layout.fragment_exercises, container, false);

        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);
        rcyViewExercises = iView.findViewById(R.id.rcyViewExercises);

        list = new ArrayList<>();
        serverExercise = new ServerExercise();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        exerciseViewAdapter = new ExerciseViewAdapter(list, this);
        rcyViewExercises.setAdapter(exerciseViewAdapter);
        rcyViewExercises.setLayoutManager(manager);

        rcyViewExercises.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    isScrolling = true;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                currentItems = manager.getChildCount();
                totalItems = manager.getItemCount();
                scrollOutItems = manager.findFirstVisibleItemPosition();

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;

                    fetchData();

                }

            }
        });

        swp2Rfsh.setOnRefreshListener(this);
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_green_dark, android.R.color.holo_orange_dark, android.R.color.holo_blue_dark);
        serverExercise.getClassExercise().setSchdId(0);
        onRefresh();

        return iView;
    }

    @Override
    public void onRefresh() {
        isScrolling = false;
        page = 0;
        list.clear();
        fetchData();
    }

    public void SearchData() {
        page = 0;
        list.clear();
        fetchData();
    }

    public void fetchData() {

        fragmentHome.homeActivity.showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                new PopulateList().execute();
            }
        }, 1000);
    }

    private class PopulateList extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected void onPreExecute() {
            fragmentHome.homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverExercise.getAllRecords(inputStr, page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("success")) {

                for (int i = 0; i < serverExercise.getList().size(); i++) {
                    ClassExercise classExercise = new ClassExercise(
                            serverExercise.getList().get(i).getId(),
                            serverExercise.getList().get(i).getSchdId(),
                            serverExercise.getList().get(i).getSets(),
                            serverExercise.getList().get(i).getTimes(),
                            serverExercise.getList().get(i).getExcercise()

                    );

                    list.add(classExercise);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Exercises Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            exerciseViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentHome.homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentHome.homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }


}
