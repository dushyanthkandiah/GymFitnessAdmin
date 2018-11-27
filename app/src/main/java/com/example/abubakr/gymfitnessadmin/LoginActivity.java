package com.example.abubakr.gymfitnessadmin;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import OtherClasses.OtherShortcuts;
import OtherClasses.ShowDialog;
import OtherClasses.Validation;
import pl.droidsonroids.gif.GifImageView;

public class LoginActivity extends AppCompatActivity {

    public EditText txtUsername, txtPassword;
    public Validation vd;

    public GifImageView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        txtUsername = findViewById(R.id.txtUsername);
        txtPassword = findViewById(R.id.txtPassword);
        progressBar = findViewById(R.id.progressBar);

        vd = new Validation(getApplicationContext());

    }

    public void btnLogin(View c) {

        String[] fieldName = {"Username", "Password"};
        EditText[] field = {txtUsername, txtPassword};

        if (vd.CheckEmptyText(fieldName, field)) {
            new LoadLogin().execute();
        }
    }

    private class LoadLogin extends AsyncTask<Void, Void, Void> {

        private String username = "", password = "";

        @Override
        protected void onPreExecute() {
            OtherShortcuts.hideKeyboard(LoginActivity.this);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... params) {

            username = txtUsername.getText().toString().trim().toLowerCase();
            password = txtPassword.getText().toString();

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(View.INVISIBLE);


            if (username.equals("admin") && password.equals("admin")) {

                Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.animation_enter, R.anim.animation_leave);
                finish();

            } else
                ShowDialog.showToast(getApplicationContext(), "Please check your Email/Password");


        }
    }
}
