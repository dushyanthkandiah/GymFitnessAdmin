package Dialogs;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import Fragments.FragmentNutritions;
import GettersAndSetters.ClassNutritions;
import GettersAndSetters.ClassSchedule;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerNutrition;
import ServerLink.ServerSchedule;

@SuppressLint("ValidFragment")
public class DialogNutrition extends BaseDialogFragment<DialogNutrition.OnDialogFragmentClickListener> {


    private static final int PICK_IMAGE = 1;
    public byte[] imgByte;
    ArrayList<ClassSchedule> schdList;
    private FragmentNutritions fragmentNutritions;
    private View iView;
    private ClassNutritions classNutritions;
    private ImageView imgViewNutrition;
    private TextView lblNutritionId;
    private EditText txtFood, txtType;
    private Button btnSave;
    private Validation vd;
    private String type;
    private int selectedSchedule = 0, autoId = 0;
    private Spinner spinnerSchdId;
    private ServerNutrition serverNutrition;

    public DialogNutrition(FragmentNutritions fragmentNutritions, ClassNutritions classNutritions, String type) {
        this.fragmentNutritions = fragmentNutritions;
        this.classNutritions = classNutritions;
        this.type = type;
    }

    public static DialogNutrition newInstance(FragmentNutritions fragmentNutritions, ClassNutritions classNutritions, String type) {
        DialogNutrition dialogSelect = new DialogNutrition(fragmentNutritions, classNutritions, type);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_nutrition, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        imgViewNutrition = iView.findViewById(R.id.imgViewNutrition);
        lblNutritionId = iView.findViewById(R.id.lblNutritionId);
        txtFood = iView.findViewById(R.id.txtFood);
        txtType = iView.findViewById(R.id.txtType);
        spinnerSchdId = iView.findViewById(R.id.spinnerSchdId);
        btnSave = iView.findViewById(R.id.btnSave);

        loadSpinner();

        if (type.equals("update")) {
            loadData();
        } else {
            lblNutritionId.setText("");
            selectDefaultImage();
        }

        serverNutrition = new ServerNutrition();

        vd = new Validation(getActivity());

        imgViewNutrition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                startActivityForResult(gallery, PICK_IMAGE);
            }
        });


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
        autoId = classNutritions.getId();
        lblNutritionId.setText("ID : " + classNutritions.getId());
        txtFood.setText("" + classNutritions.getFood());
        txtType.setText("" + classNutritions.getType());

        imgByte = classNutritions.getPicture();
        selectedSchedule = classNutritions.getSchdId();

        imgViewNutrition.setImageBitmap(Utils.getImage(imgByte));

        Utils.setSpinnerPosition(spinnerSchdId, String.valueOf(selectedSchedule));

    }

    private void validate() {
        String[] fieldName = {"Food", "Type"};
        EditText[] field = {txtFood, txtType};

        if (vd.CheckEmptyText(fieldName, field)) {
            getFieldValues();

        }
    }

    private void getFieldValues() {
        if (autoId != 0)
            serverNutrition.getClassNutritions().setId(autoId);

        serverNutrition.getClassNutritions().setFood(txtFood.getText().toString().trim());
        serverNutrition.getClassNutritions().setType(txtType.getText().toString().trim());
        serverNutrition.getClassNutritions().setSchdId(selectedSchedule);
        serverNutrition.getClassNutritions().setPicture(imgByte);

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

    @SuppressLint("ResourceType")
    private void selectDefaultImage() {
        try {

            InputStream inputstream = getActivity().getResources().openRawResource(R.drawable.nutrition_select);

            imgByte = Utils.getBytes(inputstream, imgViewNutrition);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            if (requestCode == 1 && data != null) {
                Uri imageUri = data.getData();
                InputStream iStream;

                iStream = getActivity().getContentResolver().openInputStream(imageUri);

                imgByte = Utils.getBytes(iStream, imgViewNutrition);

            } else {
                ShowDialog.showToast(getActivity(), "No Images Selected");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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
                if (serverNutrition.Save() > 0) {
                    result = true;
                } else
                    result = false;
            } else {
                if (serverNutrition.Update() > 0) {
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
                    fragmentNutritions.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Adding the Record");
            } else {
                if (result) {
                    ShowDialog.showToast(getActivity(), "Record Updated Successfully");
                    fragmentNutritions.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Updating the Record");
            }
        }

    }
}
