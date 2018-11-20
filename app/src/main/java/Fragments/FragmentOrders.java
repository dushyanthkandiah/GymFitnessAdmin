package Fragments;


import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import Adapters.OrderViewAdapter;
import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassCustomers;
import OtherClasses.ShowDialog;
import ServerLink.ServerBilling;
import ServerLink.ServerCustomer;


@SuppressLint({"ValidFragment", "NewApi", "SimpleDateFormat"})
public class FragmentOrders extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    public FragmentStore fragmentStore;
    public String inputStr = "", orderBy = "asc", fromDate, toDate;
    public ServerBilling serverBilling;
    private ServerCustomer serverCustomer;
    int check = 0;
    private View iView;
    private Spinner spinnerAscDesc, spinnerCustomerSort, spinnerStatus;
    private EditText txtFromDate, txtToDate;
    private OrderViewAdapter orderViewAdapter;
    private RecyclerView rcyView;
    private SwipeRefreshLayout swp2Rfsh;
    private ArrayList<ClassBill> list;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0, cusId = 0, status = 0;
    private SimpleDateFormat sdf;
    private Calendar fromCalendar, toCalendar;

    public FragmentOrders(FragmentStore fragmentStore) {
        this.fragmentStore = fragmentStore;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_orders, container, false);

        spinnerAscDesc = iView.findViewById(R.id.spinnerAscDesc);
        spinnerCustomerSort = iView.findViewById(R.id.spinnerCustomerSort);
        spinnerStatus = iView.findViewById(R.id.spinnerStatus);
        rcyView = iView.findViewById(R.id.rcyView);
        swp2Rfsh = iView.findViewById(R.id.swp2Rfsh);
        txtFromDate = iView.findViewById(R.id.txtFromDate);
        txtToDate = iView.findViewById(R.id.txtToDate);

        serverCustomer = new ServerCustomer();
        serverBilling = new ServerBilling();

        loadSortArray();
        loadStatusArray();
        loadCustomer();

        list = new ArrayList<>();
        orderViewAdapter = new OrderViewAdapter(list, this);
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rcyView.setAdapter(orderViewAdapter);
        rcyView.setLayoutManager(manager);

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        fromCalendar = Calendar.getInstance();
        toCalendar = Calendar.getInstance();

        /* setting a custom from date */
        fromCalendar.set(Calendar.YEAR, 2017);
        fromCalendar.set(Calendar.MONTH, 7);
        fromCalendar.set(Calendar.DAY_OF_MONTH, 1);

        txtFromDate.setText(sdf.format(fromCalendar.getTime()));
        txtToDate.setText(sdf.format(toCalendar.getTime()));
        fromDate = txtFromDate.getText().toString();
        toDate = txtToDate.getText().toString();

        final DatePickerDialog.OnDateSetListener dateFrom = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                fromCalendar.set(Calendar.YEAR, year);
                fromCalendar.set(Calendar.MONTH, monthOfYear);
                fromCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                fromDate = sdf.format(fromCalendar.getTime());
                txtFromDate.setText(fromDate);
                SearchData();
            }

        };

        final DatePickerDialog.OnDateSetListener dateTo = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                toCalendar.set(Calendar.YEAR, year);
                toCalendar.set(Calendar.MONTH, monthOfYear);
                toCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                toDate = sdf.format(toCalendar.getTime());
                txtToDate.setText(toDate);

                SearchData();
            }

        };

        txtFromDate.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateFrom,
                        fromCalendar.get(Calendar.YEAR),
                        fromCalendar.get(Calendar.MONTH),
                        fromCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        txtToDate.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateTo,
                        toCalendar.get(Calendar.YEAR),
                        toCalendar.get(Calendar.MONTH),
                        toCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });


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
                if (++check > 3) {
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

        spinnerStatus.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if (++check > 3) {
                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("Pending"))
                        status = 0;
                    else
                        status = 1;

                    SearchData();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerCustomerSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
                if (++check > 3) {

                    String selectedItemText = (String) parent.getItemAtPosition(position);

                    if (selectedItemText.equals("All")) {
                        cusId = 0;
                    } else
                        cusId = Integer.parseInt(selectedItemText.split("-")[0].trim());

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

        }

        @Override
        protected Void doInBackground(Void... params) {
            serverBilling.getClassBill().setCusId(cusId);
            serverBilling.getClassBill().setStatus(status);
            result = serverBilling.getAllRecords(fromDate, toDate, page, orderBy);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {


            if (result.equals("success")) {

                for (int i = 0; i < serverBilling.getList().size(); i++) {
                    ClassBill classBill = new ClassBill(
                            serverBilling.getList().get(i).getBillId(),
                            serverBilling.getList().get(i).getCusId(),
                            serverBilling.getList().get(i).getBillDate(),
                            serverBilling.getList().get(i).getTotal(),
                            serverBilling.getList().get(i).getStatus(),
                            serverBilling.getList().get(i).getCusName()

                    );

                    list.add(classBill);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Trainers Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            orderViewAdapter.notifyDataSetChanged();
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


    private void loadSortArray() {
        String[] sortArray = new String[]{"Ascending", "Descending"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerAscDesc.setAdapter(spinnerArrayAdapter);

    }

    private void loadStatusArray() {
        String[] sortArray = new String[]{"Pending", "Sold"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerStatus.setAdapter(spinnerArrayAdapter);

    }

    private void loadCustomer() {
        ArrayList<String> tempArr = new ArrayList<>();
        tempArr.add("All");
        if (serverCustomer.getAllRecords().equals("success")) {

            for (int i = 0; i < serverCustomer.getList().size(); i++) {
                tempArr.add(serverCustomer.getList().get(i).getId() + " - " + serverCustomer.getList().get(i).getName());
            }
        }

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, tempArr);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerCustomerSort.setAdapter(spinnerArrayAdapter);

    }


}
