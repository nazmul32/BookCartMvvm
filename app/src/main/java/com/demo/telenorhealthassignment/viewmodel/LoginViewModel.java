package com.demo.telenorhealthassignment.viewmodel;


import com.demo.telenorhealthassignment.model.local.login.LoginCredentials;
import com.demo.telenorhealthassignment.model.remote.LoginResponse;
import com.demo.telenorhealthassignment.model.remote.retrofit.APIInterface;
import com.demo.telenorhealthassignment.model.remote.retrofit.RetrofitClient;
import com.demo.telenorhealthassignment.util.Constants;
import com.demo.telenorhealthassignment.util.Helper;
import com.demo.telenorhealthassignment.util.PreferencesManager;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginViewModel extends ViewModel {
    private final MutableLiveData<Integer> loginLiveData = new MutableLiveData<>();

    public MutableLiveData<Integer> getLoginLiveData() {
        return loginLiveData;
    }

    public void processLoginData(String email, String password) {
        if (!Helper.isValidEmail(email)) {
            loginLiveData.setValue(Constants.EMAIL_INVALID);
            return;
        }

        if (!Helper.isValidPassword(password)) {
            loginLiveData.setValue(Constants.PASSWORD_INVALID);
            return;
        }

        loginLiveData.setValue(Constants.EMAIL_PASSWORD_OK);
        sendLoginRequest(email, password);
    }

    private void sendLoginRequest(String email, String password) {
        loginLiveData.setValue(Constants.SHOW_DIALOG);
        APIInterface apiInterface = RetrofitClient.getRetrofitClient().create(APIInterface.class);
        LoginCredentials loginCredentials = new LoginCredentials(email, password);
        Observable<LoginResponse> observable = apiInterface.getLoginResponse(loginCredentials);

        Observer<LoginResponse> observer = new Observer<LoginResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(LoginResponse loginResponse) {
                if (loginResponse.isError()) {
                    loginLiveData.setValue(Constants.LOGIN_FAILED);
                } else {
                    if (PreferencesManager.putString(Constants.KEY_ACCESS_TOKEN, loginResponse.getToken())) {
                        loginLiveData.setValue(Constants.HIDE_DIALOG);
                    } else {
                        loginLiveData.setValue(Constants.LOGIN_FAILED);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                loginLiveData.setValue(Constants.LOGIN_FAILED);
            }

            @Override
            public void onComplete() {

            }
        };

        observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

