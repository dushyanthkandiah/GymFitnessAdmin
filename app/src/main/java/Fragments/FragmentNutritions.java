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

import Adapters.NutritionViewAdapter;
import GettersAndSetters.ClassNutritions;
import OtherClasses.ShowDialog;
import ServerLink.ServerNutrition;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentNutritions extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public ServerNutrition serverNutrition;
    public String inputStr = "";
    private View iView;
    private FragmentHome fragmentHome;
    private RecyclerView rcyViewNutrition;
    private NutritionViewAdapter nutritionViewAdapter;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassNutritions> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;

    public FragmentNutritions(FragmentHome fragmentHome) {
        this.fragmentHome = fragmentHome;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        iView = inflater.inflate(R.layout.fragment_nutritions, container, false);

        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);
        rcyViewNutrition = iView.findViewById(R.id.rcyViewNutrition);

        list = new ArrayList<>();
        serverNutrition = new ServerNutrition();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        nutritionViewAdapter = new NutritionViewAdapter(list, this);
        rcyViewNutrition.setAdapter(nutritionViewAdapter);
        rcyViewNutrition.setLayoutManager(manager);

        rcyViewNutrition.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
        serverNutrition.getClassNutritions().setSchdId(0);
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
            result = serverNutrition.getAllRecords(inputStr, page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverNutrition.getList().size(); i++) {
                    ClassNutritions classNutritions = new ClassNutritions(
                            serverNutrition.getList().get(i).getId(),
                            serverNutrition.getList().get(i).getSchdId(),
                            serverNutrition.getList().get(i).getFood(),
                            serverNutrition.getList().get(i).getType(),
                            serverNutrition.getList().get(i).getStatus(),
                            serverNutrition.getList().get(i).getPicture()
                    );

                    list.add(classNutritions);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Nutrition Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            nutritionViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentHome.homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentHome.homeActivity.hideProgress(); /* hide the progressbar dialog here... */
            super.onCancelled();
        }

    }

}
