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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Fragments.FragmentSchedule;
import GettersAndSetters.ClassSchedule;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import ServerLink.ServerSchedule;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ScheduleViewAdapter extends RecyclerView.Adapter<ScheduleViewAdapter.VHolder> {
    private ArrayList<ClassSchedule> data;
    private FragmentSchedule fragmentSchedule;
    private ArrayList<Integer> isAdding = new ArrayList<>();
    private Validation vd;
    private ServerSchedule serverSchedule;

    public ScheduleViewAdapter(ArrayList<ClassSchedule> data, FragmentSchedule fragmentSchedule) {
        this.data = data;
        this.fragmentSchedule = fragmentSchedule;
        vd = new Validation(fragmentSchedule.getActivity());
        serverSchedule = new ServerSchedule();
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentSchedule.getActivity());
        View view = inflater.inflate(R.layout.schedule_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final VHolder holder, final int position) {
        isAdding = new ArrayList<>();
        for (int i = 0; i < data.size(); i++) {
            isAdding.add(0);
        }

        holder.lblScheduleId.setText("ID : " + data.get(position).getSchdId());
        holder.txtType.setText("" + data.get(position).getType());
        holder.txtTimePeriod.setText("" + data.get(position).getTimePeriod());

        holder.btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearAll(holder);
                isAdding.set(position, 1);
            }
        });

        holder.btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String[] fieldName = {"Type", "Time Period"};
                EditText[] field = {holder.txtType, holder.txtTimePeriod};

                if (vd.CheckEmptyText(fieldName, field)) {
                    getFieldValues(holder, position);

                }
//                ShowDialog.showToast(fragmentSchedule.getActivity(), isAdding.get(position) + "");
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                serverSchedule.getClassSchedule().setSchdId(data.get(position).getSchdId());
                if (serverSchedule.Delete() > 0) {
                    fragmentSchedule.onRefresh();
                } else
                    ShowDialog.showToast(fragmentSchedule.getActivity(), "Error While Deleting the Record");

            }
        });

    }

    private void getFieldValues(VHolder holder, int position) {
        if (isAdding.get(position) == 0)
            serverSchedule.getClassSchedule().setSchdId(data.get(position).getSchdId());

        serverSchedule.getClassSchedule().setType(holder.txtType.getText().toString().trim());
        serverSchedule.getClassSchedule().setTimePeriod(Integer.parseInt(holder.txtTimePeriod.getText().toString().trim()));

        new SaveDetails(position).execute();
    }

    private void clearAll(@NonNull VHolder holder) {
        holder.lblScheduleId.setText("Add a new Schedule.");
        holder.txtType.setText("");
        holder.txtTimePeriod.setText("");
        holder.txtType.requestFocus();

    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    private class SaveDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;
        int position;

        public SaveDetails(int position) {
            this.position = position;
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (isAdding.get(position) == 1) {
                if (serverSchedule.Save() > 0) {
                    result = true;
                } else
                    result = false;
            } else {
                if (serverSchedule.Update() > 0) {
                    result = true;
                } else
                    result = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (isAdding.get(position) == 1) {
                if (result) {
                    ShowDialog.showToast(fragmentSchedule.getActivity(), "Record Added Successfully");
                    fragmentSchedule.onRefresh();

                } else
                    ShowDialog.showToast(fragmentSchedule.getActivity(), "Error While Adding the Record");
            } else {
                if (result) {
                    ShowDialog.showToast(fragmentSchedule.getActivity(), "Record Updated Successfully");
                    fragmentSchedule.onRefresh();
                } else
                    ShowDialog.showToast(fragmentSchedule.getActivity(), "Error While Updating the Record");
            }
        }

    }

    public class VHolder extends RecyclerView.ViewHolder {
        EditText txtType, txtTimePeriod;
        TextView lblScheduleId;
        CardView cardClick;
        Button btnAdd, btnSave, btnDelete;

        public VHolder(View itemView) {
            super(itemView);
            txtType = itemView.findViewById(R.id.txtType);
            txtTimePeriod = itemView.findViewById(R.id.txtTimePeriod);
            lblScheduleId = itemView.findViewById(R.id.lblScheduleId);
            btnAdd = itemView.findViewById(R.id.btnAdd);
            btnSave = itemView.findViewById(R.id.btnSave);
            cardClick = itemView.findViewById(R.id.cardClick);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }

}
