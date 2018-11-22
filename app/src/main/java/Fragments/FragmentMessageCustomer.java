package Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;

import com.example.abubakr.gymfitnessadmin.HomeActivity;
import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Adapters.CustomerMessageViewAdapter;
import GettersAndSetters.ClassMessages;
import OtherClasses.ShowDialog;
import ServerLink.ServerMessage;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentMessageCustomer extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public FragmentCustomerSupport fragmentCustomerSupport;
    public String inputStr = "";
    public ServerMessage serverMessage;
    private View iView;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassMessages> list;
    private CustomerMessageViewAdapter customerMessageViewAdapter;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;


    public FragmentMessageCustomer(FragmentCustomerSupport fragmentCustomerSupport) {
        this.fragmentCustomerSupport = fragmentCustomerSupport;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_message_customer, container, false);

        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        serverMessage = new ServerMessage();

        list = new ArrayList<>();
        customerMessageViewAdapter = new CustomerMessageViewAdapter(list, this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcyView.setAdapter(customerMessageViewAdapter);
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
        fragmentCustomerSupport.homeActivity.showProgress();
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
            fragmentCustomerSupport.homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverMessage.getAllRecords(inputStr, page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverMessage.getList().size(); i++) {
                    ClassMessages classMessages = new ClassMessages();
                    classMessages.setMessageId(serverMessage.getList().get(i).getMessageId());
                    classMessages.setMessage(serverMessage.getList().get(i).getMessage());
                    classMessages.setDateTime(serverMessage.getList().get(i).getDateTime());
                    classMessages.setMessageOwner(serverMessage.getList().get(i).getMessageOwner());
                    classMessages.setCustomerId(serverMessage.getList().get(i).getCustomerId());
                    classMessages.setCustomerName(serverMessage.getList().get(i).getCustomerName());
                    classMessages.setPicture(serverMessage.getList().get(i).getPicture());

                    list.add(classMessages);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Trainers Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            customerMessageViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            fragmentCustomerSupport.homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentCustomerSupport.homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }

}
