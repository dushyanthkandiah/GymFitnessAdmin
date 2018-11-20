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
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Dialogs.DialogOrderedItem;
import Fragments.FragmentOrders;
import GettersAndSetters.ClassBill;
import GettersAndSetters.ClassItemCart;
import OtherClasses.ShowDialog;
import ServerLink.ServerBilling;
import ServerLink.ServerOrder;
import ServerLink.ServerSupplements;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class OrderedItemViewAdapter extends RecyclerView.Adapter<OrderedItemViewAdapter.VHolder> {
    private ArrayList<ClassItemCart> data;
    private DialogOrderedItem dialogOrderedItem;

    public OrderedItemViewAdapter(ArrayList<ClassItemCart> data, DialogOrderedItem dialogOrderedItem) {
        this.data = data;
        this.dialogOrderedItem = dialogOrderedItem;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(dialogOrderedItem.getActivity());
        View view = inflater.inflate(R.layout.recycler_ordered_item_view_design, parent, false);

        return new VHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblSupId.setText("" + data.get(position).getSupId());
        holder.lblSupName.setText("" + data.get(position).getItemName());
        holder.lblQty.setText("" + data.get(position).getQuantity());

        holder.lblSubTotal.setText("" + data.get(position).getSubTotal());


    }


    @Override
    public int getItemCount() {
        return data.size();
    }


    public class VHolder extends RecyclerView.ViewHolder {

        TextView lblSupId, lblSupName, lblQty, lblSubTotal;
        CardView markBill, cardClick;

        public VHolder(View itemView) {
            super(itemView);

            lblSupId = itemView.findViewById(R.id.lblSupId);
            lblSupName = itemView.findViewById(R.id.lblSupName);
            lblQty = itemView.findViewById(R.id.lblQty);
            lblSubTotal = itemView.findViewById(R.id.lblSubTotal);
            cardClick = itemView.findViewById(R.id.cardClick);

        }
    }

}
