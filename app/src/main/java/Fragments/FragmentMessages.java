package Fragments;


import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Adapters.MessagesAdapter;
import GettersAndSetters.ClassMessages;
import OtherClasses.SessionData;
import ServerLink.ServerMessage;

@SuppressLint({"ValidFragment", "NewApi"})
public class FragmentMessages extends Fragment {

    private int cusId;
    public byte[] customerProfilePic;
    FragmentCustomerSupport fragmentCustomerSupport;
    private View iView;
    private RecyclerView rcyViewMessages;
    private MessagesAdapter messagesAdapter;
    private ArrayList<ClassMessages> list;
    private LinearLayoutManager manager;
    public ImageView imgBtnSend;
    private TextView lblSending;
    private Boolean isScrolling = false;
    private EditText txtMessage;
    private int currentItems, totalItems, scrollOutItems, page = 0;
    public ServerMessage serverMessage;
    Handler handler = new Handler();
    boolean isPageOnBottom = true;
    private String messageString;

    public FragmentMessages(FragmentCustomerSupport fragmentCustomerSupport, int cusId, byte[] customerProfilePic) {
        this.fragmentCustomerSupport = fragmentCustomerSupport;
        this.cusId = cusId;
        this.customerProfilePic = customerProfilePic;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.fragment_messages, container, false);


        rcyViewMessages = iView.findViewById(R.id.rcyViewMessages);
        imgBtnSend = iView.findViewById(R.id.imgBtnSend);
        lblSending = iView.findViewById(R.id.lblSending);
        txtMessage = iView.findViewById(R.id.txtMessage);

        list = new ArrayList<>();

        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        messagesAdapter = new MessagesAdapter(list, this);
        rcyViewMessages.setAdapter(messagesAdapter);
        rcyViewMessages.setLayoutManager(manager);

        manager.setReverseLayout(true);

        serverMessage = new ServerMessage();

        rcyViewMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

                if (scrollOutItems > 0){
                    isPageOnBottom = false;
                }else {
                    isPageOnBottom = true;
                }

                String str = "currentItems - " + currentItems + "\nscrollOutItems - " + scrollOutItems + "\ntotalItems - " + totalItems;
//                System.out.println(str);

                if (isScrolling && (currentItems + scrollOutItems == totalItems)) {
                    isScrolling = false;
                    fragmentCustomerSupport.homeActivity.showProgress();
                    fetchData();

                }

            }
        });

        final Runnable runnable = new Runnable() {
            public void run() {
                if (isPageOnBottom)
                    loopThread();

                handler.postDelayed(this, 3000);

            }
        };
        handler.post(runnable);

        imgBtnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                messageString  = txtMessage.getText().toString().trim();

                if (!messageString.equals("")){
                    new SendMessage().execute();

                }
            }
        });

        return iView;
    }

    private void loopThread() {
        page = 0;
        list.clear();
        fetchData();
    }

    private void fetchData() {

        PopulateList populateList = new PopulateList();
        populateList.execute();

    }

    private class SendMessage extends AsyncTask<Void, Void, Void> {

        private int result = 0;

        @Override
        protected void onPreExecute() {
            lblSending.setText("Sending...");
            serverMessage.getClassMessages().setMessage(messageString);
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverMessage.Save();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (result > 0) {
                lblSending.setText("");

                txtMessage.setText("");
                loopThread();
            } else {
                lblSending.setText("Error Sending");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                lblSending.setText("");
            }

        }

        @Override
        protected void onCancelled() {
            cancel(true);

            fragmentCustomerSupport.homeActivity.hideProgress(); /* hide the progressbar dialog here... */
            super.onCancelled();
        }

    }

    private class PopulateList extends AsyncTask<Void, Void, Void> {

        private String result = "";

        @Override
        protected void onPreExecute() {
            serverMessage.getClassMessages().setCustomerId(cusId);
        }

        @Override
        protected Void doInBackground(Void... params) {
            result = serverMessage.getAllRecords(page);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("success")) {

                for (int i = 0; i < serverMessage.getList().size(); i++) {
                    ClassMessages classMessages = new ClassMessages(
                            serverMessage.getList().get(i).getMessageId(),
                            serverMessage.getList().get(i).getCustomerId(),
                            serverMessage.getList().get(i).getMessage(),
                            serverMessage.getList().get(i).getDateTime(),
                            serverMessage.getList().get(i).getMessageOwner()
                    );

                    list.add(classMessages);
                }


            }

            messagesAdapter.notifyDataSetChanged();

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
