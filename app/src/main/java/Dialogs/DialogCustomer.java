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
import android.widget.ImageView;
import android.widget.TextView;

import com.example.abubakr.gymfitnessadmin.R;

import GettersAndSetters.ClassCustomers;
import OtherClasses.Utils;
import ServerLink.ServerSchedule;
import pl.droidsonroids.gif.GifImageView;


@SuppressLint("ValidFragment")
public class DialogCustomer extends BaseDialogFragment<DialogCustomer.OnDialogFragmentClickListener> {

    private View iView;
    private ClassCustomers classCustomers;
    private ImageView imgProfilePicture;
    private TextView lblCustomerId, lblCusName, lblEmail, lblGender, lblDob, lblPhone, lblAddress, lblHeight, lblWeight, lblScheduleId, lblScheduleType;
    private GifImageView progressBar;

    public DialogCustomer(ClassCustomers classCustomers) {
        this.classCustomers = classCustomers;
    }

    public static DialogCustomer newInstance(ClassCustomers classCustomers) {
        DialogCustomer dialogSelect = new DialogCustomer(classCustomers);
        return dialogSelect;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        iView = inflater.inflate(R.layout.dialog_customer, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getDialog().getWindow().getAttributes().windowAnimations = R.style.ForDialogAnim;
        getDialog().setCanceledOnTouchOutside(true);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        setRetainInstance(true);

        imgProfilePicture = iView.findViewById(R.id.imgProfilePicture);
        lblCustomerId = iView.findViewById(R.id.lblCustomerId);
        lblCusName = iView.findViewById(R.id.lblCusName);
        lblEmail = iView.findViewById(R.id.lblEmail);
        lblGender = iView.findViewById(R.id.lblGender);
        lblDob = iView.findViewById(R.id.lblDob);
        lblPhone = iView.findViewById(R.id.lblPhone);
        lblAddress = iView.findViewById(R.id.lblAddress);
        lblHeight = iView.findViewById(R.id.lblHeight);
        lblWeight = iView.findViewById(R.id.lblWeight);
        lblScheduleId = iView.findViewById(R.id.lblScheduleId);
        lblScheduleType = iView.findViewById(R.id.lblScheduleType);
        progressBar = iView.findViewById(R.id.progressBar);

        new loadData().execute();

        return iView;
    }

    public interface OnDialogFragmentClickListener {

    }

    private class loadData extends AsyncTask<Void, Void, Void> {
        ServerSchedule serverSchedule = new ServerSchedule();
        String result;

        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            serverSchedule.getClassSchedule().setSchdId(classCustomers.getSchdId());
            result = serverSchedule.Search();
            return null;
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);

            imgProfilePicture.setImageBitmap(Utils.getImage(classCustomers.getPicture()));
            lblCusName.setText(classCustomers.getName());
            lblCustomerId.setText("ID : " + classCustomers.getId());
            lblEmail.setText("Email : " + classCustomers.getEmail());
            lblGender.setText("Gender : " + classCustomers.getGender());
            lblDob.setText("Date of Birth : " + classCustomers.getDob());
            lblPhone.setText("Phone : " + classCustomers.getPhone());
            lblAddress.setText("Address : " + classCustomers.getAddress());
            lblHeight.setText("Height : " + classCustomers.getHeight() + "\"");
            lblWeight.setText("Weight : " + classCustomers.getWeight() + " Kg");
            lblScheduleId.setText("Schedule Id : " + classCustomers.getSchdId());

            if (result.equals("success"))
                lblScheduleType.setText("Schedule Type : " + serverSchedule.getClassSchedule().getType());
            else {
                lblScheduleType.setTextColor(Color.RED);
                lblScheduleType.setText("Schedule Type : Couldn't Retrieve Schedule Type");
            }
        }
    }
}
