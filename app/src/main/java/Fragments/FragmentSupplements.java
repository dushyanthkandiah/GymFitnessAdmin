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

import Adapters.SupplementsViewAdapter;
import GettersAndSetters.ClassSupplement;
import OtherClasses.ShowDialog;
import ServerLink.ServerSupplements;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentSupplements extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    public FragmentStore fragmentStore;
    public String inputStr = "";
    private ServerSupplements serverSupplements;
    private View iView;
    private SupplementsViewAdapter supplementsViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassSupplement> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;


    public FragmentSupplements(FragmentStore fragmentStore) {
        this.fragmentStore = fragmentStore;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_supplements, container, false);

        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        serverSupplements = new ServerSupplements();
        list = new ArrayList<>();
        supplementsViewAdapter = new SupplementsViewAdapter(list, this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcyView.setAdapter(supplementsViewAdapter);
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
        page = 0;
        list.clear();
        fetchData();
    }

    public void SearchData() {
        page = 0;
        list.clear();
        fetchData();
    }

    private void fetchData() {
        fragmentStore.homeActivity.showProgress();
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
            fragmentStore.homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverSupplements.getAllRecords(inputStr, page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("success")) {

                for (int i = 0; i < serverSupplements.getList().size(); i++) {
                    ClassSupplement classSupplement = new ClassSupplement(
                            serverSupplements.getList().get(i).getId(),
                            serverSupplements.getList().get(i).getName(),
                            serverSupplements.getList().get(i).getType(),
                            serverSupplements.getList().get(i).getStock(),
                            serverSupplements.getList().get(i).getPrice()
                    );

                    list.add(classSupplement);
                }


            } else if (result.equals("nodata")) {

            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            supplementsViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentStore.homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentStore.homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }

}
