package Dialogs;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Adapters.OrderedItemViewAdapter;
import Fragments.FragmentOrders;
import GettersAndSetters.ClassCustomers;
import GettersAndSetters.ClassItemCart;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerOrder;
import ServerLink.ServerSchedule;
import pl.droidsonroids.gif.GifImageView;


@SuppressLint({"ValidFragment", "NewApi", "SetTextI18n"})
public class DialogOrderedItem extends BaseDialogFragment<DialogOrderedItem.OnDialogFragmentClickListener> {

    private View iView;

    private RecyclerView rcyView;
    private TextView lblOrderId, lblTotal;
    private GifImageView progressBar;
    private ArrayList<ClassItemCart> list;
    private OrderedItemViewAdapter orderedItemViewAdapter;
    private int billId;
    private LinearLayoutManager manager;
    private Boolean isScrolling = false;
    private int currentItems, totalItems, scrollOutItems, page = 0;
private Double total;

    public DialogOrderedItem(int billId, Double total) {
        this.billId = billId;
        this.total = total;
    }

    public static DialogOrderedItem newInstance(FragmentOrders fragmentOrders, int billId, Double total) {
        DialogOrderedItem dialogSelect = new DialogOrderedItem(billId, total);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_ordered_items, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        lblOrderId = iView.findViewById(R.id.lblOrderId);
        lblTotal = iView.findViewById(R.id.lblTotal);
        progressBar = iView.findViewById(R.id.progressBar);
        rcyView = iView.findViewById(R.id.rcyView);

        list = new ArrayList<>();
        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);

        orderedItemViewAdapter = new OrderedItemViewAdapter(list, this);
        rcyView.setAdapter(orderedItemViewAdapter);
        rcyView.setLayoutManager(manager);

        lblOrderId.setText("Order ID : " + billId);
        lblTotal.setText("Total : " + total);

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

                    new loadData().execute();
                }

            }
        });

        new loadData().execute();

        return iView;
    }


    private class loadData extends AsyncTask<Void, Void, Void> {
        ServerOrder serverOrder = new ServerOrder();
        String result;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            serverOrder.getClassItemCart().setBillId(billId);
            result = serverOrder.getAllRecords();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {

            if (result.equals("success")) {

                for (int i = 0; i < serverOrder.getList().size(); i++) {
                    ClassItemCart classItemCart = new ClassItemCart(
                            serverOrder.getList().get(i).getSupId(),
                            serverOrder.getList().get(i).getQuantity(),
                            serverOrder.getList().get(i).getSubTotal(),
                            serverOrder.getList().get(i).getItemName()

                    );

                    list.add(classItemCart);
                }


            } else if (result.equals("nodata")) {
//                ShowDialog.showToast(getActivity(), "No more Exercises Available");
            } else {
                ShowDialog.showToast(getActivity(), "Connection not Available");
            }

            orderedItemViewAdapter.notifyDataSetChanged();
            progressBar.setVisibility(View.INVISIBLE);

            page += 1;
        }
    }


    public interface OnDialogFragmentClickListener {

    }
}
