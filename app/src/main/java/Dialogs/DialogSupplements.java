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
import java.util.Arrays;
import java.util.List;

import Fragments.FragmentExercises;
import Fragments.FragmentSupplements;
import GettersAndSetters.ClassExercise;
import GettersAndSetters.ClassSchedule;
import GettersAndSetters.ClassSupplement;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerExercise;
import ServerLink.ServerSchedule;
import ServerLink.ServerSupplements;

@SuppressLint({"ValidFragment", "SetTextI18n"})
public class DialogSupplements extends BaseDialogFragment<DialogSupplements.OnDialogFragmentClickListener> {

    ArrayList<ClassSchedule> schdList;
    private FragmentSupplements fragmentSupplements;
    private View iView;
    private ClassSupplement classSupplement;
    private TextView lblSupId;
    private EditText txtSupplementName, txtStock, txtPrice;
    private Button btnSave;
    private Validation vd;
    private String type, supType = "Supplement";
    int check = 0;
    private int autoId = 0;
    private Spinner spinnerSupType;
    private ServerSupplements serverSupplements;

    public DialogSupplements(FragmentSupplements fragmentSupplements, ClassSupplement classSupplement, String type) {
        this.fragmentSupplements = fragmentSupplements;
        this.classSupplement = classSupplement;
        this.type = type;
    }

    public static DialogSupplements newInstance(FragmentSupplements fragmentSupplements, ClassSupplement classSupplement, String type) {
        DialogSupplements dialogSelect = new DialogSupplements(fragmentSupplements, classSupplement, type);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_supplements, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        lblSupId = iView.findViewById(R.id.lblSupId);
        txtSupplementName = iView.findViewById(R.id.txtSupplementName);
        txtStock = iView.findViewById(R.id.txtStock);
        txtPrice = iView.findViewById(R.id.txtPrice);
        spinnerSupType = iView.findViewById(R.id.spinnerSupType);
        btnSave = iView.findViewById(R.id.btnSave);

        loadSupType();

        if (type.equals("update")) {
            loadData();
        } else {
            lblSupId.setText("");
        }

        serverSupplements = new ServerSupplements();

        vd = new Validation(getActivity());

        spinnerSupType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);

                    supType = (String) parent.getItemAtPosition(position);

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
        autoId = classSupplement.getId();
        lblSupId.setText("ID : " + classSupplement.getId());
        txtSupplementName.setText("" + classSupplement.getName());
        txtStock.setText("" + classSupplement.getStock());
        txtPrice.setText("" + classSupplement.getPrice());

        supType = classSupplement.getType();
        Utils.setSpinnerPositionWithoutSlash(spinnerSupType, supType);
    }

    private void validate() {
        String[] fieldName = {"Item Name", "Stock", "Price"};
        EditText[] field = {txtSupplementName, txtStock, txtPrice};

        if (vd.CheckEmptyText(fieldName, field)) {
            getFieldValues();

        }
    }

    private void getFieldValues() {
        if (autoId != 0)
            serverSupplements.getClassSupplement().setId(autoId);

        serverSupplements.getClassSupplement().setName(txtSupplementName.getText().toString().trim());
        serverSupplements.getClassSupplement().setStock(Double.parseDouble(txtStock.getText().toString().trim()));
        serverSupplements.getClassSupplement().setPrice(Double.parseDouble(txtPrice.getText().toString().trim()));
        serverSupplements.getClassSupplement().setType(supType);

        new SaveDetails().execute();
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
                if (serverSupplements.Save() > 0) {
                    result = true;
                } else
                    result = false;
            } else {
                if (serverSupplements.Update() > 0) {
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
                    fragmentSupplements.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Adding the Record");
            } else {
                if (result) {
                    ShowDialog.showToast(getActivity(), "Record Updated Successfully");
                    fragmentSupplements.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Updating the Record");
            }
        }

    }

    private void loadSupType() {
        String[] sortArray = new String[]{"Supplement", "Product"};

        final List<String> sortArrayList = new ArrayList<>(Arrays.asList(sortArray));

        final ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, sortArrayList);

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerSupType.setAdapter(spinnerArrayAdapter);

    }
}
