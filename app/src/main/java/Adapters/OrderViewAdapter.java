package Adapters;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Dialogs.DialogCustomer;
import Dialogs.DialogOrderedItem;
import Fragments.FragmentCustomers;
import Fragments.FragmentOrders;
import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassCustomers;
import GettersAndSetters.ClassItemCart;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerBilling;
import ServerLink.ServerCustomer;
import ServerLink.ServerOrder;
import ServerLink.ServerSupplements;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class OrderViewAdapter extends RecyclerView.Adapter<OrderViewAdapter.VHolder> {
    private ArrayList<ClassBill> data;
    private FragmentOrders fragmentOrders;

    public OrderViewAdapter(ArrayList<ClassBill> data, FragmentOrders fragmentOrders) {
        this.data = data;
        this.fragmentOrders = fragmentOrders;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentOrders.getActivity());
        View view = inflater.inflate(R.layout.recycler_order_view_design, parent, false);

        return new VHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblBillId.setText("Bill ID - " + data.get(position).getBillId());
        holder.lblCusName.setText("" + data.get(position).getCusName());
        holder.lblOrderDate.setText("" + data.get(position).getBillDate());

        holder.lblTotal.setText("" + data.get(position).getTotal());

        if (data.get(position).getStatus() == 0) {
            holder.markBill.setVisibility(View.VISIBLE);
            holder.lblStatus.setText("Pending");
        } else {
            holder.markBill.setVisibility(View.GONE);
            holder.lblStatus.setText("Sold");
        }

        holder.markBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                new UpdateDetails(data, position).execute();



                                break;

                            case DialogInterface.BUTTON_NEGATIVE:

                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(fragmentOrders.getActivity());
                builder.setMessage("Are you sure this Bill is paid ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
            }
        });

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogOrderedItem dialogOrderedItem = DialogOrderedItem.newInstance(fragmentOrders, data.get(position).getBillId(), data.get(position).getTotal());
                dialogOrderedItem.show(fragmentOrders.getFragmentManager(), "dialog");
            }
        });

    }

    private class UpdateDetails extends AsyncTask<Void, Void, Void> {

        ArrayList<ClassBill> data;
        int position;
        boolean flag = false;
        String itemName = "";
        ServerBilling serverBilling;

        public UpdateDetails(ArrayList<ClassBill> data, int position) {

            this.data = data;
            this.position = position;

        }

        @Override
        protected void onPreExecute() {
             fragmentOrders.fragmentStore.homeActivity.showProgress();
        }

        @Override
        protected Void doInBackground(Void... params) {

            ServerOrder serverOrder = new ServerOrder();
            serverOrder.getClassItemCart().setBillId(data.get(position).getBillId());

            if (serverOrder.getAllRecords().equals("success")) {

                ArrayList<ClassItemCart> classItemCarts = serverOrder.getList();




                for (int i = 0; i < classItemCarts.size(); i++) {
                    int supId = classItemCarts.get(i).getSupId();

                    ServerSupplements serverSupplements = new ServerSupplements();
                    serverSupplements.getClassSupplement().setStock(classItemCarts.get(i).getQuantity());
                    serverSupplements.getClassSupplement().setId(supId);
                    itemName = classItemCarts.get(i).getItemName();
                    if (!serverSupplements.isStockValid().equals("success")) {
                        flag = true;

                        break;
                    }
                }

                if (!flag) {
                    for (int i = 0; i < classItemCarts.size(); i++) {
                        int supId = classItemCarts.get(i).getSupId();

                        ServerSupplements serverSupplements = new ServerSupplements();
                        serverSupplements.getClassSupplement().setId(supId);
                        serverSupplements.getClassSupplement().setStock(classItemCarts.get(i).getQuantity());

                        if (serverSupplements.ReduceStock() > 0) {

                        }
                    }

                    serverBilling = new ServerBilling();
                    serverBilling.getClassBill().setBillId(data.get(position).getBillId());
                    if (serverBilling.MarkStatusPending() > 0) {

                    }
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            fragmentOrders.fragmentStore.homeActivity.hideProgress();
            if (flag) {
                ShowDialog.showToastLong(fragmentOrders.getActivity(), itemName + " is low in stock");
            }else {
                fragmentOrders.onRefresh();
            }

        }

    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {

        TextView lblBillId, lblCusName, lblOrderDate, lblStatus, lblTotal;
        CardView markBill, cardClick;

        public VHolder(View itemView) {
            super(itemView);

            lblBillId = itemView.findViewById(R.id.lblBillId);
            lblCusName = itemView.findViewById(R.id.lblCusName);
            lblOrderDate = itemView.findViewById(R.id.lblOrderDate);
            lblStatus = itemView.findViewById(R.id.lblStatus);
            lblTotal = itemView.findViewById(R.id.lblTotal);
            markBill = itemView.findViewById(R.id.markBill);
            cardClick = itemView.findViewById(R.id.cardClick);

        }
    }

}
