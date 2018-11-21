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
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Dialogs.DialogExercise;
import Dialogs.DialogSupplements;
import Fragments.FragmentExercises;
import Fragments.FragmentSupplements;
import GettersAndSetters.ClassExercise;
import GettersAndSetters.ClassSupplement;
import OtherClasses.StringChange;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class SupplementsViewAdapter extends RecyclerView.Adapter<SupplementsViewAdapter.VHolder> {
    private ArrayList<ClassSupplement> data;
    private FragmentSupplements fragmentSupplements;

    public SupplementsViewAdapter(ArrayList<ClassSupplement> data, FragmentSupplements fragmentSupplements) {
        this.data = data;
        this.fragmentSupplements = fragmentSupplements;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentSupplements.getActivity());
        View view = inflater.inflate(R.layout.recycler_supplements_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {

        holder.lblSuplId.setText("" + data.get(position).getId());
        holder.lblSupName.setText("" + data.get(position).getName());
        holder.lblStock.setText("" + data.get(position).getStock());
        holder.lblPrice.setText("" + data.get(position).getPrice());
        holder.lblType.setText("" + data.get(position).getType());

        if (position == data.size() - 1)
            holder.divider.setVisibility(View.GONE);
        else
            holder.divider.setVisibility(View.VISIBLE);

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogSupplements dialogSupplements = DialogSupplements.newInstance(fragmentSupplements, data.get(position), "update");
                dialogSupplements.show(fragmentSupplements.getFragmentManager(), "dialog");
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblSuplId, lblSupName, lblStock, lblPrice, lblType;
        CardView cardClick;
        View divider;

        public VHolder(View itemView) {
            super(itemView);
            lblSuplId = itemView.findViewById(R.id.lblSuplId);
            lblSupName = itemView.findViewById(R.id.lblSupName);
            lblStock = itemView.findViewById(R.id.lblStock);
            lblPrice = itemView.findViewById(R.id.lblPrice);
            lblType = itemView.findViewById(R.id.lblType);
            cardClick = itemView.findViewById(R.id.cardClick);
            divider = itemView.findViewById(R.id.divider);
        }
    }

}
