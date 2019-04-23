package com.demo.telenorhealthassignment.viewmodel;


import com.demo.telenorhealthassignment.model.local.signup.SignUpCredentials;
import com.demo.telenorhealthassignment.model.remote.SignUpResponse;
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

public class SignUpViewModel extends ViewModel {
    private final MutableLiveData<Integer> signUpLiveData = new MutableLiveData<>();
    public MutableLiveData<Integer> getSignUpLiveData() {
        return signUpLiveData;
    }

    public void processSignUpData(String name, String email, String password,
                                  String confirmPassword) {
        if (!Helper.isValidName(name)) {
            signUpLiveData.setValue(Constants.NAME_INVALID);
            return;
        }

        if (!Helper.isValidEmail(email)) {
            signUpLiveData.setValue(Constants.EMAIL_INVALID);
            return;
        }

        if (!Helper.isValidPassword(password)) {
            signUpLiveData.setValue(Constants.PASSWORD_INVALID);
            return;
        }

        if (!Helper.isBothPasswordEqual(password, confirmPassword)) {
            signUpLiveData.setValue(Constants.PASSWORD_MISMATCH);
            return;
        }

        signUpLiveData.setValue(Constants.NAME_EMAIL_PASSWORD_OK);
        sendSignUpRequest(name, email, password);
    }

    private void sendSignUpRequest(String name, String email, String password) {
        signUpLiveData.setValue(Constants.SHOW_DIALOG);
        APIInterface apiInterface = RetrofitClient.getRetrofitClient().create(APIInterface.class);
        SignUpCredentials signUpCredentials = new SignUpCredentials(name, email, password);
        Observable<SignUpResponse> signUpResponseObservable = apiInterface.getSignUpResponse(signUpCredentials);

        Observer<SignUpResponse> observer = new Observer<SignUpResponse>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(SignUpResponse signUpResponse) {
                if (signUpResponse.isError()) {
                    // As after sign up no access token received. A dummy access token is created.
                    // to enter into HomeActivity
                    signUpLiveData.setValue(Constants.SIGN_UP_FAILED_UNKNOWN_REASON);
                } else {
                    if (PreferencesManager.putString(Constants.KEY_ACCESS_TOKEN, Constants.DUMMY_TOKEN)) {
                        signUpLiveData.setValue(Constants.HIDE_DIALOG);
                    } else {
                        signUpLiveData.setValue(Constants.SIGN_UP_FAILED_UNKNOWN_REASON);
                    }
                }
            }

            @Override
            public void onError(Throwable e) {
                if (Constants.TIME_OUT.equals(e.getMessage())) {
                    signUpLiveData.setValue(Constants.SIGN_UP_FAILED_TIME_OUT);
                } else if (Constants.HTTP_400_BAD_REQUEST.equals(e.getMessage())) {
                    signUpLiveData.setValue(Constants.SIGN_UP_FAILED_400_BAD_REQUEST);
                } else {
                    signUpLiveData.setValue(Constants.SIGN_UP_FAILED_UNKNOWN_REASON);
                }
            }

            @Override
            public void onComplete() {

            }
        };

        signUpResponseObservable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
    }
}

