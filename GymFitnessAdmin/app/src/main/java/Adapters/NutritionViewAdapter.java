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
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Dialogs.DialogNutrition;
import Fragments.FragmentNutritions;
import GettersAndSetters.ClassNutritions;
import OtherClasses.StringChange;
import OtherClasses.Utils;
import ServerLink.ServerNutrition;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class NutritionViewAdapter extends RecyclerView.Adapter<NutritionViewAdapter.VHolder> {
    private ArrayList<ClassNutritions> data;
    private FragmentNutritions fragmentNutritions;

    public NutritionViewAdapter(ArrayList<ClassNutritions> data, FragmentNutritions fragmentNutritions) {
        this.data = data;
        this.fragmentNutritions = fragmentNutritions;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentNutritions.getActivity());
        View view = inflater.inflate(R.layout.nutrition_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {

        StringChange.MakeColoredText(fragmentNutritions.getActivity(), R.color.colorPrimaryDark, holder.lblFood, "Food : ", data.get(position).getFood());
        holder.lblFoodType.setText("Type : " + data.get(position).getType());
        holder.imgViewNutrition.setImageBitmap(Utils.getImage(data.get(position).getPicture()));

        holder.imgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ServerNutrition serverNutrition = new ServerNutrition();
                serverNutrition.getClassNutritions().setId(data.get(position).getId());
                if (serverNutrition.Delete() > 0) {
                    fragmentNutritions.onRefresh();
                }
            }
        });

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogNutrition dialogNutrition = DialogNutrition.newInstance(fragmentNutritions, data.get(position), "update");
                dialogNutrition.show(fragmentNutritions.getFragmentManager(), "dialog");
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblFood, lblFoodType;
        CardView cardClick;
        ImageView imgViewNutrition, imgDelete;

        public VHolder(View itemView) {
            super(itemView);
            lblFood = itemView.findViewById(R.id.lblFood);
            lblFoodType = itemView.findViewById(R.id.lblFoodType);
            cardClick = itemView.findViewById(R.id.cardClick);
            imgViewNutrition = itemView.findViewById(R.id.imgViewNutrition);
            imgDelete = itemView.findViewById(R.id.imgDelete);
        }
    }

}
