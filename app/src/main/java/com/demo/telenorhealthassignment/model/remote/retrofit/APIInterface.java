package com.demo.telenorhealthassignment.model.remote.retrofit;

import com.demo.telenorhealthassignment.model.local.login.LoginCredentials;
import com.demo.telenorhealthassignment.model.local.signup.SignUpCredentials;
import com.demo.telenorhealthassignment.model.remote.BookDataResponse;
import com.demo.telenorhealthassignment.model.remote.LoginResponse;
import com.demo.telenorhealthassignment.model.remote.SignUpResponse;

import io.reactivex.Observable;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface APIInterface {
    @POST("/login")
    Observable<LoginResponse> getLoginResponse(@Body LoginCredentials loginCredentials);

    @POST("/signup")
    Observable<SignUpResponse> getSignUpResponse(@Body SignUpCredentials signUpCredentials);

    @GET("/books")
    Call<BookDataResponse> getBookDataListResponse();
}
