package Fragments;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.HomeActivity;
import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import Adapters.CustomerViewAdapter;
import GettersAndSetters.ClassCustomers;
import OtherClasses.ShowDialog;
import ServerLink.ServerCustomer;
import ServerLink.ServerTrainers;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentCustomers extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public HomeActivity homeActivity;
    public ServerTrainers serverTrainers;
    public String inputStr = "", orderBy = "asc";
    public ServerCustomer serverCustomer;
    int check = 0;
    private View iView;
    private Spinner spinnerAscDesc, spinnerTrainerSort;
    private CustomerViewAdapter customerViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassCustomers> list;
    private GridLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0, trainerId = 0;

    public FragmentCustomers(FragmentManager fragmentManager, HomeActivity homeActivity) {
        this.homeActivity = homeActivity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_customers, container, false);

        spinnerAscDesc = iView.findViewById(R.id.spinnerAscDesc);
        spinnerTrainerSort = iView.findViewById(R.id.spinnerTrainerSort);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);

        serverTrainers = new ServerTrainers();
        serverCustomer = new ServerCustomer();
        loadSortArray();
        loadTrainer();

        list = new ArrayList<>();
        serverTrainers = new ServerTrainers();
        customerViewAdapter = new CustomerViewAdapter(list, this);
        manager = new GridLayoutManager(getActivity(), 2);
        rcyView.setAdapter(customerViewAdapter);
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

        spinnerAscDesc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if (++check > 2) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Ascending"))
                        orderBy = "asc";
                    else
                        orderBy = "desc";

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerTrainerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if (++check > 2) {

                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("All")) {
                        trainerId = 0;
                    } else
                        trainerId = Integer.parseInt(selectedItemText.split("-")[0].trim());

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
        homeActivity.showProgress();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                PopulateList populateList = new PopulateList();
                populateList.execute();

            }
        }, 1000);
    }

    private void loadSortArray() {
        String[] sortArray = new String[]{"Ascending", "Descending"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerAscDesc.setAdapter(spinnerArrayAdapter);

    }

    private void loadTrainer() {
        ArrayList<String> trainerArray = new ArrayList<>();
        trainerArray.add("All");
        if (serverTrainers.getAllRecords().equals("success")) {

            for (int i = 0; i < serverTrainers.getList().size(); i++) {
                trainerArray.add(serverTrainers.getList().get(i).getId() + " - " + serverTrainers.getList().get(i).getName());
            }
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, trainerArray);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerTrainerSort.setAdapter(spinnerArrayAdapter);

    }

    private class PopulateList extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected void onPreExecute() {
            homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {
            serverCustomer.getClassCustomers().setTrainId(trainerId);
            result = serverCustomer.getAllRecords(inputStr, page, orderBy);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverCustomer.getList().size(); i++) {
                    ClassCustomers classTrainers = new ClassCustomers(
                            serverCustomer.getList().get(i).getId(),
                            serverCustomer.getList().get(i).getSchdId(),
                            serverCustomer.getList().get(i).getName(),
                            serverCustomer.getList().get(i).getAddress(),
                            serverCustomer.getList().get(i).getPhone(),
                            serverCustomer.getList().get(i).getGender(),
                            serverCustomer.getList().get(i).getEmail(),
                            serverCustomer.getList().get(i).getPassword(),
                            serverCustomer.getList().get(i).getStatus(),
                            serverCustomer.getList().get(i).getDob(),
                            serverCustomer.getList().get(i).getHeight(),
                            serverCustomer.getList().get(i).getWeight(),
                            serverCustomer.getList().get(i).getPicture()

                    );

                    list.add(classTrainers);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Trainers Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            customerViewAdapter.notifyDataSetChanged();
            swp2Rfsh.setRefreshing(false);
            homeActivity.hideProgress();
            page += 1;

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            homeActivity.hideProgress(); /*hide the progressbar dialog here...*/
            super.onCancelled();
        }

    }

}
