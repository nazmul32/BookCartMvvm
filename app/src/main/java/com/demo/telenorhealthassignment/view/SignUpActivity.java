package com.demo.telenorhealthassignment.view;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.Helper;
import com.demo.telenorhealthassignment.util.PreferencesManager;
import com.demo.telenorhealthassignment.viewmodel.LoginViewModel;
import com.demo.telenorhealthassignment.viewmodel.SignUpViewModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class SignUpActivity extends AppCompatActivity {
    private SignUpViewModel signUpViewModel;
    private TextInputEditText nameTextInputEditText;
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText confirmEmailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private Button signUpButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initView();
        initSignUpViewModel();
    }

    private void initSignUpViewModel() {
        signUpViewModel = ViewModelProviders.of(this).get(SignUpViewModel.class);
        signUpViewModel.getSignUpLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == Constants.NAME_INVALID) {
                    nameTextInputEditText.setError(getString(R.string.error_invalid_name));
                } else if (integer == Constants.EMAIL_INVALID) {
                    nameTextInputEditText.setError(null);
                    emailTextInputEditText.setError(getString(R.string.error_invalid_email));
                } else if (integer == Constants.PASSWORD_INVALID) {
                    emailTextInputEditText.setError(null);
                    passwordTextInputEditText.setError(getString(R.string.error_invalid_password));
                } else if (integer == Constants.PASSWORD_MISMATCH) {
                    passwordTextInputEditText.setError(getString(R.string.error_password_mismatch));
                    confirmEmailTextInputEditText.setError(getString(R.string.error_password_mismatch));
                } else if (integer == Constants.NAME_EMAIL_PASSWORD_OK) {
                    passwordTextInputEditText.setError(null);
                    confirmEmailTextInputEditText.setError(null);
                } else if (integer == Constants.SHOW_DIALOG) {
                    showProgressDialog();
                } else if (integer == Constants.HIDE_DIALOG) {
                    hideProgressDialog();
                    navigateToHomeActivity();
                } else if (integer == Constants.SIGN_UP_FAILED_TIME_OUT) {
                    hideProgressDialog();
                    showErrorDialog(getString(R.string.sign_up_time_out_error));
                } else if (integer == Constants.SIGN_UP_FAILED_400_BAD_REQUEST) {
                    hideProgressDialog();
                    showErrorDialog(getString(R.string.sign_up_400_error));
                } else if (integer == Constants.SIGN_UP_FAILED_UNKNOWN_REASON) {
                    hideProgressDialog();
                    showErrorDialog(getString(R.string.sign_up_error));
                } else if (integer == Constants.SIGN_UP_FAILED_NORMAL) {
                    hideProgressDialog();
                    showErrorDialog(getString(R.string.sign_up_error));
                }
            }
        });
    }

    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.sign_up);
        }

        nameTextInputEditText = findViewById(R.id.et_name);
        emailTextInputEditText = findViewById(R.id.et_email);
        passwordTextInputEditText = findViewById(R.id.et_password);
        confirmEmailTextInputEditText = findViewById(R.id.et_confirm_password);
        signUpButton = findViewById(R.id.btn_sign_up);

        setOnClickListenerSignUpButton();
    }

    private void setOnClickListenerSignUpButton() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isConnectionAvailable(SignUpActivity.this)) {
                    signUpViewModel.processSignUpData(nameTextInputEditText.getText().toString(),
                            emailTextInputEditText.getText().toString(),
                            passwordTextInputEditText.getText().toString(),
                            confirmEmailTextInputEditText.getText().toString());
                } else {
                    showErrorDialog(getString(R.string.internet_connection_unavailable));
                }
            }
        });
    }

    private void showProgressDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage(getString(R.string.please_wait));
        progressDialog.show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                hideProgressDialog();
            }
        }, Constants.READ_TIMEOUT * 1000);
    }

    private void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }

    private void navigateToHomeActivity() {
        Toast.makeText(this, getString(R.string.user_created_logged_in),
                Toast.LENGTH_SHORT).show();
        startActivity(new Intent(SignUpActivity.this, HomeActivity.class));
        finish();
    }

    private void showErrorDialog(String errorMessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(errorMessage)
                .setCancelable(false)
                .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.dismiss();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
