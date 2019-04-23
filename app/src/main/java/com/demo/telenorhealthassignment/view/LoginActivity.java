package com.demo.telenorhealthassignment.view;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.demo.telenorhealthassignment.R;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.Helper;
import com.demo.telenorhealthassignment.util.PreferencesManager;
import com.demo.telenorhealthassignment.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {
    private TextInputEditText emailTextInputEditText;
    private TextInputEditText passwordTextInputEditText;
    private LoginViewModel loginViewModel;
    private Button loginButton;
    private Button signUpButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initLoginViewModel();
        PreferencesManager.init(this);
        if (PreferencesManager.getString(Constants.KEY_ACCESS_TOKEN).length() > 0) {
            navigateToHomeActivity();
        }
    }

    private void initLoginViewModel() {
        loginViewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        loginViewModel.getLoginLiveData().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(@Nullable Integer integer) {
                if (integer == Constants.EMAIL_INVALID) {
                    emailTextInputEditText.setError(getString(R.string.error_invalid_email));
                } else if (integer == Constants.PASSWORD_INVALID) {
                    emailTextInputEditText.setError(null);
                    passwordTextInputEditText.setError(getString(R.string.error_invalid_password));
                } else if (integer == Constants.EMAIL_PASSWORD_OK) {
                    passwordTextInputEditText.setError(null);
                } else if (integer == Constants.SHOW_DIALOG) {
                    showProgressDialog();
                } else if (integer == Constants.HIDE_DIALOG) {
                    hideProgressDialog();
                    navigateToHomeActivity();
                } else if (integer == Constants.LOGIN_FAILED) {
                    hideProgressDialog();
                    showErrorDialog(getString(R.string.login_error));
                }
            }
        });
    }

    private void initView() {
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.login);
        }
        emailTextInputEditText = findViewById(R.id.et_email);
        passwordTextInputEditText = findViewById(R.id.et_password);
        loginButton = findViewById(R.id.btn_login);
        signUpButton = findViewById(R.id.btn_sign_up);

        TextView clickableTextLink = findViewById(R.id.sign_up_suggestion);
        clickableTextLink.setMovementMethod(LinkMovementMethod.getInstance());

        setOnClickListenerLoginButton();
        setOnClickListenerSignUpButton();
    }

    private void setOnClickListenerLoginButton() {
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Helper.isConnectionAvailable(LoginActivity.this)) {
                    loginViewModel.processLoginData(emailTextInputEditText.getText().toString(),
                            passwordTextInputEditText.getText().toString());
                } else {
                    showErrorDialog(getString(R.string.internet_connection_unavailable));
                }
            }
        });
    }

    private void setOnClickListenerSignUpButton() {
        signUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignUpActivity.class));
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
        passwordTextInputEditText.setText("");
        startActivity(new Intent(this, HomeActivity.class));
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
