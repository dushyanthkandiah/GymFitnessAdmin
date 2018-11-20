package Dialogs;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;

import Fragments.FragmentTrainers;
import GettersAndSetters.ClassSchedule;
import GettersAndSetters.ClassTrainers;
import OtherClasses.ShowDialog;
import OtherClasses.Utils;
import OtherClasses.Validation;
import ServerLink.ServerSchedule;
import ServerLink.ServerTrainers;


@SuppressLint("ValidFragment")
public class DialogTrainer extends BaseDialogFragment<DialogTrainer.OnDialogFragmentClickListener> {

    private static final int PICK_IMAGE = 1;
    public byte[] imgByte;
    ClassTrainers classTrainers;
    ArrayList<ClassSchedule> schdList;
    private FragmentTrainers fragmentTrainers;
    private View iView;
    private ImageView imgViewTrainer;
    private TextView lblTrainerId;
    private EditText txtName, txtDob, txtPhone, txtAddress, txtExperience;
    private Button btnSave;
    private SimpleDateFormat sdf;
    private Calendar myCalendar, myCalendarExp;
    private Validation vd;
    private String selectedGender = "", type;
    private int selectedSchedule = 0, autoId = 0;
    private RadioGroup rdGrpGender;
    private Spinner spinnerSchdId;
    private ServerTrainers serverTrainers;

    public DialogTrainer(FragmentTrainers fragmentTrainers, ClassTrainers classTrainers, String type) {
        this.fragmentTrainers = fragmentTrainers;
        this.classTrainers = classTrainers;
        this.type = type;
    }

