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
import Fragments.FragmentExercises;
import GettersAndSetters.ClassExercise;
import OtherClasses.StringChange;

@RequiresApi(api = Build.VERSION_CODES.N)
@SuppressLint("NewApi")
public class ExerciseViewAdapter extends RecyclerView.Adapter<ExerciseViewAdapter.VHolder> {
    private ArrayList<ClassExercise> data;
    private FragmentExercises fragmentExercises;

    public ExerciseViewAdapter(ArrayList<ClassExercise> data, FragmentExercises fragmentExercises) {
        this.data = data;
        this.fragmentExercises = fragmentExercises;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(fragmentExercises.getActivity());
        View view = inflater.inflate(R.layout.exercise_recycler_view_design, parent, false);

        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, final int position) {

        StringChange.MakeColoredText(fragmentExercises.getActivity(), R.color.colorPrimaryDark, holder.lblExercise, "Exercise : ", data.get(position).getExcercise());
        holder.lblSets.setText("Sets : " + data.get(position).getSets());
        holder.lblTimes.setText("Times : " + data.get(position).getTimes());

        holder.cardClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogExercise dialogExercise = DialogExercise.newInstance(fragmentExercises, data.get(position), "update");
                dialogExercise.show(fragmentExercises.getFragmentManager(), "dialog");
            }
        });

    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView lblExercise, lblSets, lblTimes;
        CardView cardClick;

        public VHolder(View itemView) {
            super(itemView);
            lblExercise = itemView.findViewById(R.id.lblExercise);
            lblSets = itemView.findViewById(R.id.lblSets);
            lblTimes = itemView.findViewById(R.id.lblTimes);
            cardClick = itemView.findViewById(R.id.cardClick);
        }
    }

}
