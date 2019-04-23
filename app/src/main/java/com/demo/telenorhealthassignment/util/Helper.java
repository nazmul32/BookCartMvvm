package com.demo.telenorhealthassignment.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Helper {

    public static boolean isValidEmail(String email) {
        if (email == null || email.trim().length() == 0) {
            return false;
        }
        email = email.trim();
        Pattern pattern = Pattern.compile(Constants.EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isValidPassword(String password) {
        if (password == null || password.trim().length() < 6) {
            return false;
        }
        return true;
    }

    public static boolean isBothPasswordEqual(String passwordOne, String passwordTwo) {
        if (passwordOne != null && passwordOne.equals(passwordTwo)) {
            return true;
        }
        return false;
    }

    public static boolean isValidName(String name) {
        if (name == null || name.trim().length() == 0) {
            return false;
        }
        return true;
    }

    public static boolean isConnectionAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo wifi = manager.getActiveNetworkInfo();
        NetworkInfo mobile = manager.getActiveNetworkInfo();

        if (wifi != null && wifi.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        } else if (mobile != null && mobile.getType() == ConnectivityManager.TYPE_MOBILE){
            return true;
        }
        return false;
    }
}
