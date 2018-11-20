package Adapters;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
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

import Dialogs.DialogCustomer;
import Fragments.FragmentCustomers;
import GettersAndSetters.ClassCustomers;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import ServerLink.ServerCustomer;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class CustomerViewAdapter extends RecyclerView.Adapter<CustomerViewAdapter.VHolder> {
    private ArrayList<ClassCustomers> data;
    private FragmentCustomers fragmentCustomers;

    public CustomerViewAdapter(ArrayList<ClassCustomers> data, FragmentCustomers fragmentCustomers) {
        this.data = data;
        this.fragmentCustomers = fragmentCustomers;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentCustomers.getActivity());
        View view = inflater.inflate(R.layout.recycler_customer_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {

        holder.lblName.setText(data.get(position).getName());
        holder.lblEmail.setText(data.get(position).getEmail());
        holder.lblGender.setText(data.get(position).getGender());
        holder.imgProfilePicture.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

        if (data.get(position).getStatus().equals("inactive")) {
            holder.imgEnableDisable.setImageDrawable(fragmentCustomers.getResources().getDrawable(R.drawable.eye_disable));
        } else
            holder.imgEnableDisable.setImageDrawable(fragmentCustomers.getResources().getDrawable(R.drawable.eye_enable));

        holder.imgEnableDisable.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SaveDetails(data.get(position), holder.imgEnableDisable).execute();
            }
        });

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogCustomer dialogCustomer = DialogCustomer.newInstance(data.get(position));
                dialogCustomer.show(fragmentCustomers.getFragmentManager(), "dialog");
            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class SaveDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;
        ServerCustomer serverCustomer;
        String name, status;
        ImageView imgEnableDisable;
        int id;
        ClassCustomers classCustomers;


        public SaveDetails(ClassCustomers classCustomers, ImageView imgEnableDisable) {
            this.classCustomers = classCustomers;

            this.name = classCustomers.getName();
            this.imgEnableDisable = imgEnableDisable;
            this.id = classCustomers.getId();

        }

        @Override
        protected void onPreExecute() {
            fragmentCustomers.homeActivity.showProgress();
            serverCustomer = new ServerCustomer();
            serverCustomer.getClassCustomers().setId(id);
        }

        @Override
        protected Void doInBackground(Void... params) {

            if (serverCustomer.getBlockStatus().equals("success")) {
                status = serverCustomer.getClassCustomers().getStatus();

                if (status.equals("active"))
                    serverCustomer.getClassCustomers().setStatus("inactive");
                else
                    serverCustomer.getClassCustomers().setStatus("active");

                if (serverCustomer.EnableDisableCustomer() > 0) {
                    result = true;
                } else
                    result = false;

            } else {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            fragmentCustomers.homeActivity.hideProgress();
            if (result) {
                if (status.equals("active")) {
                    ShowDialog.showToast(fragmentCustomers.getActivity(), name + " has been blocked by you");
                    imgEnableDisable.setImageDrawable(fragmentCustomers.getResources().getDrawable(R.drawable.eye_disable));
                } else {
                    ShowDialog.showToast(fragmentCustomers.getActivity(), name + " has been unblocked by you");
                    imgEnableDisable.setImageDrawable(fragmentCustomers.getResources().getDrawable(R.drawable.eye_enable));
                }

            } else
                ShowDialog.showToast(fragmentCustomers.getActivity(), "Error While blocking/unblocking " + name);

        }

    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblName, lblEmail, lblGender;
        CardView cardClick;
        ImageView imgProfilePicture, imgEnableDisable;

        public VHolder(View itemView) {
            super(itemView);
            lblName = itemView.findViewById(R.id.lblName);
            lblEmail = itemView.findViewById(R.id.lblEmail);
            lblGender = itemView.findViewById(R.id.lblGender);
            imgProfilePicture = itemView.findViewById(R.id.imgProfilePicture);
            imgEnableDisable = itemView.findViewById(R.id.imgEnableDisable);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }

}