    public static DialogTrainer newInstance(FragmentTrainers fragmentTrainers, ClassTrainers classTrainers, String type) {
        DialogTrainer dialogSelect = new DialogTrainer(fragmentTrainers, classTrainers, type);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_trainer, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        imgViewTrainer = iView.findViewById(R.id.imgViewTrainer);
        lblTrainerId = iView.findViewById(R.id.lblTrainerId);
        txtName = iView.findViewById(R.id.txtName);
        txtDob = iView.findViewById(R.id.txtDob);
        txtPhone = iView.findViewById(R.id.txtPhone);
        txtAddress = iView.findViewById(R.id.txtAddress);
        txtExperience = iView.findViewById(R.id.txtExperience);
        btnSave = iView.findViewById(R.id.btnSave);
        rdGrpGender = iView.findViewById(R.id.rdGrpGender);
        spinnerSchdId = iView.findViewById(R.id.spinnerSchdId);


        loadSpinner();

        if (type.equals("update")) {
            loadData();
        } else {
            selectDefaultImage("Male");
            lblTrainerId.setText("");

        }

        serverTrainers = new ServerTrainers();

        sdf = new SimpleDateFormat("yyyy-MM-dd");

        myCalendar = Calendar.getInstance();
        myCalendarExp = Calendar.getInstance();

        vd = new Validation(getActivity());

        final DatePickerDialog.OnDateSetListener dateBirth = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                txtDob.setText(sdf.format(myCalendar.getTime()));
            }

        };

        final DatePickerDialog.OnDateSetListener dateExperience = new DatePickerDialog.OnDateSetListener() {

            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                myCalendarExp.set(Calendar.YEAR, year);
                myCalendarExp.set(Calendar.MONTH, monthOfYear);
                myCalendarExp.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                txtExperience.setText(sdf.format(myCalendarExp.getTime()));
            }

        };

        txtDob.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateBirth,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        txtExperience.setOnClickListener(new View.OnClickListener() {

            @SuppressLint("NewApi")
            @Override
            public void onClick(View v) {

                new DatePickerDialog(getActivity(), dateExperience,
                        myCalendarExp.get(Calendar.YEAR),
                        myCalendarExp.get(Calendar.MONTH),
                        myCalendarExp.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        rdGrpGender.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = iView.findViewById(checkedId);
                selectedGender = radioButton.getText().toString();
                selectDefaultImage(selectedGender);
            }
        });

        imgViewTrainer.setOnClickListener(new View.OnClickListener() {
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
//                ShowDialog.showToast(getActivity(), selectedSchedule+"");
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
        autoId = classTrainers.getId();
        lblTrainerId.setText("ID : " + classTrainers.getId());
        txtName.setText("" + classTrainers.getName());
        txtDob.setText("" + classTrainers.getDob());
        txtPhone.setText("" + classTrainers.getPhone());
        txtAddress.setText("" + classTrainers.getAddress());
        txtExperience.setText("" + classTrainers.getWorkExperience());
        imgByte = classTrainers.getPicture();
        selectedGender = classTrainers.getGender();
        selectedSchedule = classTrainers.getSchdId();

        imgViewTrainer.setImageBitmap(Utils.getImage(imgByte));

        if (selectedGender.equals("Male"))
            rdGrpGender.check(R.id.rbtnMale);
        else
            rdGrpGender.check(R.id.rbtnFemale);

        Utils.setSpinnerPosition(spinnerSchdId, String.valueOf(selectedSchedule));

    }

    private void validate() {
        String[] fieldName = {"Name", "Date of Birth", "Phone Number", "Address", "Joined Date"};
        EditText[] field = {txtName, txtDob, txtPhone, txtAddress, txtExperience};

        if (vd.CheckEmptyText(fieldName, field)) {
            if (vd.PhoneCheck(txtPhone.getText().toString().trim())) {
                if (!selectedGender.equals(""))
                    getFieldValues();
                else
                    ShowDialog.showToast(getActivity(), "Please Select a Gender");
            }

        }
    }

    private void getFieldValues() {
        if (autoId != 0)
            serverTrainers.getClassTrainers().setId(autoId);

        serverTrainers.getClassTrainers().setName(txtName.getText().toString().trim());
        serverTrainers.getClassTrainers().setDob(txtDob.getText().toString().trim());
        serverTrainers.getClassTrainers().setPhone(txtPhone.getText().toString().trim());
        serverTrainers.getClassTrainers().setGender(selectedGender);
        serverTrainers.getClassTrainers().setAddress(txtAddress.getText().toString().trim());
        serverTrainers.getClassTrainers().setWorkExperience(txtDob.getText().toString().trim());
        serverTrainers.getClassTrainers().setSchdId(selectedSchedule);
        serverTrainers.getClassTrainers().setPicture(imgByte);

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
    private void selectDefaultImage(String gender) {
        try {
            byte[] MaleByte = Utils.getBytes(getActivity().getResources().openRawResource(R.drawable.profile_male));
            byte[] FemaleByte = Utils.getBytes(getActivity().getResources().openRawResource(R.drawable.profile_female));

            if (Arrays.equals(MaleByte, imgByte) || Arrays.equals(FemaleByte, imgByte) || autoId == 0) {

                InputStream inputstream;
                if (gender.equals("Male"))
                    inputstream = getActivity().getResources().openRawResource(R.drawable.profile_male);
                else
                    inputstream = getActivity().getResources().openRawResource(R.drawable.profile_female);

                imgByte = Utils.getBytes(inputstream, imgViewTrainer);

            }
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

                imgByte = Utils.getBytes(iStream, imgViewTrainer);

            } else {
                ShowDialog.showToast(getActivity(), "No Images Selected");
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public interface OnDialogFragmentClickListener {

    }

    // asyn for save
    private class SaveDetails extends AsyncTask<Void, Void, Void> {

        boolean result = false;


        @Override
        protected void onPreExecute() {

        }

        @Override
        protected Void doInBackground(Void... params) {

            if (autoId == 0) {
                if (serverTrainers.Save() > 0) {
                    result = true;
                } else
                    result = false;
            } else {
                if (serverTrainers.Update() > 0) {
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
                    fragmentTrainers.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Adding the Record");
            } else {
                if (result) {
                    ShowDialog.showToast(getActivity(), "Record Updated Successfully");
                    fragmentTrainers.onRefresh();
                    dismiss();
                } else
                    ShowDialog.showToast(getActivity(), "Error While Updating the Record");
            }
        }

    }
}
