package Adapters;

import android.annotation.SuppressLint;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Fragments.FragmentCustomerSupport;
import Fragments.FragmentMessages;
import GettersAndSetters.ClassMessages;
import OtherClasses.Utils;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class MessagesAdapter extends RecyclerView.Adapter<MessagesAdapter.VHolder> {
    private ArrayList<ClassMessages> data;
    private FragmentMessages fragmentMessages;

    public MessagesAdapter(ArrayList<ClassMessages> data, FragmentMessages fragmentMessages) {
        this.data = data;
        this.fragmentMessages = fragmentMessages;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentMessages.getActivity());
        View view = inflater.inflate(R.layout.recycler_messages_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        if (!data.get(position).getMessageOwner().equals("admin")) {

            holder.layoutReceive.setVisibility(View.VISIBLE);
            holder.layoutSend.setVisibility(View.GONE);

            holder.lblMessageReceive.setText(data.get(position).getMessage());
            holder.imgViewCusProPic.setImageBitmap(Utils.getImage(fragmentMessages.customerProfilePic));
            String dateTime = data.get(position).getDateTime();
            holder.lblReceivedTime.setText(dateTime);

            holder.cardClickReceived.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (holder.checkSelectedReceive.getText().toString().equals("0")) {
                        holder.checkSelectedReceive.setText("1");
                        holder.lblReceivedTime.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkSelectedReceive.setText("0");
                        holder.lblReceivedTime.setVisibility(View.GONE);
                    }

                }
            });


        } else {

            holder.layoutReceive.setVisibility(View.GONE);
            holder.layoutSend.setVisibility(View.VISIBLE);

            holder.lblMessageSend.setText(data.get(position).getMessage());

            String dateTime = data.get(position).getDateTime();
            holder.lblSentTime.setText(dateTime);

            holder.cardClickSend.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (holder.checkSelectedSend.getText().toString().equals("0")) {
                        holder.checkSelectedSend.setText("1");
                        holder.lblSentTime.setVisibility(View.VISIBLE);
                    } else {
                        holder.checkSelectedSend.setText("0");
                        holder.lblSentTime.setVisibility(View.GONE);
                    }
                }
            });

        }


    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        CardView cardClickSend, cardClickReceived;
        LinearLayout cardClick, layoutReceive, layoutSend;
        TextView lblMessageReceive, lblMessageSend, lblReceivedTime, lblSentTime, checkSelectedReceive, checkSelectedSend;
    ImageView imgViewCusProPic;

        public VHolder(View itemView) {
            super(itemView);

            cardClick = itemView.findViewById(R.id.cardClick);
            layoutReceive = itemView.findViewById(R.id.layoutReceive);
            layoutSend = itemView.findViewById(R.id.layoutSend);
            lblMessageReceive = itemView.findViewById(R.id.lblMessageReceive);
            lblMessageSend = itemView.findViewById(R.id.lblMessageSend);
            lblReceivedTime = itemView.findViewById(R.id.lblReceivedTime);
            lblSentTime = itemView.findViewById(R.id.lblSentTime);
            cardClickSend = itemView.findViewById(R.id.cardClickSend);
            cardClickReceived = itemView.findViewById(R.id.cardClickReceived);
            checkSelectedReceive = itemView.findViewById(R.id.checkSelectedReceive);
            checkSelectedSend = itemView.findViewById(R.id.checkSelectedSend);
            imgViewCusProPic = itemView.findViewById(R.id.imgViewCusProPic);
        }
    }

}
