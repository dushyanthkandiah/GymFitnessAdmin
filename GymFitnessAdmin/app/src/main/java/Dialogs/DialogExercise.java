package Dialogs;


import android.annotation.SuppressLint;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.util.ArrayList;

import Fragments.FragmentExercises;
import GettersAndSetters.ClassExercise;
import GettersAndSetters.ClassSchedule;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import ServerLink.ServerExercise;
import ServerLink.ServerSchedule;

@SuppressLint("ValidFragment")
public class DialogExercise extends BaseDialogFragment<DialogExercise.OnDialogFragmentClickListener> {

    ArrayList<ClassSchedule> schdList;
    private FragmentExercises fragmentExercises;
    private View iView;
    private ClassExercise classExercise;
    private TextView lblExerciseId;
    private EditText txtExercise, txtSets, txtTimes;
    private Button btnSave;
    private Validation vd;
    private String type;
    private int selectedSchedule = 0, autoId = 0;
    private Spinner spinnerSchdId;
    private ServerExercise serverExercise;

    public DialogExercise(FragmentExercises fragmentExercises, ClassExercise classExercise, String type) {
        this.fragmentExercises = fragmentExercises;
        this.classExercise = classExercise;
        this.type = type;
    }

    public static DialogExercise newInstance(FragmentExercises fragmentExercises, ClassExercise classExercise, String type) {
        DialogExercise dialogSelect = new DialogExercise(fragmentExercises, classExercise, type);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_excercise, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        lblExerciseId = iView.findViewById(R.id.lblExerciseId);
        txtExercise = iView.findViewById(R.id.txtExercise);
        txtSets = iView.findViewById(R.id.txtSets);
        txtTimes = iView.findViewById(R.id.txtTimes);
        spinnerSchdId = iView.findViewById(R.id.spinnerSchdId);
        btnSave = iView.findViewById(R.id.btnSave);

        loadSpinner();

        if (type.equals("update")) {
            loadData();
        } else {
            lblExerciseId.setText("");
        }

        serverExercise = new ServerExercise();

        vd = new Validation(getActivity());

        spinnerSchdId.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedSchedule = schdList.get(position).getSchdId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                validate();
            }
        });


        return iView;
    }

    void loadData() {
        autoId = classExercise.getId();
        lblExerciseId.setText("ID : " + classExercise.getId());
        txtExercise.setText("" + classExercise.getExcercise());
        txtSets.setText("" + classExercise.getSets());
        txtTimes.setText("" + classExercise.getTimes());

        selectedSchedule = classExercise.getSchdId();

    }

    private void validate() {
        String[] fieldName = {"Exercise", "Sets", "Times"};
        EditText[] field = {txtExercise, txtSets, txtTimes};

        if (vd.CheckEmptyText(fieldName, field)) {
            getFieldValues();

        }
    }

    private void getFieldValues() {
        if (autoId != 0)
            serverExercise.getClassExercise().setId(autoId);

        serverExercise.getClassExercise().setExcercise(txtExercise.getText().toString().trim());
        serverExercise.getClassExercise().setSets(Integer.parseInt(txtSets.getText().toString().trim()));
        serverExercise.getClassExercise().setTimes(Integer.parseInt(txtTimes.getText().toString().trim()));
        serverExercise.getClassExercise().setSchdId(selectedSchedule);

        new SaveDetails().execute();
    }

    private void loadSpinner() {
        ServerSchedule serverSchedule = new ServerSchedule();
        schdList = new ArrayList<>();
        if (serverSchedule.getAllRecords("").equals("success")) {
            schdList = serverSchedule.getList();
        }
        ArrayList<String> scheduleList = new ArrayList<>();

        selectedSchedule = schdList.get(0).getSchdId();

        for (int i = 0; i < schdList.size(); i++) {
            scheduleList.add(schdList.get(i).getSchdId() + " - " + schdList.get(i).getType());
        }

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, scheduleList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSchdId.setAdapter(arrayAdapter);
    }

    public interface OnDialogFragmentClickListener {

    }

    private class SaveDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (autoId == 0) {
                if (serverExercise.Save() > 0) {
                    result = true;
                } else
                    result = false;
            } else {
                if (serverExercise.Update() > 0) {
                    result = true;
                } else
                    result = false;
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            if (autoId == 0) {
                if (result) {
                    ShowDialog.showToast(getActivity(), "Record Added Successfully");
                    fragmentExercises.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Adding the Record");
            } else {
                if (result) {
                    ShowDialog.showToast(getActivity(), "Record Updated Successfully");
                    fragmentExercises.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Updating the Record");
            }
        }

    }
}
