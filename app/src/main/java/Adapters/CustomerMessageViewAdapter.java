package Adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Fragments.FragmentMessageCustomer;
import GettersAndSetters.ClassMessages;
import OtherClasses.Utils;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class CustomerMessageViewAdapter extends RecyclerView.Adapter<CustomerMessageViewAdapter.VHolder> {
    private ArrayList<ClassMessages> data;
    private FragmentMessageCustomer fragmentMessageCustomer;

    public CustomerMessageViewAdapter(ArrayList<ClassMessages> data, FragmentMessageCustomer fragmentMessageCustomer) {
        this.data = data;
        this.fragmentMessageCustomer = fragmentMessageCustomer;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentMessageCustomer.getActivity());
        View view = inflater.inflate(R.layout.recycler_customer_message_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblCusName.setText(data.get(position).getCustomerName());
        holder.lblCusMessage.setText(data.get(position).getMessage());
        holder.lblDateTime.setText(data.get(position).getDateTime());
        holder.imgView.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

        if (data.get(position).getMessageOwner().equals("customer")){
            holder.lblCusMessage.setTextColor(fragmentMessageCustomer.getResources().getColor(R.color.colorPrimaryDark));
            holder.lblCusMessage.setTypeface(null, Typeface.BOLD);
        }else {
            holder.lblCusMessage.setTextColor(fragmentMessageCustomer.getResources().getColor(R.color.colorMuted));
            holder.lblCusMessage.setTypeface(null, Typeface.NORMAL);
        }

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentMessageCustomer.fragmentCustomerSupport.SwitchFragments("messages", data.get(position).getCustomerId(), data.get(position).getCustomerName(), data.get(position).getPicture());
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblCusName, lblCusMessage, lblDateTime;
        CardView cardClick;
        ImageView imgView;
        View divider;

        public VHolder(View itemView) {
            super(itemView);
            lblCusName = itemView.findViewById(R.id.lblCusName);
            lblCusMessage = itemView.findViewById(R.id.lblCusMessage);
            lblDateTime = itemView.findViewById(R.id.lblDateTime);
            imgView = itemView.findViewById(R.id.imgView);
            cardClick = itemView.findViewById(R.id.cardClick);
            divider = itemView.findViewById(R.id.divider);

        }
    }

}
