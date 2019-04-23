package com.demo.telenorhealthassignment.util;

public class Constants {
    private Constants() {
    }

    //Database name
    public static final String DATABASE_NAME = "BookDatabase.db";

    //SharedPreferences name
    public static final String SHARED_PREFERENCES_NAME = "phoenix_preferences";

    // Retrofit base url
    public static final String BASE_URL = "https://booksdemo.herokuapp.com";

    //Email validation
    public static final String EMAIL_PATTERN = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";

    //Login and Sign up
    public static final int EMAIL_PASSWORD_OK = 0;
    public static final int NAME_INVALID = 1;
    public static final int EMAIL_INVALID = 2;
    public static final int PASSWORD_INVALID = 3;
    public static final int PASSWORD_MISMATCH = 4;
    public static final int LOGIN_FAILED = 5;

    //Read, Connection timeout
    public static final int READ_TIMEOUT = 5;
    public static final int CONNECTION_TIMEOUT = 5;

    //Login dialog
    public static final int SHOW_DIALOG = 6;
    public static final int HIDE_DIALOG = 7;

    //Access token
    public static final String DEFAULT_ACCESS_TOKEN = "";
    public static final String KEY_ACCESS_TOKEN = "key_access_token";

    //sing up error message
    public static final String TIME_OUT = "timeout";
    public static final String HTTP_400_BAD_REQUEST = "HTTP 400 Bad Request";
    public static final int SIGN_UP_SUCCESSFUL = 8;
    public static final int SIGN_UP_FAILED_NORMAL = 9;
    public static final int SIGN_UP_FAILED_TIME_OUT = 10;
    public static final int SIGN_UP_FAILED_400_BAD_REQUEST = 11;
    public static final int SIGN_UP_FAILED_UNKNOWN_REASON = 12;
    public static final int NAME_EMAIL_PASSWORD_OK = 13;

    public static final String DUMMY_TOKEN = "abcdefgh_12345678";

    //Fragment constants
    public static final int CART_FRAGMENT_ID = 101;
    public static final int WISH_LIST_FRAGMENT_ID = 102;
    public static final int LOG_OUT_ID = 103;
    public static final String HOME_FRAGMENT_TAG = "home_fragment_tag";
    public static final String CART_FRAGMENT_TAG = "cart_fragment_tag";
    public static final String WISH_LIST_FRAGMENT_TAG = "wish_list_fragment_tag";
    public static final String BOOK_DETAILS_FRAGMENT_TAG = "book_details_fragment_tag";

    // Room data - book type(cart list, wish list)
    public static final int CART_LIST_TYPE = 104;
    public static final int WISH_LIST_TYPE = 105;
    public static final int DEFAULT_TYPE = 106;

    //Recycler View type
    public static final int EMPTY_VIEW_TYPE = 107;
    public static final int NORMAL_VIEW_TYPE = 108;


    //Fragment Name
    public static final String HOME_FRAGMENT = "HomeFragment";
    public static final String CART_FRAGMENT = "CartFragment";
    public static final String WISH_LIST_FRAGMENT = "WishListFragment";
    public static final String BOOK_DETAILS_FRAGMENT = "BookDetailsFragment";
}
