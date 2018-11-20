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

import Adapters.ScheduleViewAdapter;
import GettersAndSetters.ClassSchedule;
import OtherClasses.ShowDialog;
import ServerLink.ServerSchedule;

@SuppressLint({"NewApi", "ValidFragment"})
public class FragmentSchedule extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    public FragmentHome fragmentHome;
    public String inputStr = "";
    public ServerSchedule serverSchedule;
    private View iView;
    private ScheduleViewAdapter scheduleViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassSchedule> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems;

    public FragmentSchedule(FragmentHome fragmentHome) {
        this.fragmentHome = fragmentHome;

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_schedule, container, false);

        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        list = new ArrayList<>();
        serverSchedule = new ServerSchedule();
        scheduleViewAdapter = new ScheduleViewAdapter(list, this);

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        rcyView.setAdapter(scheduleViewAdapter);
        rcyView.setLayoutManager(manager);


        rcyView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        swp2Rfsh.setColorSchemeResources(R.color.colorAccent, android.R.color.holo_blue_dark, android.R.color.holo_green_dark, android.R.color.holo_orange_dark);

        onRefresh();

        return iView;
    }

    @Override
    public void onRefresh() {
        isScrolling = false;
        list.clear();
        fetchData();
    }

    public void SearchData() {
        list.clear();
        fetchData();
    }

    private void fetchData() {
        fragmentHome.homeActivity.showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PopulateList populateList = new PopulateList();
                populateList.execute();

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
            result = serverSchedule.getAllRecords(inputStr);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverSchedule.getList().size(); i++) {
                    ClassSchedule classSchedule = new ClassSchedule(
                            serverSchedule.getList().get(i).getSchdId(),
                            serverSchedule.getList().get(i).getType(),
                            serverSchedule.getList().get(i).getTimePeriod()
                    );

                    list.add(classSchedule);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Trainers Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            scheduleViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentHome.homeActivity.hideProgress();

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentHome.homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }

}
